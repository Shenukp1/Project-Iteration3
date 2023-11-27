package GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Main Frame");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("This is the main frame");
        add(label);
    }
}