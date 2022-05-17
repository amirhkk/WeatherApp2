import javax.swing.*;

/**
 * Implements a screen for the weather app.
 */
public class Screen {
    private final int width;
    private final int length;
    private final int red;
    private final int green;
    private final int blue;
    private final String title;
    private final JFrame frame;

    /**
     * Constructs the screen according to parameters provided by the screen builder.
     * @param builder builds the screen, providing default but modifiable parameters for size, color, title, and image.
     */
    public Screen(ScreenBuilder builder) {
        width = builder.width;
        length = builder.length;
        red = builder.red;
        green = builder.green;
        blue = builder.blue;
        title = builder.title;

        frame = new JFrame();
        frame.setSize(width, length);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new java.awt.Color(red, green, blue));
        frame.setTitle(title);
    }

    /**
     * Adds an image to the screen frame.
     * @param path address of the image.
     */
    public void addImage(String path) {
        frame.add(new JLabel(new ImageIcon(path)));
    }

    /**
     * Displays the screen frame.
     */
    public void setVisible() {
        frame.setVisible(true);
    }

    /**
     * Hides the screen frame from view.
     */
    public void setInvisible() {
        frame.setVisible(false);
    }

    /**
     * @return width of the screen.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return length of the screen.
     */
    public int getLength() {
        return length;
    }

    /**
     * @return red component of the screen's background color.
     */
    public int getRed() {
        return red;
    }

    /**
     * @return green component of the screen's background color.
     */
    public int getGreen() {
        return green;
    }

    /**
     * @return blue component of the screen's background color.
     */
    public int getBlue() {
        return blue;
    }

    /**
     * @return title of the screen.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Builder class that provides default values for the screen.
     * Provides methods that allow parameters to be adjusted.
     */
    public static class ScreenBuilder {
        private int width = 450;
        private int length = 700;
        private int red = 204;
        private int green = 166;
        private int blue = 166;
        private String title = "Weather App";

        /**
         * Changes size of screen before building it.
         * @param width width of the screen.
         * @param length length of the screen.
         * @return current instance of ScreenBuilder.
         */
        public ScreenBuilder size(int width, int length) {
            this.width = width;
            this.length = length;
            return this;
        }

        /**
         * Changes color of screen before building it.
         * @param red color component.
         * @param green color component.
         * @param blue color component.
         * @return current instance of ScreenBuilder.
         */
        public ScreenBuilder color(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            return this;
        }

        /**
         * Changes title of screen before building it.
         * @param title the screen frame's title.
         * @return current instance of ScreenBuilder.
         */
        public ScreenBuilder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Builds the screen according to the specified parameters.
         * @return screen instance.
         */
        public Screen build() {
            return new Screen(this);
        }
    }
}
