import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Screen for showing details on warnings
 */
public class WarningScreen extends JFrame {
    private JButton exitButton;
    private JPanel warningPanel;
    private JTextArea warningTextArea;
    private final Setting settingScreen;

    /**
     * Constructor for warning screen
     * Add action listener to the exit button so that it goes to the main screen
     * Refresh the warning screen
     * @param parent the root screen containing the current panel
     * @param settingScreen the Setting screen
     */
    public WarningScreen(RootScreen parent, Setting settingScreen) {
        this.settingScreen = settingScreen;
        exitButton.addActionListener(e -> parent.goMain());
        refresh();
    }

    /**
     * @return the warning panel
     */
    public JPanel getWarningPanel() {
        return warningPanel;
    }

    /**
     * Refresh the warning screen to get updated weather information
     * Attempt to establish a connection to the API
     * If success, get the forecast from 1 hour later and check alerts
     * Add appropriate warning details and display them
     */
    public void refresh() {
        if (APIfetcher.establishConnection()) {
            Weather forecast = APIfetcher.getForecast(1, settingScreen);
            Map<Alerts, Boolean> alerts = forecast.getAlerts();
            StringBuilder stringBuilder = new StringBuilder();

            warningTextArea.setForeground(new Color(187, 31, 65));

            if (alerts.get(Alerts.STORM_SOON)) {
                stringBuilder.append("\nThunderstorm warning.\n");
            }

            if (alerts.get(Alerts.HEAVY_RAIN)) {
                stringBuilder.append("\nHeavy rain (").append(forecast.getRain()).append(" mm/h) within the next hour.\n");
            } else if (alerts.get(Alerts.RAIN_SOON)) {
                stringBuilder.append("\nRain (").append(forecast.getRain()).append(" mm/h) within the next hour.\n");
            }

            if (alerts.get(Alerts.HIGH_TEMP) || alerts.get(Alerts.LOW_TEMP)) {
                double actualTemp = forecast.getTemp();
                String unit = "°C";
                if (settingScreen.getTemperatureUnits() == settingScreen.FAHRENHEIT) {
                    actualTemp = Weather.toFahrenheit(actualTemp);
                    unit = "°F";
                }
                stringBuilder.append("\nExtreme temperatures (").append(actualTemp).append(unit).append(") within the next hour.\n");
                warningTextArea.setText(stringBuilder.toString());
            }

            warningTextArea.setText(stringBuilder.toString());
        } else {
            warningTextArea.setText("");
        }

    }
}
