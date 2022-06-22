package de.aop.parser.expressions;

import de.aop.parser.ParseString;

public class Identifier extends Atom
{
	private int exponent = 1;
	
	public Identifier(ParseString input)
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
		
		this.exponent = Integer.parseInt(number);
	}
	
	@Override
	public double eval(double x)
	{
		return Math.pow(x, exponent);
	}

}
