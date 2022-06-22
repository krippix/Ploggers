package de.aop.parser;

import de.aop.parser.expressions.Expression;
import de.aop.parser.expressions.IToken;

public class Parser 
{
	private String originalInput;
	private int currentToken = 0;
	private IToken anchor;

	public Parser(String input) throws Exception
	{
		this.originalInput = input.replaceAll("\\s+","");
		
		ParseString ps = new ParseString(this.originalInput);
		anchor = Expression.getNextToken(ps);
		anchor = anchor.optimize();
	}
	
	public String getInput()
	{
		return originalInput;
	}
	
	public double eval(double x)
	{
		// TODO: Return eval() of tree node
		return anchor.eval(x);
	}
}
