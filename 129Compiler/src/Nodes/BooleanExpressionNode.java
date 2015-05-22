package Nodes;

public class BooleanExpressionNode implements Node {

	private Boolean value;
	
	
	public BooleanExpressionNode(Boolean value) {
		super();
		this.value = value;
	}
	public BooleanExpressionNode(String value) {
		super();
		this.value = Boolean.valueOf(value);
	}
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return Node.BOOLEAN_NODE;
	}

	@Override
	public Boolean getValue() {
		// TODO Auto-generated method stub
		return value;
	}

}
