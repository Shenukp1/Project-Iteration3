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

public class StartScreen extends JPanel {
    JFrame startFrame;
    JPanel startPanel;
    JButton startSessButton;
    SessionController startS;
    SelfCheckoutLogic logicGold;
    //SelfCheckoutLogic logic;
    JFrame initial;
    MainAttendantScreen attendantScreen;

    public StartScreen(SelfCheckoutLogic logicGold, MainAttendantScreen attendantScreen)  {
    	this.logicGold = logicGold;
        this.attendantScreen = attendantScreen;
        //this.setForeground(getBackground());

        initial = logicGold.station.getScreen().getFrame();

        startPanel = new JPanel();
        startPanel.setLayout(new GridLayout(1, 1));

        addWidgets();

        initial.add(startPanel, BorderLayout.CENTER);

        initial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initial.setTitle("Welcome");

        initial.setPreferredSize(new Dimension(800, 600));
        initial.pack();
        initial.setVisible(true);
        startPanel.setVisible(true);
    }

    private void addWidgets() {
        startSessButton = new JButton("Press anywhere to start");
        Font buttonFont = new Font(startSessButton.getFont().getName(), Font.PLAIN, 20);

        startSessButton.setFont(buttonFont);

        startSessButton.setLayout(null);
        startSessButton.setBounds(0, 0, 800, 600);

        startSessButton.addActionListener(e -> {

            startPanel.setVisible(false);
            
            MainPanel mainPanel= new MainPanel(logicGold, "Session Started!", attendantScreen);

            //mainFrame.setVisible(true);
        });

        startPanel.add(startSessButton);
    }
}