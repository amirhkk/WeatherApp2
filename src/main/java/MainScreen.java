import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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

    public MainScreen() {
        setSize(450, 700);
        weatherIcon = new ImageIcon("src/main/java/Icons/Sun.png");
        Image image = weatherIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        weatherIcon = new ImageIcon(newimg);
        weatherIconLabel.setIcon(weatherIcon);

        String actualTemp = APIfetcher.getCurrentActualTemp();
        String feltTemp = APIfetcher.getCurrentFeltTemp();
        actualTempLabel.setText(actualTemp);
        feltTempLabel.setText(feltTemp);

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
