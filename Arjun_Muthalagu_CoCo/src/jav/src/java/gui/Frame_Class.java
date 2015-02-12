package src.java.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.java.api.CoCo_batch;

@SuppressWarnings("serial")
public class Frame_Class extends JFrame
{
	FilerRead filepointer; 
	JTextField textfield,Output;	
	JList<String> list ;
	JPanel panel2,panel3,panel4;
	JLabel label1,label2;
	JButton button;
	JScrollPane listScroller;
	ArrayList<String> arraylist = new ArrayList<String>();
	ActionListener_ListClass listlistener;
	CoCo_batch COCO_OBJECT;
	ActionListener_Frame_Class frame_listener;
	Frame_Class()
	{   
		this.setVisible(true);
		this.setSize(700,600);
		COCO_OBJECT = new CoCo_batch();
		
		panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createLineBorder(Color.black));
		label2 = new JLabel("Type in your complaint string below or choose from the listbox and click classify");
		button = new JButton("Classify");
		textfield= new JTextField("",30);
		textfield.setFont(new Font("Serif",Font.PLAIN,20));
		textfield.setMinimumSize(new Dimension(80,50));
		panel2.add(BorderLayout.NORTH,label2);
		panel2.add(BorderLayout.WEST,textfield);
		panel2.add(BorderLayout.EAST,button);
		panel2.setMaximumSize(new Dimension(100,60));
		this.add(BorderLayout.NORTH,panel2);
		
		
		panel3 = new JPanel(new BorderLayout());
		panel3.setBorder(BorderFactory.createLineBorder(Color.black));
		filepointer = new FilerRead("default_probs.txt");
		arraylist=filepointer.readdata();
		list = new JList(arraylist.toArray());
		//System.out.println(list.getModel().getSize());
		JScrollPane listScroller = new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		listScroller.createHorizontalScrollBar();
		listScroller.createVerticalScrollBar();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		panel3.add(listScroller);
		panel3.setMaximumSize(new Dimension(100,100));
		this.add(BorderLayout.WEST,panel3);
		
		panel4 =new JPanel();
		label1 = new JLabel();
		label1.setFont(new Font("Serif",Font.BOLD+Font.ITALIC,20));
		panel4.setBorder(BorderFactory.createLineBorder(Color.black));
		label1.setText("OUTPUT DISPLAYED HERE");
		label1.setMinimumSize(new Dimension(20,20));
		panel4.add(label1);
		this.add(BorderLayout.SOUTH,panel4);
		
		
		
		frame_listener = new ActionListener_Frame_Class();
		button.addActionListener(frame_listener);
		listlistener = new ActionListener_ListClass();
		list.addListSelectionListener(listlistener);
		
		}	

	public class ActionListener_Frame_Class  implements ActionListener 
	{
		Font f;
		boolean first_click=true;
		public void actionPerformed(ActionEvent event)
		{
			            if(event.getSource()!=button)	return ;			
 		    			String output = CoCo_batch.line(textfield.getText());
						label1.setText("SYNDROME--->"+output);
		}
	}	
	
	
	public class ActionListener_ListClass implements ListSelectionListener
	{
		boolean  is_this_duplicate_event;
	 ActionListener_ListClass()
	 {
		is_this_duplicate_event = false; 
	 }
	 public void valueChanged(ListSelectionEvent event)	
	 {  
		
		 if(!is_this_duplicate_event) 
		 {
			 String complaintstring = list.getSelectedValue()+" "+textfield.getText();
			 textfield.setText(complaintstring);
			 is_this_duplicate_event = true;
		 }
		 else 
		is_this_duplicate_event = false;												
	 }
	}	
}

