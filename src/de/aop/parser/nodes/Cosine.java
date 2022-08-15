package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Cosine extends Atom
{
	Cosine(ParseString input) throws SyntaxError
	{
		String symbol = new String();
		symbol += input.getCurrentToken();	input.next();		
		symbol += input.getCurrentToken();	input.next();
		symbol += input.getCurrentToken();	input.next();
		
		if(!symbol.equals("cos"))
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
		return Math.cos(this.left.eval(x));
	}

}
