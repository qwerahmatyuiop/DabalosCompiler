package Nodes;

public class Term {
	public int operator;
	  public Node expression;

	  public Term(int operator, Node expression) {
	    super();
	    this.operator = operator;
	    this.expression = expression;
	  }
}
