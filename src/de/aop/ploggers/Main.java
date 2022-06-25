package de.aop.ploggers;

import de.aop.gui.*;
import de.aop.exceptions.SyntaxError;
import de.aop.parser.Parser;

public class Main 
{
	public static void main(String args[]) throws Exception
	{
		Parser parser = new Parser("x^4-5");
		
		if(!parser.good())
		{
			SyntaxError error = parser.getError();
			
			System.err.println(error.getMessage() + "\n");
			System.err.println(parser.getInput());
			
			for(int i = 0; i < error.where(); i++)
				System.err.print(" ");
			System.err.print("^ here");
			
			return;
		}
		
		System.out.println(parser.eval(2));

		System.out.println("Ploggies is starting!");
		GUI gui = new GUI();
	}
}
