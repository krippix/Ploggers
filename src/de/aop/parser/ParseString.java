package de.aop.parser;

/**
 * QoL class for keeping track of which part of a string
 * is being parsed
 */
public class ParseString
{
	private String input;		// Original input
	private int currentPos = 0;	// Which token is currently being processed
	
	ParseString(String input)
	{
		this.input = input;
	}
	
	public String getString()
	{
		return input;
	}
	
	/**
	 * Gets the currently processed token
	 * @return The current token, or null byte if no more tokens are left
	 */
	public char getCurrentToken()
	{
		if(currentPos >= input.length())
			return '\0';
		
		return input.charAt(currentPos);
	}
	
	/**
	 * Gets the remaining string of unprocessed tokens
	 * @return The unprocessed substring of the input
	 */
	public String getRemaining()
	{
		return input.substring(currentPos);
	}
	
	/**
	 * Advance to the next token
	 */
	public void next()
	{
		currentPos++;
	}
	
	public int getPos()
	{
		return currentPos;
	}
}
