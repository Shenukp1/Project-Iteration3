package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import control.SelfCheckoutLogic;

public class MainPanel extends JFrame {
    SelfCheckoutLogic logicGold;
    JFrame mainFrame;
    JPanel topPanel;
    JPanel bottomPanel;
    JPanel mainPanel;
    JPanel mainLeft;
    JPanel mainRight;
    JPanel mainBottom;

    public MainPanel(SelfCheckoutLogic logicGold) {
        this.logicGold = logicGold;
        mainFrame = logicGold.station.screen.getFrame();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        
        
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));

        mainLeft = new JPanel();
        mainLeft.setLayout(new GridLayout(6, 1, 40,40));
        JLabel empty = new JLabel("");
        mainLeft.add(empty);
        
        JButton button0 = new JButton("Call Attendant");
        mainLeft.add(button0);
        JButton button1 = new JButton("Add Bags");
        mainLeft.add(button1);
        JButton button2 = new JButton("Enter Membership");
        mainLeft.add(button2);
        JButton button3 = new JButton("Pay");
        mainLeft.add(button3);
        
        JLabel total = new JLabel("Total: $" + logicGold.session.getCartTotal() );
        mainLeft.add(total);
        

        topPanel.add(mainLeft);
        topPanel.add(new JSeparator(SwingConstants.VERTICAL));
        

       
  
        mainRight = new JPanel();
        mainRight.setLayout(new GridLayout(0, 1));
        JLabel itemsLabel = new JLabel("Items Scanned:");
        
        topPanel.add(itemsLabel);
        itemsLabel.setHorizontalAlignment(JLabel.LEFT);
        
        GridBagConstraints gbcTopPanel = new GridBagConstraints();
        gbcTopPanel.gridx = 0;
        gbcTopPanel.gridy = 0;
        gbcTopPanel.weighty = 0.9;
        gbcTopPanel.fill = GridBagConstraints.BOTH;
        mainPanel.add(topPanel, gbcTopPanel);
        
        
        JLabel test = new JLabel("Consol Messages go here");
        test.setForeground(Color.RED);
        test.setHorizontalAlignment(JLabel.CENTER);
        test.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 1));
        bottomPanel.add(test);
       
        bottomPanel.add(test);
      
        GridBagConstraints gbcBottomPanel = new GridBagConstraints();
        gbcBottomPanel.gridx = 0;
        gbcBottomPanel.gridy = 1;
        gbcBottomPanel.weighty = 0.1;
        gbcBottomPanel.weightx = 1;
        gbcBottomPanel.fill = GridBagConstraints.BOTH;
        mainPanel.add(bottomPanel, gbcBottomPanel);
        
        
        
       // Box separatorBox = Box.createHorizontalBox();
        //separatorBox.add(Box.createHorizontalStrut(mainFrame.getWidth())); 
        //separatorBox.add(new JLabel(" ")); // Empty label as a separator
        //separatorBox.setOpaque(true);
        //separatorBox.setBackground(Color.RED);
        //mainPanel.add(separatorBox, BorderLayout.SOUTH);


        // Add mainPanel to the center of the main frame
        mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }
}