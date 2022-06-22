package de.aop.ploggers;

import de.aop.parser.Parser;

public class Main 
{
	public static void main(String args[]) throws Exception
	{
		Parser parser = new Parser("4*x + 2 + 4 + 6");
		System.out.println(parser.eval(2));
	}
}
