import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class APIfetcher {

    private static APIfetcher currentData;
    private static JsonObject json;
    private static final DecimalFormat df = new DecimalFormat("0.0");
    private static long TimeOfLastFetch;

    private APIfetcher(){
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?lat=52.205276&lon=0.119167&appid=82f52b4f99f73d57f10d55cb79abba32");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();


            if(connection.getResponseCode() != 200) {
                throw new RuntimeException("There is an issue with the API connection :/");
            }

            String dataAsString = new BufferedReader(new InputStreamReader(url.openStream())).lines().collect(Collectors.joining("\n"));

            this.json = JsonParser.parseString(dataAsString).getAsJsonObject();
            TimeOfLastFetch = System.currentTimeMillis();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void update(){
        currentData = new APIfetcher();
    }

    public static String getCurrentActualTemp(){
        return df.format(Double.parseDouble(currentData.json.getAsJsonObject("current").get("temp").getAsString()) - 273.15);
    }

    public static String getCurrentFeltTemp(){
        return df.format(Double.parseDouble(currentData.json.getAsJsonObject("current").get("feels_like").getAsString()) - 273.15);
    }

    private static String getCurrentIcon(){
        return currentData.json.getAsJsonObject("current").getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
    }

    private static Map<Weather, String> getWeatherAtHour(int hour){
        String actual =  df.format(Double.parseDouble(currentData.json.getAsJsonArray("hourly").get(hour).getAsJsonObject().get("temp").getAsString()) - 273.15);
        String felt =  df.format(Double.parseDouble(currentData.json.getAsJsonArray("hourly").get(hour).getAsJsonObject().get("feels_like").getAsString()) - 273.15);

        String icon = currentData.json.getAsJsonArray("hourly").get(hour).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Map<Weather, String> result = new HashMap<>();

        result.put(Weather.TEMP, actual);
        result.put(Weather.FELT, felt);
        result.put(Weather.ICON, icon);
        return result;
    }

    private static Map<Weather, String> getWeatherAtDay(int day){
        String actual = Double.toString(Double.parseDouble(currentData.json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonObject("temp").get("day").getAsString()) - 273.15);
        String felt = Double.toString(Double.parseDouble(currentData.json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonObject("feels_like").get("day").getAsString()) - 273.15);

        String icon = currentData.json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Map<Weather, String> result = new HashMap<>();

        result.put(Weather.TEMP, actual);
        result.put(Weather.FELT, felt);
        result.put(Weather.ICON, icon);

        return result;
    }

    // 0 current
    // 1-47: next two days hourly (always full hour, so like 1pm, 2pm etc.)
    // 48-52: next week daily? (excluding next 48 hrs) (always give the temperature for day time!)
    public static Map<Weather, String> getForecast(int timeIndex) {  // need to fetch the icon aswell
        if (System.currentTimeMillis() - TimeOfLastFetch > 900000) {
            APIfetcher.update(); // get most recent fetch of data
        }

        // TODO: make custom checked exception for the hour issues
        //Map<Weather, String> result = new HashMap<>();
        if (timeIndex < 0 || timeIndex > 52) {
            throw new RuntimeException("we do not provide data for this time");
        }

        else if (timeIndex == 0) {

            return getWeatherCurr();
        }
        else if (timeIndex <= 47) {
            return getWeatherAtHour(timeIndex);

        }
        else { //timeIndex <= 52)

            return getWeatherAtDay(timeIndex - 45);
        }
    }

    private static Map<Weather, String> getWeatherCurr() {
        String actual = getCurrentActualTemp();
        String felt = getCurrentFeltTemp();
        String icon = getCurrentIcon(); // currentData.json.getAsJsonObject("current").getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

        Map<Weather, String> result = new HashMap<>();

        result.put(Weather.TEMP, actual);
        result.put(Weather.FELT, felt);
        result.put(Weather.ICON, icon);

        return result;
    }
}