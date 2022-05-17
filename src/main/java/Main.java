public class Main {
    public static void main(String[] args){
        Screen screen = new Screen.ScreenBuilder()
                .size(450, 700)
                .color(204, 166, 166)
                .title("Weather App")
                .build();
        screen.addImage("Path/To/Your/image.png");
        screen.setVisible();
    }
}