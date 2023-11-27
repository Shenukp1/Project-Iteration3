package gui;

import javax.swing.*;

import item.PurchaseBags;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/*
 * Mockup of a basic Customer GUI to implement notify Attendant
 * and purchase bags use case
 */
// TODO Configure to specify which checkout station sent the help request 
public class CustomerGUIMockup extends JFrame {

    
	private List<UpdateListener> listeners = new ArrayList<>();
    private JButton helpButton;
    private JButton buyBagsButton;
    
    public CustomerGUIMockup() {
        helpButton = new JButton("Request Help");
        buyBagsButton = new JButton("Purchase Bags");

        JPanel panel = new JPanel();
        panel.add(helpButton);
        panel.add(buyBagsButton);

        add(panel);
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // set up button listeners
        setUpButtonListeners();
    }

    
    public void setUpButtonListeners() {
        // listener for help button
        ActionListener helpListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyUpdateListeners("Customer help requested");
            }
        };
        helpButton.addActionListener(helpListener);

        // listener for purchase bags button
        ActionListener purchaseBagsListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PurchaseBags.buyBags();
            }
        };
        buyBagsButton.addActionListener(purchaseBagsListener);
    }
    
    public void addUpdateListener(UpdateListener listener) {
        listeners.add(listener);
    }

    private void notifyUpdateListeners(Object updateInfo) {
        for (UpdateListener listener : listeners) {
            listener.update(updateInfo);
        }
    }

 

    public static void main(String[] args) {
        CustomerGUIMockup customerGUI = new CustomerGUIMockup();
        AttendantGUIMockup attendantGUI = new AttendantGUIMockup();

        // Add AttendantGUI as listener
        customerGUI.addUpdateListener(attendantGUI);
    }
}
