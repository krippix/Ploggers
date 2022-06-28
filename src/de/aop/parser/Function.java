package de.aop.parser;

import java.util.ArrayList;

import de.aop.exceptions.SyntaxError;

/**
 * A wrapper around a Parser object with some additional
 * QoL life methods for function evaluation and analysis
 */
public class Function
{
	// Used in the numerical differentiation algorithm
	static double MACHINE_EPSILON = Math.ulp(1.0);
	
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
		range = new Interval(this.at(domain.min), this.at(domain.min));
		
		for(double x = domain.min; x <= domain.max; x += 0.01)
		{
			double y = this.at(x);
			range.min = Math.min(range.min, y);
			range.max = Math.max(range.max, y);
		}
		
		roots 		= new ArrayList<Double>();
		extrema 	= new ArrayList<Double>();
		inflections = new ArrayList<Double>();
		poles 		= new ArrayList<Double>();
		
		findRoots(roots, domain, 0);
		findRoots(extrema, domain, 1);
		findRoots(inflections, domain, 2);
		
		findPoles(domain);
		
		System.out.println("Roots      :" + roots.toString());
		System.out.println("Extrema    :" + extrema.toString());
		System.out.println("Inflections:" + inflections.toString());
		System.out.println("Poles      :" + poles.toString());
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
	
	private void findRoots(ArrayList<Double> dest, Interval interval, int n)
	{
		final int MAX_ITERATIONS = 100;
		final double MAX_ERROR = 0.000001;
		
		Interval searchInterval = new Interval(interval.min, interval.min);
		while(searchInterval.max <= interval.max && Math.signum(this.at(searchInterval.min, n)) == Math.signum(this.at(searchInterval.max, n)))
			searchInterval.max += 0.01;
		
		if(searchInterval.max >= interval.max || Math.signum(this.at(searchInterval.min, n)) == Math.signum(this.at(searchInterval.max, n)))
			return;
		
		findRoots(dest, new Interval(searchInterval.max, interval.max), n);
		
		Interval originalSearchInterval = new Interval(searchInterval.min, searchInterval.max);
		boolean foundRoot = false;
		double rootVal = 0.0;
		
		for(int i = 0; i < MAX_ITERATIONS; i++)
		{
			double c = this.at(searchInterval.center(), n);
			System.out.println(c);
			
			if(Math.signum(c) == Math.signum(this.at(searchInterval.min, n)))
				searchInterval.min = searchInterval.center();
			else
				searchInterval.max = searchInterval.center();
			
			if (searchInterval.max - searchInterval.min <= MAX_ERROR)
			{
				if(Math.abs(this.at(searchInterval.max, n) - this.at(searchInterval.min, n)) > MAX_ERROR)
					continue;
				
				foundRoot = true;
				rootVal = searchInterval.center();
				break;
			}
		}
		
		if (foundRoot)
		{
			dest.add(rootVal);
			findRoots(dest, new Interval(originalSearchInterval.min, rootVal - 0.01), n);
			findRoots(dest, new Interval(rootVal + 0.01, originalSearchInterval.max), n);
		}
	}
	
	private double inverseAt(double x)
	{
		double y = this.at(x);
		if (Double.isNaN(y))
			return 0.0;
		
		return 1.0 / y;
	}
	
	private void findPoles(Interval interval)
	{
		final int MAX_ITERATIONS = 100;
		final double MAX_ERROR = 0.000001;
		
		Interval searchInterval = new Interval(interval.min, interval.min);
		while(searchInterval.max <= interval.max && Math.signum(this.inverseAt(searchInterval.min)) == Math.signum(this.inverseAt(searchInterval.max)))
			searchInterval.max += 0.01;
		
		if(searchInterval.max >= interval.max || Math.signum(this.inverseAt(searchInterval.min)) == Math.signum(this.inverseAt(searchInterval.max)))
			return;
		
		findPoles(new Interval(searchInterval.max, interval.max));
		
		Interval originalSearchInterval = new Interval(searchInterval.min, searchInterval.max);
		boolean foundRoot = false;
		double rootVal = 0.0;
		
		for(int i = 0; i < MAX_ITERATIONS; i++)
		{
			double c = this.inverseAt(searchInterval.center());
			System.out.println(c);
			
			if(Math.signum(c) == Math.signum(this.inverseAt(searchInterval.min)))
				searchInterval.min = searchInterval.center();
			else
				searchInterval.max = searchInterval.center();
			
			if (searchInterval.max - searchInterval.min <= MAX_ERROR)
			{
				if(Math.abs(this.inverseAt(searchInterval.max) - this.inverseAt(searchInterval.min)) > MAX_ERROR)
					continue;
				
				foundRoot = true;
				rootVal = searchInterval.center();
				break;
			}
		}
		
		if (foundRoot)
		{
			poles.add(rootVal);
			findPoles(new Interval(originalSearchInterval.min, rootVal - 0.01));
			findPoles(new Interval(rootVal + 0.01, originalSearchInterval.max));
		}
	}
}
