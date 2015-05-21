package Nodes;

public interface Node {
	public static final int IDENTIFIER_NODE = 0;
	public static final int INTEGER_NODE = 0;
	public static final int STRING_NODE = 0;
	public static final int FLOAT_NODE = 0;
	public static final int BOOLEAN_NODE = 0;
	
	public static final int EXPRESSION_NODE = 0;
	public static final int ASSIGNMENT_EXPRESSION_NODE = 0;
	public static final int PRIMARY_EXPRESSION_NODE = 0;
	public static final int POSTFIX_EXPRESSION_NODE = 0;
	public static final int LOGICAL_OR_EXPRESSION_NODE = 0;
	public static final int LOGICAL_AND_NODE = 0;
	public static final int EQUALITY_EXPRESSION_NODE = 0;
	public static final int ADDITIVE_EXPRESSION_NODE = 0;
	public static final int MULTIPLICATIVE_EXPRESSION_NODE = 0;
	
	
	public static final int FUNCTION_DEFINITION_NODE = 0;
	public static final int PARAMETERS_NODE = 0;
	public static final int ASSIGNMENT_OPERATOR_NODE = 0;
	public static final int DECLARATION_NODE = 0;
	public static final int INIT_DECLARATOR_LIST_NODE = 0;
	public static final int INIT_DECLARATOR_NODE = 0;
	public static final int DECLARATOR_NODE = 0;
	public static final int INITIALIZER_NODE = 0;
	public static final int TYPE_SPECIFIER_NODE = 0;
	public static final int STATEMENT_NODE = 0;
	public static final int COMPOUND_STATEMENT_NODE = 0;
	public static final int BLOCK_ITEM_LIST_NODE = 0;
	public static final int BLOCK_ITEM_NODE = 0;
	public static final int EXPRESSION_STATEMENT_NODE = 0;
	public static final int SELECTION_STATEMENT_NODE = 0;
	public static final int ITERATION_STATEMENT_NODE = 0;
	public static final int JUMP_STATEMENT_NODE = 0;
	
	 public int getType();
	 public Object getValue();
	 public String getDataType();
	
}
