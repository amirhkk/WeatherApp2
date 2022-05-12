import javax.swing.*;
import java.awt.*;



public class Settings {

    final private int CELSIUS = 0;
    final private int FAHRENHEIT = 1;

    JFrame frame;
    int extremeTemperatureHigh = 30; // units of temperatureUnits
    int extremeTemperatureLow = 0; // units of temperatureUnits
    int temperatureUnits = CELSIUS;
    int extremePrecipitation = 20; // units of mm per hour
    int imminentRain = 60; // units of minutes
    boolean stormWarning = true; // 0 = no notification, 1 = yes notification


    public Settings(JFrame frame) {
        this.frame = frame;


    }

    public void showSettings() {
        // Have frame pop up with values, enable input
        // Sett
    }


    public static JFrame openWindow() {
        JFrame frame = new JFrame();
        frame.setSize(450,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new java.awt.Color(204, 166, 166));
        frame.add(new JLabel(new ImageIcon("Path/To/Your/Image.png")));
        frame.setTitle("WeatherApp");
        frame.setVisible(true);
        return frame;
    }

    public static void main(String[] args) {
        JFrame frame = openWindow();
        Settings settings = new Settings(frame);

    }
}
