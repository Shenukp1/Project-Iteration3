package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;

import control.SelfCheckoutLogic;
import control.SessionController;
import powerutility.PowerGrid;

public class StartScreen {
    JFrame converterFrame;
    JPanel converterPanel;
    JTextField tempCelsius;
    JLabel celsiusLabel, fahrenheitLabel;
    JButton startSessFrame;;
    SessionController startS;
    SelfCheckoutLogic logicGold; 

    public StartScreen(SelfCheckoutLogic logicGold) {
    	logicGold.station.screen.getFrame();
        converterFrame = new JFrame("Welcome");
        converterPanel = new JPanel();
        converterPanel.setLayout(new GridLayout(1, 1));

        addWidgets();

        converterFrame.getContentPane().add(converterPanel, BorderLayout.CENTER);

        converterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        converterFrame.setPreferredSize(new Dimension(800, 600));
        converterFrame.pack();
        converterFrame.setVisible(true);
    }


	private void addWidgets() {
        startSessFrame = new JButton("Press anywhere to start");
        Font buttonFont = new Font(startSessFrame.getFont().getName(), Font.PLAIN, 20);

        startSessFrame.setFont(buttonFont);

        startSessFrame.setLayout(null);
        startSessFrame.setBounds(0, 0, 800, 600);
        
        startSessFrame.addActionListener(e -> {
        	System.out.println("Button pressed. Starting session...");
        	
            // logicGold calls session.Start()
            SelfCheckoutStationGold gold = new SelfCheckoutStationGold();
            gold.plugIn(PowerGrid.instance());
            gold.turnOn();
            gold.resetConfigurationToDefaults();
            logicGold = new SelfCheckoutLogic(gold);
            logicGold = SelfCheckoutLogic.installOn(gold);
            PowerGrid.engageUninterruptiblePowerSource();
            PowerGrid.instance().forcePowerRestore();

            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            
            
        });

        converterPanel.add(startSessFrame);
    }

}