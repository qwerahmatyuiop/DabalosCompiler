package Nodes;

import LexicalAnalyzer.Token;

public class Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MultiplicativeExpressionNode innerSum =  new MultiplicativeExpressionNode();
				innerSum.add(new IntegerExpressionNode(10), Token.MULT);
				innerSum.add(new IntegerExpressionNode(8), Token.MULT);
				
		AdditiveExpressionNode Sum =  new AdditiveExpressionNode();
				Sum.add(new IntegerExpressionNode(3), Token.PLUS);
				Sum.add(new IntegerExpressionNode(3), Token.PLUS);
				
		EqualityExpressionNode esn = new EqualityExpressionNode(Token.EQ_OP, new IntegerExpressionNode(3), new IntegerExpressionNode(3));
		System.out.println(esn.getValue());
		
	}

}
