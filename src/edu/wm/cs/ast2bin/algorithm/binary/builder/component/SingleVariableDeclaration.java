package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Modifier;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class SingleVariableDeclaration extends BinaryBuilderComponent{

	public static void convertToBinary(BinaryVertex vertex, BinaryTree tree) {

		//Siblings
		List<BinaryVertex> siblings = tree.getChildren(vertex);

		disconnectChildren(vertex, siblings, tree);

		//Header
		buildHeader(vertex, siblings, tree);

		//Expression
		if(siblings.size()>0){
			BinaryVertex expression = siblings.remove(0);
			tree.addEdge(expression, vertex);
			expression.setParent(vertex);
		}
	}


	private static void buildHeader(BinaryVertex parent, List<BinaryVertex> siblings, BinaryTree tree){

		//Create new artificial NonTerminal node
		BinaryVertex header = new BinaryVertex();
		header.setType(Type.VARIABLE_DECLARATION_HEADER);
		tree.addVertex(header);

		//Add edge to parent
		tree.addEdge(header, parent);
		header.setParent(parent);

		//Create new artificial NonTerminal node
		BinaryVertex modifierList = new BinaryVertex();
		modifierList.setType(Type.MODIFIER_LIST);
		tree.addVertex(modifierList);

		//Add edge to parent
		tree.addEdge(modifierList, header);
		modifierList.setParent(header);

		List<BinaryVertex> modifiers = getModifiers(siblings);

		if(modifiers.size() > 0){
			//FirstChild
			BinaryVertex child = modifiers.remove(0);

			//Recursion
			buildList(modifierList, child, modifiers, tree, Type.MODIFIER_LIST);
		}

		//Create new artificial NonTerminal node
		BinaryVertex variable = new BinaryVertex();
		variable.setType(Type.VARIABLE);
		tree.addVertex(variable);

		//Add edge to parent
		tree.addEdge(variable, header);
		variable.setParent(header);
		
		//Set Type
		

		//Set Type
		BinaryVertex type = siblings.remove(0);
		tree.addEdge(type, variable);
		type.setParent(variable);
		
		//Set Identifier
		BinaryVertex identifier = siblings.remove(0);
		tree.addEdge(identifier, variable);
		type.setParent(variable);
	}


	private static List<BinaryVertex> getModifiers(List<BinaryVertex> siblings){
		List<BinaryVertex> modifiers = new ArrayList<BinaryVertex>();

		for(BinaryVertex v : siblings){
			if(v.getNode() instanceof Modifier){
				modifiers.add(v);
			}
		}

		for(BinaryVertex v : modifiers){
			siblings.remove(v);
		}

		return modifiers;
	}


}
