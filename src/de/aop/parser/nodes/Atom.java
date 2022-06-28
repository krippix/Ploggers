package de.aop.parser.nodes;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.ParseString;

public abstract class Atom extends INode
{
	static INode getNextToken(ParseString input) throws SyntaxError
	{
		INode nextToken = null;
		
		switch(input.getCurrentToken())
		{
		case 'x':	// id H
			nextToken = new Identifier(input);
			break;
			
		case '-':	// Unary -
			nextToken = new Negation(input);
			break;
			
		case '(':	// Parentheses
			input.next();
			nextToken = Expression.getNextToken(input);
			if(input.getCurrentToken() != ')')
				throw new SyntaxError(input.getPos(), "Expected ')', got '" + input.getCurrentToken() + "' instead");
			
			input.next();
			break;
			
		case 's':
			nextToken = new Sine(input);
			break;
			
		case 'c':
			nextToken = new Cosine(input);
			break;
			
		case 'e':
			nextToken = new Exponential(input);
			break;
			
		default:	// Number
			nextToken = new Rational(input);
			break;
		}
		
		return nextToken;
	}
}
