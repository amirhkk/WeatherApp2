import javax.swing.*;
import java.awt.*;

public class RootScreen extends JFrame{
    private JPanel rootPanel;
    private final MainScreen mainScreen;
    private final WarningScreen warningScreen;
    private final Setting settingScreen;
    CardLayout layout;

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

    public void goMain() {
        mainScreen.refresh();
        layout.show(rootPanel, "mainPanel");
    }

    public void goWarning() {
        warningScreen.refresh();
        layout.show(rootPanel, "warningPanel");
    }

    public void goSetting() {
        layout.show(rootPanel, "settingPanel");
    }
}
