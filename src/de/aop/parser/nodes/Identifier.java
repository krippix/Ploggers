package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Identifier extends Atom
{
	private int exponent = 1;
	
	public Identifier(ParseString input) throws SyntaxError
	{
		input.next();
		if(input.getCurrentToken() != '^')
			return;
		
		String number = "";
		input.next();
		
		while(Character.isDigit(input.getCurrentToken()))
		{
			number += input.getCurrentToken();
			input.next();
		}
		
		try 
		{
			this.exponent = Integer.parseInt(number);
		}
		catch(NumberFormatException e)
		{
			throw new SyntaxError(input.getPos(), "^ needs to be followed by a positive integer.");
		}
	}
	
	@Override
	public double eval(double x)
	{
		return Math.pow(x, exponent);
	}

}
