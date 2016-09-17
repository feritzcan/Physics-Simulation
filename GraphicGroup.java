import java.awt.GridLayout;

import javax.swing.JPanel;


public class GraphicGroup extends JPanel {

	/**
	 * Create the panel.
	 */
	Graphic vel;
	Graphic x;
	Graphic y;
	Graphic acc;
	particle par;
	public GraphicGroup(particle par) {
		setSize(600,600);
		this.par=par;
		if(par!=null)
		{
			vel=new Graphic(par,3);
			x=new Graphic(par,1);
			y=new Graphic(par,2);
			acc=new Graphic(par,4);

			setLayout(new GridLayout(2, 2));
			add(vel);
			add(x);
			add(y);
			add(acc);
		}
		
	}
	
	public void repaint()
	{
		if(par!=null)
		{
			
			super.repaint();
		}
		
	}

}
