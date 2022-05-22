import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        if (System.getProperty("os.name").contains("Windows")) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RootScreen rootScreen = new RootScreen();
        rootScreen.setVisible(true);
    }
}