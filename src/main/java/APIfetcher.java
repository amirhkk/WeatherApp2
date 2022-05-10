import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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

            JsonObject dataAsJson = JsonParser.parseString(dataAsString).getAsJsonObject();
            this.json = dataAsJson;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void update(){
        APIfetcher newData = new APIfetcher();

        if (!(newData == null)) {
            currentData = newData;
        }
    }

    public static String getCurrentActualTemp(){
        APIfetcher.update();
        return Double.toString(Double.parseDouble(currentData.json.getAsJsonObject("current").get("temp").getAsString()) - 273.15);
    }

    public static String getCurrentFeltTemp(){
        APIfetcher.update();
        return Double.toString(Double.parseDouble(currentData.json.getAsJsonObject("current").get("feels_like").getAsString()) - 273.15);
    }

}
