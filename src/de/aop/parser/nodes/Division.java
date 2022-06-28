package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Division extends Expression
{
	public Division(ParseString input, INode left) throws SyntaxError
	{
		this.left = left;
		input.next();
		
		this.right = Term.getNextToken(input);
	}
	
	@Override
	public double eval(double x)
	{
		return this.left.eval(x) / this.right.eval(x);
	}

}
