package de.aop.parser.expressions;

import de.aop.parser.ParseString;

public class Sine extends Atom
{
	Sine(ParseString input) throws Exception
	{
		input.next(); input.next(); input.next();	// Flush sin
		
		input.next();
		this.left = Expression.getNextToken(input);
		if(input.getCurrentToken() != ')')
			throw new Exception("lol");
		
		input.next();
	}
	
	@Override
	public double eval(double x)
	{
		return Math.sin(this.left.eval(x));
	}

}
