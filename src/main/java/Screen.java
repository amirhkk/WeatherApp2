import javax.swing.*;

public class Screen {
    private final int width;
    private final int length;
    private final int red;
    private final int green;
    private final int blue;
    private final String title;
    private final String imagePath;
    private final JFrame frame;

    public Screen(ScreenBuilder builder) {
        width = builder.width;
        length = builder.length;
        red = builder.red;
        green = builder.green;
        blue = builder.blue;
        title = builder.title;
        imagePath = builder.imagePath;

        frame = new JFrame();
        frame.setSize(width, length);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new java.awt.Color(red, green, blue));
        frame.add(new JLabel(new ImageIcon(imagePath)));
        frame.setTitle(title);
    }

    public void setVisible() {
        frame.setVisible(true);
    }

    public void setInvisible() {
        frame.setVisible(false);
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public JFrame getFrame() {
        return frame;
    }

    public static class ScreenBuilder {
        private int width = 450;
        private int length = 700;
        private int red = 204;
        private int green = 166;
        private int blue = 166;
        private String title = "Weather App";
        private String imagePath = "Path/To/Your/Image.png";

        public ScreenBuilder size(int width, int length) {
            this.width = width;
            this.length = length;
            return this;
        }

        public ScreenBuilder color(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            return this;
        }

        public ScreenBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ScreenBuilder image(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Screen build() {
            return new Screen(this);
        }
    }
}
