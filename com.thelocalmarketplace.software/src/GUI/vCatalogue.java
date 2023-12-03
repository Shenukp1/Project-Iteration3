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

import org.junit.Assert;

import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.Product;

import control.SelfCheckoutLogic;
import control.WeightController;
import item.AddItemCatalogue;
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

    SelfCheckoutLogic logicGold;
    JFrame initial;
    MainPanel mainPanel;
//    JLabel bananaLabel = new JLabel();

    
    
    public vCatalogue(SelfCheckoutLogic logicGold)  {
    	this.logicGold = logicGold;

        initial = logicGold.station.getScreen().getFrame();

        vcPanel = new JPanel();
        vcPanel.setLayout(new GridLayout(5, 1));

        
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
//    		Image img = new ImageIcon(this.getClass().getResource("/istockphoto-173242750-612x612.jpg")).getImage();
//    		bananaLabel.setIcon(new ImageIcon(img));

    		//    		vcPanel.add(banana);
    		Image banana = new ImageIcon(this.getClass().getResource("/istockphoto-173242750-612x612.jpg")).getImage();
    		bananaButton.setIcon(new ImageIcon(banana));
			validate();

    		bananaButton.addActionListener(e -> {
	            vcPanel.setVisible(false);
				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, barcodeController.barcodedProduct, bananas.bigDecimalMass);
    			mainPanel = new MainPanel(logicGold, "Banana Added");
//                mainPanel.listModel.addElement(createItemPanel("Banana - $ " + product.getPrice()));
    	        });	        
    		vcPanel.add(bananaButton);
			
    		
    		
			
    		Image cookies = new ImageIcon(this.getClass().getResource("/cookiesCho.jpeg")).getImage();
    		cookieButton.setIcon(new ImageIcon(cookies));
    	
    		cookieButton.addActionListener(e -> {
	            vcPanel.setVisible(false);
				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, cookie.barcodedProduct, cookie.bigDecimalMass);
    			mainPanel = new MainPanel(logicGold, "Cookies Added");
//                mainPanel.listModel.addElement(createItemPanel("Banana - $ " + product.getPrice()));
    	        });	        
	        vcPanel.add(cookieButton);
			validate();
			
    		Image milk = new ImageIcon(this.getClass().getResource("/chocmilk.png")).getImage();
    		milkButton.setIcon(new ImageIcon(milk));
    		milkButton.addActionListener(e -> {
	            vcPanel.setVisible(false);
				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, milkc.barcodedProduct, milkc.bigDecimalMass);
    			mainPanel = new MainPanel(logicGold, "Milk Added");
//                mainPanel.listModel.addElement(createItemPanel("Banana - $ " + product.getPrice()));
    	        });	        
	        vcPanel.add(milkButton);
			validate();

    		Image eggs = new ImageIcon(this.getClass().getResource("/eggs.jpeg")).getImage();
    		eggButton.setIcon(new ImageIcon(eggs));
    		eggButton.addActionListener(e -> {
	            vcPanel.setVisible(false);
				AddItemCatalogue.AddItemFromCatalogue(logicGold.session, egg.barcodedProduct, egg.bigDecimalMass);
    			mainPanel = new MainPanel(logicGold, "Milk Added");
//                mainPanel.listModel.addElement(createItemPanel("Banana - $ " + product.getPrice()));
    	        });	        
	        vcPanel.add(eggButton);
			validate();
			
			homeButton.addActionListener(e -> {
	
		            vcPanel.setVisible(false);
		            
		            mainPanel= new MainPanel(logicGold, "Returned to Main screen");
	
		            //mainFrame.setVisible(true);
		        });
	
		    vcPanel.add(homeButton);
		}
		catch (Exception ex) {
			System.out.println("ERROR");
		}
\