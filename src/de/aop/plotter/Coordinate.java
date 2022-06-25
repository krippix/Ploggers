package de.aop.plotter;

// This class is supposed to make handling coordinates easier to handle
public class Coordinate
{
	public double x = 0;
	public double y = 0;
	
	
	// Constructors
	
	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	public Coordinate(int x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	public Coordinate(double x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	public Coordinate(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setCoordinates(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	public void print()
	{
		System.out.println("("+this.x+","+this.y+")");
	}
	
	public Coordinate clone()
	{
		return new Coordinate(this.x, this.y);
	}
	
	
	
	public int xAsInt()
	{
		return (int) Math.round(this.x);
	}
	
	public int yAsInt()
	{
		return (int) Math.round(this.y);
	}
}
