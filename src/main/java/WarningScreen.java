import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WarningScreen extends JFrame {
    private JTextArea warningTextArea;
    private JScrollPane warningScrollPane;
    private JButton exitButton;
    private JPanel warningPanel;

    public WarningScreen() {
        warningTextArea.setText("Placeholder Warning");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel rootPanel = (JPanel) warningPanel.getParent().getParent();
                CardLayout layout = (CardLayout) rootPanel.getLayout();
                layout.show(rootPanel, "mainPanel");
            }
        });
        add(warningPanel);
    }

    public JPanel getPanel() {
        return warningPanel;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
