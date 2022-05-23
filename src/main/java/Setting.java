import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.event.*;
import java.util.Calendar;

/**
 * Setting frame class
 */
public class Setting extends JFrame {
    // Swing elements that are bound to the GUI form
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
    private JPanel settingPanel;
    private JTextField minTempText;
    private JTextField maxTempText;
    private JToggleButton precButton;
    private JToggleButton tempButton;
    private JPanel tempPanel;
    private JPanel precPanel;
    private JTextField maxPrecText;
    private JRadioButton fRadioButton;
    private JRadioButton cRadioButton;
    private JLabel formatLabel;
    private JRadioButton bothRadioButton;
    private JRadioButton actualRadioButton;
    private JRadioButton feltRadioButton;
    private JLabel tempUnitsLabel;
    private JLabel dispTempLabel;
    private JCheckBox mondayCheckBox;
    private JCheckBox tuesdayCheckBox;
    private JCheckBox wednesdayCheckBox;
    private JCheckBox thursdayCheckBox;
    private JCheckBox fridayCheckBox;
    private JCheckBox saturdayCheckBox;
    private JCheckBox sundayCheckBox;
    private JSpinner mondaySpinner;
    private JSpinner tuesdaySpinner;
    private JSpinner wednesdaySpinner;
    private JSpinner thursdaySpinner;
    private JSpinner fridaySpinner;
    private JSpinner saturdaySpinner;
    private JSpinner sundaySpinner;
    private JLabel customNotiLabel;
    private JPanel customNotiPanel;
    private ButtonGroup tempUnitGroup = new ButtonGroup();
    private ButtonGroup displayTempGroup = new ButtonGroup();

    // TEMPERATURE SETTINGS
    final public int CELSIUS = 0;
    final public int FAHRENHEIT = 1;

    final public int ACTUAL_TEMPERATURE = 0;
    final public int FELT_TEMPERATURE = 1;
    final public int BOTH_TEMPERATURES = 2;

    // NOTIFICATION SETTINGS
    private int extremeTemperatureHighC = 30; // units of Celsius
    private int extremeTemperatureLowC = 0; // units of Celsius
    private int extremeTemperatureHighF = 86; // units of Fahrenheit
    private int extremeTemperatureLowF = 32; // units of Fahrenheit
    private int temperatureUnits = CELSIUS; // controls temperature units setting
    private boolean extremeTemperatureNotificationEnabled = true;

    private int extremePrecipitation = 20; // units of mm per hour
    private boolean extremePrecipitationNotificationsEnabled = true;

    private boolean imminentRainNotificationsEnabled = true;

    private boolean stormWarningNotificationsEnabled = true;

    // DISPLAY SETTINGS
    int displayTemperatureTypes = BOTH_TEMPERATURES;

    public Setting(RootScreen parent) {
        // Set size of the screen
        setSize(450, 700);

        // Action listener for back button to go to main screen
        backButton.addActionListener(e -> parent.goMain());

        // Action listeners for notification toggle checkboxes
        tempCheckBox.addActionListener(e -> toggleExtremeTemperatureNotifications());
        precCheckBox.addActionListener(e -> toggleExtremePrecipitationNotifications());
        rainCheckBox.addActionListener(e -> toggleImminentRainNotifications());
        stormCheckBox.addActionListener(e -> toggleStormWarningNotifications());

        // Set checkboxes to be pre-selected
        tempCheckBox.setSelected(extremeTemperatureNotificationEnabled);
        precCheckBox.setSelected(extremePrecipitationNotificationsEnabled);
        rainCheckBox.setSelected(imminentRainNotificationsEnabled);
        stormCheckBox.setSelected(stormWarningNotificationsEnabled);

        // Action listener to display additional options if the button is clicked
        tempButton.addItemListener(e -> tempPanel.setVisible(tempButton.isSelected()));
        precButton.addItemListener(e -> precPanel.setVisible(precButton.isSelected()));

        // Adjust high temperature threshold after focus is lost
        maxTempText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setNewExtremeHighTemperature(maxTempText.getText());
            }
        });

        // Adjust low temperature threshold after focus is lost
        minTempText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setNewExtremeLowTemperature(minTempText.getText());
            }
        });

        // Adjust precipitation threshold after focus is lost
        maxPrecText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setNewExtremePrecipitation(maxPrecText.getText());
            }
        });

        // Setup temperature unit buttons; celsius as default
        cRadioButton.setSelected(true);
        cRadioButton.addActionListener(e -> toggleTemperatureUnits());
        fRadioButton.addActionListener(e -> toggleTemperatureUnits());

        // Setup temperature display buttons
        actualRadioButton.addActionListener(e -> toggleTemperatureDisplay(ACTUAL_TEMPERATURE));
        feltRadioButton.addActionListener(e -> toggleTemperatureDisplay(FELT_TEMPERATURE));
        bothRadioButton.addActionListener(e -> toggleTemperatureDisplay(BOTH_TEMPERATURES));

        setDefaultSettings();

        // Create custom notification time pickers
        createTimePickerSpinner(mondaySpinner);
        createTimePickerSpinner(tuesdaySpinner);
        createTimePickerSpinner(wednesdaySpinner);
        createTimePickerSpinner(thursdaySpinner);
        createTimePickerSpinner(fridaySpinner);
        createTimePickerSpinner(saturdaySpinner);
        createTimePickerSpinner(sundaySpinner);

        // Add radio buttons to groups
        tempUnitGroup.add(cRadioButton);
        tempUnitGroup.add(fRadioButton);

        displayTempGroup.add(actualRadioButton);
        displayTempGroup.add(feltRadioButton);
        displayTempGroup.add(bothRadioButton);
        bothRadioButton.setSelected(true);

        // Set temperature and precipitation panels to invisible at start
        tempPanel.setVisible(false);
        precPanel.setVisible(false);

        add(settingPanel);
    }

    /**
     * Setup day of the week time picker spinner
     * @param spinner JSpinner element
     */
    private void createTimePickerSpinner(JSpinner spinner) {
        SpinnerDateModel model = new SpinnerDateModel();
        model.setCalendarField(Calendar.MINUTE);
        spinner.setModel(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
        JSpinner.DateEditor editor = (JSpinner.DateEditor) spinner.getEditor();
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
    }

    /**
     * Set default extreme weather thresholds
     * Temperature units should be Celsius at default
     */
    private void setDefaultSettings() {
        assert temperatureUnits == CELSIUS;
        maxTempText.setText("" + extremeTemperatureHighC);
        minTempText.setText("" + extremeTemperatureLowC);
        maxPrecText.setText("" + extremePrecipitation);
    }

    /**
     * Sync settings with what is shown on the UI
     */
    private void syncSettingsWithUI() {
        if (extremeTemperatureNotificationEnabled != tempCheckBox.isSelected()) {
            extremeTemperatureNotificationEnabled = tempCheckBox.isSelected();
        }
        if (extremePrecipitationNotificationsEnabled != precCheckBox.isSelected()) {
            extremePrecipitationNotificationsEnabled = precCheckBox.isSelected();
        }
        if (imminentRainNotificationsEnabled != rainCheckBox.isSelected()) {
            imminentRainNotificationsEnabled = rainCheckBox.isSelected();
        }
        if (stormWarningNotificationsEnabled != stormCheckBox.isSelected()) {
            stormWarningNotificationsEnabled = stormCheckBox.isSelected();
        }
        if (temperatureUnits == CELSIUS) {
            if (extremeTemperatureHighC != Integer.parseInt(maxTempText.getText())) {
                extremeTemperatureHighC = Integer.parseInt(maxTempText.getText());
            }
            if (extremeTemperatureLowC != Integer.parseInt(minTempText.getText())) {
                extremeTemperatureLowC = Integer.parseInt(minTempText.getText());
            }
        } else if (temperatureUnits == FAHRENHEIT) {
            if (extremeTemperatureHighF != Integer.parseInt(maxTempText.getText())) {
                extremeTemperatureHighF = Integer.parseInt(maxTempText.getText());
            }
            if (extremeTemperatureLowF != Integer.parseInt(minTempText.getText())) {
                extremeTemperatureLowF = Integer.parseInt(minTempText.getText());
            }
        }
        if (extremePrecipitation != Integer.parseInt(maxPrecText.getText())) {
            extremePrecipitation = Integer.parseInt(maxPrecText.getText());
        }
        // TODO: add checks for temperature units and display temperature types
    }

    /**
     * Set the new high temperature threshold
     * Check if the current temperature units are in Celsius or Fahrenheit
     * Restrict high temperature to be at least 1 higher than the low temperature
     * Restrict high temperature threshold to be 35 Celsius at maximum
     * @param newHigh New high temperature threshold value
     */
    private void setNewExtremeHighTemperature(String newHigh) {
        if (temperatureUnits == CELSIUS) {
            try {
                int parsed_newHigh = (int) (Double.parseDouble(newHigh) + 0.5);
                if (parsed_newHigh > 35) {
                    parsed_newHigh = 35;
                } else if (parsed_newHigh <= extremeTemperatureLowC) {
                    parsed_newHigh = extremeTemperatureLowC + 1;
                }
                extremeTemperatureHighC = parsed_newHigh;
                extremeTemperatureHighF = (int) (((double) parsed_newHigh) * 9.0 / 5.0 + 32.0 + 0.5);
                maxTempText.setText("" + parsed_newHigh);
            } catch (NumberFormatException e) {
                maxTempText.setText("" + extremeTemperatureHighC);
            }
        } else if (temperatureUnits == FAHRENHEIT) {
            try {
                int parsed_newHigh = (int) (Double.parseDouble(newHigh) + 0.5);
                if (parsed_newHigh > 95) {
                    parsed_newHigh = 95;
                } else if (parsed_newHigh <= extremeTemperatureLowF) {
                    parsed_newHigh = extremeTemperatureLowF + 1;
                }
                extremeTemperatureHighF = parsed_newHigh;
                extremeTemperatureHighC = (int) ((((double) parsed_newHigh) - 32.0) * 5.0 / 9.0 + 0.5);
                maxTempText.setText("" + parsed_newHigh);
            } catch (NumberFormatException e) {
                maxTempText.setText("" + extremeTemperatureHighF);
            }
        }
    }

    /**
     * Set the new low temperature threshold
     * Check if the current temperature units are in Celsius or Fahrenheit
     * Restrict high temperature to be at least 1 higher than the low temperature
     * Restrict low temperature threshold to be -15 Celsius at minimum
     * @param newLow New low temperature threshold value
     */
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

    /**
     * Set the new precipitation threshold
     * Restrict precipitation to be at minimum 0 mm/h
     * @param precipitation New precipitation threshold in mm/h
     */
    private void setNewExtremePrecipitation(String precipitation) {
        try {
            int parsed_precipitation = (int) (Double.parseDouble(precipitation) + 0.5);
            if (parsed_precipitation < 1) {
                parsed_precipitation = 0;
            } else if (parsed_precipitation > 60) {
                parsed_precipitation = 60;
            }
            extremePrecipitation = parsed_precipitation;
            maxPrecText.setText("" + parsed_precipitation);
        } catch (NumberFormatException e) {
            maxPrecText.setText("" + extremePrecipitation);
        }
    }

    /**
     * Toggle extreme temperature notification setting
     */
    private void toggleExtremeTemperatureNotifications() {
        extremeTemperatureNotificationEnabled = !extremeTemperatureNotificationEnabled;
    }

    /**
     * Toggle extreme precipitation notification setting
     */
    private void toggleExtremePrecipitationNotifications() {
        extremePrecipitationNotificationsEnabled = !extremePrecipitationNotificationsEnabled;
    }

    /**
     * Toggle imminent rain notification setting
     */
    private void toggleImminentRainNotifications() {
        imminentRainNotificationsEnabled = !imminentRainNotificationsEnabled;
    }

    /**
     * Toggle storm warning notification setting
     */
    private void toggleStormWarningNotifications() {
        stormWarningNotificationsEnabled = !stormWarningNotificationsEnabled;
    }

    /**
     * Toggle temperature units and set temperature text to the correct format
     */
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

    /**
     * Change temperature display setting based on the settingID
     * @param settingID ACTUAL_TEMPERATURE, FELT_TEMPERATURE, or BOTH_TEMPERATURES
     */
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

    /**
     * Gives the panel of the setting screen
     * @return settingPanel
     */
    public JPanel getSettingPanel() {
        return settingPanel;
    }

    /**
     * @return temperature units (celsius or fahrenheit)
     */
    public int getTemperatureUnits() {
        return temperatureUnits;
    }

    /**
     * @return display temperature types (actual, felt, or both)
     */
    public int getDisplayTemperatureTypes() {
        return displayTemperatureTypes;
    }

    /**
     * @return whether extreme temperature notifications are enabled
     */
    public boolean isExtremeTemperatureNotificationEnabled() {
        return extremeTemperatureNotificationEnabled;
    }

    /**
     * @return whether extreme precipitation notifications are enabled
     */
    public boolean isExtremePrecipitationNotificationsEnabled() {
        return extremePrecipitationNotificationsEnabled;
    }

    /**
     * @return whether imminent rain notifications are enabled
     */
    public boolean isImminentRainNotificationsEnabled() {
        return imminentRainNotificationsEnabled;
    }

    /**
     * @return whether storm warning notifications are enabled
     */
    public boolean isStormWarningNotificationsEnabled() {
        return stormWarningNotificationsEnabled;
    }

    /**
     * @return the high temperature threshold in celsius
     */
    public int getExtremeTemperatureHigh() {
        return extremeTemperatureHighC;
    }

    /**
     * @return the low temperature threshold in celsius
     */
    public int getExtremeTemperatureLow() {
        return extremeTemperatureLowC;
    }

    /**
     * @return the extreme precipitation threshold in mm/h
     */
    public int getExtremePrecipitation() {
        return extremePrecipitation;
    }
}
