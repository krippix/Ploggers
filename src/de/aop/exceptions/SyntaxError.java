package de.aop.exceptions;

public class SyntaxError extends Exception
{
	private int where;
	
	public SyntaxError(int position, String description)
	{
		super(String.format("Syntax error at position %d: %s", position, description));
		this.where = position;
	}
	
	public int where()
	{
		return where;
	}
}
