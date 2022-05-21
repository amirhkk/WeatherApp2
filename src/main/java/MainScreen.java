import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private final DateTimeFormatter dtfHourly;
    private final DateTimeFormatter dtfDaily;

    public MainScreen(RootScreen parent, Setting settingScreen) {
        setSize(450, 700);
        this.settingScreen = settingScreen;

        ImageIcon settingsIcon = new ImageIcon(new ImageIcon("src/main/java/Icons/gear.png")
                .getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH));
        settingsButton.setIcon(settingsIcon);

        ImageIcon warningIcon = new ImageIcon(new ImageIcon("src/main/java/Icons/Warning.png")
                .getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH));
        warningButton.setIcon(warningIcon);

        this.dtfHourly = DateTimeFormatter.ofPattern("MM/dd HH:00");
        this.dtfDaily = DateTimeFormatter.ofPattern("MM/dd");

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
        WeatherRecord<String> weatherRecord = forecast(value);
        actualTempLabel.setText(weatherRecord.actualTemp());
        feltTempLabel.setText(" " + weatherRecord.feltTemp());
        setTemperatureDisplay();

        weatherIconLabel.setIcon(getWeatherIcon(weatherRecord.icon()));

        LocalDateTime currentTime = LocalDateTime.now();
        if (value < 48) {
            dateLabel.setText(dtfHourly.format(currentTime.plusHours(value)));
        } else {
            dateLabel.setText(dtfDaily.format(currentTime.plusDays(value - 46)));
        }
    }

    private ImageIcon getWeatherIcon(String icon) {
        return new ImageIcon(new ImageIcon("src/main/java/Icons/" + icon + ".png")
                .getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
    }

    private WeatherRecord<String> forecast(int timeIndex) {
        Weather forecast = APIfetcher.getForecast(timeIndex, settingScreen);
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
