package Nodes;

import LexicalAnalyzer.Token;

public class Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MultiplicativeExpressionNode innerSum =  new MultiplicativeExpressionNode();
				innerSum.add(new IntegerExpressionNode(10), Token.MULT);
				innerSum.add(new IntegerExpressionNode(8), Token.MULT);
		
		EqualityExpressionNode eq = new EqualityExpressionNode(Token.EQ_OP, innerSum, innerSum);
		System.out.println(eq.getValue());
		System.out.println(8/5%1);
	}

}
