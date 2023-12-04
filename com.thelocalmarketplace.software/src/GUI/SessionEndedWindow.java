package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;

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

public class SessionEndedWindow {
	JFrame mainFrame;
	JPanel mainPanel;
	JLabel sessionEndedText;
	
	public SessionEndedWindow(SelfCheckoutLogic logic, BigDecimal recieptTotal, BigDecimal changeOwed) {
		mainFrame = logic.station.getScreen().getFrame();
		

//		JFrame frame = new JFrame("Your Frame Title");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 300);
		mainPanel = new JPanel();
        mainPanel = new JPanel(new GridBagLayout());

        sessionEndedText = new JLabel("Thank you for shopping at SENG300!");
        sessionEndedText.setFont(sessionEndedText.getFont().deriveFont(20f));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.6; 
        gbc.anchor = GridBagConstraints.PAGE_START;
       
        
        //JLabel TotalPaid = new JLabel("You paid: $"+ recieptTotal + "  your change is: $ " + Math.abs(changeOwed.doubleValue()));
        JLabel TotalPaid = new JLabel("<html>The total cost: $"+ recieptTotal.toString() +"<br/>"+logic.printController.print()+ 
        		" your change is: $" + Math.abs(changeOwed.doubleValue())+" <html>");
       
        TotalPaid.setFont(TotalPaid.getFont().deriveFont(35f));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc.weighty = 0.5;
        gbc2.anchor = GridBagConstraints.CENTER;
        
        


        mainPanel.add(sessionEndedText, gbc);
        mainPanel.add(TotalPaid, gbc2);

        mainFrame.getContentPane().add(mainPanel);

	
}}