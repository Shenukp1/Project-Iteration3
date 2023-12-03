package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import control.SelfCheckoutLogic;
import control.WeightController;
import item.AddOwnBags;

public class sessionBlocked {
	
	JFrame blockedFrame;
    JPanel blockedPanel;
    JButton addItemButton;
    JButton removeItemButton;
    JButton doNotBagButton;
    JButton attendantOverrideButton;
    SelfCheckoutLogic logicGold;
    JFrame initial;
    
    public sessionBlocked(SelfCheckoutLogic logicGold)  {
    	this.logicGold = logicGold;

        initial = logicGold.station.getScreen().getFrame();

        blockedPanel = new JPanel();
        blockedPanel.setLayout(new GridLayout(10
        		, 1));

        addWidgets();
        
        
        initial.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        
        
        
        initial.add(blockedPanel, BorderLayout.CENTER);
        
        initial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initial.setTitle("Session Blocked");

        
        initial.setPreferredSize(new Dimension(800, 600));
        initial.pack();
        initial.setVisible(true);
        blockedPanel.setVisible(true);
    }


    private void addWidgets() {
        JLabel headline = new JLabel("      Session Blocked");

        addItemButton = new JButton("Add an item");
        removeItemButton = new JButton("Remove an item");
        doNotBagButton = new JButton("Do not bag item");
        attendantOverrideButton = new JButton("Attendant Override");
	    
        headline.setLayout(null);

        addItemButton.setLayout(null);
        
        removeItemButton.setLayout(null);
        
        doNotBagButton.setLayout(null);
        
        attendantOverrideButton.setLayout(null);

        
        //need to added button functionalities of the button still
        doNotBagButton.addActionListener(e -> {
	//need to add logic still

        	logicGold.session.setBagWeight(0);
        });
	    
//        	theMassOnTheScaleNoLongerExceedsItsLimit(logicGold.station.baggingArea);
        addItemButton.addActionListener(e -> {
        //need to add logic still

//            startPanel.setVisible(false);
//            MainPanel mainPanel= new MainPanel(logicGold, "Session Started!");
//            JLabel weight = new JLabel("Total weight: " + logicGold.session.getBagWeight());
//            JLabel wanted_weight = new JLabel(logicGold.session.getCartWeight());
//            weight += logicGold.session.setBagWeight();

            //!!!
            //change the expected weight to not include the item
            //logicGold.changeWeight
            
            //mainFrame.setVisible(true);
        });

        removeItemButton.addActionListener(e -> {
	//need to add logic still
        });

        attendantOverrideButton.addActionListener(e -> {
	//need to add logic still
        });
        

        blockedPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        headline.setFont(headline.getFont().deriveFont(19f));
        blockedPanel.add(headline);
        blockedPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        addItemButton.setFont(addItemButton.getFont().deriveFont(19f));
        blockedPanel.add(addItemButton);
        blockedPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        removeItemButton.setFont(removeItemButton.getFont().deriveFont(19f));
        blockedPanel.add(removeItemButton);
        blockedPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        doNotBagButton.setFont(doNotBagButton.getFont().deriveFont(19f));
        blockedPanel.add(doNotBagButton);
        blockedPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        attendantOverrideButton.setFont(attendantOverrideButton.getFont().deriveFont(19f));
        blockedPanel.add(attendantOverrideButton);


        
    }
    
}

