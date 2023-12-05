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

import com.jjjwelectronics.EmptyDevice;

import control.SelfCheckoutLogic;
import item.PurchaseBags;

public class BagsPanel {
	JFrame mainFrame;
	PurchaseBags bagPurchaser;
	
	JPanel bagsPanel;
	JLabel bagsText;
	JButton addBag;
	JButton removeBag;
	JButton continueSession;
	
	JButton backButton;
	
	JLabel numberOfBags;
	int bagsBought = 0;
	
	JPanel bagsBottom;
	
	
	
	public BagsPanel(SelfCheckoutLogic logic, boolean endingSession) {
		bagPurchaser = new PurchaseBags(logic.station, logic.session);
		
		mainFrame = logic.station.getScreen().getFrame();
		
		bagsPanel = new JPanel();
		
		bagsPanel.setLayout(new GridLayout(4, 3, 20, 20));
		
		bagsText = new JLabel("Choose an amount of bags to purchase");
		bagsText.setHorizontalAlignment(SwingConstants.CENTER);
		bagsText.setVerticalAlignment(SwingConstants.BOTTOM);
		bagsText.setFont(bagsText.getFont().deriveFont(35f));
		
		numberOfBags = new JLabel("0");
		numberOfBags.setFont(numberOfBags.getFont().deriveFont(45f));
		numberOfBags.setHorizontalAlignment(SwingConstants.CENTER);
		numberOfBags.setVerticalAlignment(SwingConstants.BOTTOM);
		
		
		addBag = new JButton("Add (1)");
		addBag.setBackground(new Color(172, 255, 172));
		addBag.setFont(addBag.getFont().deriveFont(30f));
		
		addBag.addActionListener(e -> {
			bagsBought++;
			numberOfBags.setText(Integer.toString(bagsBought));
			removeBag.setBackground(addBag.getBackground());
		});
		
		removeBag = new JButton("Remove (1)");
		removeBag.setFont(removeBag.getFont().deriveFont(30f));
		removeBag.setBackground(new Color(255, 128, 128));
		
		removeBag.addActionListener(e -> {
			if (bagsBought != 0) {
				bagsBought--;
				if (bagsBought == 0) removeBag.setBackground(Color.LIGHT_GRAY);
			} 
			numberOfBags.setText(Integer.toString(bagsBought));
		});
		
		
		continueSession = new JButton("Finished");
		continueSession.setFont(continueSession.getFont().deriveFont(25f));
		
		continueSession.addActionListener(e -> {
			if (endingSession) {
				
				if (bagsBought > 0) { // Buys bags if the amount of bags selected is over 0, 
									// if not then it's not worth the hassle
					
					bagPurchaser.setBagsToBuy(bagsBought);
					try {
						bagPurchaser.buyBags();
					} catch (EmptyDevice e1) {
						continueSession.setText("Bag dispenser is empty!");
					}
					
				}
				
				bagsPanel.setVisible(false);
				PaymentPromptWindow paymentPrompt = new PaymentPromptWindow(logic);
				
			} else {
				
				if (bagsBought > 0) { // Buys bags if the amount of bags selected is over 0, 
									// if not then it's not worth the hassle
					bagPurchaser.setBagsToBuy(bagsBought);
					try {
						bagPurchaser.buyBags();
						
						bagsPanel.setVisible(false);
						MainPanel mainPanel = new MainPanel(logic, "Operation Complete");
						
					} catch (EmptyDevice e1) {
						
						bagsPanel.setVisible(false); // Still go to the main screen, but set 
													// the console to alert that the dispenser is empty
						MainPanel mainPanel = new MainPanel(logic, "Bag dispenser is empty!");
						
					}
				}	
			}
		});
		
		bagsPanel.add(bagsText);
		bagsPanel.add(numberOfBags);
		
		
		bagsBottom = new JPanel();
		bagsBottom.setLayout(new GridLayout(1,3));
		
		bagsBottom.add(removeBag);
		bagsBottom.add(continueSession);
		bagsBottom.add(addBag);
		
		bagsPanel.add(bagsBottom);
		
		backButton = new JButton("Cancel");
		backButton.setFont(numberOfBags.getFont().deriveFont(20f));
		backButton.setHorizontalAlignment(SwingConstants.CENTER);
		backButton.setVerticalAlignment(SwingConstants.CENTER);
		
		backButton.addActionListener(e -> {
			
			bagsPanel.setVisible(false);
			MainPanel mainPanel = new MainPanel(logic, "Cancelled operation");
			
		});
		
		bagsPanel.add(backButton);
		
		mainFrame.getContentPane().add(bagsPanel);
		
	}
}
    
