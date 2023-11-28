package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JPanel;

import control.SelfCheckoutLogic;

public class MainPanel extends JFrame {
	SelfCheckoutLogic logicGold;
	JFrame mainFrame;
	JPanel mainPanel; 
	
	
    public MainPanel(SelfCheckoutLogic logicGold) {
    	this.logicGold = logicGold;
    	mainFrame = logicGold.station.screen.getFrame();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 1));
        mainPanel.setVisible(true);
        JLabel label = new JLabel("This is the main frame");
        mainPanel.add(label);
        mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }


}