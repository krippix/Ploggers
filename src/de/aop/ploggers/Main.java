package de.aop.ploggers;

import de.aop.parser.Parser;

public class Main 
{
	public static void main(String args[]) throws Exception
	{
		Parser parser = new Parser("sin(cos(exp(cos(sin(cos(exp(exp(sin(3)))))))))");
		System.out.println(parser.eval(2));
	}
}
