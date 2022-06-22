package de.aop.parser.expressions;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Subtraction extends Expression
{
	public Subtraction(ParseString input, IToken left) throws SyntaxError
	{
		this.left = left;
		input.next();
		
		this.right = Expression.getNextToken(input);
	}
	
	@Override
	public double eval(double x)
	{
		return this.left.eval(x) - this.right.eval(x);
	}

}
