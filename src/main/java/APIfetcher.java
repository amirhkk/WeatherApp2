import com.google.gson.JsonElement;
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

/**
 * Utility class for fetching weather information from OpenWeatherMap
 */
public class APIfetcher {
    // API response in JSON format
    private static JsonObject json;
    // Number format for Double values
    private static final DecimalFormat df = new DecimalFormat("0.0");
    // Time of last API call
    private static long TimeOfLastFetch;
    // Connection status
    private static boolean connectionSuccess;

    /**
     * Ensures data is up-to-date
     */
    private static void update(){
        // Only update every 15 minutes at most
        if (System.currentTimeMillis() - TimeOfLastFetch > 900000) {
            try {
                // URL of API call
                URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?" +
                        "lat=52.205276&lon=0.119167" + // Cambridge global co-ordinates
                        "&appid=82f52b4f99f73d57f10d55cb79abba32"); // API key

                // Make connection to URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Deal with erroneous connection
                if (connection.getResponseCode() != 200) { // 200 means no error
                    throw new RuntimeException("There is an issue with the API connection :/");
                }

                // Set connectionSuccess to true after checking that the API connection works
                connectionSuccess = true;

                // Convert response to one long String
                String dataAsString = new BufferedReader(new InputStreamReader(url.openStream())).lines().collect(Collectors.joining("\n"));

                // Convert String to JSON object
                json = JsonParser.parseString(dataAsString).getAsJsonObject();

                // Remember time of last API call to (later) prevent surpassing call limit
                TimeOfLastFetch = System.currentTimeMillis();

                // Deal with RuntimeException
            } catch (RuntimeException e) {
                connectionSuccess = false;
                e.printStackTrace();

                // Deal with IOException
            } catch (IOException e) {
                // By printing stack trace
                e.printStackTrace();
            }
        }
    }

    /**
     * Format temperature as celsius
     * @return current actual temperature
     */
    private static Double getCurrentActualTemp(){
        return Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonObject("current").get("temp").getAsString()) - 273.15));
    }

    /**
     * Format temperature as celsius
     * @return current felt temperature
     */
    private static Double getCurrentFeltTemp(){
        return Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonObject("current").get("feels_like").getAsString()) - 273.15));
    }

    /**
     * Icon names are set by OpenWeatherAPI and the icons in the icons folder must match the names
     * @return weather icon matching current weather
     */
    private static String getCurrentIcon(){
        return json.getAsJsonObject("current").getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
    }

    /**
     * @return the current weather (actual and felt temperature, rain, and empty alert map)
     */
    private static Weather getWeatherCurr() {
        Double actual = getCurrentActualTemp();
        Double felt = getCurrentFeltTemp();
        String icon = getCurrentIcon(); // currentData.json.getAsJsonObject("current").getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Double rain = getRainNextHour();

        return new Weather(actual, felt, icon, rain, new HashMap<>());
    }

    /**
     * @param hour number of hours from current (0-48)
     * @return the weather at the specified hour (actual and felt temperature, rain, and empty alert map)
     */
    private static Weather getWeatherAtHour(int hour){
        Double actual = Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonArray("hourly").get(hour).getAsJsonObject().get("temp").getAsString()) - 273.15));
        Double felt = Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonArray("hourly").get(hour).getAsJsonObject().get("feels_like").getAsString()) - 273.15));
        String icon = json.getAsJsonArray("hourly").get(hour).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Double rain = getRainNextHour();

        return new Weather(actual, felt, icon, rain, new HashMap<>());
    }

    // Returns daytime weather for a certain day (0-7) (and empty alerts map)

    /**
     * @param day number of days from current (0-7)
     * @return the weather on the specified day (actual and felt temperature, rain, and empty alert map)
     */
    private static Weather getWeatherAtDay(int day){
        Double actual = Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonObject("temp").get("day").getAsString()) - 273.15));
        Double felt = Double.parseDouble(df.format(Double.parseDouble(json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonObject("feels_like").get("day").getAsString()) - 273.15));
        String icon = json.getAsJsonArray("daily").get(day).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
        Double rain = getRainNextHour();

        return new Weather(actual, felt, icon, rain, new HashMap<>());
    }

    /**
     * @return the amount of rain (mm/h) in the next hour
     */
    private static Double getRainNextHour(){
        JsonElement rainInMM = json.getAsJsonArray("hourly").get(1).getAsJsonObject().get("rain").getAsJsonObject().get("1h");
        System.out.println(rainInMM);
        if (rainInMM == null) { // rain field will only exist if there is rain!
            return 0.0;
        }
        return rainInMM.getAsDouble();
    }

    /**
     * Gets the weather id from the API and checks whether there will be a thunderstorn
     * @return whether there will be a thunderstorm in the next hour
     */
    private static boolean willStormNextHour() {
        int weatherId = json.getAsJsonArray("hourly").get(1).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("id").getAsInt();
        return (weatherId / 100) % 100 == 2; // Thunderstorm codes are 2XX
    }

    /**
     * Call update() to make sure weather information is up to date
     * @return whether the API connection was successful
     */
    public static boolean establishConnection() {
        update();
        return connectionSuccess;
    }


    //

    /**
     * timeIndex values:
     * 0: current
     * 1-47: next two days hourly (always full hour, so like 1pm, 2pm etc.)
     * 48-52: next week daily (excluding next 48 hrs) (always gives the temperature for daytime!)
     * @param timeIndex Value from date slider, ranging from 0-52
     * @param setting Setting screen
     * @return Weather object containing weather information and relevant alerts
     */
    public static Weather getForecast(int timeIndex, Setting setting) {
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
        result.addAlert(
                Alerts.HIGH_TEMP,
                setting.isExtremeTemperatureNotificationEnabled() && result.getTemp() >= setting.getExtremeTemperatureHigh()
        );
        result.addAlert(Alerts.LOW_TEMP,
                setting.isExtremeTemperatureNotificationEnabled() && result.getTemp() <= setting.getExtremeTemperatureLow()
        );
        result.addAlert(Alerts.HEAVY_RAIN,
                setting.isExtremePrecipitationNotificationsEnabled() && result.getRain() >= setting.getExtremePrecipitation()
        );
        result.addAlert(Alerts.RAIN_SOON, setting.isImminentRainNotificationsEnabled() && result.getRain() > 0);
        result.addAlert(Alerts.STORM_SOON, setting.isStormWarningNotificationsEnabled() && willStormNextHour());
        return result;
    }
}