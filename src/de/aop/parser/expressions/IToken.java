package de.aop.parser.expressions;

public abstract class IToken 
{
	protected IToken left, right;
	
	public abstract double eval(double x);
}
