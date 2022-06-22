package de.aop.parser.expressions;

import de.aop.parser.ParseString;

public abstract class Term extends IToken
{
	static IToken getNextToken(ParseString input) throws Exception
	{
		IToken nextToken = Atom.getNextToken(input);
		
		switch(input.getCurrentToken())
		{
		case '*':
			nextToken = new Multiplication(input, nextToken);
			break;
			
		case '/':
			nextToken = new Division(input, nextToken);
			break;
		}
		
		return nextToken;
	}
}
