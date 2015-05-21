package Nodes;

import LexicalAnalyzer.Token;

public class AdditiveExpressionNode extends SequenceExpressionNode {
	public AdditiveExpressionNode() {
	    super();
	  }

	  public AdditiveExpressionNode(Node a,
	                                int operator) {
	    super(a, operator);
	  }

	  public int getType() {
	    return Node.ADDITIVE_EXPRESSION_NODE;
	  }

	  public Object getValue() {
	    Double sum = 0.0;
	    String dataType = "";
	    for (Term t : terms) {
	     dataType = t.expression.getDataType();
	     switch(t.operator){
	     case Token.PLUS: sum += (dataType == "Integer") ? (int) t.expression.getValue() : (float) t.expression.getValue() ; break;
	     case Token.MINUS: sum -= (dataType == "Integer") ? (int) t.expression.getValue() : (float) t.expression.getValue(); break;
	     }
	    }
	    return (dataType == "Integer")? sum.intValue(): sum.floatValue();
	  }
}
