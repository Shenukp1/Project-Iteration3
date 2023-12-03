package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigInteger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;

import control.SelfCheckoutLogic;


public class MainPanel extends JFrame {
    SelfCheckoutLogic logicGold;
    JFrame mainFrame;
    JPanel topPanel;
    JPanel bottomPanel;
    JPanel mainPanel;
    JPanel mainLeft;
    JPanel mainRight;
    JPanel mainBottom;
    String message;
    JPanel containerPanel;
    DefaultListModel<JPanel> listModel;
    MainAttendantScreen attendantScreen;

    public MainPanel(SelfCheckoutLogic logicGold, String message, MainAttendantScreen attendantScreen) {
        this.logicGold = logicGold;
        mainFrame = logicGold.station.getScreen().getFrame();
        this.attendantScreen = attendantScreen;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //mainPanel =  topPanel + bottomPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        
        	//topPanel = mainLeft + mainRight
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        		//mainLeft
        mainLeft = new JPanel();
        mainLeft.setLayout(new GridLayout(7, 1, 40,40));
        JPanel stationLabelPanel = new JPanel();
        stationLabelPanel.setLayout(new BorderLayout());
        stationLabelPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel stationLabel = new JLabel("Station #3");
        stationLabel.setFont(stationLabel.getFont().deriveFont(16f));
        stationLabel.setHorizontalAlignment(JLabel.CENTER);

        stationLabelPanel.add(stationLabel, BorderLayout.CENTER);
        mainLeft.add(stationLabelPanel);
        JLabel empty = new JLabel("");
        mainLeft.add(empty);
        
        JButton button0 = new JButton("Call Attendant");
        button0.setFont(button0.getFont().deriveFont(19f));
        mainLeft.add(button0);
        button0.addActionListener(e -> {
        	JDialog helpNotification = new JDialog(attendantScreen.mainFrame, "Attendant Help Requested");
            JLabel dialogText = new JLabel("Station 3 requires help");
            helpNotification.add(dialogText);
        });


        JButton button1 = new JButton("Add Bags");
        button1.setFont(button1.getFont().deriveFont(19f));
        button1.addActionListener(e -> {
        	mainPanel.setVisible(false);
        	BagsPanel bagsPanel = new BagsPanel(logicGold, false);
        });
        mainLeft.add(button1);
        JButton button2 = new JButton("Enter Membership");
        mainLeft.add(button2);
        JButton button3 = new JButton("Pay");
        button3.setFont(button3.getFont().deriveFont(19f));
        button3.addActionListener(e -> {
        	mainPanel.setVisible(false);
        	BagsPanel bagsPanel = new BagsPanel(logicGold, true);
        });
        mainLeft.add(button3);
        JLabel total = new JLabel("Total: $" + logicGold.session.getCartTotal() );
        total.setFont(button3.getFont().deriveFont(25f));
        mainLeft.add(total);
        topPanel.add(mainLeft);
        topPanel.add(new JSeparator(SwingConstants.VERTICAL));
        

        		//mainRight = itemsLabel + Container
        mainRight = new JPanel();
        mainRight.setLayout(new GridBagLayout());

        GridBagConstraints gbcRightPanelTop = new GridBagConstraints();
        gbcRightPanelTop.gridx = 0;
        gbcRightPanelTop.gridy = 0;
        gbcRightPanelTop.weighty = 0.05;
        gbcRightPanelTop.weightx = 1;
        gbcRightPanelTop.fill = GridBagConstraints.BOTH;

        GridBagConstraints gbcRightPanelBot = new GridBagConstraints();
        gbcRightPanelBot.gridx = 0;
        gbcRightPanelBot.gridy = 1;
        gbcRightPanelBot.weighty = 0.8;
        gbcRightPanelBot.weightx = 1;
        gbcRightPanelBot.fill = GridBagConstraints.BOTH;



        JLabel itemsLabel = new JLabel("Items Scanned:");
        itemsLabel.setFont(itemsLabel.getFont().deriveFont(20f));
        
        listModel = new DefaultListModel<>();
        
        containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(20, 0));

        //Updated current list
        updateListModel();

        
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        mainRight.add(itemsLabel, gbcRightPanelTop);
        itemsLabel.setHorizontalAlignment(JLabel.LEFT);
        mainRight.add(scrollPane, gbcRightPanelBot);
        topPanel.add(mainRight);
        
        
        
        GridBagConstraints gbcTopPanel = new GridBagConstraints();
        gbcTopPanel.gridx = 0;
        gbcTopPanel.gridy = 0;
        gbcTopPanel.weighty = 0.8;
        gbcTopPanel.fill = GridBagConstraints.BOTH;
        mainPanel.add(topPanel, gbcTopPanel);
        
        
        JLabel test = new JLabel("Console: " + message);
        test.setForeground(Color.RED);
        test.setFont(button1.getFont().deriveFont(17f));
        test.setHorizontalAlignment(JLabel.CENTER);
        test.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 1));
        bottomPanel.add(test);
       
        bottomPanel.add(test);
      
        GridBagConstraints gbcBottomPanel = new GridBagConstraints();
        gbcBottomPanel.gridx = 0;
        gbcBottomPanel.gridy = 1;
        gbcBottomPanel.weighty = 0.1;
        gbcBottomPanel.weightx = 1;
        gbcBottomPanel.fill = GridBagConstraints.BOTH;
        mainPanel.add(bottomPanel, gbcBottomPanel);
        
        
        JPanel testPanel = new JPanel();
        testPanel.setBackground(Color.LIGHT_GRAY);

        testPanel.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, Color.GRAY));

        JLabel barcodeLabel = new JLabel("Barcode Testing");
        
        barcodeLabel.setForeground(Color.BLACK);
        barcodeLabel.setFont(new Font(barcodeLabel.getFont().getName(), Font.BOLD, 16)); 
        testPanel.add(barcodeLabel);
        
        
        
        
        JTextField barcodeInput = new JTextField();
        barcodeInput.setPreferredSize(new Dimension(200, 30));
        barcodeInput.addActionListener(e -> {
            String enteredCode = barcodeInput.getText();
            //System.out.println("Entered code: " + enteredCode);
            
            try {
                Numeral[] barcodeNumeral = new Numeral[enteredCode.length()];

                for (int i = 0; i < enteredCode.length(); i++) {
                    char numeralChar = enteredCode.charAt(i);
                    int numeralIndex = Character.getNumericValue(numeralChar) - 1;
                    barcodeNumeral[i] = Numeral.values()[numeralIndex];
                }

                Barcode enteredBarcode = new Barcode(barcodeNumeral);
                System.out.println(enteredBarcode.toString());
                BarcodedItem item;
                if ("1234".equals(enteredBarcode.toString())) {
                	item = new BarcodedItem(enteredBarcode,new Mass(new BigInteger("500")));
                	logicGold.station.getHandheldScanner().scan(item);
                	 SwingUtilities.invokeLater(() -> {
                         total.setText("Total: $" + logicGold.session.getCartTotal());                 
                         topPanel.revalidate();
                         topPanel.repaint();
                         System.out.println("Helloooo");
                     });
                	 
                	 updateListModel();
                    
                	 
               }
                else {
                	System.err.println("Invalid Barcode");
                }
                
             
                
                
            } catch (Exception ex) {
                System.err.println("Invalid input");
            } finally {
                barcodeInput.setText("");
            }
        });

        testPanel.add(barcodeInput);
        
        JButton switchToAttendantButton = new JButton("Switch to Attendant Screen");
        switchToAttendantButton.addActionListener(e -> {
        	 mainPanel.setVisible(false);
             MainAttendantScreen attendantScreen = new MainAttendantScreen(logicGold); 
        });
        testPanel.add(switchToAttendantButton); 

        GridBagConstraints gbcTestPanel = new GridBagConstraints();
        gbcTestPanel.gridx = 0;
        gbcTestPanel.gridy = 2;
        gbcTestPanel.weighty = 0.4;
        gbcTestPanel.weightx = 1;
        gbcTestPanel.fill = GridBagConstraints.BOTH;

        mainPanel.add(testPanel, gbcTestPanel);
        
        
       
        mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        
        
    }
    //Function to create the individual items " Name - $Price - Remove Button "
    private JPanel createItemPanel(String itemName) {
    	JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel itemLabel = new JLabel(itemName);
        itemLabel.setFont(itemLabel.getFont().deriveFont(18f));

        JButton removeButton = new JButton("X");
        removeButton.setForeground(Color.RED);
        removeButton.addActionListener(e -> {
            containerPanel.remove(itemPanel);
            containerPanel.revalidate();
            containerPanel.repaint();
        });

        itemPanel.add(itemLabel);
        itemPanel.add(removeButton);

        return itemPanel;
    }
    
    private void updateListModel() {
        listModel.clear();

        for (Product product : logicGold.session.Cart) {
            if (product instanceof BarcodedProduct) {
                String desc = ((BarcodedProduct) product).getDescription();
                listModel.addElement(createItemPanel(desc + "- $ " + product.getPrice()));
            } else {
                listModel.addElement(createItemPanel("Unknown Product - $ " + product.getPrice()));
            }
        }
        containerPanel.removeAll();
        for (int i = 0; i < listModel.getSize(); i++) {
            containerPanel.add(listModel.getElementAt(i));
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
   
    
}