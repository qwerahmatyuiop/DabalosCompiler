import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import SyntaxAnalyzer.Parser;
import Exceptions.ParserException;
import LexicalAnalyzer.Token;
import LexicalAnalyzer.Tokenizer;


public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args){
		  File file = new File("input.rahmat");
		  Tokenizer tokenizer = new Tokenizer();
		  	
		  	tokenizer.add("\\/\\/",0);
		  	tokenizer.add("if", 1);
		  	tokenizer.add("else", 2);
		  	tokenizer.add("while", 3);
		  	tokenizer.add("continue", 5);
		  	tokenizer.add("break", 6);
		  	tokenizer.add("return", 7);
		  	tokenizer.add("void",8);
		  	tokenizer.add("char", 9);
		  	tokenizer.add("String", 10);
		  	tokenizer.add("int", 11);
		  	tokenizer.add("boolean", 12);
		  	tokenizer.add("float", 13);
		  	
		  	tokenizer.add("\\{", 14);
		  	tokenizer.add("\\}", 15);
		  	tokenizer.add("\\(", 16);
		  	tokenizer.add("\\)", 17);
		  	tokenizer.add("\\[", 18);
		  	tokenizer.add("\\]", 19);
		  	
		  	
		  	
		  	tokenizer.add(";", 20);
		  	tokenizer.add(":", 21);
		  	tokenizer.add(",", 22);
		  	
		  	
		  	tokenizer.add("\\!=", 23);
		  	tokenizer.add("\\<=", 24);
		  	tokenizer.add("\\>=", 25);
			tokenizer.add("\\<", 26);
		  	tokenizer.add("\\>", 27);
		  	tokenizer.add("==", 28);
		  	
		  	
		    tokenizer.add("\\!", 29);
		  	tokenizer.add("\\&\\&", 30);
		  	tokenizer.add("\\|\\|", 31);
		  	
		  	tokenizer.add("(-)?[0-9]+\\.[0-9]+", 44);//float

		  	tokenizer.add("(-)?[0-9]+", 43); 
		  	tokenizer.add("--", 50);
		  	tokenizer.add("\\+\\+", 51);
		  	tokenizer.add("=", 32);
		  
		    tokenizer.add("\\+", 380); //add and minus
		    tokenizer.add("-", 381); //add and minus
		    tokenizer.add("\\*", 390); 
		    tokenizer.add("/", 391); 
		    tokenizer.add("%", 392); 
		    
		  	
		  	 tokenizer.add("\".[^\"]*\"",45);
		   
		  	tokenizer.add("print", 46);
		  	tokenizer.add("scan", 47);
		  	tokenizer.add("func", 48);
			tokenizer.add("GLOBAL", 52);
			tokenizer.add("call", 53);
		  	
			tokenizer.add("true", 40);
		  	tokenizer.add("false", 41);
		  	tokenizer.add("[a-zA-Z][a-zA-Z0-9_]*", 42); //identifier
		  	//int
		
		  	
		  
		  	String code = "";
			String line = "";
			try{
				BufferedReader scanner = new BufferedReader(new FileReader(file));
				while((line = scanner.readLine()) != null){
					code += line;
				}
				tokenizer.tokenize(code);
			}catch(IOException io){}
		  
		  try
		    {
		      /*for (Token tok : tokenizer.getTokens())
		      {
		        //System.out.println("" + tok.token + " " + tok.sequence);
		      }*/
			  Parser parse = new Parser();
			  parse.parse(tokenizer.getTokens());
		    }
		    catch (ParserException e)
		    {
		      System.out.println(e.getMessage());
		    }
	}
}
