import java.util.Map;

public class mainAPI{
    public static void main(String[] args) {
        Map<Weather, String> curr = APIfetcher.getForecast(0); // test current
        Map<Weather, String> hourly = APIfetcher.getForecast(1); // test hourly
        Map<Weather, String> daily = APIfetcher.getForecast(48); // test dailyu

        System.out.println(curr.get(Weather.TEMP));
        System.out.println(curr.get(Weather.ICON));
        System.out.println(hourly.get(Weather.TEMP));
        System.out.println(daily.get(Weather.TEMP));

    }
}