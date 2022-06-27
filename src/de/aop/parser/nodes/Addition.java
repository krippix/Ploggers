package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Addition extends Expression
{
	public Addition(ParseString input, INode left, boolean dontSkip) throws SyntaxError
	{
		this.left = left;
		
		if(!dontSkip)
			input.next();
		
		this.right = Expression.getNextToken(input);
	}
	
	@Override
	public double eval(double x)
	{
		return this.left.eval(x) + this.right.eval(x);
	}
}
