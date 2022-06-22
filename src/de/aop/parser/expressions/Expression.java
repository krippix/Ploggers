package de.aop.parser.expressions;

import de.aop.parser.ParseString;

public abstract class Expression extends IToken
{
	public static IToken getNextToken(ParseString input) throws Exception
	{
		IToken nextToken = Term.getNextToken(input);
		
		switch(input.getCurrentToken())
		{
		case '+':
			nextToken = new Addition(input, nextToken);
			break;
			
		case '-':
			nextToken = new Subtraction(input, nextToken);
			break;
		}
		
		return nextToken;
	}
}
