/*Group P3-6***
Andy Tang 10139121
Ayman Inayatali Momin 30192494
Darpal Patel 30088795
Dylan Dizon 30173525
Ellen Bowie 30191922
Emil Huseynov 30171501
Ishita Udasi 30170034
Jason Very 30222040
Jesse Leinan 00335214
Joel Parker 30021079
Kear Sang Heng 30087289
Khadeeja Abbas 30180776
Kian Sieppert 30134666
Michelle Le 30145965
Raja Muhammed Omar 30159575
Sean Gilluley 30143052
Shenuk Perera 30086618
Simrat Virk 30000516
Sina Salahshour 30177165
Tristan Van Decker 30160634
Usharab Khan 30157960
YiPing Zhang 30127823*/
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

public class PaymentPromptWindow {
	JFrame mainFrame;
	JPanel promptPanel;
	
	JLabel promptLabel;
	JLabel totalLabel;
	JButton cashButton;
	JButton cardButton;
	JButton backButton;
	
	JPanel promptBottom;
	
	public PaymentPromptWindow(SelfCheckoutLogic logic) {
		mainFrame = logic.station.getScreen().getFrame();
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		promptPanel = new JPanel();
		promptPanel.setLayout(new GridLayout(4,1,100,100));
		
		promptLabel = new JLabel("Select payment type");
		promptLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
		promptLabel.setFont(promptLabel.getFont().deriveFont(35f));
		
		promptPanel.add(promptLabel);
		
		totalLabel = new JLabel("$" + logic.session.getCartTotal().toString());
		totalLabel.setVerticalAlignment(SwingConstants.CENTER);
		totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalLabel.setFont(totalLabel.getFont().deriveFont(35f));
		
		promptPanel.add(totalLabel);
		
		
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
		
		backButton = new JButton("Cancel");
		backButton.setVerticalAlignment(SwingConstants.CENTER);
		backButton.setHorizontalAlignment(SwingConstants.CENTER);
		backButton.setFont(backButton.getFont().deriveFont(35f));
		
		backButton.addActionListener(e -> {
			
			promptPanel.setVisible(false);
			BagsPanel bagsPanel = new BagsPanel(logic, true);
			
			// Remove the bags that were just added?
			
		});
		
		promptPanel.add(backButton);
		
		mainFrame.getContentPane().add(promptPanel);
	}
	
}


