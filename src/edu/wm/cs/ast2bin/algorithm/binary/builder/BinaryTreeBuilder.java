package edu.wm.cs.ast2bin.algorithm.binary.builder;

import java.util.ArrayList;
import java.util.List;

import org.jgraph.graph.DefaultEdge;

import edu.wm.cs.ast2bin.algorithm.NonTerminalFrequency;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.AnonymousClassDeclaration;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.ArrayCreation;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.ArrayInitializer;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.Block;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.ClassInstanceCreation;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.CompilationUnit;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.ConstructorInvocation;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.EnhancedForStatement;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.FieldDeclaration;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.ForStatement;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.IfStatement;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.InfixExpression;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.MethodDeclaration;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.MethodInvocation;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.NormalAnnotation;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.ParameterizedType;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.SingleVariableDeclaration;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.SuperConstructorInvocation;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.SuperMethodInvocation;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.SwitchStatement;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.TryStatement;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.Type;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.TypeDeclaration;
import edu.wm.cs.ast2bin.algorithm.binary.builder.component.VariableDeclaration;
import edu.wm.cs.ast2bin.ast.ASTNodeVisitor;
import edu.wm.cs.ast2bin.ast.TypePrecedence;
import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;
import edu.wm.cs.ast2bin.tree.vertex.Vertex;
import edu.wm.cs.ast2bin.visualization.BinaryTreeVisualizer;

public class BinaryTreeBuilder {

 
	public static BinaryTree convertASTToBinaryTree(Tree tree){
		return convertASTToBinaryTree(tree, true, false);
	}
	
	public static BinaryTree convertASTToBinaryTree(Tree tree, boolean mergeLiterals, boolean visualization){

		BinaryTree bTree = cloneTreeInBinaryTree(tree);
		
		//Merge equal subsequent literals
		if(mergeLiterals){
			bTree.annotateNodes();
			mergeLiterals(bTree);	
		}
		
		//First Phase - Case IV
		TypePrecedence precedence = new TypePrecedence();
		performFirstPhaseConversion(bTree.getRoot(), bTree, precedence);

		if(visualization){
			BinaryTreeVisualizer bTv = new BinaryTreeVisualizer(bTree, "Case IV");
		}
		
		//Second Phase - Case II
		performSecondPhaseConversion(bTree.getRoot(), bTree, precedence);
		performSecondPhaseConversion(bTree.getRoot(), bTree, precedence);
		
		return bTree;
	}


	private static void mergeLiterals(BinaryTree bTree){
		List<BinaryVertex> toRemove = new ArrayList<BinaryVertex>();
		
		for(BinaryVertex vertex: bTree.vertexSet()){
			List<BinaryVertex> children = bTree.getChildren(vertex);
			
			boolean first = true;
			BinaryVertex prev = null;
			for(BinaryVertex child : children){
				if(first){
					prev = child;
					first = false;
				} else {
					if(toMerge(prev, child, bTree)){
						toRemove.add(prev);
					}
					prev = child;
				}
			}
			
		}
		
		for(BinaryVertex v : toRemove){
			bTree.removeVertex(v);
		}
	}
	
	private static boolean toMerge(BinaryVertex child1, BinaryVertex child2, BinaryTree bTree){
		if(!bTree.isLeaf(child1) || !bTree.isLeaf(child2)){
			return false;
		}
		
		String type1 = NonTerminalFrequency.getNodeType(child1).toLowerCase();
		String type2 = NonTerminalFrequency.getNodeType(child2).toLowerCase();
		
		if(!type1.contains("literal") || !type2.contains("literal")){
			return false;
		}
		
		if(type1.equalsIgnoreCase(type2)){
			if(type1.equalsIgnoreCase("BooleanLiteral")){
				if(child1.getNode().toString().equalsIgnoreCase(child2.getNode().toString())){
					return true;
				}
			} else {
				return true;
			}
		}
		
		return false;
	}
	
	
	private static void performFirstPhaseConversion(BinaryVertex currentNode, BinaryTree bTree, TypePrecedence precedence) {
		List<BinaryVertex> children = bTree.getChildren(currentNode);

		//CASE I
		if(children.size() == 0){
			//The current node is a terminal
			return;

			//CASE II
		} else if(children.size() == 1){

			performFirstPhaseConversion(children.get(0), bTree, precedence);
			
			//CASE III
		}  else if(children.size() == 2){

			//No need to update the tree

			//Analyze recursively the two children
			performFirstPhaseConversion(children.get(0), bTree, precedence);
			performFirstPhaseConversion(children.get(1), bTree, precedence);

			//CASE IV
		}  else {
			
			String type = ASTNodeVisitor.getASTNodeType(currentNode.getNode());
			
			if(type.equalsIgnoreCase(Type.AnonymousClassDeclaration)){
				AnonymousClassDeclaration.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.ArrayCreation)){
				ArrayCreation.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.ArrayInitializer)){
				ArrayInitializer.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.Block)){
				Block.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.ClassInstanceCreation)){
				ClassInstanceCreation.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.CompilationUnit)){
				CompilationUnit.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.ConditionalExpression)){
				IfStatement.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.ConstructorInvocation)){
				ConstructorInvocation.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.FieldDeclaration)){
				FieldDeclaration.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.ForStatement)){
				ForStatement.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.IfStatement)){
				IfStatement.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.InfixExpression)){
				InfixExpression.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.MethodDeclaration)){
				MethodDeclaration.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.MethodInvocation)){
				MethodInvocation.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.SingleVariableDeclaration)){
				SingleVariableDeclaration.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.SuperConstructorInvocation)){
				SuperConstructorInvocation.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.SuperMethodInvocation)){
				SuperMethodInvocation.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.SwitchStatement)){
				SwitchStatement.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.TryStatement)){
				TryStatement.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.TypeDeclaration)){
				TypeDeclaration.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.VariableDeclarationExpression)){
				VariableDeclaration.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.VariableDeclarationStatement)){
				VariableDeclaration.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.ParameterizedType)){
				ParameterizedType.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.EnhancedForStatement)){
				EnhancedForStatement.convertToBinary(currentNode, bTree);
			} else if(type.equalsIgnoreCase(Type.NormalAnnotation)){
				NormalAnnotation.convertToBinary(currentNode, bTree);
			} else{
				System.out.println(" ***************************** MISSING CASE IV *****************************");
				System.out.println(type);
				
				try {
					throw new Exception();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			//Continue recursively on the children (skipping the artificial nodes created)
			for(BinaryVertex bv : children){
				performFirstPhaseConversion(bv, bTree, precedence);
			}

		}

	}
	
	
	
	private static void performSecondPhaseConversion(BinaryVertex currentNode, BinaryTree bTree, TypePrecedence precedence) {
		List<BinaryVertex> children = bTree.getChildren(currentNode);

		//CASE I
		if(children.size() == 0){
			//The current node is a terminal

			//Remove the node if is artificial
			if(currentNode.isArtificialNode()){
				//System.out.println(NonTerminalFrequency.getNodeType(currentNode));
				//System.out.println("Removed!!!!");
				bTree.removeVertex(currentNode);
			}
			
			return;

			//CASE II
		} else if(children.size() == 1){
			
			BinaryVertex child = children.get(0);	
			//Apply precedence rules
			BinaryVertex typeToKeep = precedence.giveHigherPrecedenceNode(currentNode, child);
			
			if(typeToKeep.equals(currentNode)){				
				//Remove previous edge
				bTree.removeEdge(child, currentNode);
				
				//Set Terminal
				currentNode.setTerminal(child.getNode());
				//Update children
				for(BinaryVertex currentChild : bTree.getChildren(child)){
					bTree.addEdge(currentChild, currentNode);
					currentChild.setParent(currentNode);
				}

				bTree.removeVertex(child);
				
				performSecondPhaseConversion(currentNode, bTree, precedence);
				
			} else {				
				
				bTree.removeEdge(child, currentNode);
				
				for(BinaryVertex c : bTree.getChildren(child)){
					bTree.addEdge(c, currentNode);
					c.setParent(currentNode);
				}
				
				bTree.removeVertex(child);
				
				currentNode.setNode(child.getNode());
				currentNode.setTerminal(child.getTerminal());
				currentNode.setArtificialNode(child.isArtificialNode());
				
				performSecondPhaseConversion(currentNode, bTree, precedence);
			}
			
			//CASE III
		}  else if(children.size() == 2){

			//No need to update the tree

			//Analyze recursively the two children
			performSecondPhaseConversion(children.get(0), bTree, precedence);
			performSecondPhaseConversion(children.get(1), bTree, precedence);
		} 
		
		
	}
	

	private static BinaryTree cloneTreeInBinaryTree(Tree tree) {
		BinaryTree bTree = new BinaryTree(DefaultEdge.class);

		//Clone vertices
		for(Vertex v : tree.vertexSet()){
			BinaryVertex bv = convertVertexToBinaryVertex(v);
			bTree.addVertex(bv);

			//Set root
			if(v.equals(tree.getRoot())){
				bTree.setRoot(bv);
			}
		}

		//Clone edges
		for(DefaultEdge e : tree.edgeSet()){
			Vertex source = tree.getEdgeSource(e);
			Vertex target = tree.getEdgeTarget(e);

			if(source!=null){
				BinaryVertex newSource = getBinaryVertexMatch(source, bTree);
				BinaryVertex newTarget = getBinaryVertexMatch(target, bTree);

				bTree.addEdge(newSource, newTarget);
				newSource.setParent(newTarget);
			}
		}		

		return bTree;
	}


	private static BinaryVertex convertVertexToBinaryVertex(Vertex v){
		BinaryVertex bv = new BinaryVertex();

		bv.setNode(v.getNode());	

		return bv;
	}


	private static BinaryVertex getBinaryVertexMatch(Vertex v, BinaryTree bTree){

		for(BinaryVertex bv : bTree.vertexSet()){
			if(bv.getNode().equals(v.getNode())){
				return bv;
			}
		}

		return null;
	}

}
