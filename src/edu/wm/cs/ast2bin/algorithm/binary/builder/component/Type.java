package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

public class Type {

	/**
	 * ARTIFICIAL NONTERMINALS
	 */
	public static final String BRANCHES = "Branches";
	public static final String METHOD_INVOCATION_BODY = "MethodInvocationBody";
	public static final String VARIABLE = "Variable";
	public static final String SWITCH_CASE_ITEM = "SwitchCaseItem";
	public static final String TYPE_SIGNATURE = "TypeSignature";

	
	/**
	 * ARTIFICIAL NONTERMINAL HEADERS
	 */
	public static final String CLASS_INSTANCE_CREATION_HEADER = "ClassInstanceCreationHeader";
	public static final String COMPILATION_UNIT_HEADER = "CompilationUnitHeader";
	public static final String FIELD_DECLARATION_HEADER = "FieldDeclarationHeader";
	public static final String FOR_STATEMENT_HEADER = "ForStatementHeader";
	public static final String METHOD_INVOCATION_HEADER = "MethodInvocationHeader";
	public static final String VARIABLE_DECLARATION_HEADER = "VariableDeclarationHeader";
	public static final String SUPER_CONSTRUCTOR_INVOCATION_HEADER = "VariableDeclarationHeader";
	public static final String TYPE_DECLARATION_HEADER = "TypeDeclarationHeader";
	public static final String METHOD_DECLARATION_HEADER = "MethodDeclarationHeader";

	
	/**
	 * ARTIFICIAL NONTERMINAL LISTS
	 */
	public static final String BLOCK_LIST = "BlockList";
	public static final String DIMENSION_LIST = "DimensionList";
	public static final String ARRAY_INITIALIZER_LIST = "ArrayInitializerList";
	public static final String TYPE_LIST = "TypeList";
	public static final String ARGUMENT_LIST = "ArgumentList";
	public static final String IMPORT_DECLARATION_LIST = "ImportDeclarationList";
	public static final String TYPE_DECLARATION_LIST = "TypeDeclarationList";
	public static final String MODIFIER_LIST = "ModifierList";
	public static final String VARIABLE_DECLARATION_FRAGMENT_LIST = "VariableDeclarationFragmentList";
	public static final String EXPRESSION_LIST = "ExpressionList";
	public static final String INFIX_EXPRESSION_LIST = "InfixExpressionList";
	public static final String SWITCH_CASE_LIST = "SwitchCaseList";
	public static final String CATCH_CLAUSE_LIST = "CatchClauseList";
	public static final String CLASS_BODY_ELEMENT_LIST = "ClassBodyElementList";
	public static final String SIGNATURE_ELEMENT_LIST = "SignatureElementList";
	public static final String MEMBER_VALUE_PAIR_LIST = "MemberValuePairList";

	
	/**
	 * ORIGINAL NONTERMINAL
	 */
	public static final String AnonymousClassDeclaration = "AnonymousClassDeclaration";
	public static final String ArrayCreation = "ArrayCreation";
	public static final String ArrayInitializer = "ArrayInitializer";
	public static final String Block = "Block";
	public static final String ClassInstanceCreation = "ClassInstanceCreation";
	public static final String CompilationUnit = "CompilationUnit";
	public static final String ConditionalExpression = "ConditionalExpression";
	public static final String ConstructorInvocation = "ConstructorInvocation";
	public static final String FieldDeclaration = "FieldDeclaration";
	public static final String ForStatement = "ForStatement";
	public static final String IfStatement = "IfStatement";
	public static final String InfixExpression = "InfixExpression";
	public static final String MethodDeclaration = "MethodDeclaration";
	public static final String MethodInvocation = "MethodInvocation";
	public static final String SingleVariableDeclaration = "SingleVariableDeclaration";
	public static final String SuperConstructorInvocation = "SuperConstructorInvocation";
	public static final String SuperMethodInvocation = "SuperMethodInvocation";
	public static final String SwitchStatement = "SwitchStatement";
	public static final String TryStatement = "TryStatement";
	public static final String TypeDeclaration = "TypeDeclaration";
	public static final String VariableDeclarationExpression = "VariableDeclarationExpression";
	public static final String VariableDeclarationStatement = "VariableDeclarationStatement";
	public static final String ParameterizedType = "ParameterizedType";
	public static final String EnhancedForStatement = "EnhancedForStatement";
	public static final String NormalAnnotation = "NormalAnnotation";

}
