package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Sine extends Atom
{
	Sine(ParseString input) throws SyntaxError
	{
		input.next(); input.next(); input.next();	// Flush sin
		
		input.next();
		this.left = Expression.getNextToken(input);
		if(input.getCurrentToken() != ')')
			throw new SyntaxError(input.getPos(), "Expected ')', got '" + input.getCurrentToken() + "' instead");
		
		input.next();
	}
	
	@Override
	public double eval(double x)
	{
		return Math.sin(this.left.eval(x));
	}

}
