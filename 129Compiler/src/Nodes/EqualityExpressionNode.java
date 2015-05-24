package Nodes;

import LexicalAnalyzer.Token;

public class EqualityExpressionNode implements Node {

	private int operator;
	private Node left;
	private Node right;
	
	public int getType() {
		// TODO Auto-generated method stub
		return Node.EQUALITY_EXPRESSION_NODE;
	}
	
	public EqualityExpressionNode(int operator, Node left, Node right) {
		super();
		this.operator = operator;
		this.left = left;
		this.right = right;
	}


	@Override
	public Boolean getValue() {
		// TODO Auto-generated method stub
		if(left.getValue() instanceof Integer){
			switch(operator){
				case Token.EQ_OP: return (((Integer) left.getValue()) == ((Integer) right.getValue())) ? true: false;
				case Token.NEG_OP: return (((Integer) left.getValue()) != ((Integer) right.getValue())) ? true: false;
				case Token.GR: return (((Integer) left.getValue()) > ((Integer) right.getValue())) ? true: false;
				case Token.GR_EQ: return (((Integer) left.getValue()) >= ((Integer) right.getValue())) ? true: false;
				case Token.LE: return (((Integer) left.getValue()) < ((Integer) right.getValue())) ? true: false;
				case Token.LE_EQ: return (((Integer) left.getValue()) <= ((Integer) right.getValue())) ? true: false;
			}
		}else if(left.getValue() instanceof Float){
			switch(operator){
				case Token.EQ_OP: return (((Float) left.getValue()) == ((Float) right.getValue())) ? true: false;
				case Token.NEG_OP: return (((Float) left.getValue()) != ((Float) right.getValue())) ? true: false;
				case Token.GR: return (((Float) left.getValue()) > ((Float) right.getValue())) ? true: false;
				case Token.GR_EQ: return (((Float) left.getValue()) >= ((Float) right.getValue())) ? true: false;
				case Token.LE: return (((Float) left.getValue()) < ((Float) right.getValue())) ? true: false;
				case Token.LE_EQ: return (((Float) left.getValue()) <= ((Float) right.getValue())) ? true: false;
			}
		}else if(left.getValue() instanceof String){
			switch(operator){
			case Token.EQ_OP: return (left.getValue().equals(right.getValue()))? true: false;
			case Token.NEG_OP: return (!left.getValue().equals(right.getValue()))? true: false;
			}
		}
		
		return false;
	}
	
}
