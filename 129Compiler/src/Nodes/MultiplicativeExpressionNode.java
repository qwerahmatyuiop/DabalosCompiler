package Nodes;

import LexicalAnalyzer.Token;

public class MultiplicativeExpressionNode extends SequenceExpressionNode {
	public MultiplicativeExpressionNode() {
	    super();
	  }

	  public MultiplicativeExpressionNode(Node a,
	                                int operator) {
	    super(a, operator);
	  }

	  public int getType() {
	    return Node.MULTIPLICATIVE_EXPRESSION_NODE;
	  }

	  public Object getValue() {
		    Double product = 1.0;
		    String dataType = "";
		    int i = 0;
		    for (Term t : terms) {
		    dataType = t.expression.getDataType();		  
		    switch(t.operator){
		    case Token.MULT: product *= (dataType == "Integer") ? (int) t.expression.getValue() : (float) t.expression.getValue(); break;
		    case Token.DIV: product /=  (dataType == "Integer") ? (int) t.expression.getValue() : (float) t.expression.getValue(); break;
	     }
	    }
		  return  (dataType == "Integer")? product.intValue(): product.floatValue();
	  }
}
