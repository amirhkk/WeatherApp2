import java.util.Map;

public class Weather {
    // Actual temperature
    private final Double temp;
    // Felt temperature
    private final Double felt;
    // Icon png name
    private final String icon;
    // Map of alerts
    private final Map<Alerts, Boolean> alerts;

    // Public constructor only way to set variables (except adding alerts)
    public Weather(Double temp, Double felt, String icon, Map<Alerts, Boolean> alerts) {
        this.temp = temp;
        this.felt = felt;
        this.icon = icon;
        this.alerts = alerts;
    }

    // Public access to temp
    public Double getTemp() {
        return temp;
    }

    // Public access to felt
    public Double getFelt() {
        return felt;
    }

    // Public access to icon
    public String getIcon() {
        return icon;
    }

    // Public access to alerts
    public Map<Alerts, Boolean> getAlerts() {
        return alerts;
    }

    // Public addition to alerts
    public void addAlert(Alerts alert, Boolean active) {
        alerts.put(alert, active);
    }

    // Conversion to Fahrenheit
    public static double toFahrenheit(double celsiusTemp) {
        return Math.round((celsiusTemp * 9. / 5. + 32) * 10.) / 10.;
    }
}
