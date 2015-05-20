package LexicalAnalyzer;

import java.util.regex.Pattern;

public class TokenInfo{
	 public final Pattern regex;
	    /** the token id that the regular expression is linked to */
	    public final int token;

	    /**
	     * Construct TokenInfo with its values
	     */
	    public TokenInfo(Pattern regex, int token)
	    {
	      super();
	      this.regex = regex;
	      this.token = token;
	    }
}