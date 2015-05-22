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
		  int i = 0;
		  if(terms.getFirst().expression.getValue() instanceof Integer){
			  int result = (Integer) terms.getFirst().expression.getValue();
			  i = 0;
			  for(Term t: terms){
				  if(i == 0){
					  result = (Integer) t.expression.getValue();
					  i++;
					  continue;
				  }
				  switch(t.operator){
				  case Token.MULT: result *= (Integer) t.expression.getValue(); break;
				  case Token.DIV: result /= (Integer) t.expression.getValue(); break;
				  case Token.MOD: result %= (Integer) t.expression.getValue(); break;
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
				  case Token.MULT: result *= (Float) t.expression.getValue(); break;
				  case Token.DIV: result /= (Float) t.expression.getValue(); break;
				  case Token.MOD: result %= (Float) t.expression.getValue(); break;
				  }
				  i++;
			  }
			  return result;
		  }
	  }

}
