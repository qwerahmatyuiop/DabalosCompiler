package Nodes;

import java.util.LinkedList;

public abstract class SequenceExpressionNode implements Node {
	protected LinkedList<Term> terms;

	  public SequenceExpressionNode() {
	    this.terms = new LinkedList<Term>();
	  }

	  public SequenceExpressionNode(Node a, int operator) {
	    this.terms = new LinkedList<Term>();
	    this.terms.add(new Term(operator, a));
	  }

	  public void add(Node a, int operator) {
	    this.terms.add(new Term(operator, a));
	  }
	  
}
