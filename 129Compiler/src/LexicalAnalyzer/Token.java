package LexicalAnalyzer;
public class Token {
	//this is the class for recognized tokens in the language and their values for easier recognition
	public static final int EPSILON = -1;
	public static final int COMMENT = 0;
	public static final int IF = 1;
	public static final int ELSE = 2;
	public static final int WHILE = 3;
	public static final int CONTINUE = 4;
	public static final int VOID = 5;
	public static final int CHAR = 6;
	public static final int STRING = 7; 
	public static final int INT = 8;
	public static final int BOOLEAN = 9;
	public static final int FLOAT = 10;

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
	public static final int PLUS= 380;
	public static final int MINUS= 381;
	public static final int MULT= 390;
	public static final int DIV= 391;
	public static final int MOD = 392;  

	public static final int TRUE = 40;
	public static final int FALSE = 41;
	public static final int IDENTIFIER = 42;
	public static final int INTEGER_LIT = 43;
	public static final int FLOAT_LIT = 44;
	public static final int STRING_LIT = 45;



	public static final int PRINT = 46;
	public static final int PRINTLN = 461;
	public static final int SCAN = 47;
	public static final int FUNC = 48;

	public static final int GLOBAL = 52;
	public static final int STDIN = 49;
	public static final int REVERSE = 54;
	public static final int CREATEFILE =55;
	public static final int READFILE= 56;
	public static final int INC_OP = 50;
	public static final int DEC_OP = 51;
	public static final int CALL = 53;
	public static final int ARRAY_ADD= 57;
	public static final int ARRAY_GET= 58;
	public static final int ARRAY_REMOVE= 59;
	public static final int ARRAY_SORT= 60;
	public static final int ARRAY_DESCSORT = 61;
	public static final int RANDOM = 62;
	public static final int GET_SD = 63;
	public static final int READINTFILE =65;
	public static final int WRITEINTFILE =66;
	public static final int WRITEFILE =67;
	public final int token;
	public final String sequence;

	public Token(int token, String sequence) {
		super();
		this.token = token;
		this.sequence = sequence;
	}
}

