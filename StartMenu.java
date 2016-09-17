import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class StartMenu extends JPanel {

	/**
	 * Create the panel.
	 */
	
	
	String aboutInfo=" ISIM 1 \n ISIM 2...";
	
	JButton start,about;
	public StartMenu() {
		
		this.setPreferredSize(new Dimension(1000,60));
		start=new JButton("START");
		about=new JButton("ABOUT");
		this.add(start);
		this.add(about);
		butonListener listener=new butonListener();
		start.addActionListener(listener);
		about.addActionListener(listener);



	}
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	class butonListener implements ActionListener
	{
		
		//ACTION LİSTENER ACTIVATES actionPerformed method on any event 

		public void actionPerformed(ActionEvent e) 
		{
			
			if(e.getSource()==about)
			{
				JOptionPane.showMessageDialog(null,  aboutInfo,"ABOUT",2);
				
			}
			if(e.getSource()==start)
			{
				MainFrame.start();
			}
			
			
		}
	
	}

}
