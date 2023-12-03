package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;

import control.SelfCheckoutLogic;
import control.SessionController;
import java.awt.Color;

public class StartScreen extends JPanel {
    JFrame startFrame;
    JPanel startPanel;
    JButton startSessButton;
    SessionController startS;
    SelfCheckoutLogic logicGold;
    //SelfCheckoutLogic logic;
    JFrame initial;

    public StartScreen(SelfCheckoutLogic logicGold)  {
    	this.logicGold = logicGold;
        //this.setForeground(getBackground());

        initial = logicGold.station.getScreen().getFrame();

        startPanel = new JPanel();
        startPanel.setLayout(new GridLayout(1, 1));

        addWidgets();

        initial.getContentPane().add(startPanel, BorderLayout.CENTER);

        initial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initial.setTitle("Welcome");

        initial.setPreferredSize(new Dimension(800, 600));
        initial.pack();
        initial.setVisible(true);
        startPanel.setVisible(true);
    }

    private void addWidgets() {
        startSessButton = new JButton("Press anywhere to start");
        startSessButton.setForeground(new Color(0, 0, 0));
        startSessButton.setBackground(new Color(206, 206, 255));
        Font buttonFont = new Font(startSessButton.getFont().getName(), Font.PLAIN, 20);

        startSessButton.setFont(new Font("Tahoma", Font.PLAIN, 24));

        startSessButton.setLayout(null);
        startSessButton.setBounds(0, 0, 800, 600);

        startSessButton.addActionListener(e -> {

            startPanel.setVisible(false);
            
            MainPanel mainPanel= new MainPanel(logicGold, "Session Started!");

            //mainFrame.setVisible(true);
        });

        startPanel.add(startSessButton);
    }
}