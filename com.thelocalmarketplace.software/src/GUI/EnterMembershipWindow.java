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

public class EnterMembershipWindow {
	JFrame mainFrame;
	JPanel mainPanel;
	JPanel keyboardPanel;
	
	String membershipNum = "";
	int membershipNumLength = 9; // Feel free to change this if it's wrong!!!
	
	
	JButton okayButton;
	JButton backspaceButton;
	JButton cancelButton;
	
	JLabel promptLabel;
	JLabel numberLabel;
	
	String QWERTYlayout = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
	
	// JButton[] buttonArray;
	
	// There's no way I'm defining all the buttons here
	
	public EnterMembershipWindow(SelfCheckoutLogic logic) {
		mainFrame = logic.station.getScreen().getFrame();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3,1));
		
		promptLabel = new JLabel("Enter a valid membership number here:");
		promptLabel.setFont(promptLabel.getFont().deriveFont(35f));
		promptLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		promptLabel.setHorizontalAlignment(SwingConstants.CENTER);

		numberLabel = new JLabel("-".repeat(membershipNumLength));
		numberLabel.setFont(numberLabel.getFont().deriveFont(45f));
		numberLabel.setVerticalAlignment(SwingConstants.TOP);
		numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		keyboardPanel = new JPanel();
		keyboardPanel.setLayout(new GridLayout(4,10));
		
		
		okayButton = new JButton("Done");
		okayButton.addActionListener(e -> {
			
			// Pass the membership number to another function
			
			mainPanel.setVisible(false);
			MainPanel mainWindow = new MainPanel(logic, "Membership number verification not implemented yet");
			
		});
		
		cancelButton = new JButton("Quit");
		cancelButton.addActionListener(e -> {
			
			mainPanel.setVisible(false);
			MainPanel mainWindow = new MainPanel(logic, "Cancelled membership number input");
			
		});
		
		backspaceButton = new JButton("<-");
		backspaceButton.addActionListener(e -> {
			
			if (membershipNum.length() > 0) {
				
				membershipNum = membershipNum.substring(0, membershipNum.length() - 1);
				this.numberLabel.setText("X".repeat(this.membershipNum.length()) + 
						"-".repeat(membershipNumLength - this.membershipNum.length()));
			}
			
		});
		
		
		for (int i = 0; i <= 28; i++) {
			
			// Generate two rows of buttons and add them to the grid
			keyboardPanel.add(generateCharacterButton(QWERTYlayout.charAt(i)));
			
		}
		
		keyboardPanel.add(backspaceButton);
		
		for (int i = 29; i <= 35; i++) {
			
			// Generate two rows of buttons and add them to the grid
			keyboardPanel.add(generateCharacterButton(QWERTYlayout.charAt(i)));
			
		}
		
		keyboardPanel.add(okayButton);
		
		keyboardPanel.add(cancelButton);
		
		keyboardPanel.add(new JLabel());
		
		mainPanel.add(promptLabel);
		mainPanel.add(numberLabel);
		
		mainPanel.add(keyboardPanel);
		
		mainFrame.getContentPane().add(mainPanel);
	}
	
	private JButton generateCharacterButton(char character) {
		// Good thing I thought of this, would be a nightmare to do manually
		JButton button = new JButton(String.valueOf(character));
		
		button.addActionListener(e -> {
			
			if (this.membershipNum.length() < membershipNumLength) {
				
				this.membershipNum += String.valueOf(character);
				
				this.numberLabel.setText(this.membershipNum + 
						"-".repeat(membershipNumLength - this.membershipNum.length()));
				
				// Use this code to make the membership num all Xs
				// this.numberLabel.setText("X".repeat(this.membershipNum.length()) + 
				//		"-".repeat(membershipNumLength - this.membershipNum.length()));
				
			}
			
		});
		
		return button;
		
	}
	
}