import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WarningScreen extends JFrame {
    private JTextArea warningTextArea;
    private JScrollPane warningScrollPane;
    private JButton exitButton;
    private JPanel warningPanel;

    public WarningScreen(RootScreen parent) {
        warningTextArea.setText("Placeholder Warning");
        exitButton.addActionListener(e -> parent.goMain());
    }

    public JPanel getPanel() {
        return warningPanel;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
