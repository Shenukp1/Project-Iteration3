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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.junit.Assert;

import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import control.SelfCheckoutLogic;
import control.WeightController;
import item.AddItemCatalogue;
import item.AddItemPLU;
import item.AddOwnBags;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.util.*; 
import java.applet.*;
import java.awt.*;
import java.net.URL;
import javax.swing.JDialog;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class vCatalogue extends JPanel {

	JFrame vcFrame;
    JPanel vcPanel;
    JButton bananaButton = new JButton("");
    JButton milkButton = new JButton("");
    JButton cookieButton = new JButton("");
    JButton eggButton = new JButton("");
    JButton homeButton = new JButton("Home");
	BigDecimal tempMass=new BigDecimal("3000");

    LoadProductDatabases productDatabase;
    SelfCheckoutLogic logicGold;
    JFrame initial;
    MainPanel mainPanel;
	Timer timer;
	String code;
	PriceLookUpCode getter;
	String message;
	JLabel test;

   
    public vCatalogue(SelfCheckoutLogic logicGold)  {
    	timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleTimeout();
            }
    	  });

    	this.logicGold = logicGold;

        initial = logicGold.station.getScreen().getFrame();

        vcPanel = new JPanel();
        vcPanel.setLayout(new GridLayout(4, 2));

        
        image();
       
        
        initial.add(vcPanel);
        
        initial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initial.setTitle("Visual Catalogue");
        
        initial.pack();
        initial.setVisible(true);
        vcPanel.setVisible(true);
    }

    private void image() {
    	

    	try {
    		Image banana = new ImageIcon(this.getClass().getResource("/bananasImage.jpg")).getImage();    		
    		bananaButton.setIcon(new ImageIcon(banana));
			validate();
    		bananaButton.addActionListener(e -> {
    			if (!timer.isRunning()) {
    				//vcPanel.setVisible(false);
    				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, LoadProductDatabases.bananas.barcodedProduct,
						LoadProductDatabases.bananas.bigDecimalMass);
    			//(ProductDatabases.INVENTORY.get
    			//		BarcodedProduct p : ProductDatabases.BARCODED_PRODUCT_DATABASE.value
    				code = "4111";
    				getter = new PriceLookUpCode (code);
    				PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(getter);
					AddItemPLU.AddItemFromPLU(logicGold.session,getter, tempMass);
					message = "Item found! Please place item in bagging area within 10 seconds";
					test.setText("Console: " + message);  // Update text
					test.repaint();

					timer.restart();
	           	 	timer.start();
    			}
				//mainPanel = new MainPanel(logicGold, message);


              //  mainPanel.listModel.addElement(createItemPanel("Banana - $ " + bananas.barcodedProduct.getPrice()));
    	        });
    		
    		vcPanel.add(bananaButton);
			
    		
    		
			
    		Image cookies = new ImageIcon(this.getClass().getResource("/cookiesCho.jpeg")).getImage();
    		cookieButton.setIcon(new ImageIcon(cookies));
    		cookieButton.addActionListener(e -> {
    			if (!timer.isRunning()) {
    				//vcPanel.setVisible(false);
    				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, LoadProductDatabases.cookie.barcodedProduct, LoadProductDatabases.cookie.bigDecimalMass);

    			//(ProductDatabases.INVENTORY.get
    			//		BarcodedProduct p : ProductDatabases.BARCODED_PRODUCT_DATABASE.value
    				code = "5155";
    				getter = new PriceLookUpCode (code);
    				PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(getter);
					AddItemPLU.AddItemFromPLU(logicGold.session,getter, tempMass);
					message = "Item found! Please place item in bagging area within 10 seconds";
					test.setText("Console: " + message);  // Update text
					test.repaint();

					timer.restart();
	           	 	timer.start();
    			}
//				Add logic here so it happens when the button is pressed
//				here is my attempt below:
//				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, LoadProductDatabases.cookie.barcodedProduct, LoadProductDatabases.cookie.bigDecimalMass);
//    			mainPanel = new MainPanel(logicGold, "Cookies Added");
             //   mainPanel.listModel.addElement(createItemPanel("Banana - $ " + cookie.barcodedProduct.getPrice()));
    	        });	        
	        vcPanel.add(cookieButton);
			validate();
			
			
			
			
    		Image milk = new ImageIcon(this.getClass().getResource("/chocmilk.png")).getImage();
    		milkButton.setIcon(new ImageIcon(milk));
    		milkButton.addActionListener(e -> {
    			if (!timer.isRunning()) {
    				//vcPanel.setVisible(false);
    				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, LoadProductDatabases.milkc.barcodedProduct, LoadProductDatabases.milkc.bigDecimalMass);

    			//(ProductDatabases.INVENTORY.get
    			//		BarcodedProduct p : ProductDatabases.BARCODED_PRODUCT_DATABASE.value
    				code = "1234";
    				getter = new PriceLookUpCode (code);
    				PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(getter);
					AddItemPLU.AddItemFromPLU(logicGold.session,getter, tempMass);
					message = "Item found! Please place item in bagging area within 10 seconds";
					test.setText("Console: " + message);  // Update text
					test.repaint();

					timer.restart();
	           	 	timer.start();
    			}
//				Add logic here so it happens when the button is pressed
//				here is my attempt below:
//	            vcPanel.setVisible(false);
//				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, LoadProductDatabases.milkc.barcodedProduct, LoadProductDatabases.milkc.bigDecimalMass);
//   			mainPanel = new MainPanel(logicGold, "Milk Added");
             //   mainPanel.listModel.addElement(createItemPanel("Banana - $ " + milkc.barcodedProduct.getPrice()));
    	        });	        
	        vcPanel.add(milkButton);
			validate();
			
			
			

    		Image eggs = new ImageIcon(this.getClass().getResource("/eggs.jpeg")).getImage();
    		eggButton.setIcon(new ImageIcon(eggs));
    		eggButton.addActionListener(e -> {
    			if (!timer.isRunning()) {
    				//vcPanel.setVisible(false);
    				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, LoadProductDatabases.egg.barcodedProduct, LoadProductDatabases.egg.bigDecimalMass);

    			//(ProductDatabases.INVENTORY.get
    			//		BarcodedProduct p : ProductDatabases.BARCODED_PRODUCT_DATABASE.value
    				code = "4444";
    				getter = new PriceLookUpCode (code);
    				PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(getter);
					AddItemPLU.AddItemFromPLU(logicGold.session,getter, tempMass);
					message = "Item found! Please place item in bagging area within 10 seconds";
					test.setText("Console: " + message);  // Update text
					test.repaint();

					timer.restart();
	           	 	timer.start();
    			}
//				Add logic here so it happens when the button is pressed
//				here is my attempt below:
////	            vcPanel.setVisible(false);
////				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, LoadProductDatabases.egg.barcodedProduct, LoadProductDatabases.egg.bigDecimalMass);
//    			mainPanel = new MainPanel(logicGold, "Eggs Added");
          //      mainPanel.listModel.addElement(createItemPanel("Banana - $ " + egg.barcodedProduct.getPrice()));
    	        });	        
	        vcPanel.add(eggButton);
			validate();
			
	        vcPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

			message = "Choose Item";
			test = new JLabel("Console: " + message);
			test.setFont(test.getFont().deriveFont(18f));
		    test.setForeground(Color.RED);
	        vcPanel.add(test);

		
	        JButton addedItemButton = new JButton("Click to Place Item on Bagging Area");
	        addedItemButton.addActionListener(e -> {
	        	timer.stop();
	        	logicGold.station.getBaggingArea().addAnItem(new PLUCodedItem(getter, new Mass(tempMass)));
	        	vcPanel.setVisible(false);
    			mainPanel = new MainPanel(logicGold, "Item Added");
	        
	        });
	        vcPanel.add(addedItemButton);
	        
			homeButton.addActionListener(e -> {
				vcPanel.setVisible(false);
				
				MainPanel mainWindow = new MainPanel(logicGold, "Returned from the visual catalogue");
			});
	
		    vcPanel.add(homeButton);
		    
		}
    	
		catch (Exception ex) {
			System.out.println("ERROR");
		}
    }

private void handleTimeout() {
	timer.stop();	
	System.err.println("Timeout: Item not added");
	vcPanel.setVisible(false);
	sessionBlocked sessBlocked = new sessionBlocked(logicGold);
	
}


}