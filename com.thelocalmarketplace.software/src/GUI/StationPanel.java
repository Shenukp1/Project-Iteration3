package GUI;

import javax.swing.*;
import control.SelfCheckoutLogic;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StationPanel extends JPanel {
    private JFrame mainFrame;
    private JPanel stationPanel;
    private SelfCheckoutLogic logic;
    private JTextField addItemTextField;
    private JButton enableStationButton;
    private JButton disableStationButton;
    private JButton backButton;

    public StationPanel(SelfCheckoutLogic logic) {
        this.logic = logic;
        mainFrame = logic.station.getScreen().getFrame();
        stationPanel = new JPanel(new GridLayout(4, 1));

        JPanel addItemsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcAddItemsPanel = new GridBagConstraints();
        gbcAddItemsPanel.gridx = 0;
        gbcAddItemsPanel.gridy = 0;
        gbcAddItemsPanel.gridwidth = 7;
        gbcAddItemsPanel.fill = GridBagConstraints.HORIZONTAL;


        addItemTextField = new JTextField();
        addItemTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = addItemTextField.getText();

                //LOGIC: Textual search
                
                addItemTextField.setText("");
            }
        });
        
        addItemsPanel.add(new JLabel("Add Item:"), gbcAddItemsPanel);
        gbcAddItemsPanel.gridy = 1;
        addItemsPanel.add(addItemTextField, gbcAddItemsPanel);
        stationPanel.add(addItemsPanel);

        enableStationButton = new JButton("Enable Station");
        enableStationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // LOGIC: enable station
            }
        });
        stationPanel.add(enableStationButton);

        disableStationButton = new JButton("Disable Station");
        disableStationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // LOGIC: disable station
            }
        });
        stationPanel.add(disableStationButton);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stationPanel.setVisible(false);
                MainAttendantScreen sPanel = new MainAttendantScreen(logic);
            }
        });
        stationPanel.add(backButton);

        mainFrame.getContentPane().add(stationPanel);
    }
}
  

