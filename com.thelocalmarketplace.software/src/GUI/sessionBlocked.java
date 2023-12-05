package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.SwingUtilities;

import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.PLUCodedProduct;
import com.thelocalmarketplace.hardware.Product;

import control.SelfCheckoutLogic;
import control.WeightController;
import item.AddOwnBags;

public class sessionBlocked {
	
	JFrame blockedFrame;
    JPanel blockedPanel;
    JButton addItemButton;
    JButton removeItemButton;
    JButton doNotBagButton;
    JButton attendantOverrideButton;
    SelfCheckoutLogic logicGold;
    JFrame initial;
    
    public sessionBlocked(SelfCheckoutLogic logicGold)  {
    	this.logicGold = logicGold;

        initial = logicGold.station.getScreen().getFrame();

        blockedPanel = new JPanel();
        blockedPanel.setLayout(new GridLayout(10, 1));

        initial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        
        
        JLabel headline = new JLabel("                                                       Session Blocked!");

        addItemButton = new JButton("Add an item");
        removeItemButton = new JButton("Remove an item");
        doNotBagButton = new JButton("Do not bag item");
        attendantOverrideButton = new JButton("Attendant Override");
	    
        
        doNotBagButton.addActionListener(e -> {
        	
        	if (logicGold.session.Cart.size() > 0) {
            	Product product = logicGold.session.Cart.get(logicGold.session.Cart.size()-1);
            	double temp = logicGold.session.getCartWeight();
           
            		if (product instanceof BarcodedProduct) {
                        double desc = ((BarcodedProduct) product).getExpectedWeight();
                        logicGold.session.setBulkyWeight(desc);
                        System.out.println(logicGold.session.getCartWeight());
                        System.out.println(logicGold.session.getBulkyWeight());
                    } 
                    if (product instanceof PLUCodedProduct) {
                    	double desc = ((BarcodedProduct) product).getExpectedWeight();
                    	logicGold.session.setBulkyWeight(desc);
                    }
                    
                    
                    blockedPanel.setVisible(false);
                    MainPanel newPanel = new MainPanel(logicGold, "Do not bag item!");
                        

            	}
        });
	    
        addItemButton.addActionListener(e -> {
        	
        	if (logicGold.session.Cart.size() > 0) {
        	Product product = logicGold.session.Cart.get(logicGold.session.Cart.size()-1);
        	double temp = logicGold.session.getCartWeight();
       
        		if (product instanceof BarcodedProduct) {
                    double desc = ((BarcodedProduct) product).getExpectedWeight();
                    temp = temp - desc;
                    logicGold.session.setCartWeight(temp);
                } 
                if (product instanceof PLUCodedProduct) {
                	double desc = ((BarcodedProduct) product).getExpectedWeight();
                	temp = temp - desc;
                	logicGold.session.setCartWeight(temp);
                }
                
                SwingUtilities.invokeLater(() -> {
                    blockedPanel.setVisible(false);
                    MainPanel newPanel = new MainPanel(logicGold, "Discrepancy fixed!");
                    
                });
        	}

        });

        removeItemButton.addActionListener(e -> {
        });

        attendantOverrideButton.addActionListener(e -> {
        	logicGold.weightController.theMassOnTheScaleHasChanged(logicGold.station.getScanningArea(), new Mass(logicGold.session.getCartWeight()));
        	//Override expected weight
        	 SwingUtilities.invokeLater(() -> {
                 blockedPanel.setVisible(false);
                 MainPanel newPanel = new MainPanel(logicGold, "Discrepancy fixed by Attendant!");
                 
             });
        });
        

        blockedPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        headline.setFont(headline.getFont().deriveFont(19f));
        blockedPanel.add(headline);
        blockedPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        addItemButton.setFont(addItemButton.getFont().deriveFont(19f));
        blockedPanel.add(addItemButton);
        blockedPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        removeItemButton.setFont(removeItemButton.getFont().deriveFont(19f));
        blockedPanel.add(removeItemButton);
        blockedPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        doNotBagButton.setFont(doNotBagButton.getFont().deriveFont(19f));
        blockedPanel.add(doNotBagButton);
        blockedPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        attendantOverrideButton.setFont(attendantOverrideButton.getFont().deriveFont(19f));
        blockedPanel.add(attendantOverrideButton);
        
        
        
        
         
        
        
        
        //initial.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        
        
        initial.add(blockedPanel, BorderLayout.CENTER);
        
        
        initial.setTitle("Session Blocked");

        
        initial.setPreferredSize(new Dimension(800, 600));

    }

    
}


