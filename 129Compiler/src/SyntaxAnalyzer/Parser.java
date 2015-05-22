package SyntaxAnalyzer;

import java.util.LinkedList;
import java.util.List;

import Exceptions.ParserException;
import LexicalAnalyzer.Token;
import Nodes.AdditiveExpressionNode;
import Nodes.BooleanExpressionNode;
import Nodes.EqualityExpressionNode;
import Nodes.FloatExpressionNode;
import Nodes.IdentifierExpressionNode;
import Nodes.IntegerExpressionNode;
import Nodes.LogicalAndExpressionNode;
import Nodes.LogicalOrExpressionNode;
import Nodes.MultiplicativeExpressionNode;
import Nodes.Node;
import Nodes.StringExpressionNode;

public class Parser {

	LinkedList<Token> tokens;
	  Token lookahead;
	  
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
		  if(lookahead.token == Token.FUNC){
		    	function_definition();
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
		  System.out.println(lookahead.sequence+" in function definition");
		  nextToken();
		  type_specifier();
		  if(lookahead.token == Token.IDENTIFIER){
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
				
				  compound_statement();
			  }else{
			      throw new ParserException("(PARSER)Unexpected symbol "+lookahead.token+" found");
			  }
		  }else 
		      throw new ParserException("(PARSER)Unexpected symbol "+lookahead.token+" found");
		  
	  }
	  private void parameters(){
		  System.out.println(lookahead.sequence+" in parameters");
		while(lookahead.token != Token.CLOSE_PAREN){
			 type_specifier();
			 declarator();
		 }
	  }
	  private void type_specifier(){
		  System.out.println(lookahead.sequence+" in type_specifier");
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
	  }
	  private void statement() {
		System.out.println(lookahead.sequence+" in statement");
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
		//statement -> jump_statement
		else{
			expression_statement();
		}
	}
	  private void print_statement() {
		nextToken();
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			if(lookahead.token == Token.STDIN){
				nextToken();
			}else{
				expression();
			}
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				if(lookahead.token == Token.SEMI_COLON){
					nextToken();
				}
			}
		}
		
	}
	  private void jump_statement() {
		System.out.println(lookahead.sequence+" in jump statement");
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
				expression();
			}
			if(lookahead.token == Token.SEMI_COLON)
			nextToken();
		}
		else
			throw new ParserException("(JUMP1)Unexpected symbol"+lookahead+" found");

		
	}
	  private void compound_statement() {
		System.out.println(lookahead.sequence+" in compound statement");
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
		System.out.println(lookahead.sequence+" in block_item_list");
		block_item();
		if(lookahead.token != Token.CLOSE_BRACKET)
			block_item_list();
	}
	  private void block_item(){
		System.out.println(lookahead.sequence+" in block_item");
		if(lookahead.token >= Token.CHAR && lookahead.token <= Token.FLOAT){
			declaration();
		}else{
			statement();
		}
	}
	  private void declaration(){
		System.out.println(lookahead.sequence+" in declaration");
		if(lookahead.token >= Token.CHAR && lookahead.token <= Token.FLOAT){
			type_specifier();
		}
		init_declarator_list();
		if(lookahead.token == Token.SEMI_COLON)
			nextToken();
		else
			throw new ParserException("(DECLARATION)Unexpected symbol "+lookahead.token+" found");
	}
	  private void init_declarator_list(){
		System.out.println(lookahead.sequence+" in init_declator_list");
		init_declarator();
		if(lookahead.token == Token.COMMA){
			nextToken();
			init_declarator();
		}
	}
	  private void init_declarator(){
		System.out.println(lookahead.sequence+" in init_declarator");
		declarator();
		if(lookahead.token == Token.ASSIGN){
			nextToken();
			initializer();
		}
	}
	  private void declarator(){
		System.out.println(lookahead.sequence+" in declarator");
		if(lookahead.token == Token.OPEN_PAREN){
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
		}
		else if(lookahead.token == Token.IDENTIFIER){
			nextToken();
		}
		else
			declarator();
		
	}
	  private void initializer(){
		System.out.println(lookahead.sequence+" in initializer");
		assignment_expression();
	}
	  private void assignment_expression(){
		System.out.println(lookahead.sequence+" in assignment_expression");
		Node ae = logical_or_expression();
		System.out.println(ae.getType());
		if(lookahead.token >= Token.ASSIGN && lookahead.token <= Token.DIV_ASSIGN){
			assignment_operator();
			assignment_expression();
		}
	}
	  private void assignment_operator(){
		System.out.println(lookahead.sequence+" in assignment_operator");
		if(lookahead.token == Token.ASSIGN){
			nextToken();
		}
		else if(lookahead.token == Token.MUL_ASSIGN){
			nextToken();
		}
		else if(lookahead.token == Token.DIV_ASSIGN){
			nextToken();
		}
		else if(lookahead.token == Token.MOD_ASSIGN){
			nextToken();
		}
		else if(lookahead.token == Token.ADD_ASSIGN){
			nextToken();
		}
		else if(lookahead.token == Token.SUB_ASSIGN){
			nextToken();
		}
	}
	  private Node logical_or_expression() {
		System.out.println(lookahead.sequence+" in logical_or_expression");
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
		System.out.println(lookahead.sequence+" in logical_and_expression");
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
		System.out.println(lookahead.sequence+" in equality_expression");
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
		System.out.println(lookahead.sequence+" in additive_expression");
		Node addE = multiplicative_expression();
		if(addE.getValue() instanceof Integer){
			 ires = (Integer) addE.getValue();
			 type = true;
		}else{
			 fres = (Float) addE.getValue();
		}
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
			Node ms = new IntegerExpressionNode(ires);
			return ms;
		}
		else{
			Node ms = new FloatExpressionNode(fres);
			return ms;
		}
		
	}
	  private Node multiplicative_expression(){
		int ires = 0;
		float fres =0;
		boolean type = false;
		System.out.println(lookahead.sequence+" in multiplicative_expression");
		Node mult = postfix_expression();
		if(mult.getValue() instanceof Integer){
			 ires = (Integer) mult.getValue();
			 type = true;
		}else{
			 fres = (Float) mult.getValue();
		}
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
			Node ms = new IntegerExpressionNode(ires);
			return ms;
		}
		else{
			Node ms = new FloatExpressionNode(fres);
			return ms;
		}
	}
	  private Node postfix_expression() {
		System.out.println(lookahead.sequence+" in postfix_expression");
		Node primExpr = primary_expression();
		if(primExpr.getType() == Node.IDENTIFIER_NODE){
			if(lookahead.token == Token.OPEN_BRACE){
				nextToken();
				expression();
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
		System.out.println(lookahead.sequence+" in primary_expression");
		if(lookahead.token == Token.IDENTIFIER){
			Node expr = new IdentifierExpressionNode(lookahead.sequence);
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
	  private void expression_statement() {
		System.out.println(lookahead.sequence+" in expression_Statement");
		if(lookahead.token == Token.SEMI_COLON){
			nextToken();
		}
		else{
		expression();
		}
		if(lookahead.token == Token.SEMI_COLON){
			nextToken();
		}
		else
			throw new ParserException("(EXPRESSION_STATEMENT)Unexpected symbol "+lookahead.token+" found");
	}
	  private void expression() {
		System.out.println(lookahead.sequence+" in expression");
		assignment_expression();
	}
	  private void iteration_statement() {
		System.out.println(lookahead.sequence+" in iteration statement");
		nextToken();
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			expression();
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				statement();
			}else
				throw new ParserException("(ITER)Unexpected symbol"+lookahead+" found");
		}
		else
			throw new ParserException("(ITER)Unexpected symbol"+lookahead+" found");

	}
	  private void selection_statement() {
		System.out.println(lookahead.sequence+" in selection statement");
		nextToken();
		if(lookahead.token == Token.OPEN_PAREN){
			nextToken();
			expression();
			if(lookahead.token == Token.CLOSE_PAREN){
				nextToken();
				statement();
				if(lookahead.token == Token.ELSE){
					nextToken();
					statement();
				}
			}
			else 
				throw new ParserException("(SELECTION)Unexpected symbol"+lookahead+" found");
		}else{
			throw new ParserException("(SELECTION)Unexpected symbol"+lookahead+" found");
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
