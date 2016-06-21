package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.List;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class TryStatement extends BinaryBuilderComponent{

	public static void convertToBinary(BinaryVertex vertex, BinaryTree tree) {

		//Siblings
		List<BinaryVertex> siblings = tree.getChildren(vertex);
		
		disconnectChildren(vertex, siblings, tree);
		
		//Block
		BinaryVertex block = siblings.remove(0);
		tree.addEdge(block, vertex);
		block.setParent(vertex);
		
		//Create new artificial NonTerminal node
		BinaryVertex nextParent = new BinaryVertex();
		nextParent.setType(Type.CATCH_CLAUSE_LIST);
		tree.addVertex(nextParent);
		
		//Add edge to parent
		tree.addEdge(nextParent, vertex);
		nextParent.setParent(vertex);

		//FirstChild
		BinaryVertex child = siblings.remove(0);
		
		//Recursion
		buildList(nextParent, child, siblings, tree, Type.CATCH_CLAUSE_LIST);
		
	}
}
