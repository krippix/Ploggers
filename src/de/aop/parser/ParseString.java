package de.aop.parser;

public class ParseString
{
	private String input;
	private int currentPos = 0;
	
	ParseString(String input)
	{
		this.input = input;
	}
	
	public String getString()
	{
		return input;
	}
	
	public char getCurrentToken()
	{
		if(currentPos >= input.length())
			return '\0';
		
		return input.charAt(currentPos);
	}
	
	public String getRemaining()
	{
		return input.substring(currentPos);
	}
	
	public void next()
	{
		currentPos++;
	}
	
	public int getPos()
	{
		return currentPos;
	}
}
