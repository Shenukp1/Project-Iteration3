package gui;

import javax.swing.*;
/*
 * Mockup of a basic Attendant GUI to implement Notify Attendant use case
 * and purchase bags use case
 */
public class AttendantGUIMockup extends JFrame implements UpdateListener {

private JLabel notificationLabel;

 public AttendantGUIMockup() {
     notificationLabel = new JLabel("No notifications");

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
         notificationLabel.setText(notification);
     } else {
         // Unknown update type
         System.out.println("Attendant notified: Unknown update type");
     }
 }

}
