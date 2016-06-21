package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public abstract class BinaryBuilderComponent {
	
	//public abstract void convertToBinary(BinaryVertex vertex, BinaryTree tree);

	
	
	protected static void disconnectChildren(BinaryVertex parent, List<BinaryVertex> children, BinaryTree tree){
		//Disconnect children from the parent
		for(BinaryVertex c : children){
			tree.removeEdge(c, parent);
		}
	}
	
	
	protected static void buildList(BinaryVertex parent, BinaryVertex child, List<BinaryVertex> siblings, BinaryTree tree, String nonTerminalType){
		
		if(siblings.size()==0){
			child.setParent(parent);
			tree.addEdge(child, parent);
			return;
		
		} else{
			//Create new artificial NonTerminal node
			BinaryVertex nextParent = new BinaryVertex();
			nextParent.setType(nonTerminalType);
			tree.addVertex(nextParent);
			
			//Add first child
			child.setParent(nextParent);
			tree.addEdge(child, parent);
			
			//Add edge to parent
			tree.addEdge(nextParent, parent);
			nextParent.setParent(parent);
			
			//Recursion
			BinaryVertex nextChild = siblings.remove(0);
			buildList(nextParent, nextChild, siblings, tree, nonTerminalType);
			
		}
		
	}
	
	
	protected static BinaryVertex getAsociatedBinaryVertex(ASTNode node, List<BinaryVertex> children){
		for(BinaryVertex c : children){
			if(c.getNode().equals(node)){
				return c;
			}
		}
		
		return null;
	}
	
	
	
}
