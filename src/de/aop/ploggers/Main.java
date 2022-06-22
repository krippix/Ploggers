package de.aop.ploggers;

import de.aop.parser.Parser;

public class Main 
{
	public static void main(String args[]) throws Exception
	{
		Parser parser = new Parser("2.384 + -9.2839");
		System.out.println(parser.eval(0));
	}
}
