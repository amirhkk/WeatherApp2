public class mainAPI{
    public static void main(String[] args) {
        Setting setting = new Setting(new RootScreen());
        // Need to temporarily make methods public to test with next 2 lines
        //setting.toggleExtremeTemperatureNotifications();
        //setting.setNewExtremeHighTemperature("10");

        Weather curr = APIfetcher.getForecast(0, setting); // test current

        System.out.println("extreme temp alerts active: " + setting.isExtremeTemperatureNotificationEnabled());
        System.out.println("extreme temp high: " + setting.getExtremeTemperatureHigh());
        System.out.println("current temp: " + curr.getTemp());
        System.out.println("extreme temp high alert: " + curr.getAlerts().get(Alerts.HIGH_TEMP));
        System.out.println(APIfetcher.getRainAtDay(0));
        System.out.println(curr.getRain());
    }

}