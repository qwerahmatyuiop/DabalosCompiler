package Nodes;

import java.util.LinkedList;

public abstract class SequenceExpressionNode {
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
	  public String getDataType(){
		  return this.terms.get(0).expression.getDataType();
	  }
}
