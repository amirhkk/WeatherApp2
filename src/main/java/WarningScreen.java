import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class WarningScreen extends JFrame {
    private JButton exitButton;
    private JPanel warningPanel;
    private JLabel temperatureWarningLabel;
    private JLabel precipitationWarningLabel;
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
        Weather forecast = APIfetcher.getForecast(0, settingScreen);
        Map<Alerts, Boolean> alerts = forecast.getAlerts();

        if (alerts.get(Alerts.HIGHTEMP) || alerts.get(Alerts.LOWTEMP)) {
            double actualTemp = forecast.getTemp();
            String unit = "°C";
            if (settingScreen.getTemperatureUnits() == settingScreen.FAHRENHEIT) {
                actualTemp = Weather.toFahrenheit(actualTemp);
                unit = "°F";
            }
            temperatureWarningLabel.setText("Temperature alert: " + actualTemp + unit);
            temperatureWarningLabel.setForeground(new Color(187, 31, 65));
        } else {
            temperatureWarningLabel.setText("No temperature warnings.");
            temperatureWarningLabel.setForeground(new Color(55, 140, 84));
        }
    }
}
