package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
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

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;

import control.SelfCheckoutLogic;

public class CashPaymentWindow {
	JFrame mainFrame;
	JPanel mainPanel;
	
	JPanel topPanel;
	JPanel midPanel;
	JPanel midPanelLeft;
	JPanel midPanelRight;
	
	
	JButton cent5Button;
	JButton cent10Button;
	JButton cent25Button;
	JButton cent100Button;
	JButton cent200Button;
	
	JButton note5Button;
	JButton note10Button;
	JButton note20Button;
	JButton note50Button;
	JButton note100Button;
	
	JLabel mainLabel;
	JLabel remainingTotalLabel;
	
	BigDecimal remainingTotal;
	BigDecimal changeDue;
	

	JButton cancelButton;
	
	public CashPaymentWindow(SelfCheckoutLogic logic) {
		mainFrame = logic.station.getScreen().getFrame();
		
		remainingTotal = logic.session.getCartTotal();
		changeDue = BigDecimal.ZERO;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3,1));
		
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,2));
		
		midPanel = new JPanel();
		midPanel.setLayout(new GridLayout(1,2));
		
		midPanelLeft = new JPanel();
		midPanelLeft.setLayout(new GridLayout(3,2));
		
		midPanelRight = new JPanel();
		midPanelRight.setLayout(new GridLayout(3,2));
		
		mainLabel = new JLabel("Insert coins/notes"); // mainLabel doubles as a prompt to start and the amount of change due
		mainLabel.setFont(mainLabel.getFont().deriveFont(30f));
		mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainLabel.setVerticalAlignment(SwingConstants.CENTER);
		
		remainingTotalLabel = new JLabel("Remaining: $" + logic.session.getCartTotal());
		remainingTotalLabel.setFont(remainingTotalLabel.getFont().deriveFont(30f));
		remainingTotalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		remainingTotalLabel.setVerticalAlignment(SwingConstants.CENTER);
		
		topPanel.add(mainLabel);
		topPanel.add(remainingTotalLabel);
		
		mainPanel.add(topPanel);
		
		
		float fontSize = 15f;
		
		JButton cent5Button = new JButton("5c");
		cent5Button.setFont(cent5Button.getFont().deriveFont(fontSize));
		JButton cent10Button = new JButton("10c");
		cent10Button.setFont(cent10Button.getFont().deriveFont(fontSize));
		JButton cent25Button = new JButton("25c");
		cent25Button.setFont(cent25Button.getFont().deriveFont(fontSize));
		JButton cent100Button = new JButton("$1");
		cent100Button.setFont(cent100Button.getFont().deriveFont(fontSize));
		JButton cent200Button = new JButton("$2");
		cent200Button.setFont(cent200Button.getFont().deriveFont(fontSize));
		
		JButton note5Button = new JButton("$5");
		note5Button.setFont(note5Button.getFont().deriveFont(fontSize));
		JButton note10Button = new JButton("$10");
		note10Button.setFont(note10Button.getFont().deriveFont(fontSize));
		JButton note20Button = new JButton("$20");
		note20Button.setFont(note20Button.getFont().deriveFont(fontSize));
		JButton note50Button = new JButton("$50");
		note50Button.setFont(note50Button.getFont().deriveFont(fontSize));
		JButton note100Button = new JButton("$100");
		note100Button.setFont(note100Button.getFont().deriveFont(fontSize));
		
		cent5Button.addActionListener(e -> {
			booleanGuardForPayment( logic);
			logic.station.getCoinSlot().enable();
			logic.station.getCoinValidator().enable();
			try {
				logic.station.getCoinValidator().receive(DollarsAndCurrency.dime);
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CashOverloadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Use refreshLabel(); to set the labels to reflect the change due and remaining total, as well as hide the cancel button
			booleanGuardForPayment( logic);
			refreshLabel(logic);
		});
		cent10Button.addActionListener(e -> {
			booleanGuardForPayment( logic);
			logic.station.getCoinSlot().enable();
			logic.station.getCoinValidator().enable();
			try {
				logic.station.getCoinValidator().receive(DollarsAndCurrency.dime);
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CashOverloadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			booleanGuardForPayment( logic);
			refreshLabel(logic);
			
		});
		cent25Button.addActionListener(e -> {
			booleanGuardForPayment( logic);
			logic.station.getCoinSlot().enable();
			logic.station.getCoinValidator().enable();
			try {
				logic.station.getCoinValidator().receive(DollarsAndCurrency.quarter);
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CashOverloadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			booleanGuardForPayment( logic);
			refreshLabel(logic);
			
		});
		cent100Button.addActionListener(e -> {
			booleanGuardForPayment( logic);
			logic.station.getCoinSlot().enable();
			logic.station.getCoinValidator().enable();
			try {
				logic.station.getCoinValidator().receive(DollarsAndCurrency.dollars);
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CashOverloadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			booleanGuardForPayment( logic);
			refreshLabel(logic);
			
		});
		cent200Button.addActionListener(e -> {
			booleanGuardForPayment( logic);
			logic.station.getCoinSlot().enable();
			logic.station.getCoinValidator().enable();
			try {
				logic.station.getCoinValidator().receive(DollarsAndCurrency.twodollar);
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CashOverloadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			booleanGuardForPayment( logic);
			refreshLabel(logic);
			
		});
		
		note5Button.addActionListener(e -> {
			booleanGuardForPayment( logic);
			logic.station.getBanknoteInput().enable();
			logic.station.getBanknoteValidator().enable();
			try {
				logic.station.getBanknoteValidator().receive(DollarsAndCurrency.five);
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CashOverloadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			booleanGuardForPayment( logic);
			refreshLabel(logic);
		});
		note10Button.addActionListener(e -> {
			booleanGuardForPayment( logic);
			logic.station.getBanknoteInput().enable();
			logic.station.getBanknoteValidator().enable();
			try {
				logic.station.getBanknoteValidator().receive(DollarsAndCurrency.ten);
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CashOverloadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
			booleanGuardForPayment( logic);
			refreshLabel(logic);
		});
		note20Button.addActionListener(e -> {
			booleanGuardForPayment( logic);
			logic.station.getBanknoteInput().enable();
			logic.station.getBanknoteValidator().enable();
			try {
				logic.station.getBanknoteValidator().receive(DollarsAndCurrency.twenty);
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CashOverloadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			booleanGuardForPayment( logic);
			refreshLabel(logic);
			
		});
		note50Button.addActionListener(e -> {
			booleanGuardForPayment( logic);
			logic.station.getBanknoteInput().enable();
			logic.station.getBanknoteValidator().enable();
			try {
				logic.station.getBanknoteValidator().receive(DollarsAndCurrency.fifty);
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CashOverloadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			booleanGuardForPayment( logic);
			refreshLabel(logic);
			
		});
		note100Button.addActionListener(e -> {
			booleanGuardForPayment( logic);
			logic.station.getBanknoteInput().enable();
			logic.station.getBanknoteValidator().enable();
			try {
				logic.station.getBanknoteValidator().receive(DollarsAndCurrency.hundred);
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CashOverloadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			booleanGuardForPayment( logic);
			refreshLabel(logic);
		});

		midPanelLeft.add(cent5Button);
		midPanelLeft.add(cent10Button);
		midPanelLeft.add(cent25Button);
		midPanelLeft.add(cent100Button);
		midPanelLeft.add(cent200Button);
		
		midPanel.add(midPanelLeft);
		
		midPanelRight.add(note5Button);
		midPanelRight.add(note10Button);
		midPanelRight.add(note20Button);
		midPanelRight.add(note50Button);
		midPanelRight.add(note100Button);
		
		midPanel.add(midPanelRight);
		
		mainPanel.add(midPanel);
		
		/*
		JButton fiveDollarButton = new JButton("5$");
		fiveDollarButton.setFont(fiveDollarButton.getFont().deriveFont(10f));
		fiveDollarButton.addActionListener(e ->{
		// PINEntryWindow paymentPromptWindow = new PINEntryWindow(logic);
		
		logic.station.getBanknoteInput().enable();
		try {
			logic.station.getBanknoteInput().receive(DollarsAndCurrency.five);
		} catch (DisabledException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CashOverloadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  });
		*/
		
		
		JButton cancelButton = new JButton("Tap to cancel operation");
		cancelButton.setFont(cancelButton.getFont().deriveFont(30f));
		cancelButton.addActionListener(e -> {
			
			mainPanel.setVisible(false);
	
			
			PaymentPromptWindow paymentPromptWindow = new PaymentPromptWindow(logic);
			
		});

		mainPanel.add(cancelButton);
		
		mainFrame.getContentPane().add(mainPanel);
		
	}
	
	private void refreshLabel(SelfCheckoutLogic logic) {
		
		remainingTotalLabel = new JLabel("Remaining: $" + logic.session.getCartTotal());
		remainingTotalLabel.setFont(remainingTotalLabel.getFont().deriveFont(30f));
		remainingTotalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		remainingTotalLabel.setVerticalAlignment(SwingConstants.CENTER);
		remainingTotalLabel.setText(remainingTotal.toString());
		mainLabel.setText(changeDue.toString());
		cancelButton.setVisible(false);
		
	}
	private void booleanGuardForPayment(SelfCheckoutLogic logic) {
		if(logic.session.getCartTotal().doubleValue()>0) {
			//do the normal stuff
		}
		else {
			logic.station.getBanknoteInput().disable();
			logic.station.getCoinSlot().disable();
			mainPanel.setVisible(false);
			SessionEndedWindow endSesh = new SessionEndedWindow(logic);
		}
			
	}
	
	
	
}