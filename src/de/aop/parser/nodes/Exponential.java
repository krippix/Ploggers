package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Exponential extends Atom
{
	Exponential(ParseString input) throws SyntaxError
	{
		String symbol = new String();
		symbol += input.getCurrentToken();	input.next();		
		symbol += input.getCurrentToken();	input.next();
		symbol += input.getCurrentToken();	input.next();
		
		if(!symbol.equals("exp"))
			throw new SyntaxError(input.getPos(), "Unknown symbol '" + symbol + "'");
		
		if(input.getCurrentToken() != '(')
			throw new SyntaxError(input.getPos(), "Expected '(', got '" + input.getCurrentToken() + "' instead");
		
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
