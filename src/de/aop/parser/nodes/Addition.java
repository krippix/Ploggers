package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Addition extends Expression
{
	public Addition(ParseString input, INode left) throws SyntaxError
	{
		this.position = input.getPos();
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
