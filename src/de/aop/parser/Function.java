package de.aop.parser;

import de.aop.exceptions.SyntaxError;

/**
 * A wrapper around a Parser object with some additional
 * QoL life methods for function evaluation and analysis
 */
public class Function
{
	// Used in the numerical differentiation algorithm
	static double MACHINE_EPSILON = Math.ulp(1.0);
	
	Parser parser = null;
	
	public Function(String input)
	{
		parser = new Parser(input);
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
}
