import javax.swing.*;
import java.awt.*;

public class RootScreen extends JFrame{
    private JPanel rootPanel;
    CardLayout layout;

    public RootScreen() {
        setSize(450, 700);
        layout = (CardLayout) rootPanel.getLayout();
        rootPanel.add("mainPanel", new MainScreen(this).getMainPanel());
        rootPanel.add("warningPanel", new WarningScreen(this).getWarningPanel());
        rootPanel.add("settingPanel", new Setting(this).getSettingPanel());
        layout.show(rootPanel, "mainPanel");
        add(rootPanel);
    }

    public void goMain() {
        layout.show(rootPanel, "mainPanel");
    }

    public void goWarning() {
        layout.show(rootPanel, "warningPanel");
    }

    public void goSetting() {
        layout.show(rootPanel, "settingPanel");
    }
}
