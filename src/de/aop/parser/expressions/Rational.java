package de.aop.parser.expressions;

import java.util.Scanner;

import de.aop.parser.ParseString;

public class Rational extends Atom
{
	double value;
	
	Rational(ParseString input)
	{
		String number = "";
		
		while(Character.isDigit(input.getCurrentToken()) || input.getCurrentToken() == '.')
		{
			number += input.getCurrentToken();
			input.next();
		}
		
		this.value = Double.parseDouble(number);
	}

	@Override
	public double eval(double x)
	{
		return value;
	}

}
