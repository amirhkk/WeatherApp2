import java.util.Map;

public class mainAPI{
    public static void main(String[] args) {
       // String tmp = APIfetcher.getCurrentActualTemp();
       // String felt = APIfetcher.getCurrentFeltTemp();
       // System.out.println("current temp: " + tmp + ". current felt temp: " + felt);
       // Map<String, String> hourly = APIfetcher.getHourlyAt("0");
       // System.out.println(hourly.get("actual") + "\n" + hourly.get("felt"));
       // System.out.println(APIfetcher.getCurrentIcon());
       // System.out.println(APIfetcher.getFromUnixToDate());

        Map<String, Double> curr = APIfetcher.getForecast(0); // test current
        Map<String, Double> hourly = APIfetcher.getForecast(1); // test hourly
        Map<String, Double> daily = APIfetcher.getForecast(48); // test daily

        System.out.println(curr.get("Actual"));
        System.out.println(hourly.get("Actual"));
        System.out.println(daily.get("Actual"));

    }
}