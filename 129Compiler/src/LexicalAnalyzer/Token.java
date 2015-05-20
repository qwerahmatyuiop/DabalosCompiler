package LexicalAnalyzer;
	public class Token {
		public static final int EPSILON = -1;
		public static final int COMMENT = 0;
		public static final int IF = 1;
		public static final int ELSE = 2;
		public static final int WHILE = 3;
		public static final int CONTINUE = 5;
		public static final int BREAK = 6;
		public static final int RETURN = 7;
		public static final int VOID = 8;
		public static final int CHAR = 9;
		public static final int STRING = 10; 
		public static final int INT = 11;
		public static final int BOOLEAN = 12;
		public static final int FLOAT = 13;
		
		public static final int OPEN_BRACKET = 14;
		public static final int CLOSE_BRACKET = 15;
		public static final int OPEN_PAREN = 16;
		public static final int CLOSE_PAREN = 17;
		public static final int OPEN_BRACE = 18;
		public static final int CLOSE_BRACE = 19;
		  
		public static final int SEMI_COLON = 20;
		public static final int COLON = 21;
		public static final int COMMA = 22;
		
		  
		  
		public static final int NEG_OP = 23;
		public static final int LE_EQ = 24;
		public static final int GR_EQ = 25;
		public static final int LE = 26;
		public static final int GR = 27;
		public static final int EQ_OP = 28 ;
		  
		  
		public static final int NEG = 29;		  
		public static final int AND_OP = 30;
		public static final int OR_OP = 31;
		 
		  
		  
		public static final int ASSIGN = 32;
		public static final int MOD_ASSIGN = 33;
		public static final int ADD_ASSIGN = 34;
		public static final int SUB_ASSIGN = 35;
		public static final int MUL_ASSIGN = 36;
		public static final int DIV_ASSIGN = 37;
		public static final int PLUSMINUS = 38;
		public static final int MULTDIV = 39;  
		  
		public static final int TRUE = 40;
		public static final int FALSE = 41;
		public static final int IDENTIFIER = 42;
		public static final int INTEGER_LIT = 43;
		public static final int FLOAT_LIT = 44;
		public static final int STRING_LIT = 45;
		 
		 
		
		public static final int PRINT = 46;
		public static final int SCAN = 47;
		public static final int FUNC = 48;
		
		public final int token;
		public final String sequence;

		  public Token(int token, String sequence) {
		    super();
		    this.token = token;
		    this.sequence = sequence;
		  }
		}
