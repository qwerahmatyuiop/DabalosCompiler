package Nodes;

import LexicalAnalyzer.Token;

public class LogicalAndExpressionNode implements Node {

	private int operator;
	private Node left;
	private Node right;

	public int getType() {
		// TODO Auto-generated method stub
		return Node.LOGICAL_AND_EXPRESSION_NODE;
	}

	public LogicalAndExpressionNode(int operator, Node left, Node right) {
		super();
		this.operator = operator;
		this.left = left;
		this.right = right;
	}


	@Override
	public Boolean getValue() {
		// TODO Auto-generated method stub
		return ((Boolean)left.getValue() &&  (Boolean)right.getValue()) ? true: false;
	}

}
