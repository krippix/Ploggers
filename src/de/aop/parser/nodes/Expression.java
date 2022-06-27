package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public abstract class Expression extends INode
{
	public static INode getNextToken(ParseString input) throws SyntaxError
	{
		INode nextToken = Term.getNextToken(input);
		
		switch(input.getCurrentToken())
		{
		case '+':
			nextToken = new Addition(input, nextToken, false);
			break;
			
		case '-':
			nextToken = new Addition(input, nextToken, true);
			break;
			
		case '\0':
			break;
		}
		
		return nextToken;
	}
}
