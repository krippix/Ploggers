package de.aop.parser;

import de.aop.exceptions.SyntaxError;
import de.aop.parser.nodes.Expression;
import de.aop.parser.nodes.INode;

/**
 * @brief Constructs an expression tree from a given input
 */
public class Parser 
{
	private String originalInput;	// Store original user input for later reference
	private INode anchor;			// Root node of the expression tree
	
	private SyntaxError error = null;	// Any errors that were produced while parsing are stored for later reference

	public Parser(String input)
	{
		// Remove whitespace
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
		
		// Optimize expression tree by essentially simplifying the expression
		// E.g. (3 - 1)*x + 3 + 4 will be reduced to 2*x + 7
		anchor = anchor.optimize();
	}
	
	public String getInput()
	{
		return originalInput;
	}
	
	/**
	 * Evaluates the expression at a position
	 * @param x Position to evaluate the expression at
	 * @return The value of the expression at x
	 */
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
	
	/**
	 * Whether the parsing was successful or not
	 * @return True if there were no SyntaxErrors during parsing
	 */
	public boolean good()
	{
		return error == null;
	}
}
