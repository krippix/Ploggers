package de.aop.parser.expressions;

import de.aop.parser.ParseString;

public class Cosine extends Atom
{
	Cosine(ParseString input) throws Exception
	{
		input.next(); input.next(); input.next();	// Flush cos
		
		input.next();
		this.left = Expression.getNextToken(input);
		if(input.getCurrentToken() != ')')
			throw new Exception("lol");
		
		input.next();
	}
	
	@Override
	public double eval(double x)
	{
		return Math.cos(this.left.eval(x));
	}

}
