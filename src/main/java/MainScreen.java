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
    private ImageIcon weatherIcon;
    private ImageIcon warningIcon;

    public MainScreen() {
        setSize(450, 700);

        weatherIcon = new ImageIcon("src/main/java/Icons/02d.png");
        weatherIcon = new ImageIcon(weatherIcon.getImage().getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH));
        weatherIconLabel.setIcon(weatherIcon);

        warningIcon = new ImageIcon("src/main/java/Icons/Warning.png");
        warningIcon = new ImageIcon(warningIcon.getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH));
        warningButton.setIcon(warningIcon);

        String actualTemp = APIfetcher.getCurrentActualTemp();
        String feltTemp = APIfetcher.getCurrentFeltTemp();
        actualTempLabel.setText(actualTemp + "°C");
        feltTempLabel.setText(feltTemp + "°C");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtfHourly = DateTimeFormatter.ofPattern("MM/dd HH:00");
        DateTimeFormatter dtfDaily = DateTimeFormatter.ofPattern("MM/dd");
        dateLabel.setText(dtfHourly.format(now));

        dateSlider.addChangeListener(e -> {
            int value = dateSlider.getValue();
            LocalDateTime currentTime = LocalDateTime.now();
            if (value < 48) {
                dateLabel.setText(dtfHourly.format(currentTime.plusHours(value)));
            } else {
                dateLabel.setText(dtfDaily.format(currentTime.plusDays(value - 46)));
            }
        });


        add(panel);
    }
}
