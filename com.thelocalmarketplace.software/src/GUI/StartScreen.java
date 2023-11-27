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

public class StartScreen extends JPanel {
    JFrame startFrame;
    JPanel startPanel;
    JTextField tempCelsius;
    JLabel celsiusLabel, fahrenheitLabel;
    JButton startSessFrame;;
    SessionController startS;
    SelfCheckoutLogic logicGold;
    SelfCheckoutLogic logic;

    public StartScreen(SelfCheckoutLogic logicGold)  {
    	this.logicGold = logic;
    	this.setForeground(getBackground());
    	
        startFrame = new JFrame("Welcome");
        startPanel = new JPanel();
        startPanel.setLayout(new GridLayout(1, 1));

        addWidgets();

        startFrame.getContentPane().add(startPanel, BorderLayout.CENTER);

        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setPreferredSize(new Dimension(800, 600));
        startFrame.pack();
        startFrame.setVisible(true);
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
            //SelfCheckoutStationGold gold = new SelfCheckoutStationGold();
            //gold.plugIn(PowerGrid.instance());
            //gold.turnOn();
            //gold.resetConfigurationToDefaults();
            //logicGold = new SelfCheckoutLogic(gold);
            //logicGold = SelfCheckoutLogic.installOn(gold);
            //PowerGrid.engageUninterruptiblePowerSource();
            //PowerGrid.instance().forcePowerRestore();

            MainFrame mainFrame = new MainFrame();
            startFrame.setVisible(false);
            
            mainFrame.setVisible(true);
            
            
        });

        startPanel.add(startSessFrame);
    }

}