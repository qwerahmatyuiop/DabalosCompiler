package Nodes;

public class StringExpressionNode implements Node {

	private String value;
	
	public StringExpressionNode(String value) {
		super();
		this.value = value;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return Node.STRING_NODE;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return value;
	}
}
