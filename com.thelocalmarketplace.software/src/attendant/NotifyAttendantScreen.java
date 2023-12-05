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
package attendant;

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
