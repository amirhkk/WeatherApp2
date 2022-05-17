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
    private JCheckBox tempCheckBox;
    private JCheckBox precCheckBox;
    private JCheckBox rainCheckBox;
    private JCheckBox stormCheckBox;
    private JPanel panel;
    private JTextField lowTempText;
    private JTextField HighTempText;
    private JPanel tempPanel;
    private JTextField LowPrecText;
    private JPanel precPanel;
    private JTextField HighPrecText;
    private JToggleButton precButton;
    private JToggleButton tempButton;
    private JLabel lowTempLabel;
    private JLabel highTempLabel;
    private JLabel lowPrecLabel;
    private JLabel highPrecLabel;

    public Setting() {
        setSize(450, 700);


        tempButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(tempButton.isSelected()) {
                    tempPanel.setVisible(true);
                } else {
                    tempPanel.setVisible(false);
                }
            }
        });

        precButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(precButton.isSelected()) {
                    precPanel.setVisible(true);
                } else {
                    precPanel.setVisible(false);
                }
            }
        });

        tempPanel.setVisible(false);
        precPanel.setVisible(false);
        add(panel);
    }
}
