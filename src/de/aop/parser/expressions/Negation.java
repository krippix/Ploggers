package de.aop.parser.expressions;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public class Negation extends Atom
{
	Negation(ParseString input) throws SyntaxError
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
