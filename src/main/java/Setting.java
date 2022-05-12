import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Setting extends JFrame {
    private JLabel notiLabel;
    private JLabel settingLabel;
    private JButton backButton;
    private JLabel tempLabel;
    private JLabel precLabel;
    private JLabel rainLabel;
    private JLabel stormLabel;
    private JCheckBox tempCheckBox;
    private JCheckBox precCheckBox;
    private JCheckBox rainCheckBox;
    private JCheckBox stormCheckBox;
    private JPanel panel;

    final private int CELSIUS = 0;
    final private int FAHRENHEIT = 1;

    final private int ACTUAL_TEMPERATURE = 0;
    final private int FELT_TEMPERATURE = 1;
    final private int BOTH_TEMPERATURES = 2;

    // NOTIFICATION SETTINGS
    private int extremeTemperatureHighC = 30; // units of Celsius
    private int extremeTemperatureLowC = 0; // units of Celsius
    private int extremeTemperatureHighF = 86; // units of Fahrenheit
    private int extremeTemperatureLowF = 32; // units of Fahrenheit
    private int temperatureUnits = CELSIUS; // controls temperature units setting
    private boolean extremeTemperatureNotificationEnabled = false;

    private int extremePrecipitation = 20; // units of mm per hour
    private boolean extremePrecipitationNotificationsEnabled = false;

    private int imminentRainTime = 60; // units of minutes
    private boolean imminentRainNotificationsEnabled = false;

    private boolean stormWarningNotificationsEnabled = false;

    // DISPLAY SETTINGS
    int displayTemperatureTypes = BOTH_TEMPERATURES;

    public Setting() {
        setSize(450, 700);
        add(panel);

        tempCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleExtremeTemperatureNotifications();
            }
        });

        precCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleExtremePrecipitationNotifications();
            }
        });

        rainCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleImminentRainNotifications();
            }
        });

        stormCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleStormWarningNotifications();
            }
        });

    }

    private boolean allSettingsSyncedWithUI() {
        if (this.extremeTemperatureNotificationEnabled != tempCheckBox.isSelected()) {
            return false;
        }
        if (this.extremePrecipitationNotificationsEnabled != precCheckBox.isSelected()) {
            return false;
        }
        if (this.imminentRainNotificationsEnabled != rainCheckBox.isSelected()) {
            return false;
        }
        if (this.stormWarningNotificationsEnabled != stormCheckBox.isSelected()) {
            return false;
        }
        return true;
    }


    private void setNewExtremeHighTemperature(int newHigh) {
        // TODO: Check is valid input
        if (this.temperatureUnits == CELSIUS) { // If temperature is in Celsius
            this.extremeTemperatureHighC = newHigh; // Set Celsius temperature
            this.extremeTemperatureHighF = newHigh * 9 / 5 + 32; // Calculate and set Fahrenheit temperature
        } else if (this.temperatureUnits == FAHRENHEIT) { // If temperature is in Fahrenheit
            this.extremeTemperatureHighF = newHigh; // Set Fahrenheit temperature
            this.extremeTemperatureHighC = (newHigh - 32) * 5 / 9; // Calculate and set Celsius temperature
        }
    }

    private void setNewExtremeLowTemperature(int newLow) {
        // TODO: Check is valid input
        if (this.temperatureUnits == CELSIUS) { // If temperature is in Celsius
            this.extremeTemperatureLowC = newLow; // Set Celsius temperature
            this.extremeTemperatureLowF = newLow * 9 / 5 + 32; // Calculate and set Fahrenheit temperature
        } else if (this.temperatureUnits == FAHRENHEIT) { // If temperature is in Fahrenheit
            this.extremeTemperatureLowF = newLow; // Set Fahrenheit temperature
            this.extremeTemperatureLowC = (newLow - 32) * 5 / 9; // Calculate and set Celsius temperature
        }
    }

    private void setNewExtremePrecipitation(int precipitation) {
        // TODO: Check is valid input
        this.extremePrecipitation = precipitation;
    }

    private void setNewImminentRain(int time) {
        // TODO: Check is valid input
        this.imminentRainTime = time;
    }

    private void toggleExtremeTemperatureNotifications() {
        this.extremeTemperatureNotificationEnabled = !this.extremeTemperatureNotificationEnabled;
    }

    private void toggleExtremePrecipitationNotifications() {
        this.extremePrecipitationNotificationsEnabled = !this.extremePrecipitationNotificationsEnabled;
    }

    private void toggleImminentRainNotifications() {
        this.imminentRainNotificationsEnabled = !this.imminentRainNotificationsEnabled;
    }

    private void toggleStormWarningNotifications() {
        this.stormWarningNotificationsEnabled = !this.stormWarningNotificationsEnabled;
    }

    private void toggleTemperatureUnits() {
        this.temperatureUnits = 1 - this.temperatureUnits;
    }

    private void toggleTemperatureDisplay(int settingID) {
        if (settingID == ACTUAL_TEMPERATURE) {
            this.displayTemperatureTypes = ACTUAL_TEMPERATURE;
        } else if (settingID == FELT_TEMPERATURE) {
            this.displayTemperatureTypes = FELT_TEMPERATURE;
        } else if (settingID == BOTH_TEMPERATURES) {
            this.displayTemperatureTypes = BOTH_TEMPERATURES;
        } else {
            throw new IllegalArgumentException("Invalid temperature display setting " + settingID);
        }
    }
}







//    public static JFrame openWindow() {
//        JFrame frame = new JFrame();
//        frame.setSize(450,700);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().setBackground(new java.awt.Color(204, 166, 166));
//        frame.add(new JLabel(new ImageIcon("Path/To/Your/Image.png")));
//        frame.setTitle("WeatherApp");
//        frame.setVisible(true);
//        return frame;
//    }
//
//    public static void main(String[] args) {
//        JFrame frame = openWindow();
//        Settings settings = new Settings(frame);
//
//    }
//
//    private static JToggleButton createToggleButton() {
//        JToggleButton toggleButton = new JToggleButton("ON");
//        toggleButton.setForeground(Color.BLUE);
//
//        toggleButton.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if (toggleButton.isSelected()) {
//                    toggleButton.setText("OFF");
//                    toggleButton.setForeground(Color.BLACK);
//                } else {
//                    toggleButton.setText("ON");
//                    toggleButton.setForeground(Color.BLUE);
//                }
//            }
//        });
//
//        toggleButton.setSize(15, 15);
//
//        return toggleButton;
//    }
//}
