package src.java.gui;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
@SuppressWarnings("serial")
public class ActionListener_Frame_Class extends Frame_Class implements ActionListener 
	{
		Font f;
		public void actionPerformed(ActionEvent event)
		{
			f = new Font("Serif",Font.ITALIC,10);
			textfield.setFont(f);
		
		}
	}