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
		  int i = 0;
		  if(terms.getFirst().expression.getValue() instanceof Integer){
			  int result = 0;
			  i = 0;
			  for(Term t: terms){
				  if(i == 0){
					  result = (Integer) t.expression.getValue();
					  i++;
					  continue;
				  }
				  switch(t.operator){
				  case Token.PLUS: result += (Integer) t.expression.getValue(); break;
				  case Token.MINUS: result -= (Integer) t.expression.getValue(); break;
				  }
				  i++;
			  }
			  return result;
		  }
		  else{
			  float result = 0;
			  i = 0;
			  for(Term t: terms){
				  if(i == 0){
					  result = (Float) t.expression.getValue();
					  i++;
					  continue;
				  }
				  switch(t.operator){
				  case Token.PLUS: result += (Float) t.expression.getValue(); break;
				  case Token.MINUS: result -= (Float) t.expression.getValue(); break;
				  }
				  i++;
			  }
			  return result;
		  }
	  }
}
