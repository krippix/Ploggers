package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public abstract class Term extends INode
{
	static INode getNextToken(ParseString input) throws SyntaxError
	{
		INode nextToken = Atom.getNextToken(input);
		
		switch(input.getCurrentToken())
		{
		case '*':
			nextToken = new Multiplication(input, nextToken);
			break;
			
		case '/':
			nextToken = new Division(input, nextToken);
			break;
			
		case '\0':
			break;
		}
		
		return nextToken;
	}
}
