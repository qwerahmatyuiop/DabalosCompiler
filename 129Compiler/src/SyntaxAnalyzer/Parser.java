package SyntaxAnalyzer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import Exceptions.EvaluationException;
import Exceptions.ParserException;
import LexicalAnalyzer.Token;
import Nodes.AdditiveExpressionNode;
import Nodes.ArrayExpressionNode;
import Nodes.BooleanExpressionNode;
import Nodes.EqualityExpressionNode;
import Nodes.FloatExpressionNode;
import Nodes.Function;
import Nodes.IdentifierExpressionNode;
import Nodes.IntegerExpressionNode;
import Nodes.LogicalAndExpressionNode;
import Nodes.LogicalOrExpressionNode;
import Nodes.MultiplicativeExpressionNode;
import Nodes.Node;
import Nodes.StringExpressionNode;

public class Parser {
	LinkedList<Token> funcStatements = new LinkedList<Token>();
	LinkedList<Token> tokens;
	LinkedList<Function> functionCalls = new LinkedList<Function>();
	Token lookahead;
	LinkedList<IdentifierExpressionNode> symbols = new LinkedList<IdentifierExpressionNode>();
	Stack<String> stack = new Stack<String>();
	Token dum;

	public Parser() {
	}
	public void parse(List<Token> tokens)
	{

		this.tokens = new LinkedList<Token>(tokens); //list of tokens
		lookahead = this.tokens.getFirst();

		entry_point();

		if (lookahead.token != Token.EPSILON)
			throw new ParserException("(PARSER)Unexpected symbol "+lookahead.token+" found");
	}
	//entry_point -> entry_point external_declaration 
	// 			|  entry_point statement
	private void entry_point(){
		while(lookahead.token != Token.EPSILON){
			if(lookahead.token == Token.FUNC || lookahead.token == Token.GLOBAL){
				external_declaration();
			}else{
				statement();

			}
		}
	}
	//external_declaration -> "FUNC" function_definition
	// 						| "GLOBAL" declaration
	private void external_declaration(){
		if(lookahead.token == Token.FUNC){
			function_definition();
		}
		else 
			declaration();
	}
	//function_definition -> 
	private void function_definition(){
		String name;
		nextToken();
		type_specifier();
		if(lookahead.token == Token.IDENTIFIER){
			name = lookahead.sequence;
			nextToken();
			if(lookahead.token == Token.OPEN_PAREN){
				nextToken();
				if(lookahead.token == Token.CLOSE_PAREN){
					nextToken();
				}
				else{
					parameters();
					if(lookahead.token == Token.CLOSE_PAREN){
						nextToken();
					}
					else{
						throw new ParserException("(function_definition)Unexpected symbol "+lookahead.token+" found");
					}
				}
				if(lookahead.token == Token.OPEN_BRACKET){
					Stack<String> s = new Stack<String>();
					s.push("{");
					funcStatements.add(lookahead);
					nextToken();
					while(!s.isEmpty()){
						funcStatements.add(lookahead);
						if(lookahead.sequence.equals("{")){
							s.push("{");
						}else if(lookahead.sequence.equals("}")){
							s.pop();
						}
						nextToken();
					}
					Function f = new Function(name, funcStatements);
					functionCalls.add(f);
					funcStatements.clear();
				}
				else{
					throw new ParserException("(PARSER)Unexpected symbol "+lookahead.sequence +" found");
				}
			}else{
				throw new ParserException("(PARSER)Unexpected symbol "+lookahead.sequence +" found");
			}
		}else 
			throw new ParserException("(PARSER)Unexpected symbol "+lookahead.sequence +" found");

	}
	private void parameters(){

		while(lookahead.token != Token.CLOSE_PAREN){
			type_specifier();
			declarator();
		}
	}
	private String type_specifier(){
		String sequence = lookahead.sequence;
		if(lookahead.token == Token.VOID){
			nextToken();
		}
		else if(lookahead.token == Token.CHAR){
			nextToken();
		}
		else if(lookahead.token == Token.STRING){
			nextToken();
		}
		else if(lookahead.token == Token.INT){
			nextToken();
		}
		else if(lookahead.token == Token.BOOLEAN){
			nextToken();
		}
		else if(lookahead.token == Token.FLOAT){
			nextToken();
		}
		return sequence;
	}

	private Node declaration(){
		nextToken();
		String dataType = "void";
		if(lookahead.token >= Token.CHAR && lookahead.token <= Token.FLOAT){
			dataType = type_specifier();
		}
		Node es = init_declarator_list(dataType);
		if(lookahead.token == Token.SEMI_COLON){
			nextToken();
			return es;
		}
		else
			throw new ParserException("(DECLARATION)Unexpected symbol "+lookahead.token+" found");
	}
	private Node init_declarator_list(String dataType){
		Node es = init_declarator(dataType);
		if(lookahead.token == Token.COMMA){
			nextToken();
			init_declarator(dataType);
		}
		return es;
	}
	private Node init_declarator(String dataType){
		IdentifierExpressionNode newVariable  =(IdentifierExpressionNode)declarator();
		if(lookahead.token == Token.ASSIGN){
			nextToken();
			Node init = assignment_expression();
			newVariable.setValue(init.getValue());
			newVariable.setDataType(dataType);
			symbols.add(newVariable);
			return  newVariable;
		}
		else{
			Node init = new IntegerExpressionNode(0);
			newVariable.setValue(init.getValue());
			newVariable.setDataType(dataType);
			symbols.add(newVariable);
			return newVariable;
		}

	}
	private Node declarator(){
		/*if(lookahead.token == Token.OPEN_PAREN){
			declarator();
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
			}
			else
				throw new ParserException("(DECLARATOR1)Unexpected symbol "+lookahead.token+" found");
		}
		else if(lookahead.token == Token.OPEN_BRACE){
			nextToken();
			if(lookahead.token != Token.CLOSE_BRACE)
				assignment_expression();

			if(lookahead.token == Token.CLOSE_BRACE){
				nextToken();
			}
			else{
				throw new ParserException("(DECLARATOR2)Unexpected symbol "+lookahead.token+" found");
			}
		}*/
		if(lookahead.token == Token.IDENTIFIER){
			String name = lookahead.sequence;
			nextToken();
			Node Name;
			if(lookahead.token == Token.OPEN_BRACE){
				nextToken();
				Name = new ArrayExpressionNode(name);
				nextToken();
			}else{
				Name = new IdentifierExpressionNode(name);
			}
			return Name;
		}
		return null;

	}
	private Node assignment_expression(){
		Node ae = null;
		if(lookahead.token == Token.REVERSE){
			ae = reverse_expression();
		}
		else if(lookahead.token == Token.SCAN){
			ae = scan_expression();
		}
		else if(lookahead.token == Token.READFILE){
			ae = readFile_expression();
		}
		else if(lookahead.token == Token.ARRAY_GET || lookahead.token == Token.ARRAY_REMOVE || lookahead.token == Token.ARRAY_DESCSORT || lookahead.token == Token.ARRAY_SORT){
			ae = arrayFunctions();
		}
		else if(lookahead.token == Token.RANDOM){
			ae = random_expression();
		}
		else if (lookahead.token == Token.GET_SD){
			ae = SD_expression();
		}
		else if(lookahead.token == Token.READINTFILE){
			ae = readIntFile_expression();
		}
		else{
			ae = logical_or_expression();
			if(ae.getType() == Node.IDENTIFIER_NODE || ae.getType() == Node.IDENTIFIER_ARRAY_NODE){
				if(lookahead.token == Token.ASSIGN){
					ae = assignment_operator(ae);
				}
			}

		}
		return ae;
	}
	private Node SD_expression() {
		nextToken();
		float value;
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			int x1 = (Integer)primary_expression().getValue();
			nextToken();
			float x2 = (Float)primary_expression().getValue();
			value =(float) x1 / x2;
			value = (float) Math.sqrt(value);
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				Node ret = new FloatExpressionNode(value);
				return ret;
			}
		}else{
			throw new ParserException(" ");
		}
		return null;
	}
	private Node random_expression() {
		nextToken();
		int value;
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			Random random = new Random();
			int n = Integer.parseInt(lookahead.sequence);
			value = (random.nextInt(n*2)-n);
			nextToken();
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				return new IntegerExpressionNode(value);
			}
		}else{
			throw new ParserException(" ");
		}
		return null;
	}
	private Node arrayFunctions() {
		if(lookahead.token == Token.ARRAY_GET){
			nextToken();
			if(lookahead.token == Token.OPEN_PAREN){
				nextToken();
				ArrayExpressionNode aen = (ArrayExpressionNode) findSymbol(lookahead.sequence);
				nextToken();
				nextToken();
				int value = aen.get((Integer)primary_expression().getValue());
				if(lookahead.token == Token.CLOSE_PAREN){
					nextToken();	
					Node es = new IntegerExpressionNode(value);
					return es;
				}else{
					//error
					throw new ParserException(" ");
				}
			}else{
				//error
				throw new ParserException(" ");
			}
		}
		else if(lookahead.token == Token.ARRAY_REMOVE){

			nextToken();
			if(lookahead.token == Token.OPEN_PAREN){
				nextToken();
				ArrayExpressionNode aen = (ArrayExpressionNode) findSymbol(lookahead.sequence);
				nextToken();
				nextToken();
				int value = aen.delete((Integer)primary_expression().getValue());

				if(lookahead.token == Token.CLOSE_PAREN){
					nextToken();	
					Node es = new IntegerExpressionNode(value);
					return es;
				}else{
					//error
					throw new ParserException(" ");
				}
			}else{
				//error
				throw new ParserException(" ");
			}

		}
		else if(lookahead.token == Token.ARRAY_SORT || lookahead.token == Token.ARRAY_DESCSORT){
			boolean sort = (lookahead.token  == Token.ARRAY_SORT)? true: false;
			nextToken();
			if(lookahead.token == Token.OPEN_PAREN){
				nextToken();
				ArrayExpressionNode aen = (ArrayExpressionNode) findSymbol(lookahead.sequence);
				nextToken();
				if(sort)
					aen.setValue(aen.sort());
				else
					aen.setValue(aen.sortdesc());

				if(lookahead.token == Token.CLOSE_PAREN){
					nextToken();
					return aen;
				}
				else{
					//error
					throw new ParserException(" ");
				}
			}
			else{
				//error
				throw new ParserException(" ");
			}
		}
		else{

			throw new ParserException(" ");
		}
	}
	private Node readFile_expression() {
		String temp = "";
		File file;
		nextToken();
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			Node ae = primary_expression();
			String s = (String) ae.getValue();
			s = s.replaceAll("\"", "");

			try {
				Scanner scan = new Scanner(new FileReader(s));

				while(scan.hasNextLine()){
					temp += scan.nextLine()+ "\n";
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		 
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
			}
			return new StringExpressionNode(temp);
		}else{
			throw new ParserException(" ");
		}
	}
	private Node readIntFile_expression() {
		String temp = "";
		File file;
		nextToken();
		//for(Token s: tokens) System.out.println(s.sequence);

		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			Node ae = primary_expression();
			String s = (String) ae.getValue();
			s = s.replaceAll("\"", "");
			nextToken();
			ArrayExpressionNode aen = (ArrayExpressionNode) findSymbol(lookahead.sequence);
			nextToken();
			try {
				Scanner scan = new Scanner(new FileReader(s));

				while(scan.hasNextInt()){
					aen.add(scan.nextInt());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		 
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
			}
			return aen;
		}else{
			throw new ParserException(" ");
		}
	}
	private Node assignment_operator(Node ae){
		IdentifierExpressionNode ass = (IdentifierExpressionNode)ae;
		if(lookahead.token == Token.ASSIGN){
			nextToken();
			ass.setValue(assignment_expression().getValue());
			symbols.remove(findSymbol(ass.getName()));
			symbols.add(ass);

		}
		return ass;
	}
	private Node logical_or_expression() {
		boolean test;
		Node s1  = logical_and_expression();
		if(s1.getType() != Node.BOOLEAN_NODE){
			return s1;
		}else{
			test = (Boolean)s1.getValue();
			while(lookahead.token == Token.OR_OP){
				nextToken();
				Node s2  = logical_and_expression();
				if(s2.getType() != Node.BOOLEAN_NODE){
					throw new ParserException("Unexpected symbol "+lookahead.sequence+" found"); 
				}else{
					s1 = new BooleanExpressionNode(test);
					LogicalOrExpressionNode loe = new LogicalOrExpressionNode(Token.AND_OP,s1 , s2);
					test = loe.getValue();
				}
			}
		}
		Node ret = new BooleanExpressionNode(test);
		return ret;

	}
	private Node logical_and_expression() {
		boolean test;
		Node s1 = equality_expression();

		if(s1.getType() != Node.BOOLEAN_NODE){
			return s1;
		}else{
			test = (Boolean)s1.getValue();
			while(lookahead.token == Token.AND_OP){
				nextToken();
				Node s2 = equality_expression();
				if(s2.getType() != Node.BOOLEAN_NODE){
					throw new ParserException("Unexpected symbol "+lookahead.sequence+" found"); 
				}else{
					s1 = new BooleanExpressionNode(test);
					LogicalAndExpressionNode lae = new LogicalAndExpressionNode(Token.AND_OP,s1 , s2);
					test = lae.getValue();
				}
			}
		}
		Node ret = new BooleanExpressionNode(test);
		return ret;

	}
	private Node equality_expression(){

		Node eq1 = additive_expression();
		boolean test = true;
		boolean changed = false;

		if(lookahead.token == Token.EQ_OP || lookahead.token == Token.NEG_OP || lookahead.token == Token.GR || lookahead.token == Token.LE || lookahead.token == Token.GR_EQ || lookahead.token == Token.LE_EQ){
			changed = true;
			int operator = lookahead.token;
			nextToken();
			Node eq2 = additive_expression();
			EqualityExpressionNode eq = new EqualityExpressionNode(operator,eq1, eq2);
			test = eq.getValue();
		}
		if(changed){
			Node b = new BooleanExpressionNode(test);
			return b;
		}
		else {
			return eq1;
		}
	}
	private Node additive_expression(){
		double temp = 0.0;
		double temp2 = 0.0;

		Node addE = multiplicative_expression();
		if( lookahead.token == Token.PLUS || lookahead.token == Token.MINUS){
			if(addE.getValue() instanceof Integer){
				int s = (Integer) addE.getValue();
				temp = (double) s;
			}else{
				float s = (Float) addE.getValue();
				temp = (double) s;
			}
			while( lookahead.token == Token.PLUS || lookahead.token == Token.MINUS){
				int	x = lookahead.token;
				nextToken();
				Node mult2 = postfix_expression();
				if(mult2.getValue() instanceof Integer){
					int s = (Integer) mult2.getValue();
					temp2 = (double) s;
				}else{
					float s = (Float) mult2.getValue();
					temp2 = (double) s;
				}
				AdditiveExpressionNode mulOper = new AdditiveExpressionNode(x,temp,temp2);
				temp = mulOper.getValue();
			}
			if(addE.getValue() instanceof Integer){
				return new IntegerExpressionNode((int)temp);
			}else{
				return new FloatExpressionNode((float)temp);
			}
		}else{
			return addE;
		}
	}
	private Node multiplicative_expression(){
		double temp = 0.0;
		double temp2 = 0.0;
		Node mult = postfix_expression();
		if( lookahead.token == Token.MOD || lookahead.token == Token.DIV || lookahead.token == Token.MULT){
			if(mult.getValue() instanceof Integer){
				int s = (Integer) mult.getValue();
				temp = (double) s;
			}else{
				float s = (Float) mult.getValue();
				temp = (double) s;
			}
			while( lookahead.token == Token.MOD || lookahead.token == Token.DIV || lookahead.token == Token.MULT){
				int	x = lookahead.token;
				nextToken();
				Node mult2 = postfix_expression();
				if(mult2.getValue() instanceof Integer){
					int s = (Integer) mult2.getValue();
					temp2 = (double) s;
				}else{
					float s = (Float) mult2.getValue();
					temp2 = (double) s;
				}
				MultiplicativeExpressionNode mulOper = new MultiplicativeExpressionNode(x,temp,temp2);
				temp = mulOper.getValue();
			}
			if(mult.getValue() instanceof Integer){
				return new IntegerExpressionNode((int)temp);
			}else{
				return new FloatExpressionNode((float)temp);
			}
		}else{
			return mult;
		}
	}
	private Node postfix_expression() {
		Node primExpr = primary_expression();
		if(primExpr.getType() == Node.IDENTIFIER_NODE){
			if (lookahead.token == Token.INC_OP || lookahead.token == Token.DEC_OP){
				int x = lookahead.token;
				nextToken();
				if(primExpr.getValue() instanceof Integer){
					IdentifierExpressionNode s = (IdentifierExpressionNode) primExpr;
					if(x == Token.INC_OP)
						s.setValue((Integer)primExpr.getValue()+1);
					else 
						s.setValue((Integer)primExpr.getValue()-1);
					symbols.remove(findSymbol(s.getName()));
					symbols.add(s);
					return (Node) s;
				}
				else{
					throw new ParserException(" ");
				}
			}
			return primExpr;
		}
		else{
			return primExpr;
		}
	}
	private Node primary_expression() {
		if(lookahead.token == Token.IDENTIFIER){
			Node expr = findSymbol(lookahead.sequence);
			nextToken();
			return expr;
		}else if(lookahead.token == Token.INTEGER_LIT){
			Node expr = new IntegerExpressionNode(lookahead.sequence);
			nextToken();
			return expr;
		}else if(lookahead.token == Token.FLOAT_LIT){
			Node expr = new FloatExpressionNode(lookahead.sequence);
			nextToken();
			return expr;
		}
		else if(lookahead.token == Token.STRING_LIT){
			Node expr = new StringExpressionNode(lookahead.sequence);
			nextToken();
			return expr;
		}
		else if(lookahead.token == Token.TRUE || lookahead.token == Token.FALSE){
			Node expr = new BooleanExpressionNode(lookahead.sequence);
			nextToken();
			return expr;
		}
		if(lookahead.token == Token.EPSILON){
			throw new ParserException("Unexpected end of input");
		}else{
			throw new ParserException("Unexpected symbol "+lookahead.sequence +" found");
		}
	}
	private Node reverse_expression() {
		nextToken();
		String rev = "";
		String temp = "";
		Node es = new StringExpressionNode(rev);
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			if(lookahead.token == Token.IDENTIFIER){
				Node ss = primary_expression();
				temp = ss.getValue().toString();
			}else if (lookahead.token == Token.STRING){
				temp = lookahead.sequence;
				nextToken();
			}else{
				//error
			}
			for( int i = temp.length()-1 ; i >= 0; i--){
				rev += temp.charAt(i);
			}
			es = new StringExpressionNode(rev);

			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
			}
		}else{
			//error
		}
		return es;
	}
	private Node scan_expression(){
		nextToken();
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
			}
			else{
				//error
			}
		}else{
			//error
		}
		Scanner scan = new Scanner(System.in);
		String trys = scan.nextLine();
		if(trys.matches("(-)?[0-9]+\\.[0-9]+")){
			return new FloatExpressionNode(trys);
		}else if(trys.matches("(-)?[0-9]+")){
			return new IntegerExpressionNode(trys);

		}
		else if(trys.matches("\".[^\"]*\"")){
			return new StringExpressionNode(trys);
		}
		else 
			throw new ParserException("Invalid Input");
	}
	private IdentifierExpressionNode findSymbol(String sequence) {
		for(IdentifierExpressionNode n: symbols){
			if(n.getName().equals(sequence)){
				return n;
			}
		}

		throw new EvaluationException("Variable '" 
				+ sequence + "' was not initialized.");
	}
	private void statement() {
		//statement -> compound_statement

		if(lookahead.token == Token.OPEN_BRACKET){
			compound_statement();
		}
		//statement -> selection_statement
		else if(lookahead.token == Token.IF){
			selection_statement();
		}
		//statement -> iteration_statement
		else if(lookahead.token == Token.WHILE){
			iteration_statement();
		}
		//statement -> jump_statement
		else if(lookahead.token == Token.PRINT){
			print_statement();
		}
		else if(lookahead.token == Token.PRINTLN){
			println_statement();
		}
		else if(lookahead.token == Token.ARRAY_ADD){
			arrayAdd_statement();
		}
		else if(lookahead.token == Token.WRITEINTFILE){
			writeIntFile_statement();
		}
		else if(lookahead.token == Token.WRITEFILE){
			writeFile_statement();
		}
		else if(lookahead.token == Token.CALL){
			call_statement();
		}
		else{
			expression_statement();

		}
	}
	private void writeIntFile_statement() {
		nextToken();
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			Node ae = primary_expression();
			String s = (String) ae.getValue();
			s = s.replaceAll("\"", "");
			nextToken();
			ArrayExpressionNode aen = (ArrayExpressionNode)findSymbol(lookahead.sequence);
			nextToken();
			LinkedList<Integer> nums = new LinkedList<Integer>(aen.getValue());

			try {
				File f = new File(s);
				if(!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
				PrintWriter printer = new PrintWriter(f);
				for(int sx: nums){
					if(sx == nums.getLast())
						printer.print(sx);
					else
						printer.println(sx);
				}
				printer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				if(lookahead.token == Token.SEMI_COLON)
					nextToken();
			}
		}

	}
	private void writeFile_statement() {
		nextToken();
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			Node ae = primary_expression();
			String s = (String) ae.getValue();
			s = s.replaceAll("\"", "");
			nextToken();
			IdentifierExpressionNode ien =(IdentifierExpressionNode) findSymbol(lookahead.sequence);
			nextToken();
			try {
				File f = new File(s);
				if(!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
				PrintWriter printer = new PrintWriter(f);
				printer.print(((String) ien.getValue()).replaceAll("\"", ""));
				printer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				if(lookahead.token == Token.SEMI_COLON)
					nextToken();
			}
		}

	}
	private void arrayAdd_statement() {
		nextToken();
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			if(lookahead.token == Token.IDENTIFIER){
				ArrayExpressionNode aen = (ArrayExpressionNode) findSymbol(lookahead.sequence);
				nextToken();
				nextToken();				
				IntegerExpressionNode s = new IntegerExpressionNode((Integer)primary_expression().getValue());
				aen.add(s.getValue());
				if(lookahead.token == Token.CLOSE_PAREN){
					nextToken();
					if(lookahead.token == Token.SEMI_COLON){
						nextToken();
					}
					else{
						throw new ParserException("Unexpected symbol "+lookahead.sequence+" found");
					}
				}else{
					throw new ParserException("Unexpected symbol "+lookahead.sequence+" found");
				}
			}
			else{
				throw new ParserException("Unexpected symbol "+lookahead.sequence+" found");
			}
		}else{
			throw new ParserException("Unexpected symbol "+lookahead.sequence+" found");
		}
	}
	private void call_statement() {
		nextToken();
		for(Function x: functionCalls){
			if(x.getName().equals(lookahead.sequence)){
				nextToken();
				nextToken();
				nextToken();
				nextToken();
				for(Token y: x.getTokens()){
					tokens.push(y);
				}
				compound_statement();
			}
		}
	}
	private void print_statement() {
		nextToken();
		Node es = new StringExpressionNode("");
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			if(lookahead.token == Token.STDIN){
				nextToken();
			}else{
				es = assignment_expression();
			}
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				if(lookahead.token == Token.SEMI_COLON){
					nextToken();
				}
			}
		}
		if(es.getType() == Node.STRING_NODE){
			String printer = "";
			String esp = es.getValue().toString();
			for(char s: esp.toCharArray()){
				if(s != '"'){
					printer += s;
				}
			}

			System.out.print(printer);
		}
		else{
			System.out.print(es.getValue().toString());
		}
	}
	private void println_statement() {
		nextToken();
		Node es = new StringExpressionNode("");
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			if(lookahead.token == Token.STDIN){
				nextToken();
			}else{
				es = assignment_expression();

			}
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				if(lookahead.token == Token.SEMI_COLON){
					nextToken();
				}
			}
		}
		if(es.getType() == Node.STRING_NODE){
			String printer = "";
			String esp = es.getValue().toString();
			for(char s: esp.toCharArray()){
				if(s != '"'){
					printer += s;
				}
			}

			System.out.println(printer);
		}
		else{
			System.out.println(es.getValue().toString());
		}
	}

	private void compound_statement() {
		nextToken();
		if(lookahead.token == Token.CLOSE_BRACKET){
			nextToken();
		}
		else{
			block_item_list();
			if(lookahead.token == Token.CLOSE_BRACKET){
				nextToken();
			}
		}
	}
	private void block_item_list() {
		block_item();
		if(lookahead.token != Token.CLOSE_BRACKET)
			block_item_list();
	}
	private void block_item(){
		if(lookahead.token >= Token.CHAR && lookahead.token <= Token.FLOAT ){
			declaration();
		}else{
			statement();
		}
	}
	private void expression_statement() {
		if(lookahead.token == Token.SEMI_COLON){
			nextToken();
		}
		else{
			Node es = assignment_expression();
		}
		if(lookahead.token == Token.SEMI_COLON){
			nextToken();
		}
		else
			throw new ParserException("(EXPRESSION_STATEMENT)Unexpected symbol "+lookahead.token+" found");
	}
	private void iteration_statement() {
		LinkedList<Token> dummy = new LinkedList<Token>(tokens);
		nextToken();
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			Node cond = assignment_expression();
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				if((Boolean)cond.getValue()){
					compound_statement();	
					tokens.clear();
					tokens = new LinkedList<Token>(dummy);
					iteration_statement();
				}else{
					if(lookahead.token == Token.OPEN_BRACKET){
						stack.push(lookahead.sequence);
						nextToken();
						while(!stack.isEmpty()){
							String see = lookahead.sequence;
							nextToken();
							if(see.equals("{")){
								stack.push("{");
							}
							else if(see.equals("}")){
								stack.pop();
							}
						}
					}else{
						//error
					}
				}
			}else
				throw new ParserException("(ITER)Unexpected symbol"+lookahead.sequence+" found");
		}
		else
			throw new ParserException("(ITER2)Unexpected symbol"+lookahead.sequence+" found");

	}
	private void selection_statement() {
		stack.clear();
		nextToken();
		Boolean select = false;
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			Node es = assignment_expression();
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				if((Boolean)es.getValue()){
					statement();
					select = true;
				}
				else{
					if(lookahead.token == Token.OPEN_BRACKET){
						stack.push("{");
						nextToken();
						while(!stack.isEmpty()){
							String see = tokens.peek().sequence;
							nextToken();
							if(see.equals("}")){
								stack.pop();
							}
							else if(see.equals("{")){
								stack.push("{");
							}
						}
					}
				}
				if(lookahead.token == Token.ELSE){
					if(!select){
						nextToken();
						statement();
					}
					else{
						nextToken();
						if(lookahead.token == Token.OPEN_BRACKET){
							stack.push("{");
							nextToken();
							while(!stack.isEmpty()){
								String see = tokens.peek().sequence;
								nextToken();
								if(see.equals("}")){
									stack.pop();
								}
								else if(see.equals("{")){
									stack.push("{");
								}
							}
						}
					}
				}else{

				}

			}else{
				throw new ParserException("(SELECTION)Unexpected symbol"+lookahead+" found");
			}
		}
	}
	private void nextToken()
	{
		tokens.pop();
		// at the end of input we return an epsilon token
		if (tokens.isEmpty())
			lookahead = new Token(Token.EPSILON, "");
		else
			lookahead = tokens.getFirst();
	}
}
