import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        MainScreen mainScreen = new MainScreen();
        mainScreen.setVisible(true);
        Setting setting = new Setting();
        setting.setVisible(true);
        /*Screen screen = new Screen.ScreenBuilder()
                .size(450, 700)
                .color(204, 166, 166)
                .title("Weather App")
                .build();
        screen.addImage(new URL("https://cdn-icons-png.flaticon.com/512/4052/4052984.png"));
        screen.setVisible();*/
    }
}