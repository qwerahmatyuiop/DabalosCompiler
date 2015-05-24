package SyntaxAnalyzer;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import Exceptions.EvaluationException;
import Exceptions.ParserException;
import LexicalAnalyzer.Token;
import Nodes.AdditiveExpressionNode;
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
	LinkedList<Token> copy;
	LinkedList<Token> funcStatements = new LinkedList<Token>();
	LinkedList<Token> tokens;
	LinkedList<Function> functionCalls = new LinkedList<Function>();
	
	  Token lookahead;
	 LinkedList<IdentifierExpressionNode> symbols = new LinkedList<IdentifierExpressionNode>();
	 Stack<String> stack = new Stack<String>();
	 Token dum;
	 Boolean execute = true;
	public Parser() {
	}
	
	  public void parse(List<Token> tokens)
	  {
	    this.tokens = new LinkedList<Token>(tokens);
	    lookahead = this.tokens.getFirst();
	    
	    entry_point();
	    if (lookahead.token != Token.EPSILON)
	      throw new ParserException("(PARSER)Unexpected symbol "+lookahead.token+" found");
	  }
	  private void entry_point(){
		  while(lookahead.token != Token.EPSILON){
		  if(lookahead.token == Token.FUNC || lookahead.token == Token.GLOBAL){
		    	external_declaration();
		    }else{
		    	statement();
		    	
		    }
		  }
	  }
	  private void external_declaration(){
		  if(lookahead.token == Token.FUNC){
			  
			  function_definition();
			  
		  }
		  else 
			  declaration();
	  }
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
					  //error
				  }
			  }else{
			      throw new ParserException("(PARSER)Unexpected symbol "+lookahead.token+" found");
			  }
		  }else 
		      throw new ParserException("(PARSER)Unexpected symbol "+lookahead.token+" found");
		  
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
	  private void block_item_list() {
		block_item();
		if(lookahead.token != Token.CLOSE_BRACKET)
			block_item_list();
	}
	  private void block_item(){
		if(lookahead.token >= Token.CHAR && lookahead.token <= Token.FLOAT){
			declaration();
		}else{
			statement();
		}
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
			Node Name = new IdentifierExpressionNode(lookahead.sequence);
			nextToken();
			return Name;
		}
		return null;
		
	}
	  private Node assignment_expression(){
		Node ae;
		  if(lookahead.token == Token.REVERSE){
			  ae = reverse_expression();
		  }
		  else if(lookahead.token == Token.SCAN){
			  ae = scan_expression();
		  }
		  else{
			  ae = logical_or_expression();
			  if(ae.getType() == Node.IDENTIFIER_NODE){
				  if(lookahead.token == Token.ASSIGN){
					  ae = assignment_operator(ae);
				  }
			  }
		  }
		return ae;
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
		int ires = 0;
		float fres =0;
		boolean type = false;
		boolean var = false;
		Node addE = multiplicative_expression();
		if(addE.getType() == Node.IDENTIFIER_NODE){
			var = true;
		}
		
		 if(addE.getValue() instanceof Integer){
			 ires = (Integer) addE.getValue();
			 type = true;
		}else if (addE.getValue() instanceof Float){
			 fres = (Float) addE.getValue();
		}
		else return addE;
		while(lookahead.token == Token.PLUS || lookahead.token == Token.MINUS){
			if(lookahead.token == Token.PLUS){
				nextToken();
				if(type){
					Node mult2 = multiplicative_expression();
					AdditiveExpressionNode multi = new AdditiveExpressionNode();
					multi.add(new IntegerExpressionNode(ires), Token.PLUS);
					multi.add(mult2, Token.PLUS );
					ires = (Integer)multi.getValue();
				}
				else{
					Node mult2 = multiplicative_expression();
					AdditiveExpressionNode multi = new AdditiveExpressionNode();
					multi.add(new FloatExpressionNode(fres), Token.PLUS);
					multi.add(mult2, Token.PLUS );
					fres = (Float) multi.getValue();
				}
			}
			else if(lookahead.token == Token.MINUS){
				nextToken();
				if(type){
					Node mult2 = multiplicative_expression();
					AdditiveExpressionNode multi = new AdditiveExpressionNode();
					multi.add(new IntegerExpressionNode(ires), Token.PLUS);
					multi.add(mult2, Token.MINUS );
					ires = (Integer)multi.getValue();
				}
				else{
					Node mult2 = multiplicative_expression();
					AdditiveExpressionNode multi = new AdditiveExpressionNode();
					multi.add(new FloatExpressionNode(fres), Token.PLUS);
					multi.add(mult2, Token.MINUS );
					fres = (Float) multi.getValue();
				}
			}
		}
		if(type){
			if(var){
				IdentifierExpressionNode ien = (IdentifierExpressionNode)addE;
				ien.setValue(ires);
				return ien;
			}
			Node ms = new IntegerExpressionNode(ires);
			return ms;
		}
		else{
			if(var){
				IdentifierExpressionNode ien = (IdentifierExpressionNode)addE;
				ien.setValue(ires);
				return ien;
			}
			Node ms = new FloatExpressionNode(fres);
			return ms;
		}
		
	}
	  private Node multiplicative_expression(){
		int ires = 0;
		float fres =0;
		boolean type = false;
		boolean var = false;
		Node mult = postfix_expression();
		if(mult.getType() == Node.IDENTIFIER_NODE){
			var = true;
		}
		 if(mult.getValue() instanceof Integer){
			 ires = (Integer) mult.getValue();
			 type = true;
		}else if (mult.getValue() instanceof Float){
			 fres = (Float) mult.getValue();
		}
		else return mult;
		
		while(lookahead.token == Token.MULT || lookahead.token == Token.DIV || lookahead.token == Token.MOD ){
			if(lookahead.token == Token.MULT){
				nextToken();
				if(type){
					Node mult2 = postfix_expression();
					MultiplicativeExpressionNode multi = new MultiplicativeExpressionNode();
					multi.add(new IntegerExpressionNode(ires), Token.MULT);
					multi.add(mult2, Token.MULT );
					ires = (Integer)multi.getValue();
				}
				else{
					Node mult2 = postfix_expression();
					MultiplicativeExpressionNode multi = new MultiplicativeExpressionNode();
					multi.add(new FloatExpressionNode(fres), Token.MULT);
					multi.add(mult2, Token.MULT );
					fres = (Float) multi.getValue();
				}
			}
			else if(lookahead.token == Token.DIV){
				nextToken();
				if(type){
					Node mult2 = postfix_expression();
					MultiplicativeExpressionNode multi = new MultiplicativeExpressionNode();
					multi.add(new IntegerExpressionNode(ires), Token.MULT);
					multi.add(mult2, Token.DIV );
					ires = (Integer)multi.getValue();
				}
				else{
					Node mult2 = postfix_expression();
					MultiplicativeExpressionNode multi = new MultiplicativeExpressionNode();
					multi.add(new FloatExpressionNode(fres), Token.MULT);
					multi.add(mult2, Token.DIV );
					fres = (Float) multi.getValue();
				}
			}
			else if(lookahead.token == Token.MOD){
				nextToken();
				if(type){
					Node mult2 = postfix_expression();
					MultiplicativeExpressionNode multi = new MultiplicativeExpressionNode();
					multi.add(new IntegerExpressionNode(ires), Token.MULT);
					multi.add(mult2, Token.MOD );
					ires = (Integer)multi.getValue();
				}
				else{
					Node mult2 = postfix_expression();
					MultiplicativeExpressionNode multi = new MultiplicativeExpressionNode();
					multi.add(new FloatExpressionNode(fres), Token.MULT);
					multi.add(mult2, Token.MOD);
					fres = (Float) multi.getValue();
				}
			}
		}
		if(type){
			if(var){
				IdentifierExpressionNode ien = (IdentifierExpressionNode)mult;
				ien.setValue(ires);
				return ien;
			}
			Node ms = new IntegerExpressionNode(ires);
			return ms;
		}
		else{
			
			if(var){
					IdentifierExpressionNode ien = (IdentifierExpressionNode)mult;
					ien.setValue(fres);
					return ien;
			}
			Node ms = new FloatExpressionNode(fres);
			return ms;
		}
	}
	  private Node postfix_expression() {
		Node primExpr = primary_expression();
		if(primExpr.getType() == Node.IDENTIFIER_NODE){
			if(lookahead.token == Token.OPEN_BRACE){
				nextToken();
				assignment_expression();
				if(lookahead.token == Token.CLOSE_BRACE){
					nextToken();
					postfix_expression();
				}
				else
					throw new ParserException("(POSTFIX_EXPRESSION)Unexpected symbol "+lookahead.token+" found");
			}
			else if (lookahead.token == Token.INC_OP || lookahead.token == Token.DEC_OP){
				nextToken();
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
		else if(lookahead.token == Token.CONTINUE || lookahead.token == Token.BREAK || lookahead.token == Token.RETURN){
			jump_statement();
		}
		else if(lookahead.token == Token.PRINT){
			print_statement();
		}
		else if(lookahead.token == Token.PRINTLN){
			println_statement();
		}
		else if(lookahead.token == Token.CALL){
			call_statement();
		}
		else{
			expression_statement();
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
			String esp = (String)es.getValue();
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
				String esp = (String)es.getValue();
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
	  private void jump_statement() {
		if(lookahead.token == Token.CONTINUE){
			nextToken();
			
			if(lookahead.token == Token.SEMI_COLON){
				nextToken();
			}else
				throw new ParserException("(JUMP1)Unexpected symbol"+lookahead+" found");
		}
		else if(lookahead.token == Token.BREAK){
			nextToken();
			if(lookahead.token == Token.SEMI_COLON){
				nextToken();
			}else
				throw new ParserException("(JUMP2)Unexpected symbol"+lookahead+" found");
			
		}
		else if(lookahead.token == Token.RETURN){
			nextToken();
			if(lookahead.token != Token.SEMI_COLON){
				assignment_expression();
			}
			if(lookahead.token == Token.SEMI_COLON)
			nextToken();
		}
		else
			throw new ParserException("(JUMP1)Unexpected symbol"+lookahead+" found");

		
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
