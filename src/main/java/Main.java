import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Use the System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RootScreen rootScreen = new RootScreen();
        rootScreen.setVisible(true);
    }
}