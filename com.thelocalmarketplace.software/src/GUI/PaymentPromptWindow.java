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

public class PaymentPromptWindow {
	JFrame mainFrame;
	JPanel promptPanel;
	
	JLabel promptLabel;
	JButton cashButton;
	JButton cardButton;
	
	JPanel promptBottom;
	
	public PaymentPromptWindow(SelfCheckoutLogic logic) {
		mainFrame = logic.station.screen.getFrame();
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		promptPanel = new JPanel();
		promptPanel.setLayout(new GridLayout(2,1,50,50));
		
		promptLabel = new JLabel("Select payment type");
		promptLabel.setVerticalAlignment(SwingConstants.CENTER);
		promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
		promptLabel.setFont(promptLabel.getFont().deriveFont(35f));
		
		promptPanel.add(promptLabel);
		
		promptBottom = new JPanel();
		promptBottom.setLayout(new GridLayout(1,2,50,50));
		
		cashButton = new JButton("Cash");
		cashButton.setFont(cashButton.getFont().deriveFont(35f));
		
		cashButton.addActionListener(e -> {
			
			promptPanel.setVisible(false);
			CashPaymentWindow cashPaymentWindow = new CashPaymentWindow(logic);
			
		});
		
		
		cardButton= new JButton("Credit/Debit");
		cardButton.setFont(cardButton.getFont().deriveFont(35f));
		
		cardButton.addActionListener(e -> {
			
			promptPanel.setVisible(false);
			CardPaymentWindow cardPaymentWindow = new CardPaymentWindow(logic);
			
		});
		
		promptBottom.add(cashButton);
		promptBottom.add(cardButton);
		
		promptPanel.add(promptBottom);
		
		mainFrame.getContentPane().add(promptPanel);
	}
	
}


