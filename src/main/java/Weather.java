import java.util.Map;

public class Weather {
    // Actual temperature
    private static Double temp;
    // Felt temperature
    private static Double felt;
    // Icon png name
    private static String icon;
    // Map of alerts
    private static Map<Alerts, Boolean> alerts;

    // Public constructor only way to set variables (except adding alerts)
    public Weather(Double temp, Double felt, String icon, Map<Alerts, Boolean> alerts) {
        this.temp = temp;
        this.felt = felt;
        this.icon = icon;
        this.alerts = alerts;
    }

    // Public access to temp
    public static Double getTemp() {
        return temp;
    }

    // Public access to felt
    public static Double getFelt() {
        return felt;
    }

    // Public access to icon
    public static String getIcon() {
        return icon;
    }

    // Public access to alerts
    public static Map<Alerts, Boolean> getAlerts() {
        return alerts;
    }

    // Public addition to alerts
    public static void addAlert(Alerts alert, Boolean active) {
        alerts.put(alert, active);
    }
}
