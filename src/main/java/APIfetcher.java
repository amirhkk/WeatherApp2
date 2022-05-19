import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class APIfetcher {

    // Singleton pattern - only ever one JSON object in focus
    private static APIfetcher currentData;
    // Actual data in JSON format
    private JsonObject json;

    private APIfetcher(){
        try {
            // URL we fetch data from
            URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?" // API website
                    + "lat=52.205276&lon=0.119167" // Global co-ordinates for Cambridge
                    +"&appid=861bc7cd5c98494df6f104259f04fb3b"); // API key

            // Make connection to website using URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Deal with if fetch fails
            if(connection.getResponseCode() != 200) {
                throw new RuntimeException("There is an issue with the API connection :/");
            }

            // Puts data from website into one long String
            String dataAsString = new BufferedReader(new InputStreamReader(url.openStream())).lines().collect(Collectors.joining("\n"));

            // Parses one long string into a JSON object which is easier to work with
            this.json = JsonParser.parseString(dataAsString).getAsJsonObject();

        } catch (IOException e) { // If large-scale error with fetch
            // Chain exception - gives all information about error
            e.printStackTrace();
        }
    }

    private static void update(){ // Ensures currentData is up-to-date
        currentData = new APIfetcher();
    }

    public static Double getCurrentActualTemp(){
        APIfetcher.update(); // Updates currentData to current values
        return Double.parseDouble(
                currentData.json.getAsJsonObject("current").get("temp").getAsString() // Get current.temp as String
        ) - 273.15; // Convert String to Double then from Kelvin to Celcius
    }

    public static Double getCurrentFeltTemp(){
        APIfetcher.update(); // Updates currentData to current values
        return Double.parseDouble(
                currentData.json.getAsJsonObject("current").get("feels_like").getAsString() // Get current.feels_like as String
        ) - 273.15; // Convert String to Double then from Kelvin to Celcius
    }

    public static Map<String, String> getHourlyAt(String hour){
        APIfetcher.update(); // Updates currentData to current values
        int hourAsInt = Integer.parseInt(hour); // Parses String argument to Integer
        if (hourAsInt > 47 || hourAsInt < 0) { // Range check on argument
            throw new IllegalArgumentException("We only provide forecasts for the next 48hrs (0-47"); // Exception if failed range check
        }

        Map<String, String> result = new HashMap<>(); // Create String->String hash map to return

        JsonObject queryResult = currentData.json.getAsJsonArray("hourly").get(hourAsInt).getAsJsonObject();
        result.put("actual", Double.toString(Double.parseDouble(queryResult.get("temp").getAsString()) - 273.15));
        result.put("felt", Double.toString(Double.parseDouble(queryResult.get("feels_like").getAsString()) - 273.15));

        return result;
    }

    public static String getCurrentIcon(){
        APIfetcher.update();
        return currentData.json.getAsJsonObject("current").getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
    }

    public static Date getFromUnixToDate(){
        APIfetcher.update();
        System.out.println(Long.parseLong(currentData.json.getAsJsonObject("current").get("dt").getAsString()));
        Date date = new Date(Long.parseLong(currentData.json.getAsJsonObject("current").get("dt").getAsString())* 1000);
        System.out.println(date.getMinutes());
        return date;
    }

    private static Map<String, Double> getTempAtTime(int hour){
       APIfetcher.update();
       Double actual =  Double.parseDouble(currentData.json.getAsJsonArray("hourly").get(hour).getAsJsonObject().get("temp").getAsString());
       Double felt =  Double.parseDouble(currentData.json.getAsJsonArray("hourly").get(hour).getAsJsonObject().get("feels_like").getAsString());
       Map<String, Double> result = new HashMap<>();

       result.put("Actual", actual);
       result.put("feels", felt);
       return result;
    }

    private static Map<String, Double> getTempAtDay(int day){
        APIfetcher.update();
        Double actual = Double.parseDouble(currentData.json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonObject("temp").get("day").getAsString());
        Double felt = Double.parseDouble(currentData.json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonObject("feels_like").get("day").getAsString());

        Map<String, Double> result = new HashMap<>();

        result.put("Actual", actual);
        result.put("felt", felt);

        return result;
    }
    //public static Date fromUnixTODate(){}



   // private static long getRelativeTime(){

   // }
    // 0 current
    // 1-47: next two days hourly (always full hour, so like 1pm, 2pm etc.)
    // 48-52: next week daily? (excluding next 48 hrs) (always give the temperature for day time!)
    public static Map<String, Double> getForecast(int timeIndex) {  // need to fetch the icon aswell
        // TODO: make custom checked exception for the hour issues
        Map<String, Double> result = new HashMap<>();
        if (timeIndex < 0 || timeIndex > 52) {
            throw new RuntimeException("we do not provide data for this time");
        }

        else if (timeIndex == 0) {
            // current and return
            result.put("Actual", getCurrentActualTemp());
            result.put("Felt", getCurrentFeltTemp());

            return result;
        }
        else if (timeIndex <= 47) {
            // next 48 hrs, 0 for getting index, return
            return getTempAtTime(timeIndex - 1);

        }
        else { //timeIndex <= 52)

            return getTempAtDay(timeIndex - 48);
        }

    }
}

