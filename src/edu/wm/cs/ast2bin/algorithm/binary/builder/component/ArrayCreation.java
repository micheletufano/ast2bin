package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.List;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class ArrayCreation extends BinaryBuilderComponent{

	public static void convertToBinary(BinaryVertex vertex, BinaryTree tree){

		//Siblings
		List<BinaryVertex> siblings = tree.getChildren(vertex);

		disconnectChildren(vertex, siblings, tree);

		//Set ArrayType as first child
		BinaryVertex type = siblings.remove(0);
		tree.addEdge(type, vertex);

		//Create NonTerminal DimensionList
		BinaryVertex dimensionList = new BinaryVertex();
		dimensionList.setType(Type.DIMENSION_LIST);
		tree.addVertex(dimensionList);

		//Add edge to parent
		tree.addEdge(dimensionList, vertex);
		dimensionList.setParent(vertex);

		//FirstChild
		BinaryVertex child = siblings.remove(0);

		//Recursion
		buildList(dimensionList, child, siblings, tree, Type.DIMENSION_LIST);

	}
}
