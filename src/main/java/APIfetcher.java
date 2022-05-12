import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class APIfetcher {

    private static APIfetcher currentData;
    private JsonObject json;

    private APIfetcher(){
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?lat=52.205276&lon=0.119167&exclude=&appid=861bc7cd5c98494df6f104259f04fb3b");

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
        //System.out.println(currentData.json.get("hourly").getAsJsonObject().getAsJsonObject(hour));

        // JsonObject queryResult = currentData.json.getAsJsonArray().get(hour);
        JsonObject queryResult = currentData.json.getAsJsonArray("hourly").get(hourAsInt).getAsJsonObject(); //.getAsJsonObject(hour);
        //JsonObject queryResult = currentData.json.getAsJsonObject("hourly"); //.getAsJsonObject(hour);
        //System.out.println(queryResult.get("temp"));
        result.put("actual", Double.toString(Double.parseDouble(queryResult.get("temp").getAsString()) -273.15));
        result.put("felt", Double.toString(Double.parseDouble(queryResult.get("feels_like").getAsString()) - 273.15));

        return result;


    }

}
