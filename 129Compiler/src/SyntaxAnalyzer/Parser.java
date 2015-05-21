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
	public void declaration(){
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
	public void init_declarator_list(){
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
		logical_or_expression();
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
	
	private void logical_or_expression() {
		System.out.println(lookahead.sequence+" in logical_or_expression");
		logical_and_expression();
		if(lookahead.token == Token.OR_OP){
			nextToken();
			logical_or_expression();
		}
		
	}
	private void logical_and_expression() {
		System.out.println(lookahead.sequence+" in logical_and_expression");
		equality_expression();
		if(lookahead.token == Token.AND_OP){
			nextToken();
			logical_and_expression();
		}
		
	}
	private void equality_expression(){
		System.out.println(lookahead.sequence+" in equality_expression");
		additive_expression();
		if(lookahead.token == Token.EQ_OP || lookahead.token == Token.NEG_OP || lookahead.token == Token.GR || lookahead.token == Token.LE || lookahead.token == Token.GR_EQ || lookahead.token == Token.LE_EQ){
			nextToken();
			equality_expression();
		}
	}
	private void additive_expression(){
		System.out.println(lookahead.sequence+" in additive_expression");
		multiplicative_expression();
		if(lookahead.token == Token.PLUS){
			nextToken();
			additive_expression();
		}
		else if(lookahead.token == Token.MINUS){
			nextToken();
			additive_expression();
		}
	}
	private void multiplicative_expression(){
		System.out.println(lookahead.sequence+" in multiplicative_expression");
		postfix_expression();
		if(lookahead.token == Token.MULT){
			nextToken();
			multiplicative_expression();
		}
		else if(lookahead.token == Token.DIV){
			nextToken();
			multiplicative_expression();
		}
		/*else if(lookahead.token == Token.MOD){
			nextToken();
			multiplicative_expression();
		}*/
	}

	private void postfix_expression() {
		System.out.println(lookahead.sequence+" in postfix_expression");
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
		else if (lookahead.token == Token.INC_OP || lookahead.token == Token.DEC_OP){
			nextToken();
		}
		
	}

	private void primary_expression() {
		System.out.println(lookahead.sequence+" in primary_expression");
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
