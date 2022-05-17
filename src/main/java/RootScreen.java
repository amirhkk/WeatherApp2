import javax.swing.*;
import java.awt.*;

public class RootScreen extends JFrame{
    private JPanel rootPanel;

    private MainScreen mainScreen;
    private WarningScreen warningScreen;

    private JPanel mainPanel;
    private JPanel warningPanel;

    CardLayout layout = (CardLayout) rootPanel.getLayout();

    public RootScreen() {
        setSize(450, 700);
        layout.show(rootPanel, "mainPanel");

        mainScreen = new MainScreen();
        mainPanel = mainScreen.getPanel();

        warningScreen = new WarningScreen();
        warningPanel = warningScreen.getPanel();

        add(warningPanel);
        add(mainPanel);
    }

    private void warningButtonListener() {
        layout.show(rootPanel, "warningPanel");
    }

    public static void main(String[] args) {
        RootScreen rootScreen = new RootScreen();
        rootScreen.setVisible(true);
    }
}
