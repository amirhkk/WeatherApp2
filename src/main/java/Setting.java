import javax.swing.*;
import java.awt.event.*;

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
    private JTextField minTempText;
    private JTextField maxTempText;
    private JToggleButton precButton;
    private JToggleButton tempButton;
    private JPanel tempPanel;
    private JPanel precPanel;
    private JTextField maxPrecText;
    private JToggleButton rainButton;
    private JPanel rainPanel;
    private JTextField rainTimeText;

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

        tempButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(tempButton.isSelected()) {
                    tempPanel.setVisible(true);
                } else {
                    tempPanel.setVisible(false);
                }

            }
        });
        precButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(precButton.isSelected()) {
                    precPanel.setVisible(true);
                } else {
                    precPanel.setVisible(false);
                }
            }
        });

        rainButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(rainButton.isSelected()) {
                    rainPanel.setVisible(true);
                } else {
                    rainPanel.setVisible(false);
                }
            }
        });

        maxTempText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setNewExtremeHighTemperature(maxTempText.getText());
            }
        });

        minTempText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setNewExtremeLowTemperature(minTempText.getText());
            }
        });

        maxPrecText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setNewExtremePrecipitation(maxPrecText.getText());
            }
        });

        rainTimeText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setNewImminentRain(rainTimeText.getText());
            }
        });

        tempPanel.setVisible(false);
        precPanel.setVisible(false);
        rainPanel.setVisible(false);
        add(panel);
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

    private void setNewExtremeHighTemperature(String newHigh) {
        if (this.temperatureUnits == CELSIUS) {
            try {
                int parsed_newHigh = (int) (Double.parseDouble(newHigh) + 0.5);
                if (parsed_newHigh > 35) {
                    parsed_newHigh = 35;
                } else if (parsed_newHigh <= this.extremeTemperatureLowC) {
                    parsed_newHigh = this.extremeTemperatureLowC + 1;
                }
                this.extremeTemperatureHighC = parsed_newHigh;
                this.extremeTemperatureHighF = (int) (((double) parsed_newHigh) * 9.0 / 5.0 + 32.0 + 0.5);
                this.maxTempText.setText("" + parsed_newHigh);
            } catch (NumberFormatException e) {
                this.maxTempText.setText("" + this.extremeTemperatureHighC);
            }
        } else if (this.temperatureUnits == FAHRENHEIT) {
            try {
                int parsed_newHigh = (int) (Double.parseDouble(newHigh) + 0.5);
                if (parsed_newHigh > 95) {
                    parsed_newHigh = 95;
                } else if (parsed_newHigh <= this.extremeTemperatureLowF) {
                    parsed_newHigh = this.extremeTemperatureLowF + 1;
                }
                this.extremeTemperatureHighF = parsed_newHigh;
                this.extremeTemperatureHighC = (int) ((((double) parsed_newHigh) - 32.0) * 5.0 / 9.0 + 0.5);
                this.maxTempText.setText("" + parsed_newHigh);
            } catch (NumberFormatException e) {
                this.maxTempText.setText("" + this.extremeTemperatureHighF);
            }
        }
    }

    private void setNewExtremeLowTemperature(String newLow) {
        if (temperatureUnits == CELSIUS) {
            try {
                int parsed_newLow = (int) (Double.parseDouble(newLow) + 0.5);
                if (parsed_newLow >= extremeTemperatureHighC) {
                    parsed_newLow = extremeTemperatureHighC - 1;
                } else if (parsed_newLow < -15) {
                    parsed_newLow = -15;
                }
                extremeTemperatureLowC = parsed_newLow;
                extremeTemperatureLowF = (int) (((double) parsed_newLow) * 9.0 / 5.0 + 32.0 + 0.5);
                minTempText.setText("" + parsed_newLow);
            } catch (NumberFormatException e) {
                minTempText.setText("" + extremeTemperatureLowC);
            }
        } else if (temperatureUnits == FAHRENHEIT) {
            try {
                int parsed_newLow = (int) (Double.parseDouble(newLow) + 0.5);
                if (parsed_newLow >= extremeTemperatureHighF) {
                    parsed_newLow = extremeTemperatureHighF - 1;
                } else if (parsed_newLow < 5) {
                    parsed_newLow = 5;
                }
                extremeTemperatureLowF = parsed_newLow;
                extremeTemperatureLowC = (int) ((((double) parsed_newLow) - 32.0) * 5.0 / 9.0 + 0.5);
                minTempText.setText("" + parsed_newLow);
            } catch (NumberFormatException e) {
                minTempText.setText("" + extremeTemperatureLowF);
            }
        }
    }

    private void setNewExtremePrecipitation(String precipitation) {
        try {
            int parsed_precipitation = (int) (Double.parseDouble(precipitation) + 0.5);
            if (parsed_precipitation < 1) {
                parsed_precipitation = 1;
            } else if (parsed_precipitation > 60) {
                parsed_precipitation = 60;
            }
            extremePrecipitation = parsed_precipitation;
            maxPrecText.setText("" + parsed_precipitation);
        } catch (NumberFormatException e) {
            maxPrecText.setText("" + extremePrecipitation);
        }
    }

    private void setNewImminentRain(String time) {
        try {
            int parsed_time = (int) (Double.parseDouble(time) + 0.5);
            if (parsed_time < 2) {
                parsed_time = 2;
            } else if (parsed_time > 300) {
                parsed_time = 300;
            }
            imminentRainTime = parsed_time;
            rainTimeText.setText("" + parsed_time);
        } catch (NumberFormatException e) {
            rainTimeText.setText("" + imminentRainTime);
        }
    }

    private void toggleExtremeTemperatureNotifications() {
        extremeTemperatureNotificationEnabled = !extremeTemperatureNotificationEnabled;
    }

    private void toggleExtremePrecipitationNotifications() {
        extremePrecipitationNotificationsEnabled = !extremePrecipitationNotificationsEnabled;
    }

    private void toggleImminentRainNotifications() {
        imminentRainNotificationsEnabled = !imminentRainNotificationsEnabled;
    }

    private void toggleStormWarningNotifications() {
        stormWarningNotificationsEnabled = !stormWarningNotificationsEnabled;
    }

    private void toggleTemperatureUnits() {
        temperatureUnits = 1 - temperatureUnits;
        if (temperatureUnits == CELSIUS) {
            minTempText.setText("" + extremeTemperatureLowC);
            maxTempText.setText("" + extremeTemperatureHighC);
        } else if (temperatureUnits == FAHRENHEIT) {
            minTempText.setText("" + extremeTemperatureLowF);
            maxTempText.setText("" + extremeTemperatureHighF);
        }
    }

    private void toggleTemperatureDisplay(int settingID) {
        if (settingID == ACTUAL_TEMPERATURE) {
            displayTemperatureTypes = ACTUAL_TEMPERATURE;
        } else if (settingID == FELT_TEMPERATURE) {
            displayTemperatureTypes = FELT_TEMPERATURE;
        } else if (settingID == BOTH_TEMPERATURES) {
            displayTemperatureTypes = BOTH_TEMPERATURES;
        } else {
            throw new IllegalArgumentException("Invalid temperature display setting " + settingID);
        }
    }

    public int getTemperatureUnits() {
        return temperatureUnits;
    }

    public int getDisplayTemperatureTypes() {
        return displayTemperatureTypes;
    }
}






