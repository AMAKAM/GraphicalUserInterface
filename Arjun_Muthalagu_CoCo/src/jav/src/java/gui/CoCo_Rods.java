package src.java.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import src.java.api.CoCo_batch;

/**
* <p>Title: </p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: </p>
* @author not attributable
* @version 1.0
*/

public class CoCo_Rods extends JFrame {
 JPanel jPanel1 = new JPanel();
 JPanel jPanel2 = new JPanel();
 JLabel jLabel1 = new JLabel();
 JTextArea Input = new JTextArea();
 JLabel jLabel2 = new JLabel();
 JPanel jPanel3 = new JPanel();
 JLabel jLabel3 = new JLabel();
 JButton jButton1 = new JButton();
 JTextArea Output = new JTextArea();

 public CoCo_Rods() {
   try {
     jbInit();
   }
   catch(Exception ex) {
     ex.printStackTrace();
   }
 }

 void jbInit() throws Exception {
   this.getContentPane().setLayout(null);
   jPanel1.setBorder(BorderFactory.createEtchedBorder());
   jPanel1.setBounds(new Rectangle(1, 23, 396, 219));
   jPanel1.setLayout(null);
   jPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
   jPanel2.setBounds(new Rectangle(7, 33, 383, 95));
   jPanel2.setLayout(null);
   jLabel1.setText("Please Enter the Text to Classifying");
   jLabel1.setBounds(new Rectangle(20, 14, 274, 15));
   Input.setBorder(BorderFactory.createLineBorder(Color.black));
   Input.setText("");
   Input.setBounds(new Rectangle(20, 36, 354, 24));
   jLabel2.setForeground(Color.magenta);
   jLabel2.setText("COCO RODS Classifier");
   jLabel2.setBounds(new Rectangle(144, 8, 125, 22));
   jPanel3.setBorder(BorderFactory.createLineBorder(Color.black));
   jPanel3.setBounds(new Rectangle(8, 132, 383, 82));
   jPanel3.setLayout(null);
   jLabel3.setText("The Input is classified as:");
   jLabel3.setBounds(new Rectangle(14, 9, 182, 15));
   jButton1.setBounds(new Rectangle(291, 67, 81, 22));
   jButton1.setText("Classify");
   jButton1.addActionListener(new CoCo_Rods_jButton1_actionAdapter(this));
   Output.setBorder(BorderFactory.createLineBorder(Color.black));
   Output.setText("");
   Output.setBounds(new Rectangle(19, 37, 354, 25));
   this.getContentPane().add(jPanel1, null);
   jPanel2.add(Input, null);
   jPanel2.add(jLabel1, null);
   jPanel2.add(jButton1, null);
   jPanel1.add(jPanel3, null);
   jPanel3.add(jLabel3, null);
   jPanel3.add(Output, null);
   jPanel1.add(jLabel2, null);
   jPanel1.add(jPanel2, null);
 }

 public static void main(String[] args) {
   CoCo_Rods coCo_Rods = new CoCo_Rods();
   coCo_Rods.setTitle("COCO RODS Classifier");
   coCo_Rods.setSize(400,271);
   coCo_Rods.setVisible(true);
 }

 void jButton1_actionPerformed(ActionEvent e) {
//
String inputtext = Input.getText();
CoCo_batch Compar = new CoCo_batch();
String output = CoCo_batch.line(inputtext);
Output.setText(output);
 }
}

class CoCo_Rods_jButton1_actionAdapter implements
java.awt.event.ActionListener {
 CoCo_Rods adaptee;

 CoCo_Rods_jButton1_actionAdapter(CoCo_Rods adaptee) {
   this.adaptee = adaptee;
 }
 public void actionPerformed(ActionEvent e) {
 	
   adaptee.jButton1_actionPerformed(e);
 }
}
