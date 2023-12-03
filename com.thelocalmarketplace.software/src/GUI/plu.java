package GUI;

import java.awt.GridLayout;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.PriceLookUpCode;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import control.SelfCheckoutLogic;
import item.AddItemPLU;

public class plu {
	JFrame mainFrame;
	JPanel mainPanel;
	JPanel bottomPanel;
	JPanel keyboardPanel;
	
	String pluNum = "";
	PriceLookUpCode getter;
	BigDecimal tempMass=new BigDecimal("2000");
	Mass temp = new Mass (tempMass);
	int pluNumLength = 4; // Feel free to change this if it's wrong!!!
	
	JButton okayButton;
	JButton backspaceButton;
	
	JLabel promptLabel;
	JLabel numberLabel;

public plu(SelfCheckoutLogic logic) {
	mainFrame = logic.station.getScreen().getFrame();
	
	mainPanel = new JPanel();
	mainPanel.setLayout(new GridLayout(3,1));
	
	promptLabel = new JLabel("Enter a valid PLU number:");
	promptLabel.setFont(promptLabel.getFont().deriveFont(35f));
	promptLabel.setVerticalAlignment(SwingConstants.BOTTOM);
	promptLabel.setHorizontalAlignment(SwingConstants.CENTER);

	numberLabel = new JLabel("-".repeat(pluNumLength));
	numberLabel.setFont(numberLabel.getFont().deriveFont(45f));
	numberLabel.setVerticalAlignment(SwingConstants.TOP);
	numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
	
	bottomPanel = new JPanel();
	bottomPanel.setLayout(new GridLayout(1,3));
	
	keyboardPanel = new JPanel();
	keyboardPanel.setLayout(new GridLayout(4,3));
	
	
	okayButton = new JButton("Done");
	okayButton.addActionListener(e -> {
		getter = new PriceLookUpCode (pluNum);
		
		//logic to add item to station instance. string may need to be converted to plu
		AddItemPLU.AddItemFromPLU(logic.session,getter, tempMass);
		// Pass the PLU number to another function
		
		mainPanel.setVisible(false);
		MainPanel mainWindow = new MainPanel(logic, "Adding item corresponding with the PLU not implemented yet");
		
	});
	
	backspaceButton = new JButton("Quit");
	backspaceButton.addActionListener(e -> {
		
		if (pluNum.length() == 0) {
			
			mainPanel.setVisible(false);
			MainPanel mainWindow = new MainPanel(logic, "Cancelled PLU number input");
			
		} else if (pluNum.length() > 0) {
			
			pluNum = pluNum.substring(0, pluNum.length() - 1);
			this.numberLabel.setText(pluNum + 
					"-".repeat(pluNumLength - this.pluNum.length()));
			
			if (pluNum.length() == 0) {
				
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
		
		if (this.pluNum.length() < pluNumLength) {
			
			this.pluNum += String.valueOf(character);
			
			this.numberLabel.setText(this.pluNum + 
					"-".repeat(pluNumLength - this.pluNum.length()));
			
			backspaceButton.setText("<-"); // Replace the quit button to a backspace button
			
			// Use this code to make the membership num all Xs
			// this.numberLabel.setText("X".repeat(this.membershipNum.length()) + 
			//		"-".repeat(membershipNumLength - this.membershipNum.length()));
			
		}
		
	});
	
	return button;
	
}

}