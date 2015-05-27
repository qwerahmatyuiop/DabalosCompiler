package Nodes;

import java.util.ArrayList;
import java.util.Collections;

import Exceptions.EvaluationException;

public class ArrayExpressionNode extends IdentifierExpressionNode implements Node{
	private String name;
	private ArrayList<Integer> values = new ArrayList<Integer>();
	private boolean valueSet;
	private String dataType;
	

	public ArrayExpressionNode(String name) {
		super(name);
		this.name = name;
		valueSet = false;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return Node.IDENTIFIER_ARRAY_NODE;
	}
	
	@Override
	public ArrayList<Integer> getValue() {
		// TODO Auto-generated method stub
		 if (valueSet)
		      return values;
		    else
		      throw new EvaluationException("Variable '" 
		        + name + "' was not initialized.");
	}
	public String getName(){
		return this.name;
	}
	public void add(int s){
		values.add(s);
		valueSet = true;
	}
	public int delete(int s){
		return values.remove(s);
	}
	public int get(int s){
		return values.get(s);
	}
	public ArrayList<Integer> sort(){
		ArrayList<Integer> values2 = new ArrayList<Integer> (values);
		Collections.sort(values2);
		return values2;
	}
	public ArrayList<Integer> sortdesc(){
		ArrayList<Integer> values2 = new ArrayList<Integer> (values);
		Collections.sort(values2);
		Collections.reverse(values2);
		return values2;
	}
	public void setValue(ArrayList<Integer> value){
		values.clear();
		for (int s: value){
			values.add(s);
		}
		valueSet = true;
	}


}
