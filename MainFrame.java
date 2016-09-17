import javax.swing.*;

public class MainFrame  extends JFrame{

	public static JFrame frame;
	public static StartMenu menu;
	public static World physicsWorld;
	public static  ControlPanel controlPanel;
	public static void main(String[] args)
	{
		
		
		frame = new JFrame("Physics Smilator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));

		menu=new StartMenu();
		 physicsWorld = new World();
		 controlPanel = new ControlPanel(physicsWorld);
		 
		 
		frame.getContentPane().add(physicsWorld);
		frame.getContentPane().add(menu);
	
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
		frame.pack();
		frame.setVisible(true);
		physicsWorld.start();		

	}
	
	
	public static void start()
	{
		
		World.gameStarted=true;
		menu.setVisible(false);
		frame.getContentPane().add(controlPanel);
		frame.getContentPane().repaint();
		frame.pack();
		
	}

}
