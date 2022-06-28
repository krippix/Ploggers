package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Exponential extends Atom
{
	Exponential(ParseString input) throws SyntaxError
	{
		input.next(); input.next(); input.next();	// Flush exp
		
		input.next();
		this.left = Expression.getNextToken(input);
		if(input.getCurrentToken() != ')')
			throw new SyntaxError(input.getPos(), "Expected ')', got '" + input.getCurrentToken() + "' instead");
		
		input.next();
	}
	
	@Override
	public double eval(double x)
	{
		return Math.exp(this.left.eval(x));
	}

}
