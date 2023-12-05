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


