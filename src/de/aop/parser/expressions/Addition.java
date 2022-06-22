package de.aop.parser.expressions;

import de.aop.parser.ParseString;

public class Addition extends Expression
{
	public Addition(ParseString input, IToken left) throws Exception
	{
		this.left = left;
		input.next();
		
		this.right = Expression.getNextToken(input);
	}
	
	@Override
	public double eval(double x)
	{
		return this.left.eval(x) + this.right.eval(x);
	}
}
