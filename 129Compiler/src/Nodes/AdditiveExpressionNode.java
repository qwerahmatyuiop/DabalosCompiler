package Nodes;

import LexicalAnalyzer.Token;

public class AdditiveExpressionNode implements Node {
	

	 private int operator;
	private double left;
	private double right;

	public AdditiveExpressionNode(int operator, double left, double right) {
			super();
			this.operator = operator;
			this.left = left;
			this.right = right;
		}
	 
	  public int getType() {
	    return Node.ADDITIVE_EXPRESSION_NODE;
	  }

	  public Double getValue() {
		  double sum = 0.0;
		 
		      switch(operator){
		      case Token.PLUS : sum +=  left + right; break;
		      case Token.MINUS : sum += left - right; break;
		    
		  }
		  return sum;
	  }
}
