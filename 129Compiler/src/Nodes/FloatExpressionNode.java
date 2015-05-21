package Nodes;

public class FloatExpressionNode implements Node {

	private float value;
	
	
	public FloatExpressionNode(float value) {
		super();
		this.value = value;
	}
	public FloatExpressionNode(String value) {
		super();
		this.value = Float.valueOf(value);
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return Node.FLOAT_NODE;
	}

	@Override
	public Float getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	@Override
	public String getDataType() {
		// TODO Auto-generated method stub
		return "String" ;
	}
	

}
