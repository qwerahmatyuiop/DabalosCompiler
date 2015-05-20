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
}
