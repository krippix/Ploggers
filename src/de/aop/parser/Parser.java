package de.aop.parser;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.expressions.Expression;
import de.aop.parser.expressions.IToken;

public class Parser 
{
	private String originalInput;
	private IToken anchor;
	
	private SyntaxError error = null;

	public Parser(String input)
	{
		this.originalInput = input.replaceAll("\\s+","");
		
		ParseString ps = new ParseString(this.originalInput);
		
		try 
		{
			anchor = Expression.getNextToken(ps);
		}
		catch(SyntaxError e)
		{
			error = e;
			return;
		}
		
		anchor = anchor.optimize();
	}
	
	public String getInput()
	{
		return originalInput;
	}
	
	public double eval(double x)
	{
		if(error != null)
			return Double.NaN;
		
		return anchor.eval(x);
	}
	
	public SyntaxError getError()
	{
		return error;
	}
	
	public boolean good()
	{
		return error == null;
	}
}
