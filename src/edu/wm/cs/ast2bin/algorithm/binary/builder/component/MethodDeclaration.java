package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.List;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class MethodDeclaration extends BinaryBuilderComponent{

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
		header.setType(Type.METHOD_DECLARATION_HEADER);
		tree.addVertex(header);

		//Add edge to parent
		tree.addEdge(header, parent);
		header.setParent(parent);

		//Create new artificial NonTerminal node
		BinaryVertex signatureElementList = new BinaryVertex();
		signatureElementList.setType(Type.SIGNATURE_ELEMENT_LIST);
		tree.addVertex(signatureElementList);

		//Add edge to parent
		tree.addEdge(signatureElementList, header);
		signatureElementList.setParent(header);

		//FirstChild
		BinaryVertex child = siblings.remove(0);

		//Recursion
		buildList(signatureElementList, child, siblings, tree, Type.SIGNATURE_ELEMENT_LIST);

	}
}
