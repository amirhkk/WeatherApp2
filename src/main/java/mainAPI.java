public class mainAPI{
    public static void main(String[] args) {
        //APIfetcher.update();
        String tmp = APIfetcher.getCurrentActualTemp();
        String felt = APIfetcher.getCurrentFeltTemp();
        System.out.println("current temp: " + tmp + ". current felt temp: " + felt);
    }
}