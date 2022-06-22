package de.aop.parser.expressions;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Cosine extends Atom
{
	Cosine(ParseString input) throws SyntaxError
	{
		input.next(); input.next(); input.next();	// Flush cos
		
		input.next();
		this.left = Expression.getNextToken(input);
		if(input.getCurrentToken() != ')')
			throw new SyntaxError(input.getPos(), "Expected ')', got '" + input.getCurrentToken() + "' instead");
		
		input.next();
	}
	
	@Override
	public double eval(double x)
	{
		return Math.cos(this.left.eval(x));
	}

}
