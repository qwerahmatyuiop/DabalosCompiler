package Nodes;

import LexicalAnalyzer.Token;

public class MultiplicativeExpressionNode implements Node {
	private int operator;
	private double left;
	private double right;

	public MultiplicativeExpressionNode(int operator, double left, double right) {
		super();
		this.operator = operator;
		this.left = left;
		this.right = right;
	}

	public int getType() {
		return Node.MULTIPLICATIVE_EXPRESSION_NODE;
	}

	public Double getValue() {
		double value = 0.0;
		switch(operator){
		case Token.MULT : value = left * right; break;
		case Token.DIV : value = left / right; break;
		case Token.MOD : value = left % right; break;  
		}
		return value;
	}

}
