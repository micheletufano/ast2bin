package edu.wm.cs.ast2bin.ast;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class TypePrecedence {
	
	private static final int ARTIFICIAL_NODE_PRECEDENCE = -1;
	private static final int DEFAULT_NODE_PRECEDENCE = 6;
	
	private HashMap<String, Integer> precedence;
	private String logAll = "";
	private String logSamePrec = "";
	
	
	public TypePrecedence(){
		precedence = new HashMap<String, Integer>();
		
		//Artificial -1
		precedence.put("block", 0);
		precedence.put("parenthesizedexpression", 1);
		precedence.put("simplename", 2);
		precedence.put("simpletype", 3);
		precedence.put("qualifiedname", 4);
		precedence.put("expressionstatement", 5);
		//All the other nodes have precedence value = 6
		precedence.put("methoddeclaration", 7);
		precedence.put("typedeclaration", 8);
	}
	
	
	public BinaryVertex giveHigherPrecedenceNode(BinaryVertex parentVertex, BinaryVertex childVertex){
		int parentPrec = getPrecedenceValue(parentVertex);
		int childPrec = getPrecedenceValue(childVertex);

		if(childPrec > parentPrec){
			return childVertex;
		} else {
			return parentVertex;
		}
	}
	
	
	public Integer getPrecedenceValue(BinaryVertex vertex){
		if(vertex.isArtificialNode()){
			return ARTIFICIAL_NODE_PRECEDENCE;
		}
		
		ASTNode node = vertex.getNode();
		String type = ASTNodeVisitor.getASTNodeType(node).toLowerCase();
		Integer precedenceValue = precedence.get(type);

		if(precedenceValue == null){
			return DEFAULT_NODE_PRECEDENCE;
		}
		
		return precedenceValue;
	}
	
	
	public BinaryVertex giveHigherPrecedenceNode_old(BinaryVertex parentVertex, BinaryVertex childVertex){
		//Precedence for real nodes (instead of artificial)
		if(parentVertex.isArtificialNode() && childVertex.isArtificialNode()){
			return parentVertex;
		} else if(parentVertex.isArtificialNode()){
			return childVertex;
		} else if(childVertex.isArtificialNode()){
			return parentVertex;
		}
		
		
		ASTNode parent = parentVertex.getNode();
		ASTNode child = childVertex.getNode();
		String parentType = ASTNodeVisitor.getASTNodeType(parent).toLowerCase();
		String childType = ASTNodeVisitor.getASTNodeType(child).toLowerCase();
		
		Integer parentPrec = precedence.get(parentType);
		Integer childPrec = precedence.get(childType);
		boolean samePrecedence = false;
		
		
		//Applying Precedence 
		if(parentPrec == null && childPrec == null){
			samePrecedence = true;
			
			exportLog(parent, child, parent, samePrecedence);
			
			return parentVertex;
		} else if(childPrec == null){
			
			exportLog(parent, child, child, samePrecedence);
			
			return childVertex;
		} else if(parentPrec == null){
			
			exportLog(parent, child, parent, samePrecedence);

			
			return parentVertex;
		} else{
			if(parentPrec == childPrec){
				samePrecedence = true;
				
				exportLog(parent, child, parent, samePrecedence);
				
				return parentVertex;
			} else if (parentPrec > childPrec){
				exportLog(parent, child, parent, samePrecedence);
				
				return parentVertex;
			} else {
				exportLog(parent, child, child, samePrecedence);
				
				return childVertex;
			}
		}

	}
	
	private void exportLog(ASTNode parent, ASTNode child, ASTNode selected, boolean samePrecedence){
		String parentType = ASTNodeVisitor.getASTNodeType(parent);
		String childType = ASTNodeVisitor.getASTNodeType(child);
		String selectedType = ASTNodeVisitor.getASTNodeType(selected);
		
		String line = parentType+","+childType+","+selectedType;
		
		//FileUtility.appendToFile(new File(logAll), line);
		
		if(samePrecedence){
			//FileUtility.appendToFile(new File(logSamePrec), line);
		}
	}



	public HashMap<String, Integer> getPrecedence() {
		return precedence;
	}

	public void setPrecedence(HashMap<String, Integer> precedence) {
		this.precedence = precedence;
	}

	public String getLogAll() {
		return logAll;
	}

	public void setLogAll(String logAll) {
		this.logAll = logAll;
	}

	public String getLogSamePrec() {
		return logSamePrec;
	}

	public void setLogSamePrec(String logSamePrec) {
		this.logSamePrec = logSamePrec;
	}
	
}
