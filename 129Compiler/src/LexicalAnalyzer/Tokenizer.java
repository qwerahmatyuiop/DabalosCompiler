package LexicalAnalyzer;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exceptions.ParserException;

public class Tokenizer {
	private LinkedList<TokenInfo> tokenInfos;
	private LinkedList<Token> tokens;
	
	public Tokenizer() {
	  tokenInfos = new LinkedList<TokenInfo>();
	  tokens = new LinkedList<Token>();
	}
	public void add(String regex, int token) {
		  tokenInfos.add(
		  new TokenInfo(
		  Pattern.compile("^("+regex+")"), token));
		}
	public void tokenize(String str)
	  {
	    String s = str.trim();
	    int totalLength = s.length();
	    tokens.clear();
	    while (!s.equals(""))
	    {
	      int remaining = s.length();
	      boolean match = false;
	      for (TokenInfo info : tokenInfos)
	      {
	        Matcher m = info.regex.matcher(s);
	        if (m.find())
	        {
	          match = true;
	          String tok = m.group().trim();
	          System.out.println("Success matching: " + tok);
	          s = m.replaceFirst("").trim();
	          tokens.add(new Token(info.token, tok));
	          break;
	        }
	      }
	      if (!match)
	        throw new ParserException("Unexpected character in input: " + s);
	    }
	  }
	  public LinkedList<Token> getTokens()
	  {
	    return tokens;
	  }
	public void addExpressions() {
		this.add("\\/\\/",0);
	  	this.add("if", 1);
	  	this.add("else", 2);
	  	this.add("while", 3);
	  	this.add("continue", 5);
	  	this.add("break", 6);
	  	this.add("return", 7);
	  	this.add("void",8);
	  	this.add("char", 9);
	  	this.add("String", 10);
	  	this.add("int", 11);
	  	this.add("boolean", 12);
	  	this.add("float", 13);
	  	this.add("\\{", 14);
	  	this.add("\\}", 15);
	  	this.add("\\(", 16);
	  	this.add("\\)", 17);
	  	this.add("\\[", 18);
	  	this.add("\\]", 19);
	  	
	  	
	  	
	  	this.add(";", 20);
	  	this.add(":", 21);
	  	this.add(",", 22);
	  	
	  	
	  	this.add("\\!=", 23);
	  	this.add("\\<=", 24);
	  	this.add("\\>=", 25);
		this.add("\\<", 26);
	  	this.add("\\>", 27);
	  	this.add("==", 28);
	  	
	  	
	    this.add("\\!", 29);
	  	this.add("\\&\\&", 30);
	  	this.add("\\|\\|", 31);
	  	
	  	this.add("(-)?[0-9]+\\.[0-9]+", 44);//float

	  	this.add("(-)?[0-9]+", 43); 
	  	this.add("--", 50);
	  	this.add("\\+\\+", 51);
	  	this.add("=", 32);
	  
	    this.add("\\+", 380); //add and minus
	    this.add("minus", 381); //add and minus
	    this.add("\\*", 390); 
	    this.add("/", 391); 
	    this.add("%", 392); 
	    
	    this.add("readFile", 56);
	    this.add("readIntFile", 65);
	    this.add("writeIntFile", 66);
	    this.add("writeFile", 67);
	    this.add("writeFile", 64);
	  	this.add("createFile", 55);
	  	 this.add("\".[^\"]*\"",45);
	 	this.add("println", 461);
	  	this.add("print", 46);
	  	this.add("scan", 47);
	  	this.add("func", 48);
	  	this.add("rand", 62);
	  	this.add("getSD", 63);
		this.add("GLOBAL", 52);
		this.add("call", 53);
		this.add("reverse", 54);
	  	this.add("Array\\.Add", 57);
	  	this.add("Array\\.Get", 58);
	  	this.add("Array\\.Remove", 59);
	  	this.add("Array\\.Sort", 60);
	  	this.add("Array\\.DescSort", 61);
		this.add("true", 40);
	  	this.add("false", 41);
	  	this.add("[a-zA-Z][a-zA-Z0-9_]*", 42); //identifier
	}
}
