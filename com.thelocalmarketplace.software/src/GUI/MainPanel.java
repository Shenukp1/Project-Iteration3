package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

public class MainPanel extends JFrame {
    SelfCheckoutLogic logicGold;
    JFrame mainFrame;
    JPanel topPanel;
    JPanel bottomPanel;
    JPanel mainPanel;
    JPanel mainLeft;
    JPanel mainRight;
    JPanel mainBottom;
    String message;
    JPanel containerPanel;

    public MainPanel(SelfCheckoutLogic logicGold, String message) {
    	this.message = message;			//Console message to be printed 
        this.logicGold = logicGold;
        mainFrame = logicGold.station.screen.getFrame();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //mainPanel =  topPanel + bottomPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        
        	//topPanel = mainLeft + mainRight
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        		//mainLeft
        mainLeft = new JPanel();
        mainLeft.setLayout(new GridLayout(6, 1, 40,40));
        JLabel empty = new JLabel("");
        mainLeft.add(empty);
        JButton button0 = new JButton("Call Attendant");
        button0.setFont(button0.getFont().deriveFont(19f));
        mainLeft.add(button0);
        JButton button1 = new JButton("Add Bags");
        button1.setFont(button1.getFont().deriveFont(19f));
        mainLeft.add(button1);
        JButton button2 = new JButton("Enter Membership");
        button2.setFont(button2.getFont().deriveFont(19f));
        mainLeft.add(button2);
        JButton button3 = new JButton("Pay");
        button3.setFont(button3.getFont().deriveFont(19f));
        mainLeft.add(button3);
        JLabel total = new JLabel("Total: $" + logicGold.session.getCartTotal() );
        total.setFont(button3.getFont().deriveFont(25f));
        mainLeft.add(total);
        topPanel.add(mainLeft);
        topPanel.add(new JSeparator(SwingConstants.VERTICAL));
        

        		//mainRight = itemsLabel + Container
        mainRight = new JPanel();
        mainRight.setLayout(new GridBagLayout());

        GridBagConstraints gbcRightPanelTop = new GridBagConstraints();
        gbcRightPanelTop.gridx = 0;
        gbcRightPanelTop.gridy = 0;
        gbcRightPanelTop.weighty = 0.05;
        gbcRightPanelTop.weightx = 1;
        gbcRightPanelTop.fill = GridBagConstraints.BOTH;

        GridBagConstraints gbcRightPanelBot = new GridBagConstraints();
        gbcRightPanelBot.gridx = 0;
        gbcRightPanelBot.gridy = 1;
        gbcRightPanelBot.weighty = 0.8;
        gbcRightPanelBot.weightx = 1;
        gbcRightPanelBot.fill = GridBagConstraints.BOTH;



        JLabel itemsLabel = new JLabel("Items Scanned:");
        itemsLabel.setFont(itemsLabel.getFont().deriveFont(20f));
        
        DefaultListModel<JPanel> listModel = new DefaultListModel<>();
        listModel.addElement(createItemPanel("Item 1 - $10"));
        listModel.addElement(createItemPanel("Item 2 - $20"));

        containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(20, 0));

        //Adds item elements to containerPanel
        for (int i = 0; i < listModel.getSize(); i++) {
            containerPanel.add(listModel.getElementAt(i));
        }

        
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        mainRight.add(itemsLabel, gbcRightPanelTop);
        itemsLabel.setHorizontalAlignment(JLabel.LEFT);
        mainRight.add(scrollPane, gbcRightPanelBot);
        topPanel.add(mainRight);
        
        
        
        GridBagConstraints gbcTopPanel = new GridBagConstraints();
        gbcTopPanel.gridx = 0;
        gbcTopPanel.gridy = 0;
        gbcTopPanel.weighty = 0.9;
        gbcTopPanel.fill = GridBagConstraints.BOTH;
        mainPanel.add(topPanel, gbcTopPanel);
        
        
        JLabel test = new JLabel("Console: " + message);
        test.setForeground(Color.RED);
        test.setFont(button1.getFont().deriveFont(17f));
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
        
       
        mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        
        
    }
    //Function to create the individual items " Name - $Price - Remove Button "
    private JPanel createItemPanel(String itemName) {
    	JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel itemLabel = new JLabel(itemName);
        itemLabel.setFont(itemLabel.getFont().deriveFont(18f));

        JButton removeButton = new JButton("X");
        removeButton.setForeground(Color.RED);
        removeButton.addActionListener(e -> {
            containerPanel.remove(itemPanel);
            containerPanel.revalidate();
            containerPanel.repaint();
        });

        itemPanel.add(itemLabel);
        itemPanel.add(removeButton);

        return itemPanel;
    }
    
}