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

    private final LocalDateTime now;
    private final DateTimeFormatter dtfHourly;
    private final DateTimeFormatter dtfDaily;

    public MainScreen(RootScreen parent) {
        setSize(450, 700);

        ImageIcon weatherIcon = new ImageIcon(new ImageIcon("src/main/java/Icons/" + APIfetcher.getCurrentIcon()  + ".png")
                .getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
        weatherIconLabel.setIcon(weatherIcon);

        ImageIcon warningIcon = new ImageIcon(new ImageIcon("src/main/java/Icons/Warning.png")
                .getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH));
        warningButton.setIcon(warningIcon);

        TempPair<String> tempPair = forecast(0, true);
        actualTempLabel.setText(tempPair.getActualTemp());
        feltTempLabel.setText(tempPair.getFeltTemp());

        this.now = LocalDateTime.now();
        this.dtfHourly = DateTimeFormatter.ofPattern("MM/dd HH:00");
        this.dtfDaily = DateTimeFormatter.ofPattern("MM/dd");

        dateLabel.setText(dtfHourly.format(now));
        dateSlider.addChangeListener(e -> dataSliderListener());

        settingsButton.addActionListener(e -> parent.goSetting());
        warningButton.addActionListener(e -> parent.goWarning());
    }

    private void dataSliderListener() {
        int value = dateSlider.getValue();
        TempPair<String> tempPair = forecast(value, true);
        actualTempLabel.setText(tempPair.getActualTemp());
        feltTempLabel.setText(tempPair.getFeltTemp());
        LocalDateTime currentTime = LocalDateTime.now();
        if (value < 48) {
            dateLabel.setText(dtfHourly.format(currentTime.plusHours(value)));
        } else {
            dateLabel.setText(dtfDaily.format(currentTime.plusDays(value - 46)));
        }
    }

    private TempPair<String> forecast(int timeIndex, boolean isCelsius) {
        Map<String, Double> forecast = APIfetcher.getForecast(timeIndex);
        double actualTemp = forecast.get("Actual");
        double feltTemp = forecast.get("Felt");
        String unit = "°C";
        if (!isCelsius) {
            actualTemp = toFahrenheit(actualTemp);
            feltTemp = toFahrenheit(actualTemp);
            unit = "°F";
        }
        actualTemp = Math.round(actualTemp);
        feltTemp = Math.round(feltTemp);
        return new TempPair<String>(actualTemp + unit, feltTemp + unit);
    }

    private double toFahrenheit(double celsiusTemp) {
        return celsiusTemp * 9. / 5. + 32;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
