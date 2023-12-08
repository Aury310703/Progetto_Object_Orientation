package GUI;

import javax.swing.*;
import java.awt.*;

public class Home {
    private JPanel Panel;
    private JButton LoginBt;
    private JPanel PanelCerca;
    private JTextField CercaPaginaText;
    private JButton cercaButton;
    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("Home");
        frame.setContentPane(new Home().Panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}