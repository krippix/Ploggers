package de.aop.parser.expressions;

import de.aop.parser.ParseString;

public class Negation extends Atom
{
	Negation(ParseString input) throws Exception
	{
		input.next();
		this.left = Atom.getNextToken(input);
	}

	@Override
	public double eval(double x)
	{
		return -1 * left.eval(x);
	}

}
