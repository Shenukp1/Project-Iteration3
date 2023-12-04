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
	JLabel sessionTotalLabel;
	
	public SessionEndedWindow(SelfCheckoutLogic logic, BigDecimal cartTotal) {
		mainFrame = logic.station.getScreen().getFrame();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,1));
		
		sessionEndedText = new JLabel("Thank you for shopping at SENG300!");
		sessionEndedText.setFont(sessionEndedText.getFont().deriveFont(35f));
		sessionEndedText.setVerticalAlignment(SwingConstants.CENTER);
		sessionEndedText.setHorizontalAlignment(SwingConstants.CENTER);
		sessionTotalLabel = new JLabel("You paid: $"+ cartTotal.toString());
		sessionTotalLabel.setFont(sessionEndedText.getFont().deriveFont(35f));
		sessionTotalLabel.setVerticalAlignment(SwingConstants.TOP);
		sessionTotalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		mainPanel.add(sessionEndedText);
		mainPanel.add(sessionTotalLabel);
		
		
		
		mainFrame.getContentPane().add(mainPanel);
	}
	
}