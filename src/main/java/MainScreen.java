import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

public class MainScreen extends JFrame {
    private JPanel panel;
    private JButton settingsButton;
    private JSlider slider1;
    private JLabel warningLabel;
    private JLabel actualTempLabel;
    private JLabel feltTempLabel;
    private JLabel weatherIconLabel;
    private JLabel dateLabel;
    private ImageIcon weatherIcon;

    public MainScreen() {
        setSize(450, 700);
        try {
            weatherIcon = new ImageIcon(new URL("https://cdn-icons-png.flaticon.com/512/4052/4052984.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        weatherIconLabel.setIcon(weatherIcon);

        String actualTemp = APIfetcher.getCurrentActualTemp();
        String feltTemp = APIfetcher.getCurrentFeltTemp();
        actualTempLabel.setText(actualTemp);
        feltTempLabel.setText(feltTemp);

        add(panel);
    }
}
