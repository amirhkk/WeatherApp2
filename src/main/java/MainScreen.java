import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Main screen that will display important weather information
 */
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

    /**
     * Constructor for Main Screen
     * @param parent The parent screen that contains the main panel
     * @param settingScreen The settings screen
     */
    public MainScreen(RootScreen parent, Setting settingScreen) {
        // set the size of the main screen
        setSize(450, 700);
        this.settingScreen = settingScreen;

        // set the settings button icon to a gear icon
        setButtonIcon(settingsButton, "gear");

        // add action listeners to the date slider, settings button, and warning button
        dateSlider.addChangeListener(e -> refresh());
        settingsButton.addActionListener(e -> parent.goSetting());
        warningButton.addActionListener(e -> parent.goWarning());

        // call refresh to get weather information
        refresh();
    }

    /**
     * Refreshes main screen and gets weather information
     */
    public void refresh() {
        // Gets the value of the date slider, and adjusts values 0-87 -> 0-52
        // 0-47: hours from current
        // 48-79: adjusted to become 48-52, which is equivalent to 2-7 days from current
        int value = dateSlider.getValue();
        if (value > 47) {
            value = 48 + (value - 47) / 8;
        }

        // Sets correct date display
        LocalDateTime currentTime = LocalDateTime.now();
        if (value < 48) {
            dateLabel.setText(dtfHourly.format(currentTime.plusHours(value)));
        } else {
            dateLabel.setText(dtfDaily.format(currentTime.plusDays(value - 46)));
        }

        // Attempts to establish connection to API
        // If it succeeds, display weather information
        // If it fails, show connection error
        if (APIfetcher.establishConnection()) {
            WeatherRecord weatherRecord = forecast(value);

            actualTempLabel.setText(weatherRecord.actualTemp());
            feltTempLabel.setText(" " + weatherRecord.feltTemp());
            setTemperatureDisplay();

            setWarningDisplay();
            setButtonIcon(warningButton, "Warning");
            warningButton.setEnabled(true);

            weatherIconLabel.setIcon(getWeatherIcon(weatherRecord.icon()));
        } else {
            weatherIconLabel.setIcon(getWeatherIcon("Warning"));

            warningButton.setIcon(null);
            warningButton.setEnabled(false);
            warningButton.setVisible(true);
            warningButton.setText("<html>Connection Error!</html>");

            actualTempLabel.setVisible(false);
            actualTextLabel.setVisible(false);
            feltTempLabel.setVisible(false);
            feltTextLabel.setVisible(false);
        }
    }

    /**
     * Formats the weather icon
     * @param icon name of icon file
     * @return scaled image icon (150x150)
     */
    private ImageIcon getWeatherIcon(String icon) {
        return new ImageIcon(new ImageIcon("src/main/java/Icons/" + icon + ".png")
                .getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
    }

    /**
     * Sets the button icon to a 40x40 image
     * @param button JButton
     * @param icon name of icon file
     */
    private void setButtonIcon(JButton button, String icon) {
        button.setIcon(new ImageIcon(new ImageIcon("src/main/java/Icons/" + icon + ".png")
                .getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH)));
    }

    /**
     * Weather record for holding temperature and weather icon information
     */
    private record WeatherRecord(String actualTemp, String feltTemp, String icon) {}

    /**
     * Gets weather forecast and formats temperature based on celsius or fahrenheit
     * @param timeIndex value of date slider
     * @return weather record containing temperature and weather icon info
     */
    private WeatherRecord forecast(int timeIndex) {
        Weather forecast = APIfetcher.getForecast(timeIndex, settingScreen);
        double actualTemp = forecast.getTemp();
        double feltTemp = forecast.getFelt();
        String unit = "°C";
        if (settingScreen.getTemperatureUnits() == settingScreen.FAHRENHEIT) {
            actualTemp = Weather.toFahrenheit(actualTemp);
            feltTemp = Weather.toFahrenheit(feltTemp);
            unit = "°F";
        }
        return new WeatherRecord(actualTemp + unit, feltTemp + unit, forecast.getIcon());
    }

    /**
     * Set the warning display based on the weather 1 hour into the future
     * If there are alerts, add corresponding emojis to the warning button
     * If there are no alerts, hide the warning button
     */
    private void setWarningDisplay() {
        Map<Alerts, Boolean> alerts = APIfetcher.getForecast(1, settingScreen).getAlerts();
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

    /**
     * Set the temperature display based on settings (actual, felt, both)
     */
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

    /**
     * @return panel of main screen
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
