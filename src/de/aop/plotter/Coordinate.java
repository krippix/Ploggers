package de.aop.plotter;

// This class is supposed to make handling coordinates easier to handle
public class Coordinate
{
	public int x = 0;
	public int y = 0;
	
	
	Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	// doubles will be rounded
	Coordinate(double x, double y)
	{
		this.x = (int)Math.round(x);
		this.y = (int)Math.round(x);
	}
	
	Coordinate(double x, int y)
	{
		this.x = (int)Math.round(x);
		this.y = y;
	}
	
	Coordinate(int x, double y)
	{
		this.x = x;
		this.y = (int)Math.round(x);
	}
}
