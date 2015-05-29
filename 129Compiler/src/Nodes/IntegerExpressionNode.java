package Nodes;

public class IntegerExpressionNode implements Node {

	private int value;


	public IntegerExpressionNode(int value) {
		super();
		this.value = value;
	}
	public IntegerExpressionNode(String value) {
		super();
		this.value = Integer.valueOf(value);
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return Node.INTEGER_NODE;
	}

	@Override
	public Integer getValue() {
		// TODO Auto-generated method stub
		return value;
	}
}
