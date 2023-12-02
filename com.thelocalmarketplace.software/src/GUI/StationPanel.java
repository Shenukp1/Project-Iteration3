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
        stationPanel = new JPanel(new GridLayout(5, 1));

        addItemTextField = new JTextField();
        stationPanel.add(new JLabel("Add Item:"));
        stationPanel.add(addItemTextField);

        enableStationButton = new JButton("Enable Station");
        enableStationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // JOptionPane.showMessageDialog(null, "Enable Station clicked");
            }
        });
        stationPanel.add(enableStationButton);

        disableStationButton = new JButton("Disable Station");
        disableStationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // JOptionPane.showMessageDialog(null, "Disable Station clicked");
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
  
