import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Setting extends JFrame{
    private JLabel notiLabel;
    private JLabel settingLabel;
    private JButton backButton;
    private JLabel tempLabel;
    private JLabel precLabel;
    private JLabel rainLabel;
    private JLabel stormLabel;
    private JCheckBox tempCheckBoxCheckBox;
    private JCheckBox precCheckBoxCheckBox;
    private JCheckBox rainCheckBoxCheckBox;
    private JCheckBox stormCheckBoxCheckBox;
    private JPanel panel;

    public Setting() {
        setSize(450, 700);
        add(panel);
    }
}
