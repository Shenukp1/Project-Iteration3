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

public class EnterMembershipWindow {
	JFrame mainFrame;
	JPanel mainPanel;
	JPanel bottomPanel;
	JPanel keyboardPanel;
	
	String membershipNum = "";
	int membershipNumLength = 9; // Feel free to change this if it's wrong!!!
	
	JButton okayButton;
	JButton backspaceButton;
	
	JLabel promptLabel;
	JLabel numberLabel;
	
	// JButton[] buttonArray;
	
	// There's no way I'm defining all the buttons here
	
	public EnterMembershipWindow(SelfCheckoutLogic logic) {
		mainFrame = logic.station.getScreen().getFrame();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3,1));
		
		promptLabel = new JLabel("Enter your membership ID");
		promptLabel.setFont(promptLabel.getFont().deriveFont(35f));
		promptLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		promptLabel.setHorizontalAlignment(SwingConstants.CENTER);

		numberLabel = new JLabel("-".repeat(membershipNumLength));
		numberLabel.setFont(numberLabel.getFont().deriveFont(45f));
		numberLabel.setVerticalAlignment(SwingConstants.TOP);
		numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1,3));
		
		keyboardPanel = new JPanel();
		keyboardPanel.setLayout(new GridLayout(4,3));
		
		
		okayButton = new JButton("Done");
		okayButton.setBackground(new Color(189, 255, 189));
		okayButton.addActionListener(e -> {
			
			// Pass the membership number to current session
			logic.session.membership = membershipNum;
			
			mainPanel.setVisible(false);
			MainPanel mainWindow = new MainPanel(logic, "Membership number verification not implemented yet");
			
		});
		
		backspaceButton = new JButton("Quit");
		backspaceButton.setBackground(new Color(255, 128, 128));
		backspaceButton.addActionListener(e -> {
			
			if (membershipNum.length() == 0) {
				
				mainPanel.setVisible(false);
				MainPanel mainWindow = new MainPanel(logic, "Cancelled membership number input");
				
			} else if (membershipNum.length() > 0) {
				
				membershipNum = membershipNum.substring(0, membershipNum.length() - 1);
				this.numberLabel.setText(membershipNum + 
						"-".repeat(membershipNumLength - this.membershipNum.length()));
				
				if (membershipNum.length() == 0) {
					
					backspaceButton.setText("Quit");
					
				}
			}
			
		});
		
		
		for (int i = 1; i <= 9; i++) {
			
			// Generate two rows of buttons and add them to the grid
			keyboardPanel.add(generateCharacterButton(i));
			
		}
		
		keyboardPanel.add(backspaceButton);
		
		keyboardPanel.add(generateCharacterButton(0));
		
		keyboardPanel.add(okayButton);
		
		mainPanel.add(promptLabel);
		mainPanel.add(numberLabel);
		
		bottomPanel.add(new JLabel());
		bottomPanel.add(keyboardPanel);
		bottomPanel.add(new JLabel());
		
		mainPanel.add(bottomPanel);
		
		mainFrame.getContentPane().add(mainPanel);
	}
	
	private JButton generateCharacterButton(int character) {
		// Good thing I thought of this, would be a nightmare to do manually
		JButton button = new JButton(String.valueOf(character));
		button.setFont(button.getFont().deriveFont(25f));
		
		button.addActionListener(e -> {
			
			if (this.membershipNum.length() < membershipNumLength) {
				
				this.membershipNum += String.valueOf(character);
				
				this.numberLabel.setText(this.membershipNum + 
						"-".repeat(membershipNumLength - this.membershipNum.length()));
				
				backspaceButton.setText("<-"); // Replace the quit button to a backspace button
				
				// Use this code to make the membership num all Xs
				// this.numberLabel.setText("X".repeat(this.membershipNum.length()) + 
				//		"-".repeat(membershipNumLength - this.membershipNum.length()));
				
			}
			
		});
		
		return button;
		
	}
	
}
