package Nodes;

import java.util.LinkedList;

import LexicalAnalyzer.Token;

public class Function {
	private LinkedList<Token> tokens = new LinkedList<Token>();
	private String name;
	public Function(String name, LinkedList<Token> tokens) {
		super();
		this.name = name;
		for(Token s: tokens){
			this.tokens.push(s);
		}
	}
	public String getName(){
		return this.name;
	}
	public LinkedList<Token> getTokens(){
		return this.tokens;
	}
	
}
