package Nodes;

import Exceptions.EvaluationException;

public class IdentifierExpressionNode implements Node{
	private String name;
	private Object value;
	private boolean valueSet;
	private String dataType;
	

	public IdentifierExpressionNode(String name) {
		super();
		this.name = name;
		valueSet = false;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return Node.IDENTIFIER_NODE;
	}
	
	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		 if (valueSet)
		      return value;
		    else
		      throw new EvaluationException("Variable '" 
		        + name + "' was not initialized.");
	}
	public String getName(){
		return this.name;
	}
	public void setValue(Object value){
		this.value = value;
		valueSet = true;
	}
	public String getdataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


}
