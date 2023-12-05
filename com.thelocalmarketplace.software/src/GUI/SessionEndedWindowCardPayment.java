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
import java.awt.Component;
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

public class SessionEndedWindowCardPayment {
	JFrame mainFrame;
	JPanel mainPanel;
	JLabel sessionEndedText;
	JLabel sessionTotalLabel;
	
	public SessionEndedWindowCardPayment(SelfCheckoutLogic logic, BigDecimal cartTotal) {
			mainFrame = logic.station.getScreen().getFrame();
	

//			JFrame frame = new JFrame("Your Frame Title");
//	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	        frame.setSize(400, 300);
			mainPanel = new JPanel();
	        mainPanel = new JPanel(new GridBagLayout());

	        sessionEndedText = new JLabel("Thank you for shopping at SENG300!");
	        sessionEndedText.setFont(sessionEndedText.getFont().deriveFont(35f));

	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.weighty = 0.6; 
	        gbc.anchor = GridBagConstraints.PAGE_START;
	        
	        
	        JLabel TotalPaid = new JLabel("<html>The total cost: $"+ cartTotal.toString() 
	        +"<br/>"+logic.printController.print()+ " <html>");
	       
	        TotalPaid.setFont(TotalPaid.getFont().deriveFont(35f));

	        GridBagConstraints gbc2 = new GridBagConstraints();
	        gbc2.gridx = 0;
	        gbc2.gridy = 0;
	        gbc.weighty = 0.5;
	        gbc2.anchor = GridBagConstraints.CENTER;
	        
	        


	        mainPanel.add(sessionEndedText, gbc);
	        mainPanel.add(TotalPaid, gbc2);

	        mainFrame.getContentPane().add(mainPanel);




    }

	
}
