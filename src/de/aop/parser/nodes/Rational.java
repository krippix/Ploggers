package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Rational extends Atom
{
	double value;
	
	Rational(ParseString input) throws SyntaxError
	{
		String number = "";
		
		while(Character.isDigit(input.getCurrentToken()) || input.getCurrentToken() == '.')
		{
			number += input.getCurrentToken();
			input.next();
		}
		
		try 
		{
			this.value = Double.parseDouble(number);			
		}
		catch(NumberFormatException e)
		{
			throw new SyntaxError(input.getPos(), "Ill-formed rational number.");
		}
	}
	
	public Rational(double defaultVal)
	{
		this.value = defaultVal;
	}

	@Override
	public double eval(double x)
	{
		return value;
	}

}
