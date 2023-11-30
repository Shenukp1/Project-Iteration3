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

public class CardPaymentWindow {
	JFrame mainFrame;
	JPanel mainPanel;
	
	JLabel promptLabel;
	JLabel statusLabel;
	
	JButton cancelButton;
	
	
	public CardPaymentWindow(SelfCheckoutLogic logic) {
		mainFrame = logic.station.screen.getFrame();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(4,1));
		
		promptLabel = new JLabel("Insert card into card reader");
		promptLabel.setFont(promptLabel.getFont().deriveFont(30f));
		promptLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		statusLabel = new JLabel("Waiting for card...");
		statusLabel.setFont(statusLabel.getFont().deriveFont(30f));
		statusLabel.setVerticalAlignment(SwingConstants.CENTER);
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		cancelButton = new JButton("Tap to cancel operation");
		cancelButton.setFont(cancelButton.getFont().deriveFont(30f));
		
		cancelButton.addActionListener(e -> {
			
			mainPanel.setVisible(false);
			PaymentPromptWindow paymentPromptWindow = new PaymentPromptWindow(logic);
			
		});

		mainPanel.add(promptLabel);
		mainPanel.add(statusLabel);
		mainPanel.add(new JLabel());
		mainPanel.add(cancelButton);
		
		mainFrame.getContentPane().add(mainPanel);
	}
	
}