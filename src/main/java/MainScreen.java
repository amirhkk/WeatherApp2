import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MainScreen extends JFrame {
    private JPanel mainPanel;
    private JButton settingsButton;
    private JSlider dateSlider;
    private JLabel actualTempLabel;
    private JLabel feltTempLabel;
    private JLabel weatherIconLabel;
    private JLabel dateLabel;
    private JButton warningButton;
    private JLabel feltTextLabel;
    private JLabel actualTextLabel;

    private final Setting settingScreen;

    private final DateTimeFormatter dtfHourly = DateTimeFormatter.ofPattern("MM/dd HH:00");
    private final DateTimeFormatter dtfDaily = DateTimeFormatter.ofPattern("MM/dd");

    public MainScreen(RootScreen parent, Setting settingScreen) {
        setSize(450, 700);
        this.settingScreen = settingScreen;

        setButtonIcon(settingsButton, "gear");

        dateSlider.addChangeListener(e -> refresh());
        settingsButton.addActionListener(e -> parent.goSetting());
        warningButton.addActionListener(e -> parent.goWarning());

        refresh();
    }

    public void refresh() {
        int value = dateSlider.getValue();
        if (value > 47) {
            value = 47 + (value - 47) / 8;
        }

        LocalDateTime currentTime = LocalDateTime.now();
        if (value < 48) {
            dateLabel.setText(dtfHourly.format(currentTime.plusHours(value)));
        } else {
            dateLabel.setText(dtfDaily.format(currentTime.plusDays(value - 46)));
        }

        if (APIfetcher.hasError()) {
            weatherIconLabel.setIcon(getWeatherIcon("Warning"));

            warningButton.setIcon(null);
            warningButton.setEnabled(false);
            warningButton.setVisible(true);
            warningButton.setText("<html>Connection Error!</html>");

            actualTempLabel.setVisible(false);
            actualTextLabel.setVisible(false);
            feltTempLabel.setVisible(false);
            feltTextLabel.setVisible(false);
        } else {
            WeatherRecord<String> weatherRecord = forecast(value);

            actualTempLabel.setText(weatherRecord.actualTemp());
            feltTempLabel.setText(" " + weatherRecord.feltTemp());
            setTemperatureDisplay();

            setButtonIcon(warningButton, "Warning");
            warningButton.setEnabled(true);

            weatherIconLabel.setIcon(getWeatherIcon(weatherRecord.icon()));
        }
    }

    private ImageIcon getWeatherIcon(String icon) {
        return new ImageIcon(new ImageIcon("src/main/java/Icons/" + icon + ".png")
                .getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
    }

    private void setButtonIcon(JButton button, String icon) {
        button.setIcon(new ImageIcon(new ImageIcon("src/main/java/Icons/" + icon + ".png")
                .getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH)));
    }

    private record WeatherRecord<T>(T actualTemp, T feltTemp, T icon) {}

    private WeatherRecord<String> forecast(int timeIndex) {
        Weather forecast = APIfetcher.getForecast(timeIndex, settingScreen);
        setWarningDisplay(forecast);
        double actualTemp = forecast.getTemp();
        double feltTemp = forecast.getFelt();
        String unit = "°C";
        if (settingScreen.getTemperatureUnits() == settingScreen.FAHRENHEIT) {
            actualTemp = Weather.toFahrenheit(actualTemp);
            feltTemp = Weather.toFahrenheit(feltTemp);
            unit = "°F";
        }
        return new WeatherRecord<>(actualTemp + unit, feltTemp + unit, forecast.getIcon());
    }

    private void setWarningDisplay(Weather forecast) {
        Map<Alerts, Boolean> alerts = forecast.getAlerts();
        StringBuilder stringBuilder = new StringBuilder();
        if (alerts.get(Alerts.STORM_SOON)) {
            stringBuilder.append("\uD83C\uDF29️");
        }
        if (alerts.get(Alerts.HEAVY_RAIN)) {
            stringBuilder.append("\uD83C\uDF27️");
        }
        if (alerts.get(Alerts.RAIN_SOON)) {
            stringBuilder.append("☂️");
        }
        if (alerts.get(Alerts.HIGH_TEMP)) {
            stringBuilder.append("\uD83D\uDD25");
        }
        if (alerts.get(Alerts.LOW_TEMP)) {
            stringBuilder.append("❄");
        }

        if (stringBuilder.length() == 0) {
            warningButton.setVisible(false);
        } else {
            warningButton.setText("Warnings: "+ stringBuilder);
            warningButton.setVisible(true);
        }
    }

    private void setTemperatureDisplay() {
        int displayTempTypes = settingScreen.getDisplayTemperatureTypes();
        if (displayTempTypes == settingScreen.BOTH_TEMPERATURES) {
            actualTempLabel.setVisible(true);
            actualTextLabel.setVisible(true);
            feltTempLabel.setVisible(true);
            feltTextLabel.setVisible(true);
        } else if (displayTempTypes == settingScreen.ACTUAL_TEMPERATURE) {
            actualTempLabel.setVisible(true);
            actualTextLabel.setVisible(true);
            feltTempLabel.setVisible(false);
            feltTextLabel.setVisible((false));
        } else { // displayTempTypes == settingScreen.FELT_TEMPERATURE
            actualTempLabel.setVisible(false);
            actualTextLabel.setVisible(false);
            feltTempLabel.setVisible(true);
            feltTextLabel.setVisible(true);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
