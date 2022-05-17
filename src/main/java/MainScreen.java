import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainScreen extends JFrame {
    private JPanel panel;
    private JButton settingsButton;
    private JSlider dateSlider;
    private JLabel actualTempLabel;
    private JLabel feltTempLabel;
    private JLabel weatherIconLabel;
    private JLabel dateLabel;
    private JButton warningButton;

    private LocalDateTime now;
    private DateTimeFormatter dtfHourly;
    private DateTimeFormatter dtfDaily;

    public MainScreen() {
        setSize(450, 700);

        ImageIcon weatherIcon = new ImageIcon(new ImageIcon("src/main/java/Icons/" + APIfetcher.getCurrentIcon()  + ".png")
                .getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
        weatherIconLabel.setIcon(weatherIcon);

        ImageIcon warningIcon = new ImageIcon(new ImageIcon("src/main/java/Icons/Warning.png")
                .getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH));
        warningButton.setIcon(warningIcon);

        actualTempLabel.setText(actualTemp(true));
        feltTempLabel.setText(feltTemp(true));

        this.now = LocalDateTime.now();
        this.dtfHourly = DateTimeFormatter.ofPattern("MM/dd HH:00");
        this.dtfDaily = DateTimeFormatter.ofPattern("MM/dd");

        dateLabel.setText(dtfHourly.format(now));
        dateSlider.addChangeListener(e -> dataSliderListener());


        add(panel);
    }

    private void dataSliderListener() {
        int value = dateSlider.getValue();
        LocalDateTime currentTime = LocalDateTime.now();
        if (value < 48) {
            dateLabel.setText(dtfHourly.format(currentTime.plusHours(value)));
        } else {
            dateLabel.setText(dtfDaily.format(currentTime.plusDays(value - 46)));
        }
    }

    private String actualTemp(boolean isCelsius) {
        double actualTemp = Double.parseDouble(APIfetcher.getCurrentActualTemp());
        String unit = "°C";
        if (!isCelsius) {
            actualTemp = actualTemp * 9. / 5. + 32;
            unit = "°F";
        }
        return Double.toString(Math.round(actualTemp)) + unit;
    }

    private String feltTemp(boolean isCelsius) {
        double feltTemp = Double.parseDouble(APIfetcher.getCurrentFeltTemp());
        String unit = "°C";
        if (!isCelsius) {
            feltTemp = feltTemp * 9. / 5. + 32;
            unit = "°F";
        }
        return Double.toString(Math.round(feltTemp)) + unit;
    }
}
