package de.aop.parser.expressions;

import de.aop.parser.ParseString;

public class Division extends Expression
{
	public Division(ParseString input, IToken left) throws Exception
	{
		this.left = left;
		input.next();
		
		this.right = Term.getNextToken(input);
		// Maybe check for explicit division by 0
	}
	
	@Override
	public double eval(double x)
	{
		return this.left.eval(x) / this.right.eval(x);
	}

}
