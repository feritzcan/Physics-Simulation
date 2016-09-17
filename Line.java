
public class Line {
	
	int startX, startY;
	int endX, endY;
	
	public Line(int start,int starty,int end,int endy)
	{
		this.startX=start;
		this.startY=starty;
		this.endX=end;
		this.endY=endy;
	}
	
	public void setEnds(int endx,int endy)
	{
		this.endX=endx;
		this.endY=endy;
	}

}
