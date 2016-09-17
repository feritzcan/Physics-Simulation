import java.awt.*;
import java.util.ArrayList;

public class particle {

	public Vector2d velocity;
	public Vector2d position;
	public float mass;
	public float radius;
	public Color color=Color.cyan;
	public boolean isSelected=false;
	
	
	int timePast;
	int timeCurrent;
	
	long creationTime;

	//GRAPHICAL DATA
	public ArrayList<Float> posX;
	public ArrayList<Float> posY;
	public ArrayList<Float> vel;
	public ArrayList<Float> accerelation;

	

	public particle(float x, float y, float radius, float mass)
	{
		
		this.color=ControlPanel.preferredColor;
		this.velocity = new Vector2d(0, 0);
		this.position = new Vector2d(x, y);
		this.setMass(mass);
		this.setRadius(radius);
		
		posX=new ArrayList<Float>();
		posY=new ArrayList<Float>();
		vel=new ArrayList<Float>();
		accerelation=new ArrayList<Float>();
		
		posX.add(x);
		posY.add(y);
		vel.add(velocity.getLength());
		accerelation.add(0f);
	}

	public Color getColor(float magnitude)
	{
		return color;
	}


	public void draw(Graphics2D g2)
	{

		if(isSelected)
			g2.setColor(color.WHITE);
		else
		g2.setColor(getColor(velocity.getLength()));
		g2.fillOval( (int) (position.getX() - getRadius()), (int) (position.getY() - getRadius()), (int) (2 * getRadius()) , (int) (2 * getRadius()) );

	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}


	//Two ovals collide only when distance btw their centers <= their total radius
	public boolean colliding(particle particle)
	{
		float xd = position.getX() - particle.position.getX();
		float yd = position.getY() - particle.position.getY();

		float sumRadius = getRadius() + particle.getRadius();
		float sqrRadius = sumRadius * sumRadius;

		float distSqr = (xd * xd) + (yd * yd);

		if (distSqr <= sqrRadius)
		{
			return true;
		}

		return false;

	}

	
	public void resolveCollision(particle particle)
	{

		// get the mtd
		Vector2d delta = (position.subtract(particle.position));
		float r = getRadius() + particle.getRadius();
		float dist2 = delta.dot(delta);

		if (dist2 > r*r) return; // they aren't colliding


		float d = delta.getLength();

		Vector2d mtd;
		if (d != 0.0f)
		{
			mtd = delta.multiply(((getRadius() + particle.getRadius())-d)/d); // minimum translation distance to push particles apart after intersecting

		}
		else // Special case. Balls are exactly on top of eachother.  Don't want to divide by zero.
		{
			d = particle.getRadius() + getRadius() - 1.0f;
			delta = new Vector2d(particle.getRadius() + getRadius(), 0.0f);

			mtd = delta.multiply(((getRadius() + particle.getRadius())-d)/d);
		}

		// resolve intersection
		float im1 = 1 / getMass(); // inverse mass quantities
		float im2 = 1 / particle.getMass();

		// push-pull them apart
		position = position.add(mtd.multiply(im1 / (im1 + im2)));
		particle.position = particle.position.subtract(mtd.multiply(im2 / (im1 + im2)));

		// impact speed
		Vector2d v = (this.velocity.subtract(particle.velocity));
		float vn = v.dot(mtd.normalize());

		// sphere intersecting but moving away from each other already
		if (vn > 0.0f) return;

		// collision impulse
		float i = (-(1.0f + World.restitution) * vn) / (im1 + im2);
		Vector2d impulse = mtd.multiply(i);

		// change in momentum
		this.velocity = this.velocity.add(impulse.multiply(im1));
		particle.velocity = particle.velocity.subtract(impulse.multiply(im2));

	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getMass() {
		return mass;
	}

	public int compareTo(particle particle) {
		if (this.position.getX() - this.getRadius() > particle.position.getX() - particle.getRadius())
		{
			return 1;
		}
		else if (this.position.getX() - this.getRadius() < particle.position.getX() - particle.getRadius())
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
	
	public boolean isSelected(int x,int y)
	{
		return colliding(new particle(x, y, 0, 0));
	}

}




