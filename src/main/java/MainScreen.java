import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private LocalDateTime now;
    private DateTimeFormatter dtfHourly;
    private DateTimeFormatter dtfDaily;

    public MainScreen(RootScreen parent) {
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

        warningButton.addActionListener(e -> parent.goWarning());
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
        double actualTemp = APIfetcher.getCurrentActualTemp();
        String unit = "°C";
        if (!isCelsius) {
            actualTemp = actualTemp * 9. / 5. + 32;
            unit = "°F";
        }
        return Double.toString(Math.round(actualTemp)) + unit;
    }

    private String feltTemp(boolean isCelsius) {
        double feltTemp = APIfetcher.getCurrentFeltTemp();
        String unit = "°C";
        if (!isCelsius) {
            feltTemp = feltTemp * 9. / 5. + 32;
            unit = "°F";
        }
        return Double.toString(Math.round(feltTemp)) + unit;
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JButton getWarningButton() {
        return warningButton;
    }
}
