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

public class CashPaymentWindow {
	JFrame mainFrame;
	JPanel mainPanel;
	
	JLabel mainLabel;
	JLabel remainingTotalLabel;
	
	BigDecimal remainingTotal;
	
	JButton cancelButton;
	
	public CashPaymentWindow(SelfCheckoutLogic logic) {
		mainFrame = logic.station.getScreen().getFrame();
		
		remainingTotal = logic.session.getCartTotal();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(4,1));
		
		mainLabel = new JLabel("Start inserting cash to begin");
		mainLabel.setFont(mainLabel.getFont().deriveFont(30f));
		mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton cancelButton = new JButton("Tap to cancel operation");
		cancelButton.setBackground(new Color(255, 128, 128));
		cancelButton.setFont(cancelButton.getFont().deriveFont(30f));
		cancelButton.addActionListener(e -> {
			
			mainPanel.setVisible(false);
			// PINEntryWindow paymentPromptWindow = new PINEntryWindow(logic);
			
			PaymentPromptWindow paymentPromptWindow = new PaymentPromptWindow(logic);
			
		});
		
		mainPanel.add(mainLabel);
		
		remainingTotalLabel = new JLabel("Remaining total: $" + remainingTotal);
		remainingTotalLabel.setForeground(new Color(87, 87, 87));
		remainingTotalLabel.setFont(remainingTotalLabel.getFont().deriveFont(30f));
		remainingTotalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		remainingTotalLabel.setVerticalAlignment(SwingConstants.CENTER);
		mainPanel.add(remainingTotalLabel);
		mainPanel.add(new JLabel());
		mainPanel.add(cancelButton);
		
		mainFrame.getContentPane().add(mainPanel);
		
	}
	
	
	
}