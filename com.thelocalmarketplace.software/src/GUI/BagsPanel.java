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

public class BagsPanel {
	JFrame mainFrame;
	
	JPanel bagsPanel;
	JLabel bagsText;
	JButton addBag;
	JButton removeBag;
	JButton continueSession;
	JLabel numberOfBags;
	int bagsBought = 0;
	
	JPanel bagsBottom;
	
	
	
	public BagsPanel(SelfCheckoutLogic logic, boolean endingSession) {
		mainFrame = logic.station.getScreen().getFrame();
		
		bagsPanel = new JPanel();
		
		bagsPanel.setLayout(new GridLayout(3, 3, 80, 80));
		
		bagsText = new JLabel("Choose an amount of bags to purchase");
		bagsText.setHorizontalAlignment(SwingConstants.CENTER);
		bagsText.setVerticalAlignment(SwingConstants.BOTTOM);
		bagsText.setFont(bagsText.getFont().deriveFont(35f));
		
		numberOfBags = new JLabel("0");
		numberOfBags.setFont(numberOfBags.getFont().deriveFont(45f));
		numberOfBags.setHorizontalAlignment(SwingConstants.CENTER);
		numberOfBags.setVerticalAlignment(SwingConstants.BOTTOM);
		
		
		addBag = new JButton("+1");
		addBag.setFont(addBag.getFont().deriveFont(30f));
		
		addBag.addActionListener(e -> {
			bagsBought++;
			numberOfBags.setText(Integer.toString(bagsBought));
			removeBag.setBackground(addBag.getBackground());
		});
		
		removeBag = new JButton("-1");
		removeBag.setFont(removeBag.getFont().deriveFont(30f));
		removeBag.setBackground(Color.LIGHT_GRAY);
		
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
				
				// Remember to actually add the bags!
				
				bagsPanel.setVisible(false);
				SessionEndedWindow sessionEndedWindow = new SessionEndedWindow(logic);
				
				//bagsPanel.setVisible(false);
				//PaymentPromptWindow paymentPrompt = new PaymentPromptWindow(logic);
				
			} else {
				bagsPanel.setVisible(false);
				MainPanel mainPanel = new MainPanel(logic, "Operation Complete");
				
				
				
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
		
		mainFrame.getContentPane().add(bagsPanel);
		
	}
}
    