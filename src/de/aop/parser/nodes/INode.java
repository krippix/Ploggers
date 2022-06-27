package de.aop.parser.nodes;

/**
 * Base class for all Nodes of the expression tree
 */
public abstract class INode 
{
	// Handles to the left and right branches of the expression tree
	protected INode left, right;
	
	/**
	 * Evaluates this node at position x
	 * @param x Position to evaluate node at
	 * @return The value of the node at position x
	 */
	public abstract double eval(double x);
	
	/**
	 * Optimizes this node and the subsequent subtree
	 * @return A new optimized Node (of a possibly different type, e.g. an AdditionNode could become a RationalNode)
	 */
	public INode optimize()
	{
		// If this node is an identifier, we cannot optimize anything
		if(this instanceof Identifier)
			return this;
		
		// Optimize subtrees
		if(this.left != null)
			this.left = this.left.optimize();
		
		if(this.right != null)
			this.right = this.right.optimize();
		
		// If the left and right subtrees are empty or Rational nodes, then we can turn this 
		// node into a rational as well
		if((this.left == null || this.left instanceof Rational) && 
		   (this.right == null || this.right instanceof Rational))
			return new Rational(this.eval(0));
		
		return this;
	}
}
