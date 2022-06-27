package de.aop.parser;

import de.aop.exceptions.SyntaxError;

public class Function
{
	static double MACHINE_EPSILON = Math.ulp(1.0);
	
	Parser parser = null;
	
	public Function(String input)
	{
		parser = new Parser(input);
		System.out.println(MACHINE_EPSILON);
	}
	
	public boolean isValid()
	{
		return parser != null && parser.good();
	}
	
	public SyntaxError getSyntaxError()
	{
		return parser.getError();
	}
	
	public double at(double x)
	{
		return parser.eval(x);
	}
	
	public double at(double x, int derivative) throws IllegalArgumentException
	{
		if(derivative < 0)
			throw new IllegalArgumentException("derivative has to be a non-negative integer");
		
		if(derivative == 0)
			return parser.eval(x);
		
		double h = Math.cbrt(MACHINE_EPSILON) * x;
		
		return (this.at(x + h, derivative - 1) - this.at(x - h, derivative - 1)) / (2.0*h);
	}
}
