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
		  tokenizer.addExpressions();
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
