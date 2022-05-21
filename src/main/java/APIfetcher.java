import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.stream.Collectors;

public class APIfetcher {

    // Singleton pattern - only ever one APIfetcher in focus
    private static APIfetcher currentData;
    // API response in JSON format
    private static JsonObject json;
    // Number format for Double values
    private static final DecimalFormat df = new DecimalFormat("0.0");
    // Time of last API call
    private static long TimeOfLastFetch;

    // Constructor called in update()
    private APIfetcher(){
        update();
    }

    // Ensures data is up-to-date
    private static void update(){
        try {
            // URL of API call
            URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?" +
                    "lat=52.205276&lon=0.119167" + // Cambridge global co-ordinates
                    "&appid=82f52b4f99f73d57f10d55cb79abba32"); // API key

            // Make connection to URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Deal with erroneous connection
            if(connection.getResponseCode() != 200) { // 200 means no error
                throw new RuntimeException("There is an issue with the API connection :/");
            }

            // Convert response to one long String
            String dataAsString = new BufferedReader(new InputStreamReader(url.openStream())).lines().collect(Collectors.joining("\n"));

            // Convert String to JSON object
            json = JsonParser.parseString(dataAsString).getAsJsonObject();

            // Remember time of last API call to (later) prevent surpassing call limit
            TimeOfLastFetch = System.currentTimeMillis();

            // Deal with IOException
        } catch (IOException e) {
            // By printing stack trace
            e.printStackTrace();
        }
    }

    // Returns current temperature
    private static Double getCurrentActualTemp(){
        return Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonObject("current").get("temp").getAsString()) - 273.15));
    }

    // Returns current felt temperature
    private static Double getCurrentFeltTemp(){
        return Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonObject("current").get("feels_like").getAsString()) - 273.15));
    }

    // Returns icon name for current weather
    private static String getCurrentIcon(){
        return json.getAsJsonObject("current").getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
    }

    // Returns current weather (and empty alerts map)
    private static Weather getWeatherCurr() {
        Double actual = getCurrentActualTemp();
        Double felt = getCurrentFeltTemp();
        String icon = getCurrentIcon(); // currentData.json.getAsJsonObject("current").getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

        return new Weather(actual, felt, icon, new HashMap<>());
    }

    // Returns weather for a certain hour (0-48) (and empty alerts map)
    private static Weather getWeatherAtHour(int hour){
        Double actual = Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonArray("hourly").get(hour).getAsJsonObject().get("temp").getAsString()) - 273.15));
        Double felt = Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonArray("hourly").get(hour).getAsJsonObject().get("feels_like").getAsString()) - 273.15));
        String icon = json.getAsJsonArray("hourly").get(hour).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

        return new Weather(actual, felt, icon, new HashMap<>());
    }

    // Returns daytime weather for a certain day (0-7) (and empty alerts map)
    private static Weather getWeatherAtDay(int day){
        Double actual = Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonObject("temp").get("day").getAsString()) - 273.15));
        Double felt = Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonObject("feels_like").get("day").getAsString()) - 273.15));
        String icon = json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

        return new Weather(actual, felt, icon, new HashMap<>());
    }

    // 0: current
    // 1-47: next two days hourly (always full hour, so like 1pm, 2pm etc.)
    // 48-52: next week daily (excluding next 48 hrs) (always gives the temperature for daytime!)
    public static Weather getForecast(int timeIndex, Setting setting) {
        // Only update every 15 minutes at most
        if (System.currentTimeMillis() - TimeOfLastFetch > 900000) {
            APIfetcher.update();
        }

        // TODO: make custom checked exception for the hour issues
        // Create object to return
        Weather result;
        // And assign current/hourly/daily to it
        if (timeIndex == 0) { // Current
            result = getWeatherCurr();
        }
        else if (timeIndex <= 47 && timeIndex > 0) { // Hourly
            result = getWeatherAtHour(timeIndex);
        }
        else if (timeIndex <= 52 && timeIndex > 47) { // Daily
            result = getWeatherAtDay(timeIndex - 45);
        }
        else { // Invalid argument
            throw new RuntimeException("we do not provide data for this time");
        }
        // Add alerts to alerts map
        // True if enabled and conditions are fulfilled
        result.addAlert(Alerts.HIGHTEMP,
                setting.isExtremeTemperatureNotificationEnabled() && result.getTemp()>setting.getExtremeTemperatureHigh()
        );
        result.addAlert(Alerts.LOWTEMP,
                setting.isExtremeTemperatureNotificationEnabled() && result.getTemp()<setting.getExtremeTemperatureLow()
        );
        return result;
    }
}