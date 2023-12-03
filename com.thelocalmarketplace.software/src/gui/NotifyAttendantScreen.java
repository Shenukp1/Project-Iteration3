package gui;

import javax.swing.*;
/*
 * Mockup of a basic Attendant GUI to implement Notify Attendant use case
 * and purchase bags use case
 */
public class NotifyAttendantScreen extends JFrame implements NotifyAttendantListener {

private JLabel notificationLabel;

 public NotifyAttendantScreen() {
	 
     notificationLabel = new JLabel("<html>Issues In the Self Checkout Station:<br/>No Issues So Far");
     JPanel panel = new JPanel();
     panel.add(notificationLabel);

     add(panel);
     setSize(300, 200);
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setLocationRelativeTo(null);
     setVisible(true);
 }

 @Override
 public void update(Object updateInfo) {
     if (updateInfo instanceof String) {
         String notification = (String) updateInfo;
         notificationLabel.setText("<html>Issues In the Self Checkout Station:<br/>" + notification);
     } else {
         System.out.println("Attendant notified: Unknown update type");
     }
 }

}