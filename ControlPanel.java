import java.awt.*;
import java.util.Random;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.image.*;
import java.lang.Thread;

public class ControlPanel extends JPanel {

	
	 World world;
	 JButton resetButton;
	 JButton generateButton;
	 JButton scatterButton;
	 JButton colorButton;
	 JButton startButon;
	 
	 JButton graphics;
	 JSlider gravitySlider;
	 JSlider corSlider;
	 JSlider radius;
	 JRadioButton part;
	 JRadioButton obst;
	 
	
	
	
	//Selection of radio button
	public static boolean particle=false;
	public static boolean obstacle=false;
	//Selected radius, base 30
	public static int preferredRadius=30;
	public static Color preferredColor=Color.red;
	
	public ControlPanel(World world)
	{
		this.setPreferredSize(new Dimension(1200, 60));
		this.world = world;
	
		// instantiate controls
		part=new JRadioButton("Particle");
		obst=new JRadioButton("Obstacle");
		
		resetButton = new JButton("Reset");
		colorButton=new JButton("Color");
		generateButton = new JButton("Generate");
		scatterButton = new JButton("Scatter");
		graphics=new JButton("Graphics.");
		startButon=new JButton("Start/Stop");
		
		//Slider settings, information and tick space
		gravitySlider = new JSlider(JSlider.HORIZONTAL, 0, 3000, 2000);
		gravitySlider.setBorder(BorderFactory.createTitledBorder("Gravity - " + gravitySlider.getValue() + "px/s"));
		gravitySlider.setMajorTickSpacing(200);
		gravitySlider.setMinorTickSpacing(100);
		//Draw ticks
		gravitySlider.setPaintTicks(true);
		
		corSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 2);
		corSlider.setBorder(BorderFactory.createTitledBorder("Restitution - " + corSlider.getValue() + "%"));
		corSlider.setMajorTickSpacing(10);
		corSlider.setMinorTickSpacing(1);
		corSlider.setPaintTicks(true);
		
		radius = new JSlider(JSlider.HORIZONTAL, 0, 100, 85);
		radius.setBorder(BorderFactory.createTitledBorder("Radius - " + radius.getValue() + "px"));
		radius.setMajorTickSpacing(10);
		radius.setMinorTickSpacing(5);
		radius.setPaintTicks(true);
		
		// add controls to panel
		this.add(gravitySlider);
		this.add(corSlider);
		this.add(part);
		this.add(obst);
		this.add(radius);
		this.add(colorButton);
		this.add(graphics);
		this.add(resetButton);
		this.add(startButon);
		
		
		// Use a  class for button events... (Under this class)
		butonListener buttonHandler = new butonListener();
		resetButton.addActionListener(buttonHandler);
		scatterButton.addActionListener(buttonHandler);
		generateButton.addActionListener(buttonHandler);
		part.addActionListener(buttonHandler);
		graphics.addActionListener(buttonHandler);
		colorButton.addActionListener(buttonHandler);
		obst.addActionListener(buttonHandler);
		startButon.addActionListener(buttonHandler);
		
		// Use a  class for slider events... (Under this class)

		sliderListener sliderHandler = new sliderListener();
		gravitySlider.addChangeListener(sliderHandler);
		radius.addChangeListener(sliderHandler);
		corSlider.addChangeListener(sliderHandler);
		
	}
	
	 class butonListener implements ActionListener
	{
		
		//ACTION LİSTENER ACTIVATES actionPerformed method on any event 

		public void actionPerformed(ActionEvent e) 
		{
			
			if(e.getSource()==colorButton)
			{
				preferredColor= JColorChooser.showDialog(null,
			            "Color Selection",Color.cyan);
			}
			if (e.getSource() == resetButton)
			{
				world.clearBalls();
			}
			
			//IF RADIO BUTTON SELECTED OR NOT
			if(e.getSource()==part)
			{
				if(part.isSelected())
				{
					particle=true;
					obstacle=false;
					

				}
				else
				{
					particle=false;
					part.setSelected(false);
					repaint();

				}
			}
			if(e.getSource()==startButon)
			{
				world.started=!world.started;
			}
			if(e.getSource()==graphics)
			{
				
				world.openGraphic();
			}
			if(e.getSource()==obst)
			{
				if(obst.isSelected())
				{
					obstacle=true;
					particle=false;
					
				}
				else
				{
					
					obstacle=false;
					obst.setSelected(false);
					repaint();

				}
			}
			
			
			
			
			
		}
	
	}
	
	 class sliderListener implements ChangeListener
	{

		//ChangeListener activates stateChanged method on any change 
		public void stateChanged(ChangeEvent e) 
		{
			
			JSlider source = (JSlider)e.getSource();
			
			//IF GRAVITY SLIDER CHANGED
			if (source == gravitySlider)
			{
				source.setBorder(BorderFactory.createTitledBorder("Gravity - " + source.getValue() + "px/s"));
				world.setGravity(source.getValue());
			}
			
			//IF RADIUS SLIDER CHANGED
			if(source==radius)
			{
				radius.setBorder(BorderFactory.createTitledBorder("Radius - " + radius.getValue() + "px"));
				preferredRadius=radius.getValue();
			}
			
			if(source==corSlider)
			{
				corSlider.setBorder(BorderFactory.createTitledBorder("Restitution - " + corSlider.getValue() + "%"));
				world.restitution=corSlider.getValue();
			}
			repaint();

		}
	}	
}
