import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class APIfetcher {

    private static APIfetcher currentData;
    private JsonObject json;

    private APIfetcher(){
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?lat=52.205276&lon=0.119167&exclude=&appid=82f52b4f99f73d57f10d55cb79abba32");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            if(connection.getResponseCode() != 200) {
                throw new RuntimeException("There is an issue with the API connection :/");
            }

            String dataAsString = new BufferedReader(new InputStreamReader(url.openStream())).lines().collect(Collectors.joining("\n"));

            this.json = JsonParser.parseString(dataAsString).getAsJsonObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void update(){
        currentData = new APIfetcher();
    }

    public static String getCurrentActualTemp(){
        APIfetcher.update();
        return Double.toString(Double.parseDouble(currentData.json.getAsJsonObject("current").get("temp").getAsString()) - 273.15);
    }

    public static String getCurrentFeltTemp(){
        APIfetcher.update();
        return Double.toString(Double.parseDouble(currentData.json.getAsJsonObject("current").get("feels_like").getAsString()) - 273.15);
    }

    public static Map<String, String> getHourlyAt(String hour){
        APIfetcher.update();
        int hourAsInt = Integer.parseInt(hour);
        if (hourAsInt > 47 || hourAsInt < 0) {
            throw new IllegalArgumentException("We only provide forecasts for the next 48hrs (0-47");
        }

        Map<String, String> result = new HashMap<>();

        JsonObject queryResult = currentData.json.getAsJsonArray("hourly").get(hourAsInt).getAsJsonObject();
        result.put("actual", Double.toString(Double.parseDouble(queryResult.get("temp").getAsString()) -273.15));
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

    public static Map<Weather, String> getWeatherAtHour(int hour){
        APIfetcher.update();
        String actual =  Double.toString(Double.parseDouble(currentData.json.getAsJsonArray("hourly").get(hour).getAsJsonObject().get("temp").getAsString()) - 273.15);
        String felt =  Double.toString(Double.parseDouble(currentData.json.getAsJsonArray("hourly").get(hour).getAsJsonObject().get("feels_like").getAsString()) - 273.15);

        String icon = currentData.json.getAsJsonArray("hourly").get(hour).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Map<Weather, String> result = new HashMap<>();

        result.put(Weather.TEMP, actual);
        result.put(Weather.FELT, felt);
        result.put(Weather.ICON, icon);
        return result;
    }

    private static Map<Weather, String> getWeatherAtDay(int day){
        APIfetcher.update();
        String actual = Double.toString(Double.parseDouble(currentData.json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonObject("temp").get("day").getAsString()) - 273.15);
        String felt = Double.toString(Double.parseDouble(currentData.json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonObject("feels_like").get("day").getAsString()) - 273.15);

        String icon = currentData.json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Map<Weather, String> result = new HashMap<>();

        result.put(Weather.TEMP, actual);
        result.put(Weather.FELT, felt);
        result.put(Weather.ICON, icon);

        return result;
    }
    //public static Date fromUnixTODate(){}



    // private static long getRelativeTime(){

    // }
    // 0 current
    // 1-47: next two days hourly (always full hour, so like 1pm, 2pm etc.)
    // 48-52: next week daily? (excluding next 48 hrs) (always give the temperature for day time!)
    public static Map<Weather, String> getForecast(int timeIndex) {  // need to fetch the icon aswell
        // TODO: make custom checked exception for the hour issues
        //Map<Weather, String> result = new HashMap<>();
        if (timeIndex < 0 || timeIndex > 52) {
            throw new RuntimeException("we do not provide data for this time");
        }

        else if (timeIndex == 0) {
            // current and return
            //result.put(Weather.TEMP, getCurrentActualTemp());
            //result.put(Weather.FELT, getCurrentFeltTemp());

            return getWeatherCurr();
            //return result;
        }
        else if (timeIndex <= 47) {
            // next 48 hrs, 0 for getting index, return
            return getWeatherAtHour(timeIndex);

        }
        else { //timeIndex <= 52)

            return getWeatherAtDay(timeIndex - 45);
        }

    }

    private static Map<Weather, String> getWeatherCurr() {
        String actual = getCurrentActualTemp();
        String felt = getCurrentFeltTemp();
        String icon = currentData.json.getAsJsonObject("current").getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

        Map<Weather, String> result = new HashMap<>();

        result.put(Weather.TEMP, actual);
        result.put(Weather.FELT, felt);
        result.put(Weather.ICON, icon);

        return result;
    }
}