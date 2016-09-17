import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.Thread;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class World extends Canvas {

	
	
	
	//GAME STARTED OR NOT  ( ABOUT BUTTON/ START BUTTON)
	public static boolean gameStarted=false;
	
	//WORLD IS A CANVAS OBJECT TO DRAW ON....READ ON CANVAS FROM WEB!!!!
	
	int timePast=(int)(System.currentTimeMillis())/1000;
	int timeCurrent;
	
	public static boolean started=false;
	public static boolean graphicOnline=false;
	
	static GraphicGroup graphic=null;
	

	
	
	
	//CONSTANTS
	public static float gravity = 2000;  // pixels per second
	public static float restitution = 0.85F;
	
	
	
	// Rendering / Buffer objects to draw  (THESE STUFF IS TO EASE OUR DRAWING, LIKE A PAINTING BOARD
	public BufferStrategy strategy;
	public Graphics2D g2;

	// Particle objects
	public particle[] particles = new particle[50];
	public particle currentParticle;
	public int particleCount=0;
	//Obstacle objects
	Obstacle[] obstacles=new Obstacle[50];
	int obstacleCount=0;
	
	public static particle selectedParticle=null;
	

	// Power Arrow
	public PowerArrow powerArrow;
	public float arrowScale = 5.0f;

	
	int mouseX=0, mouseY=0;


	public World()
	{
		setPreferredSize(new Dimension(1200, 600));

		//USING A PRIVATE CLASS FOR MOUSE EVENTS
		MouseHandler mouseHandler = new MouseHandler();
		addMouseMotionListener(mouseHandler);
		addMouseListener(mouseHandler);
	}

	// Start Render and Update ( THE LOOP )
	public void start()
	{
		mainLoop();
	}

	public void mainLoop()
	{

		while(true)
		{
			

			//update game
			if(started&&gameStarted)
			updateGame();
			//draw objects
			render();
			

		}

	}


	public void clearBalls()
	{
		particleCount = 0;
	}

	public void setGravity(float pixelsPerSecond)
	{
		gravity = pixelsPerSecond;
	}

	

	

	public void render()
	{
		if (strategy == null || strategy.contentsLost())
		{
			// Create BufferStrategy for rendering/drawing
			createBufferStrategy(2);
			strategy = getBufferStrategy();
			Graphics g = strategy.getDrawGraphics();
			this.g2 = (Graphics2D) g;
		}
		// Turn on anti-aliasing.. Antialiasing makes screen pixels fluit for ovals. Delete to see the difference. 
				this.g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		
				//DRAW BG IMAGE IF NOT STARTED 
		if(!gameStarted)
		{
			this.g2.setColor(Color.BLACK);
			this.g2.fillRect(0, 0, getWidth(), getHeight());
			
			this.g2.drawImage(new ImageIcon("src/img.jpg").getImage(), 0, 0, getWidth(), getHeight(), this);
			
			//TO OPEN DRAWING OBJECT IF ITS NOT VISIBLE
	  		if (!strategy.contentsLost()) strategy.show();
	  		return;
		}
		
		
		if(graphicOnline)
		{
			if(graphic!=null)
				graphic.repaint();
		}

		


		


		// Render Background
		this.g2.setColor(Color.BLACK);
		this.g2.fillRect(0, 0, getWidth(), getHeight());
		
		this.g2.setColor(Color.white);
			this.g2.drawString("Mouse X: "+mouseX+"Mouse Y:"+mouseY,  getWidth()/12 - (this.g2.getFontMetrics().stringWidth("Mouse X: "+mouseX+"Mouse Y:"+mouseY)/2), getHeight()/12);


		

		// Render  Objects
		for(int i = 0; i < particleCount; i++)
		{
			particles[i].draw(this.g2);
		}
		for(int i=0;i<obstacleCount;i++)
		{
			obstacles[i].draw(g2);
		}

		//THE PARTICLE THAT IS JUST BEING CREATED
		particle tempBall = currentParticle;
		if (tempBall != null) tempBall.draw(this.g2);


		// Render Foreground (texts, etc)

  		// Draw Power Arrow and Speed Text along arrow if we are launching a particle
  		PowerArrow tempArrow = powerArrow;
  		if (tempArrow != null)
  		{
  			tempArrow.draw(this.g2);

  			}

  		// Display Help Text in center if no particles
  		if (particleCount == 0 && currentParticle == null)
  		{
  			String helpString = "Click and drag your mouse to launch a particle.";
  			this.g2.setColor(Color.WHITE);
  			this.g2.drawString(helpString,  getWidth()/2 - (this.g2.getFontMetrics().stringWidth(helpString)/2), getHeight()/2);
  		}

  		// Draw particle count
  		this.g2.setColor(Color.WHITE);
  		this.g2.drawString(" Number of particles: "  + particleCount, 15, 15);


  		//TO OPEN DRAWING OBJECT IF ITS NOT VISIBLE
  		if (!strategy.contentsLost()) strategy.show();

	}

	public void updateGame()
	{
		timeCurrent=(int)(System.currentTimeMillis())/1000;

		if(timeCurrent!=timePast)
		{
			timePast++;
			
			for(int a=0;a<particleCount;a++)
			{
				particles[a].posX.add(particles[a].position.getX());
				particles[a].posY.add(particles[a].position.getY());
				particles[a].vel.add(particles[a].velocity.getLength());
				particles[a].accerelation.add(particles[a].vel.get(particles[a].vel.size()-1)-particles[a].vel.get(particles[a].vel.size()-2));



			}	
			
		}
		
		// step the position of particles based on their velocity/gravity and elapsedTime
		
		// We update game every 5 miliseconds not every seconds so , we apply vectors with a constant factor (float) (0.01)
		for (int i = 0; i < particleCount; i++)
		{
			//APPLY GRAVITY TO PARTICLES
			particles[i].velocity.setY(particles[i].velocity.getY() + (gravity *(float) (0.01)));
			//CHANGE POSITION AFTER APPLYING VELOCITY
			particles[i].position.setX(particles[i].position.getX() + (particles[i].velocity.getX() * (float) (0.01)));
			particles[i].position.setY(particles[i].position.getY() + (particles[i].velocity.getY() * (float) (0.01)));

			
		}

		checkCollisions();

	}

	public static void openGraphic()
	{
		if(selectedParticle!=null)
		{
			JFrame frame = new JFrame ("GRAPHICAL RESULTS");
			graphic=new GraphicGroup(selectedParticle);
	        frame.getContentPane().add(graphic);
	        frame.pack();
	        frame.setSize(620,620);
	        frame.setVisible (true);
	        frame.repaint();
	        frame.validate();
	        graphicOnline=true;

		}
	}

	public void checkCollisions()
	{


		// Check for collision with walls
		for (int i = 0; i < particleCount; i++)
		{

			if (particles[i].position.getX() - particles[i].getRadius() < 0)
			{
				particles[i].position.setX(particles[i].getRadius()); // Place ball against edge
				particles[i].velocity.setX(-(particles[i].velocity.getX() * restitution)); // Reverse direction and substitue friction.
				particles[i].velocity.setY(particles[i].velocity.getY() * restitution);
			}
			else if (particles[i].position.getX() + particles[i].getRadius() > getWidth()) // Right Wall
			{
				particles[i].position.setX(getWidth() - particles[i].getRadius());		// Place ball against edge
				particles[i].velocity.setX(-(particles[i].velocity.getX() * restitution)); // Reverse direction and substitue friction.
				particles[i].velocity.setY((particles[i].velocity.getY() * restitution));
			}

			if (particles[i].position.getY() - particles[i].getRadius() < 0)				// Top Wall
			{
				particles[i].position.setY(particles[i].getRadius());				// Place ball against edge
				particles[i].velocity.setY(-(particles[i].velocity.getY() * restitution)); // Reverse direction and substitue friction.
				particles[i].velocity.setX((particles[i].velocity.getX() * restitution));
			}
			else if (particles[i].position.getY() + particles[i].getRadius() > getHeight()) // Bottom Wall
			{
				particles[i].position.setY(getHeight() - particles[i].getRadius());		// Place ball against edge
				particles[i].velocity.setY(-(particles[i].velocity.getY() * restitution));    // Reverse direction and substitue friction.
				particles[i].velocity.setX((particles[i].velocity.getX() * restitution));
			}

			// Ball to Ball collision
			
			for(int a=0;a<particleCount;a++)
			{
				for(int b=0;b<particleCount;b++)
				{
					if(particles[a].colliding(particles[b]))
					{
						particles[a].resolveCollision(particles[b]);
					}
				}
			}
			for(int a=0;a<particleCount;a++)
			{
				for(int b=0;b<obstacleCount;b++)
				{
					if(obstacles[b].colliding(particles[a]))
					{
						obstacles[b].resolveCollision(particles[a]);
					}
				}
			}
		}

	}

	
	//MOUSE CONTROL CLASS
	public class MouseHandler extends MouseAdapter implements MouseMotionListener
	{
		public void mousePressed(MouseEvent e)
	   	{
			//IF DESIRED TO CREATE PARTICLE
			if(ControlPanel.particle)
			{
				currentParticle = new particle(e.getX(), e.getY(), ControlPanel.preferredRadius, ControlPanel.preferredRadius);
				powerArrow = new PowerArrow(e.getX(), e.getY(), e.getX(), e.getY());
			}
			//IF OBSTACLE
			else if(ControlPanel.obstacle)
			{
				obstacles[obstacleCount]=new Obstacle(e.getX(), e.getY(),ControlPanel.preferredRadius);
				obstacleCount++;
			}
			else
			{
				deselectAll();
				getSelected(e.getX(), e.getY());
				
			}
			
	   	}

		
	   public void mouseReleased(MouseEvent e)
	   {
		   
		   if(ControlPanel.particle)
		   {
			// Change in x/y per second
			   float xVector = (powerArrow.getX2() - powerArrow.getX1()) * 5;
			   float yVector = (powerArrow.getY2() - powerArrow.getY1()) * 5;

			   //SET VELOCITY OF LAST PARTICLE TO OBSERVED VECTORS (FROM POWER ARROW)
			   currentParticle.velocity.set(xVector, yVector);
			   particles[particleCount] = currentParticle;
			   particleCount++;

			   
			   currentParticle = null;
			   powerArrow = null;
		   }
		   
	   }

	   public void mouseDragged(MouseEvent e)
	   {
		   
		   if(ControlPanel.particle)
		   {
			   //OBSERVE CHANGE IN POWER ARROW
				int x1 = powerArrow.getX1();
				int y1 = powerArrow.getY1();
				int x2 = e.getX();
				int y2 = e.getY();
				int dx = Math.abs(x2 - x1);
				int dy = Math.abs(y2 - y1);

				if ((x2 - x1) < 0)
				{
					powerArrow.setX2(x1 + dx);
				}
				else
				{
					powerArrow.setX2(x1 - dx);
				}

				if ((y2 - y1) < 0)
				{
					powerArrow.setY2(y1 + dy);
				}
				else
				{
					powerArrow.setY2(y1 - dy);
				}
		   }
		  
	   }

	   public void mouseMoved(MouseEvent e)
	   {
		   mouseX=e.getX();
		   mouseY=e.getY();
	   }
	   
	   public void getSelected(int x,int y)
	   {
		   selectedParticle=null;
		   for(int a=0;a<particleCount;a++)
		   {
			   if(particles[a].isSelected(x, y))
			   {
				   selectedParticle=particles[a];
				   selectedParticle.isSelected=true;
				   break;
			   }
		   }
	   }
	   public void deselectAll()
	   {
		   for(int a=0;a<particleCount;a++)
		   {
			   particles[a].isSelected=false;
		   }
	   }
	   
	   
  }



}
