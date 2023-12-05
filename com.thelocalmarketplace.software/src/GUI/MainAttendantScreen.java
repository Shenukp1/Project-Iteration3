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

import javax.swing.*;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;

import control.SelfCheckoutLogic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

public class MainAttendantScreen extends JFrame {
	JFrame mainFrame;
	JPanel attendantPanel;
	SelfCheckoutLogic logic;

	public MainAttendantScreen(SelfCheckoutLogic logic) {
		this.logic = logic;
        mainFrame = logic.station.getScreen().getFrame();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        attendantPanel = new JPanel(new GridLayout(6, 1));

        JLabel pickStationLabel = new JLabel("Pick a station:");
        pickStationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pickStationLabel.setFont(new Font(pickStationLabel.getFont().getName(), Font.BOLD, 24)); 
        attendantPanel.add(pickStationLabel);

        JButton button1 = new JButton("1");
        JButton button2 = new JButton("2");
        JButton button3 = new JButton("3");
        JButton button4 = new JButton("4");

        button1.setFont(new Font(button1.getFont().getName(), Font.BOLD, 18)); 
        button2.setFont(new Font(button2.getFont().getName(), Font.BOLD, 18)); 
        button3.setFont(new Font(button3.getFont().getName(), Font.BOLD, 18)); 
        button4.setFont(new Font(button4.getFont().getName(), Font.BOLD, 18)); 

        button1.addActionListener(new StationButtonListener(1));
        button2.addActionListener(new StationButtonListener(2));
        button3.addActionListener(new StationButtonListener(3));
        button4.addActionListener(new StationButtonListener(4));

        attendantPanel.add(button1);
        attendantPanel.add(button2);
        attendantPanel.add(button3);
        attendantPanel.add(button4);
        
        JPanel testPanel = new JPanel();
        testPanel.setBackground(Color.LIGHT_GRAY);
        testPanel.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, Color.GRAY));
        
        JButton switchToAttendantButton = new JButton("Switch to Main Screen");
        switchToAttendantButton.addActionListener(e -> {
        	 attendantPanel.setVisible(false);
             MainPanel mainScreen = new MainPanel(logic,"Back to Station 3"); 
        });
        testPanel.add(switchToAttendantButton);
        attendantPanel.add(testPanel);

        mainFrame.getContentPane().add(attendantPanel);

    }

    

        private class StationButtonListener implements ActionListener {
            private int stationNumber;

            public StationButtonListener(int stationNumber) {
                this.stationNumber = stationNumber;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
            	if (stationNumber == 3) {
            		attendantPanel.setVisible(false);
                	StationPanel stationPanel = new StationPanel(logic);
                } else {
                    // JOptionPane.showMessageDialog(null, "Station " + stationNumber + " is unavailable for access");
                }
            }
        }
    }
   
