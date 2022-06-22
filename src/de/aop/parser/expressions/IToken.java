package de.aop.parser.expressions;

public abstract class IToken 
{
	protected IToken left, right;
	
	public abstract double eval(double x);
	
	public IToken optimize()
	{
		if(this.left != null)
			this.left = this.left.optimize();
		
		if(this.right != null)
			this.right = this.right.optimize();
		
		if(this.left instanceof Rational && this.right instanceof Rational)
			return new Rational(this.eval(0));
		
		return this;
	}
}
