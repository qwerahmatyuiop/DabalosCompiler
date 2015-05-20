package SyntaxAnalyzer;

import java.util.LinkedList;
import java.util.List;

import Exceptions.ParserException;
import LexicalAnalyzer.Token;

public class Parser {

	LinkedList<Token> tokens;
	  Token lookahead;
	  
	public Parser() {
		// TODO Auto-generated constructor stub
	}
	
	  public void parse(List<Token> tokens)
	  {
	    this.tokens = new LinkedList<Token>(tokens);
	    for(Token s: this.tokens){
	    	System.out.print(s.sequence);
	    }
	    lookahead = this.tokens.getFirst();
	    System.out.print(lookahead.token);
	    if(lookahead.token == Token.FUNC){
	    	function_definition();
	    }else{
	    statement();
	    }
	    if (lookahead.token != Token.EPSILON)
	      throw new ParserException("(PARSER)Unexpected symbol "+lookahead.token+" found");
	  }
	  private void translation_unit(){
		  external_declaration();
	  }
	  private void external_declaration(){
		  if(lookahead.token == Token.FUNC){
			  function_definition();
		  }
		  else 
			  declaration();
	  }
	  private void function_definition(){
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
				  }
				  compound_statement();
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
	  private void type_specifier(){
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
		//statement -> jump_statement
		else{
			expression_statement();
		}
	}


	private void jump_statement() {
		
		if(lookahead.token == Token.CONTINUE){
			nextToken();
		}
		else if(lookahead.token == Token.BREAK){
			nextToken();
		}
		else if(lookahead.token == Token.RETURN){
			nextToken();
			if(lookahead.token != Token.SEMI_COLON){
				expression();
			}
		}
		else
			throw new ParserException("(JUMP)Unexpected symbol"+lookahead+" found");
		if(lookahead.token == Token.SEMI_COLON){
			nextToken();
		}else
			throw new ParserException("(JUMP)Unexpected symbol"+lookahead+" found");
		
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
		if(lookahead.token >= Token.CHAR && lookahead.token <= Token.FLOAT){
			declaration();
		}else{
			statement();
		}
	}
	public void declaration(){
		if(lookahead.token >= Token.CHAR && lookahead.token <= Token.FLOAT){
			type_specifier();
		}
		init_declarator_list();
		if(lookahead.token == Token.SEMI_COLON)
			nextToken();
		else
			throw new ParserException("(DECLARATION)Unexpected symbol "+lookahead.token+" found");
	}
	public void init_declarator_list(){
		init_declarator();
		if(lookahead.token == Token.COMMA){
			nextToken();
			init_declarator();
		}
	}
	private void init_declarator(){
		declarator();
		if(lookahead.token == Token.ASSIGN){
			nextToken();
			initializer();
		}
	}
	private void declarator(){
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
		assignment_expression();
	}
	private void assignment_expression(){
		logical_or_expression();
		if(lookahead.token >= Token.ASSIGN && lookahead.token <= Token.DIV_ASSIGN){
			assignment_operator();
			assignment_expression();
		}
	}
	private void assignment_operator(){
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
	
	private void logical_or_expression() {
		logical_and_expression();
		if(lookahead.token == Token.OR_OP){
			nextToken();
			logical_or_expression();
		}
		
	}
	private void logical_and_expression() {
		equality_expression();
		if(lookahead.token == Token.AND_OP){
			nextToken();
			logical_and_expression();
		}
		
	}
	private void equality_expression(){
		additive_expression();
		if(lookahead.token == Token.EQ_OP || lookahead.token == Token.NEG_OP || lookahead.token == Token.GR || lookahead.token == Token.LE || lookahead.token == Token.GR_EQ || lookahead.token == Token.LE_EQ){
			nextToken();
			equality_expression();
		}
	}
	private void additive_expression(){
		multiplicative_expression();
		if(lookahead.token == Token.PLUSMINUS){
			nextToken();
			additive_expression();
		}
	}
	private void multiplicative_expression(){
		postfix_expression();
		if(lookahead.token == Token.MULTDIV){
			nextToken();
			multiplicative_expression();
		}
	}

	private void postfix_expression() {
		primary_expression();
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
	}

	private void primary_expression() {
		if(lookahead.token == Token.IDENTIFIER){
			nextToken();
		}else if(lookahead.token == Token.INTEGER_LIT){
			nextToken();
		}else if(lookahead.token == Token.FLOAT_LIT){
			nextToken();
		}
		else if(lookahead.token == Token.STRING_LIT){
			nextToken();
		}
		else if(lookahead.token == Token.TRUE || lookahead.token == Token.FALSE){
			nextToken();
		}
		else{
			
		}
	}

	private void expression_statement() {
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
		assignment_expression();
	}

	private void iteration_statement() {
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
