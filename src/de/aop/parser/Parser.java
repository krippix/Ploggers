package de.aop.parser;

public class Parser 
{
	private String originalInput;
	
	Parser(String input)
	{
		this.originalInput = input;
	}
	
	public String getInput()
	{
		return originalInput;
	}
	
	public double eval()
	{
		// TODO: Return eval() of tree node
		return 0.0;
	}
}
