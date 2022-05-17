import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RootScreen extends JFrame{
    private JPanel rootPanel;
    CardLayout layout;

    public RootScreen() {
        setSize(450, 700);
        layout = (CardLayout) rootPanel.getLayout();
        rootPanel.add("mainPanel", new MainScreen(this).getPanel());
        rootPanel.add("warningPanel", new WarningScreen(this).getPanel());
        layout.show(rootPanel, "mainPanel");
        add(rootPanel);
    }

    public void goMain() {
        layout.show(rootPanel, "mainPanel");
    }

    public void goWarning() {
        layout.show(rootPanel, "warningPanel");
    }

    public JPanel getPanel() {
        return rootPanel;
    }
}
