import java.util.Map;

public class Weather {
    // Actual temperature
    private final Double temp;
    // Felt temperature
    private final Double felt;
    // Icon png name
    private final String icon;
    // rain in mm
    private final Double rain;
    // Map of alerts
    private final Map<Alerts, Boolean> alerts;

    // Public constructor only way to set variables (except adding alerts)
    public Weather(Double temp, Double felt, String icon, Double rain, Map<Alerts, Boolean> alerts) {
        this.temp = temp;
        this.felt = felt;
        this.icon = icon;
        this.rain = rain;
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

    // Public access to rain
    public double getRain(){
        return rain;
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
