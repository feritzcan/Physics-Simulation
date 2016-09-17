import java.awt.*;

public class Obstacle{

	public Vector2d velocity;
	public Vector2d position;
	public float mass;
	public float radius;
	public Color color=Color.red;
	

	

	public Obstacle(float x, float y, float radius)
	{
		this.color=ControlPanel.preferredColor;

		this.velocity = new Vector2d(0, 0);
		this.position = new Vector2d(x, y);
		this.mass=1000;
		this.setRadius(radius);
	}

	public Color getColor()
	{
		return color;
	}


	//DRAW THE OBSTACLE ON GIVEN Graphics object.
	public void draw(Graphics2D g2)
	{

		g2.setColor(getColor());
		g2.fillOval( (int) (position.getX() - getRadius()), (int) (position.getY() - getRadius()), (int) (2 * getRadius()) , (int) (2 * getRadius()) );

	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}


	//Two ovals collide only when distance btw their centers <= their total radius
	public boolean colliding(particle partic)
	{
		float xd = position.getX() - partic.position.getX();
		float yd = position.getY() - partic.position.getY();

		float sumRadius = getRadius() + partic.getRadius();
		float sqrRadius = sumRadius * sumRadius;

		float distSqr = (xd * xd) + (yd * yd);

		if (distSqr <= sqrRadius)
		{
			return true;
		}

		return false;

	}

	public void resolveCollision(particle partic)
	{

		// get the mtd
		Vector2d delta = (position.subtract(partic.position));
		float r = getRadius() + partic.getRadius();
		float dist2 = delta.dot(delta);

		if (dist2 > r*r) return; // they aren't colliding


		float d = delta.getLength();

		Vector2d mtd;
		if (d != 0.0f)
		{
			mtd = delta.multiply(((getRadius() + partic.getRadius())-d)/d); // minimum translation distance to push partics apart after intersecting

		}
		else // Special case. Balls are exactly on top of eachother.  Don't want to divide by zero.
		{
			d = partic.getRadius() + getRadius() - 1.0f;
			delta = new Vector2d(partic.getRadius() + getRadius(), 0.0f);

			mtd = delta.multiply(((getRadius() + partic.getRadius())-d)/d);
		}

		// resolve intersection
		float impulse1 = 1 / getMass(); // inverse mass quantities
		float impulse2 = 1 / partic.getMass();

		// push-pull them apart
		//position = position.add(mtd.multiply(impulse1 / (impulse1 + impulse2)));
		partic.position = partic.position.subtract(mtd.multiply(impulse2 / (impulse1 + impulse2)));

		// impact speed
		Vector2d v = (this.velocity.subtract(partic.velocity));
		float vn = v.dot(mtd.normalize());

		// sphere intersecting but moving away from each other already
		if (vn > 0.0f) return;

		// collision impulse
		float i = (-(1.0f + World.restitution) * vn) / (impulse1 + impulse2);
		Vector2d impulse = mtd.multiply(i);

		// change in momentum
		this.velocity = this.velocity.add(impulse.multiply(impulse1));
		partic.velocity = partic.velocity.subtract(impulse.multiply(impulse2));

	}

	

	public float getMass() {
		return mass;
	}

	
	
}




