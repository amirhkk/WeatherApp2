import javax.swing.*;

public class WarningScreen extends JFrame {
    private JTextArea warningTextArea;
    private JScrollPane warningScrollPane;
    private JButton exitButton;
    private JPanel warningPanel;

    public WarningScreen(RootScreen parent) {
        warningTextArea.setText("Placeholder Warning");
        exitButton.addActionListener(e -> parent.goMain());
    }

    public JPanel getWarningPanel() {
        return warningPanel;
    }

    public void refresh() {
        warningTextArea.setText("Refreshed Placeholder Warning");
    }
}
