import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class WarningScreen extends JFrame {
    private JButton exitButton;
    private JPanel warningPanel;
    private JTextArea warningTextArea;
    private final Setting settingScreen;

    public WarningScreen(RootScreen parent, Setting settingScreen) {
        this.settingScreen = settingScreen;
        exitButton.addActionListener(e -> parent.goMain());
        refresh();
    }

    public JPanel getWarningPanel() {
        return warningPanel;
    }

    public void refresh() {
        if (!APIfetcher.hasError()) {
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
