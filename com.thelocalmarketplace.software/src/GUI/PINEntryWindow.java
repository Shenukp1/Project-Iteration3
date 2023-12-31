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
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.jjjwelectronics.card.BlockedCardException;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.InvalidPINException;

import ca.ucalgary.seng300.simulation.SimulationException;
import control.SelfCheckoutLogic;
import payment.PaymentCardController;

public class PINEntryWindow extends JFrame {
	JFrame mainFrame;
	JPanel mainPanel;
	JPanel keyboardPanel;
	
	JLabel promptLabel;
	JLabel PINLabel;
	
	Card paymentCard;
	PaymentCardController cardController;
	String PIN = "";
	
	JButton zerobutton = new JButton("0");
	JButton onebutton = new JButton("1");
	JButton twobutton = new JButton("2");
	JButton threebutton = new JButton("3");
	JButton fourbutton = new JButton("4");
	JButton fivebutton = new JButton("5");
	JButton sixbutton = new JButton("6");
	JButton sevenbutton = new JButton("7");
	JButton eightbutton = new JButton("8");
	JButton ninebutton = new JButton("9");
	
	JButton confirmButton;
	JButton backButton;
	
	public PINEntryWindow(SelfCheckoutLogic logic, Card paymentCard, PaymentCardController cardController) {
		this.cardController = cardController;
		this.paymentCard = paymentCard;
		

		mainFrame = logic.station.getScreen().getFrame();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3,1));
		
		keyboardPanel = new JPanel();
		keyboardPanel.setLayout(new GridLayout(3,4));
		
		confirmButton = new JButton("Confirm");
		confirmButton.setBackground(new Color(128, 255, 128));
		backButton = new JButton("<-");
		
		promptLabel = new JLabel("Enter PIN:");
		promptLabel.setFont(promptLabel.getFont().deriveFont(30f));
		promptLabel.setVerticalAlignment(SwingConstants.CENTER);
		promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		PINLabel = new JLabel("----");
		PINLabel.setFont(PINLabel.getFont().deriveFont(30f));
		PINLabel.setVerticalAlignment(SwingConstants.CENTER);
		PINLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		PINButtonActionListener(zerobutton, "0");
		PINButtonActionListener(onebutton, "1");
		PINButtonActionListener(twobutton, "2");
		PINButtonActionListener(threebutton, "3");
		PINButtonActionListener(fourbutton, "4");
		PINButtonActionListener(fivebutton, "5");
		PINButtonActionListener(sixbutton, "6");
		PINButtonActionListener(sevenbutton, "7");
		PINButtonActionListener(eightbutton, "8");
		PINButtonActionListener(ninebutton, "9");
		
		confirmButton.addActionListener(e -> {
			
			// Pass the value of the PIN to the appropriate function
			if (PIN.length() == 3) {
				try {
					CardData cardData = logic.station.getCardReader().insert(paymentCard, PIN);
					//cardController.theDataFromACardHasBeenRead(cardData);
					logic.station.getCardReader().remove();
					mainPanel.setVisible(false);
					new SessionEndedWindowCardPayment(logic, cardController.getTotal());
				} catch (IOException error){
					// Something that either restarts this scren or says to reinput pin
					new PINEntryWindow(logic, paymentCard, cardController);
				}
				catch( InvalidPINException e1) {
					logic.station.getCardReader().remove();
					PINLabel.setText("----");
					PIN="";


				}
			}else {
				// Soemthing to say not enough pin numbers.
			}
		});
		
		backButton.addActionListener(e -> {
			if (PIN.length() >= 1) {
				PIN = PIN.substring(0, PIN.length()-1);
			}
			PINLabel.setText(("X".repeat(PIN.length())) + ("-".repeat(4 - PIN.length())));
		});
		
		
		mainPanel.add(promptLabel);
		mainPanel.add(PINLabel);
		
		keyboardPanel.add(onebutton);
		keyboardPanel.add(twobutton);
		keyboardPanel.add(threebutton);
		keyboardPanel.add(zerobutton);
		
		keyboardPanel.add(fourbutton);
		keyboardPanel.add(fivebutton);
		keyboardPanel.add(sixbutton);
		keyboardPanel.add(backButton);
		
		keyboardPanel.add(sevenbutton);
		keyboardPanel.add(eightbutton);
		keyboardPanel.add(ninebutton);
		keyboardPanel.add(confirmButton);
		
		mainPanel.add(keyboardPanel);
		
		mainFrame.getContentPane().add(mainPanel);
	}
	
	private void PINButtonActionListener(JButton button, String number) {
		button.addActionListener(e -> {
			if (PIN.length() <= 3) {
				PIN += number;
			}
			PINLabel.setText(("X".repeat(PIN.length())) + ("-".repeat(4 - PIN.length())));
		});
		
	}
	
}
