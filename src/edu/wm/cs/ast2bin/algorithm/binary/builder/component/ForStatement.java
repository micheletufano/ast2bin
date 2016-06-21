package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.List;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class ForStatement extends BinaryBuilderComponent{

	public static void convertToBinary(BinaryVertex vertex, BinaryTree tree) {

		//Siblings
		List<BinaryVertex> siblings = tree.getChildren(vertex);

		disconnectChildren(vertex, siblings, tree);

		BinaryVertex body = siblings.remove(siblings.size()-1);

		//Header
		buildHeader(vertex, siblings, tree);

		//Body
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

		//Create new artificial NonTerminal node
		BinaryVertex expressionList = new BinaryVertex();
		expressionList.setType(Type.EXPRESSION_LIST);
		tree.addVertex(expressionList);

		//Add edge to parent
		tree.addEdge(expressionList, header);
		expressionList.setParent(header);

		//FirstChild
		BinaryVertex child = siblings.remove(0);

		//Recursion
		buildList(expressionList, child, siblings, tree, Type.EXPRESSION_LIST);

	}
}
