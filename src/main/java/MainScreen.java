import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

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

        weatherIcon = new ImageIcon("src/main/java/Icons/SunCloudy.png");
        weatherIcon = new ImageIcon(weatherIcon.getImage().getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH));
        weatherIconLabel.setIcon(weatherIcon);

        warningIcon = new ImageIcon("src/main/java/Icons/Warning.png");
        warningIcon = new ImageIcon(warningIcon.getImage().getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH));
        warningButton.setIcon(warningIcon);

        String actualTemp = APIfetcher.getCurrentActualTemp();
        String feltTemp = APIfetcher.getCurrentFeltTemp();
        actualTempLabel.setText(actualTemp);
        feltTempLabel.setText(feltTemp);

        add(panel);
    }
}
