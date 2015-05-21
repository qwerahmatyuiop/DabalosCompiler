package Nodes;

import LexicalAnalyzer.Token;

public class EqualityExpressionNode implements Node {

	private int operator;
	private Node left;
	private Node right;
	private SequenceExpressionNode lefts;
	private SequenceExpressionNode rights;
	private String dataType;
	
	public int getType() {
		// TODO Auto-generated method stub
		return Node.EQUALITY_EXPRESSION_NODE;
	}
	
	public EqualityExpressionNode(int operator, Node left, Node right) {
		super();
		this.operator = operator;
		this.left = left;
		this.right = right;
		this.dataType = left.getDataType();
	}
	public EqualityExpressionNode(int operator, SequenceExpressionNode left, SequenceExpressionNode  right ) {
		super();
		this.operator = operator;
		this.lefts = left;
		this.rights = right;
		this.dataType = left.getDataType();
	}

	@Override
	public Boolean getValue() {
		// TODO Auto-generated method stub
		if(dataType == "Integer"){
			switch(operator){
			case Token.EQ_OP: return (((Integer) left.getValue()).intValue() == ((Integer) right.getValue()).intValue()) ? true: false;
			case Token.NEG_OP: return (((Integer) left.getValue()).intValue() != ((Integer) right.getValue()).intValue()) ? true: false;
			case Token.GR: return (((Integer) left.getValue()).intValue() > ((Integer) right.getValue()).intValue()) ? true: false;
			case Token.GR_EQ: return (((Integer) left.getValue()).intValue() >= ((Integer) right.getValue()).intValue()) ? true: false;
			case Token.LE: return (((Integer) left.getValue()).intValue() < ((Integer) right.getValue()).intValue()) ? true: false;
			case Token.LE_EQ: return (((Integer) left.getValue()).intValue() <= ((Integer) right.getValue()).intValue()) ? true: false;
			}
		}else if( dataType == "Float"){
			switch(operator){
			case Token.EQ_OP: return (((Float) left.getValue()).intValue() == ((Float) right.getValue()).intValue()) ? true: false;
			case Token.NEG_OP: return (((Float) left.getValue()).intValue() != ((Float) right.getValue()).intValue()) ? true: false;
			case Token.GR: return (((Float) left.getValue()).intValue() > ((Float) right.getValue()).intValue()) ? true: false;
			case Token.GR_EQ: return (((Float) left.getValue()).intValue() >= ((Float) right.getValue()).intValue()) ? true: false;
			case Token.LE: return (((Float) left.getValue()).intValue() < ((Float) right.getValue()).intValue()) ? true: false;
			case Token.LE_EQ: return (((Float) left.getValue()).intValue() <= ((Float) right.getValue()).intValue()) ? true: false;
			}
		}
		
		return false;
	}

	public String getDataType() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
