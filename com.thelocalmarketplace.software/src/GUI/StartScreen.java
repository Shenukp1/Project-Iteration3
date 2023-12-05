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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;

import control.SelfCheckoutLogic;
import control.SessionController;
import java.awt.Color;

public class StartScreen extends JPanel {
    JFrame startFrame;
    JPanel startPanel;
    JButton startSessButton;
    SessionController startS;
    SelfCheckoutLogic logicGold;
    //SelfCheckoutLogic logic;
    JFrame initial;

    public StartScreen(SelfCheckoutLogic logicGold)  {
    	this.logicGold = logicGold;
        //this.setForeground(getBackground());

        initial = logicGold.station.getScreen().getFrame();

        startPanel = new JPanel();
        startPanel.setLayout(new GridLayout(1, 1));

        addWidgets();

        initial.getContentPane().add(startPanel, BorderLayout.CENTER);

        initial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initial.setTitle("Welcome");

        initial.setPreferredSize(new Dimension(800, 600));
        initial.pack();
        initial.setVisible(true);
        startPanel.setVisible(true);
    }

    private void addWidgets() {
        startSessButton = new JButton("Press anywhere to start");
        startSessButton.setForeground(new Color(0, 0, 0));
        startSessButton.setBackground(new Color(206, 206, 255));
        Font buttonFont = new Font(startSessButton.getFont().getName(), Font.PLAIN, 20);

        startSessButton.setFont(new Font("Tahoma", Font.PLAIN, 24));

        startSessButton.setLayout(null);
        startSessButton.setBounds(0, 0, 800, 600);

        startSessButton.addActionListener(e -> {

            startPanel.setVisible(false);
            
            MainPanel mainPanel= new MainPanel(logicGold, "Session Started!");

            //mainFrame.setVisible(true);
        });

        startPanel.add(startSessButton);
    }
}
