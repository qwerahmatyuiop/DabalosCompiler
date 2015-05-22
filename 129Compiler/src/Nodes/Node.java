package Nodes;

public interface Node {
	public static final int IDENTIFIER_NODE = 0;
	public static final int INTEGER_NODE = 1;
	public static final int STRING_NODE = 2;
	public static final int FLOAT_NODE = 3;
	public static final int BOOLEAN_NODE = 4;
	
	public static final int EXPRESSION_NODE = 5;
	public static final int ASSIGNMENT_EXPRESSION_NODE = 6;
	public static final int PRIMARY_EXPRESSION_NODE = 7;
	public static final int POSTFIX_EXPRESSION_NODE = 8;
	public static final int LOGICAL_OR_EXPRESSION_NODE = 9;
	public static final int LOGICAL_AND_EXPRESSION_NODE = 10;
	public static final int EQUALITY_EXPRESSION_NODE = 11;
	public static final int ADDITIVE_EXPRESSION_NODE = 12;
	public static final int MULTIPLICATIVE_EXPRESSION_NODE = 13;
	
	
	public static final int FUNCTION_DEFINITION_NODE = 14;
	public static final int PARAMETERS_NODE = 15;
	public static final int ASSIGNMENT_OPERATOR_NODE = 16;
	public static final int DECLARATION_NODE = 17;
	public static final int INIT_DECLARATOR_LIST_NODE = 18;
	public static final int INIT_DECLARATOR_NODE = 19;
	public static final int DECLARATOR_NODE = 20;
	public static final int INITIALIZER_NODE = 21;
	public static final int TYPE_SPECIFIER_NODE = 22;
	public static final int STATEMENT_NODE = 23;
	public static final int COMPOUND_STATEMENT_NODE = 24;
	public static final int BLOCK_ITEM_LIST_NODE = 25;
	public static final int BLOCK_ITEM_NODE = 26;
	public static final int EXPRESSION_STATEMENT_NODE = 27;
	public static final int SELECTION_STATEMENT_NODE = 28;
	public static final int ITERATION_STATEMENT_NODE = 29;
	public static final int JUMP_STATEMENT_NODE = 30;
	
	 public int getType();
	 public Object getValue();
	
}
