package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.List;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class EnhancedForStatement extends BinaryBuilderComponent{

	public static void convertToBinary(BinaryVertex vertex, BinaryTree tree) {

		//Siblings
		List<BinaryVertex> siblings = tree.getChildren(vertex);

		disconnectChildren(vertex, siblings, tree);

		//Header
		buildHeader(vertex, siblings, tree);

		//Body
		BinaryVertex body = siblings.remove(0);
		tree.addEdge(body, vertex);
		body.setParent(vertex);
		
	}


	private static void buildHeader(BinaryVertex parent, List<BinaryVertex> siblings, BinaryTree tree){

		//Create new artificial NonTerminal node
		BinaryVertex header = new BinaryVertex();
		header.setType(Type.FOR_STATEMENT_HEADER);
		tree.addVertex(header);

		//Add edge to parent
		tree.addEdge(header, parent);
		header.setParent(parent);

		//Add FormalParameter
		BinaryVertex formalParameter = siblings.remove(0);
		tree.addEdge(formalParameter, header);
		formalParameter.setParent(header);
		
		
		//Add Expression
		BinaryVertex expression = siblings.remove(0);
		tree.addEdge(expression, header);
		expression.setParent(header);

	}
}
