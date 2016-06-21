package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.List;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;
public class IfStatement extends BinaryBuilderComponent{


	public static void convertToBinary(BinaryVertex vertex, BinaryTree tree){

		//Siblings
		List<BinaryVertex> siblings = tree.getChildren(vertex);

		disconnectChildren(vertex, siblings, tree);

		//Set condition as first child
		BinaryVertex condition = siblings.get(0);
		tree.addEdge(condition, vertex);

		//Create new artificial NonTerminal node
		BinaryVertex branches = new BinaryVertex();
		branches.setType(Type.BRANCHES);
		tree.addVertex(branches);

		//Add edge to parent
		tree.addEdge(branches, vertex);
		branches.setParent(vertex);
		
		//Set statements as children of Branches
		BinaryVertex codeTrue = siblings.get(1);
		BinaryVertex codeFalse = siblings.get(2);
		tree.addEdge(codeTrue, branches);
		tree.addEdge(codeFalse, branches);
		codeTrue.setParent(branches);
		codeFalse.setParent(branches);


	}

}
