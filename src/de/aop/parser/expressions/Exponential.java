package de.aop.parser.expressions;

import de.aop.parser.ParseString;

public class Exponential extends Atom
{
	Exponential(ParseString input) throws Exception
	{
		input.next(); input.next(); input.next();	// Flush exp
		
		input.next();
		this.left = Expression.getNextToken(input);
		if(input.getCurrentToken() != ')')
			throw new Exception("lol");
		
		input.next();
	}
	
	@Override
	public double eval(double x)
	{
		return Math.exp(this.left.eval(x));
	}

}
