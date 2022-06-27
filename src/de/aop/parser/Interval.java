package de.aop.parser;

public class Interval
{
	public double min, max;
	
	public Interval(double min, double max)
	{
		this.min = min;
		this.max = max;
	}
	
	public double center()
	{
		return 0.5 * (max + min);
	}
	
	public boolean isInside(double x)
	{
		return (x >= this.min) && (x <= this.max);
	}
}
