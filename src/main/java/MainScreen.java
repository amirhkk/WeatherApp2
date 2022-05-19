import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final Setting settingScreen;
    private final DateTimeFormatter dtfHourly;
    private final DateTimeFormatter dtfDaily;

    public MainScreen(RootScreen parent, Setting settingScreen) {
        setSize(450, 700);
        this.settingScreen = settingScreen;

        ImageIcon weatherIcon = new ImageIcon(new ImageIcon("src/main/java/Icons/" + APIfetcher.getCurrentIcon()  + ".png")
                .getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
        weatherIconLabel.setIcon(weatherIcon);

        ImageIcon warningIcon = new ImageIcon(new ImageIcon("src/main/java/Icons/Warning.png")
                .getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH));
        warningButton.setIcon(warningIcon);

        TemperatureRecord<String> temperatureRecord = forecast(0);
        actualTempLabel.setText(temperatureRecord.actualTemp());
        feltTempLabel.setText(temperatureRecord.feltTemp());

        LocalDateTime now = LocalDateTime.now();
        this.dtfHourly = DateTimeFormatter.ofPattern("MM/dd HH:00");
        this.dtfDaily = DateTimeFormatter.ofPattern("MM/dd");

        dateLabel.setText(dtfHourly.format(now));
        dateSlider.addChangeListener(e -> dataSliderListener());

        settingsButton.addActionListener(e -> parent.goSetting());
        warningButton.addActionListener(e -> parent.goWarning());
    }

    private void dataSliderListener() {
        int value = dateSlider.getValue();
        TemperatureRecord<String> temperatureRecord = forecast(value);
        actualTempLabel.setText(temperatureRecord.actualTemp());
        feltTempLabel.setText(temperatureRecord.feltTemp());
        LocalDateTime currentTime = LocalDateTime.now();
        if (value < 48) {
            dateLabel.setText(dtfHourly.format(currentTime.plusHours(value)));
        } else {
            dateLabel.setText(dtfDaily.format(currentTime.plusDays(value - 46)));
        }
    }

    private TemperatureRecord<String> forecast(int timeIndex) {
        Map<String, Double> forecast = APIfetcher.getForecast(timeIndex);
        double actualTemp = forecast.get("Actual");
        double feltTemp = forecast.get("Felt");
        String unit = "°C";
        if (settingScreen.getTemperatureUnits() == settingScreen.FAHRENHEIT) {
            actualTemp = toFahrenheit(actualTemp);
            feltTemp = toFahrenheit(actualTemp);
            unit = "°F";
        }
        actualTemp = Math.round(actualTemp);
        feltTemp = Math.round(feltTemp);
        return new TemperatureRecord<>(actualTemp + unit, feltTemp + unit);
    }

    private double toFahrenheit(double celsiusTemp) {
        return celsiusTemp * 9. / 5. + 32;
    }

    public void refresh() {
        dataSliderListener();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
