import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;


public class Graphic extends JPanel {

	/**
	 * Create the panel.
	 */
	
	int time;
	ArrayList<Float> data;
	double dataPerPixel;
	int timeStepPixel;
	int type;
	particle par;
	public Graphic(particle par,int type) {

		this.par=par;
		this.type=type;//1=X,2=Y,3=VELOCITY,4=ACCERELATION
		setSize(300,300);
		this.time=par.timePast;
		time=5;
		if(type==4)
				dataPerPixel=1;
		else
				dataPerPixel=0.25;
		
		if(type==1)
			this.data=par.posX;
		else if(type==2)
			this.data=par.posY;
		else if(type==3)
			this.data=par.vel;
		else if(type==4)
			this.data=par.accerelation;
		
		
		
	}
	
	public void paintComponent(Graphics g)
	{
		
		//UPDATE DATA
		time=par.timePast;
		if(type==1)
			this.data=par.posX;
		else if(type==2)
			this.data=par.posY;
		else if(type==3)
			this.data=par.vel;
		else if(type==4)
			this.data=par.accerelation;
		
		
		
//		if(data.size()<280)
	//	dataPerPixel=280/data.size();
		//else
	//	dataPerPixel=data.size()/280;
		
		if(time<280&&time!=0)
		{
			timeStepPixel=280/time;
		}
		else if(time==0)
			timeStepPixel=280;
		
		
		
		
		//GRAPHIC MAIN LINES
		g.drawLine(10, 0, 10, 280);
		g.drawLine(10, 280, 290, 280);
		if(type==1)
			g.drawString("POS-X/TIME",150,150);
		else if(type==2)
			g.drawString("POS-Y/TIME",150,150);
		else if(type==3)
			g.drawString("VELOCITY/TIME",150,150);
		else if(type==4)
			g.drawString("ACCERELATION/TIME",150,150);

		///////////////////////////////
			//DATA LINES
		
		for(int a=0;a<data.size();a++)
		{
			if(a+1<data.size())
			{
				
				g.drawLine(10+a*timeStepPixel, 280-(int)(data.get(a)*dataPerPixel), 10+(a+1)*timeStepPixel, 280-(int)(data.get(a+1)*dataPerPixel));
			}
		}
	}
	
	

}
