package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

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

import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.ChipFailureException;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import control.SelfCheckoutLogic;
import payment.PaymentCardController;

public class CardPaymentWindow {
	JFrame mainFrame;
	JPanel mainPanel;
	
	JLabel promptLabel;
	JLabel statusLabel;
	
	JButton cancelButton;

	CardIssuer cardIssuer = new CardIssuer("TD Trust", 123321);
	Calendar calendar = Calendar.getInstance();
	
	
	Card creditCard = new Card("credit", "555", "Donkey Kong", "312", "911", true, true);

	
	PaymentCardController cardController;
	
	
	public CardPaymentWindow(SelfCheckoutLogic logic) {
		
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		calendar.add(Calendar.YEAR, 2024);
		calendar.add(Calendar.MONTH, 02);
		
		cardIssuer.addCardData(creditCard.number, creditCard.cardholder,
				calendar,creditCard.cvv , 1000);
		cardController = new PaymentCardController(logic.session, logic.station, cardIssuer);
		cardController.onPayWithCard();
		logic.station.getCardReader().enable();

		mainFrame = logic.station.getScreen().getFrame();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(4,1));
		
		promptLabel = new JLabel("Insert/tap/swipe card");
		promptLabel.setFont(promptLabel.getFont().deriveFont(30f));
		promptLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
		

		
		statusLabel = new JLabel("Waiting for card...");
		statusLabel.setFont(statusLabel.getFont().deriveFont(30f));
		statusLabel.setVerticalAlignment(SwingConstants.CENTER);
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JButton cardInsertedButton = new JButton("Card Has been Inserted");
    cardInsertedButton.setFont(cardInsertedButton.getFont().deriveFont(19f));
		cardInsertedButton.addActionListener(e -> {
			mainPanel.setVisible(false);
			cardController.aCardHasBeenInserted();
			new PINEntryWindow(logic, creditCard, cardController);
		});
		
		JButton cardTappedButton = new JButton("Card Has been tapped");
		cardTappedButton.setFont(cardTappedButton.getFont().deriveFont(19f));
		cardTappedButton.addActionListener(e -> {
				mainPanel.setVisible(false);
				try {
					logic.station.getCardReader().tap(creditCard);
					cardController.aCardHasBeenTapped();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//new PINEntryWindow(logic, creditCard, cardController);
				new SessionEndedWindowCardPayment(logic, cardController.getTotal());
			});
		
		JButton cardSwipedButton = new JButton("Card Has been swiped");
		cardSwipedButton.setFont(cardSwipedButton.getFont().deriveFont(19f));
		cardSwipedButton.addActionListener(e -> {
				mainPanel.setVisible(false);
				try {
					logic.station.getCardReader().swipe(creditCard);
					cardController.aCardHasBeenSwiped();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//new PINEntryWindow(logic, creditCard, cardController);
				new SessionEndedWindowCardPayment(logic, cardController.getTotal());
			});

		
		cancelButton = new JButton("Tap to cancel operation");
		cancelButton.setFont(cancelButton.getFont().deriveFont(30f));
		
		cancelButton.addActionListener(e -> {
			
			mainPanel.setVisible(false);
			PaymentPromptWindow paymentPromptWindow = new PaymentPromptWindow(logic);
			
		});

		mainPanel.add(promptLabel);
		mainPanel.add(statusLabel);
		mainPanel.add(cardInsertedButton);
		mainPanel.add(cardTappedButton);
		mainPanel.add(cardSwipedButton);
		mainPanel.add(new JLabel());
		mainPanel.add(cancelButton);
		
		
		mainFrame.getContentPane().add(mainPanel);
	}
	
}