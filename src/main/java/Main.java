import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(450,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new java.awt.Color(204, 166, 166));
        frame.add(new JLabel(new ImageIcon("Path/To/Your/Image.png")));
        frame.setTitle("WeatherApp");
        frame.setVisible(true);
    }
}