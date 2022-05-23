import javax.swing.*;
import java.awt.*;

/**
 * RootScreen is the screen that is displayed, containing all 3 screens and allowing for switching screens
 */
public class RootScreen extends JFrame{
    private JPanel rootPanel;
    private final MainScreen mainScreen;
    private final WarningScreen warningScreen;
    private final Setting settingScreen;
    CardLayout layout;

    /**
     * Constructor for RootScreen
     * Initializes main, setting, and warning screens and adds them to the root panel
     */
    public RootScreen() {
        setSize(450, 700);
        layout = (CardLayout) rootPanel.getLayout();
        settingScreen = new Setting(this);
        warningScreen = new WarningScreen(this, settingScreen);
        mainScreen= new MainScreen(this, settingScreen);

        rootPanel.add("mainPanel", mainScreen.getMainPanel());
        rootPanel.add("warningPanel", warningScreen.getWarningPanel());
        rootPanel.add("settingPanel", settingScreen.getSettingPanel());
        layout.show(rootPanel, "mainPanel");

        add(rootPanel);
    }

    /**
     * Refresh the main screen
     * Show the main screen
     */
    public void goMain() {
        mainScreen.refresh();
        layout.show(rootPanel, "mainPanel");
    }

    /**
     * Refresh the warning screen
     * Show the warning screen
     */
    public void goWarning() {
        warningScreen.refresh();
        layout.show(rootPanel, "warningPanel");
    }

    /**
     * Refresh the setting screen
     * Show the setting screen
     */
    public void goSetting() {
        layout.show(rootPanel, "settingPanel");
    }
}
