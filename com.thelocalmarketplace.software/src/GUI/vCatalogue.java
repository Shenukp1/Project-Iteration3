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

import control.SelfCheckoutLogic;
import control.WeightController;
import item.AddOwnBags;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;

public class vCatalogue extends JPanel {

	JFrame vcFrame;
    JPanel vcPanel;
    JButton addBanana;
    JButton addMilk;
    JButton addCookies;
    JButton addEggs;
    SelfCheckoutLogic logicGold;
    JFrame initial;


    public vCatalogue(SelfCheckoutLogic logicGold)  {
    	this.logicGold = logicGold;

        initial = logicGold.station.screen.getFrame();

        vcPanel = new JPanel();
        vcPanel.setLayout(new GridLayout(1, 1));

        addWidgets();
       
        
        initial.add(vcPanel);
        
        initial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initial.setTitle("Visual Catalogue");
        
        initial.pack();
        initial.setVisible(true);
        vcPanel.setVisible(true);
    }

    private void addWidgets() {
    	try {
	        ImageIcon image1 = new ImageIcon("/Orange_Background.png");
	        vcPanel.add(new JLabel(image1));
		}
		catch (Exception ex) {
			System.out.println("ERROR");
		}
    }
}