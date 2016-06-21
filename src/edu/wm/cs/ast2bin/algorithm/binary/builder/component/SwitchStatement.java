package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.SwitchCase;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class SwitchStatement extends BinaryBuilderComponent{

	public static void convertToBinary(BinaryVertex vertex, BinaryTree tree) {

		//Siblings
		List<BinaryVertex> siblings = tree.getChildren(vertex);

		disconnectChildren(vertex, siblings, tree);

		//Expression
		BinaryVertex expression = siblings.remove(0);
		tree.addEdge(expression, vertex);
		expression.setParent(vertex);

		//SwitchCaseList
		buildSwitchCaseList(vertex, siblings, tree);



	}


	private static void buildSwitchCaseList(BinaryVertex parent, List<BinaryVertex> siblings, BinaryTree tree){

		//Create new artificial NonTerminal node
		BinaryVertex switchCaseList = new BinaryVertex();
		switchCaseList.setType(Type.SWITCH_CASE_LIST);
		tree.addVertex(switchCaseList);

		//Add edge to parent
		tree.addEdge(switchCaseList, parent);
		switchCaseList.setParent(parent);

		BinaryVertex currentParent = switchCaseList;
		
		while(siblings.size() > 0){
			//SwitchCaseItem
			createSwitchCaseItem(currentParent, siblings, tree);
			
			//Create new artificial NonTerminal node
			BinaryVertex nextCurrentParent = new BinaryVertex();
			nextCurrentParent.setType(Type.SWITCH_CASE_LIST);
			tree.addVertex(nextCurrentParent);

			//Add edge to parent
			tree.addEdge(nextCurrentParent, currentParent);
			nextCurrentParent.setParent(currentParent);
			
			currentParent = nextCurrentParent;
		}

	}

	private static void createSwitchCaseItem(BinaryVertex parent, List<BinaryVertex> siblings, BinaryTree tree){
		//Create new artificial NonTerminal node
		BinaryVertex switchCaseItem = new BinaryVertex();
		switchCaseItem.setType(Type.SWITCH_CASE_ITEM);
		tree.addVertex(switchCaseItem);
		
		//Add parent
		tree.addEdge(switchCaseItem, parent);
		switchCaseItem.setParent(parent);
		
		//SwitchCase
		BinaryVertex switchCase = siblings.remove(0);
		tree.addEdge(switchCase, switchCaseItem);
		switchCase.setParent(switchCaseItem);

		//Create new artificial NonTerminal node
		BinaryVertex expressionList = new BinaryVertex();
		expressionList.setType(Type.EXPRESSION_LIST);
		tree.addVertex(expressionList);

		//Add edge to parent
		tree.addEdge(expressionList, switchCaseItem);
		expressionList.setParent(switchCaseItem);

		List<BinaryVertex> expressions = getExpressions(siblings);
		
		if(expressions.size() > 0){
			//FirstChild
			BinaryVertex child = expressions.remove(0);

			//Recursion
			buildList(expressionList, child, expressions, tree, Type.EXPRESSION_LIST);
		}
	}


	private static List<BinaryVertex> getExpressions(List<BinaryVertex> siblings){
		List<BinaryVertex> expressions = new ArrayList<BinaryVertex>();

		for(BinaryVertex v : siblings){
			if(v.getNode() instanceof SwitchCase){
				break;
			} else {
				expressions.add(v);
			}
		}

		for(BinaryVertex v : expressions){
			siblings.remove(v);
		}

		return expressions;
	}
}
