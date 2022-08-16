package de.aop.parser;

import java.util.ArrayList;

import de.aop.exceptions.SyntaxError;

/**
 * A wrapper around a Parser object with some additional
 * QoL methods for function evaluation and analysis
 */
public class Function
{
	// Used in the numerical differentiation algorithm
	static final double MACHINE_EPSILON = Math.ulp(1.0);
	static final double MAX_ITERATIONS = 100;
	static final double ERROR_SCALE = 0.0001;
	
	private Parser parser = null;
	
	// Domain and range of the function
	private Interval domain;
	private Interval range;
	
	private ArrayList<Double> roots;
	private ArrayList<Double> extrema;
	private ArrayList<Double> inflections;
	private ArrayList<Double> poles;
	
	public Function(String input, double from, double to)
	{
		parser = new Parser(input);
		
		if(!isValid())
			return;
		
		domain = new Interval(from, to);
		this.analyze();
	}
	
	private void analyze()
	{
		range = new Interval(0, 0);
	
		
		roots 		= new ArrayList<Double>();
		extrema 	= new ArrayList<Double>();
		inflections = new ArrayList<Double>();
		poles 		= new ArrayList<Double>();
		
		findRoots(roots, domain, 0);
		findRoots(extrema, domain, 1);
		findRoots(inflections, domain, 2);
		
		findPoles(domain);
		
		if(!poles.isEmpty())
		{
			if(!extrema.isEmpty()) 
			{
				for (Double x : extrema) 
				{
					range.min = Math.min(range.min, this.at(x));
					range.max = Math.max(range.max, this.at(x));
				}
			}
			else 
			{
				range.min = -10;
				range.max = 10;
			}
		}
		else
		{
			for(double x = domain.min; x <= domain.max; x += 0.01)
			{
				double y = this.at(x);
				range.min = Math.min(range.min, y);
				range.max = Math.max(range.max, y);
			}
		}
	}
	
	public ArrayList<Double> getRoots()
	{
		return roots;
	}
	
	public ArrayList<Double> getExtrema()
	{
		return extrema;
	}
	
	public ArrayList<Double> getInflections()
	{
		return inflections;
	}
	
	public ArrayList<Double> getPoles()
	{
		return poles;
	}
	
	public void setDomain(double newMin, double newMax)
	{
		this.domain.min = newMin;
		this.domain.max = newMax;
		
		this.analyze();
	}
	
	/**
	 * Whether the parsing was successful
	 * @return True if the expression was parsed without errors
	 */
	public boolean isValid()
	{
		return parser != null && parser.good();
	}
	
	public SyntaxError getSyntaxError()
	{
		return parser.getError();
	}
	
	/**
	 * Value of the function at position x
	 * @param x Position to evaluate the function at
	 * @return The value of the function at position x
	 */
	public double at(double x)
	{
		return parser.eval(x);
	}
	
	/**
	 * Value of the n-th derivative of the function, evaluated at x
	 * @param x Position to evaluate f^(n) at
	 * @param n Which derivative of the function to evaluate (must be non-negative)
	 * @return The value of the n-th derivative of the function at position x
	 * 
	 * @throws IllegalArgumentException If n was negative
	 */
	public double at(double x, int n) throws IllegalArgumentException
	{
		if(n < 0)
			throw new IllegalArgumentException("derivative has to be a non-negative integer");
		
		// 0th derivative is just the function itself
		if(n == 0)
			return parser.eval(x);
		
		// Calculate h such that x+h and x-h have exact representations as floating point numbers
		double h = Math.cbrt(MACHINE_EPSILON) * x;
		
		// Perform numerical differentiation
		return (this.at(x + h, n - 1) - this.at(x - h, n - 1)) / (2.0*h);
	}
	
	public Interval getDomain()
	{
		return domain;
	}
	
	public Interval getRange()
	{
		return range;
	}
	
	private boolean signsDifferent(double a, double b)
	{
		return Math.signum(a) != Math.signum(b) && Math.abs(b - a) > ERROR_SCALE;
	}
	
	/**
	 * Finds all roots of the n-th derivative of the function on an interval.
	 * The algorithm used is the bisection algorithm
	 * 
	 * @param dest Array to hold the found roots
	 * @param interval Interval to search for roots
	 * @param n Derivative to inspect
	 */
	private void findRoots(ArrayList<Double> dest, Interval interval, int n)
	{
		// Find the interval this function should search.
		// It starts with the smallest possible interval and then incrementally
		// enlarges it until the conditions for bisection are satisfied
		Interval searchInterval = new Interval(interval.min, interval.min);
		double stepSize = 0.001 * interval.length();
		while (searchInterval.max <= interval.max && !signsDifferent(this.at(searchInterval.min, n), this.at(searchInterval.max, n)))
			searchInterval.max += stepSize;
			
		// If the search interval is larger than the given interval, abort; there are no roots on this interval
		if(searchInterval.max > interval.max || !signsDifferent(this.at(searchInterval.min, n), this.at(searchInterval.max, n)))
			return;
		
		// Find all roots on the remaining interval
		findRoots(dest, new Interval(searchInterval.max, interval.max), n);
		
		final double MAX_ERROR = ERROR_SCALE * searchInterval.length();
		
		// Bisection algorithm
		for(int i = 0; i < MAX_ITERATIONS; i++)
		{
			// Calculate f at center point of interval
			double c = this.at(searchInterval.center(), n);
			
			// Shrink interval on the left or right, depending on the sign of the function values
			if(Math.signum(c) == Math.signum(this.at(searchInterval.min, n)))
				searchInterval.min = searchInterval.center();
			else
				searchInterval.max = searchInterval.center();
			
			// If the interval is small enough, try to finish
			if (searchInterval.max - searchInterval.min <= MAX_ERROR)
			{
				// If the function values are above the error, this point is most likely a pole and not a root
				// To be sure, perform more iterations
				if(Math.abs(this.at(searchInterval.max, n) - this.at(searchInterval.min, n)) > MAX_ERROR)
					continue;
				
				// Add the root to the array
				dest.add(searchInterval.center());
				break;
			}
		}
	}
	
	/**
	 * Returns the inverse of the function at position x. It is used in the pole finding algorithm
	 * @param x Position to evaluate the function at
	 * @return The inverse of the function at position x
	 */
	private double inverseAt(double x)
	{
		double y = this.at(x);
		if (Double.isNaN(y))
			return 0.0;
		
		return 1.0 / y;
	}
	
	/**
	 * Finds all poles of the function on an interval.
	 * It performs a bisection of the inverse of f
	 * 
	 * @param interval
	 */
	private void findPoles(Interval interval)
	{
		Interval searchInterval = new Interval(interval.min, interval.min);
		double stepSize = 0.001 * interval.length();
		while(searchInterval.max <= interval.max && !signsDifferent(this.inverseAt(searchInterval.min), this.inverseAt(searchInterval.max)))
			searchInterval.max += stepSize;
		
		if(searchInterval.max >= interval.max || !signsDifferent(this.inverseAt(searchInterval.min), this.inverseAt(searchInterval.max)))
			return;
		
		findPoles(new Interval(searchInterval.max, interval.max));
		
		final double MAX_ERROR = ERROR_SCALE * searchInterval.length();
		
		for(int i = 0; i < MAX_ITERATIONS; i++)
		{
			double c = this.inverseAt(searchInterval.center());
			
			if(Math.signum(c) == Math.signum(this.inverseAt(searchInterval.min)))
				searchInterval.min = searchInterval.center();
			else
				searchInterval.max = searchInterval.center();
			
			if (searchInterval.max - searchInterval.min <= MAX_ERROR)
			{
				if(Math.abs(this.inverseAt(searchInterval.max) - this.inverseAt(searchInterval.min)) > MAX_ERROR)
					continue;
				
				poles.add(searchInterval.center());
				break;
			}
		}
	}
}
