import javax.swing.*;

public class WarningScreen extends JFrame {
    private JTextArea warningTextArea;
    private JScrollPane warningScrollPane;
    private JButton exitButton;
    private JPanel warningPanel;
    private final Setting settingScreen;

    public WarningScreen(RootScreen parent, Setting settingScreen) {
        this.settingScreen = settingScreen;
        exitButton.addActionListener(e -> parent.goMain());
        refresh();
    }

    public JPanel getWarningPanel() {
        return warningPanel;
    }

    public void refresh() {
        if (settingScreen.isExtremePrecipitationNotificationsEnabled()) {
            warningTextArea.setText("Extreme Precipitation Warning!");
        }
    }
}
