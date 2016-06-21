package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class ClassInstanceCreation extends BinaryBuilderComponent{

	public static void convertToBinary(BinaryVertex vertex, BinaryTree tree) {

		//Siblings
		List<BinaryVertex> siblings = tree.getChildren(vertex);

		disconnectChildren(vertex, siblings, tree);

		//Header
		buildHeader(vertex, siblings, tree);

		//Arguments
		buildArguments(vertex, siblings, tree);

	}

	private static void buildHeader(BinaryVertex parent, List<BinaryVertex> siblings, BinaryTree tree){
		org.eclipse.jdt.core.dom.ClassInstanceCreation node = (org.eclipse.jdt.core.dom.ClassInstanceCreation) parent.getNode();

		//Create new artificial NonTerminal node
		BinaryVertex header = new BinaryVertex();
		header.setType(Type.CLASS_INSTANCE_CREATION_HEADER);
		tree.addVertex(header);

		//Add edge to parent
		tree.addEdge(header, parent);
		header.setParent(parent);

		//Set first child as Expression
		Expression expression = node.getExpression();

		if(expression != null){
			BinaryVertex vExpression = siblings.remove(0);

			//Set as first child
			tree.addEdge(vExpression, header);
			vExpression.setParent(header);
		}


		//Create new artificial NonTerminal node
		BinaryVertex typeList = new BinaryVertex();
		typeList.setType(Type.TYPE_LIST);
		tree.addVertex(typeList);

		//Add edge to parent
		tree.addEdge(typeList, header);
		typeList.setParent(header);

		List<BinaryVertex> types = getTypes(node, siblings);


		//FirstChild
		BinaryVertex child = types.remove(0);

		//Recursion
		buildList(typeList, child, types, tree, Type.TYPE_LIST);


	}

	private static void buildArguments(BinaryVertex parent, List<BinaryVertex> siblings, BinaryTree tree){
		//Create new artificial NonTerminal node
		BinaryVertex argumentList = new BinaryVertex();
		argumentList.setType(Type.ARGUMENT_LIST);
		tree.addVertex(argumentList);

		//Add edge to parent
		tree.addEdge(argumentList, parent);
		argumentList.setParent(parent);


		//FirstChild
		BinaryVertex child = siblings.remove(0);

		//Recursion
		buildList(argumentList, child, siblings, tree, Type.ARGUMENT_LIST);


	}


	private static List<BinaryVertex> getTypes(org.eclipse.jdt.core.dom.ClassInstanceCreation node, List<BinaryVertex> siblings){
		List<BinaryVertex> types = new ArrayList<BinaryVertex>();

		for(Object obj : node.typeArguments()){
			ASTNode n = (ASTNode) obj;
			BinaryVertex vn = getAsociatedBinaryVertex(n, siblings);
			types.add(vn);
			siblings.remove(vn);
		}

		ASTNode nType = node.getType();
		BinaryVertex vt = getAsociatedBinaryVertex(nType, siblings);
		types.add(vt);
		siblings.remove(vt);

		return types;
	}

}
