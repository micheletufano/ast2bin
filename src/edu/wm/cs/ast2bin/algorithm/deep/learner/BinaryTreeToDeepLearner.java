package edu.wm.cs.ast2bin.algorithm.deep.learner;

import java.util.Map;

import edu.wm.cs.ast2bin.algorithm.NonTerminalFrequency;
import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class BinaryTreeToDeepLearner {
	
	private static final int TERMINAL_DL_TYPE = -1;
	
	private BinaryTree tree;
	private int terminalCounter = 1;
	private int nonTerminalCounter;
	private Map<String, Integer> mapping;
	private int nonTerminaIndex = 1;
	private double[][] matrix;
	private int secondPhaseIndex = 0;
	
	
	public BinaryTreeToDeepLearner(BinaryTree tree){
		this.tree = tree;
	}
	
	public void analyzeBinaryTree(){
		
		//Annotate tree's nodes with leaf and children info
		tree.annotateNodes();
		initNonTerminalCounter();
		
		//Print info
		initMatrix();

		//First PostOrder Visit - Assign IDs and DeepLearner Type
		firstVisit(tree.getRoot());
		
		//Second PostOrder Visit - Generate Matrix
		secondVisit(tree.getRoot());
		
		//Print Tree
		//printTree();
		//System.out.println("Leaves: "+tree.getLeaves().size());
	}
	
	private void initNonTerminalCounter(){
		nonTerminalCounter = 1;
		for(BinaryVertex vertex : tree.vertexSet()){
			if(vertex.isLeaf()){
				nonTerminalCounter++;
			}
		}
	}
	
	
	private void firstVisit(BinaryVertex vertex){
		for(BinaryVertex child : vertex.getChildren()){
			firstVisit(child);
		}
		
		if(vertex.isLeaf()){
			vertex.setID(terminalCounter++);
			vertex.setDLtype(TERMINAL_DL_TYPE);
		} else {
			vertex.setID(nonTerminalCounter++);
			
			Integer DLType = mapping.get(vertex.getNodeType());
			if(DLType == null){
				mapping.put(vertex.getNodeType(), nonTerminaIndex++);
				DLType = nonTerminaIndex;
			}
			vertex.setDLtype(DLType);
		}
	}
	
	
	private void initMatrix(){
		int numberOfNonTerminals = tree.vertexSet().size() - nonTerminalCounter;
		matrix = new double[numberOfNonTerminals+1][];
	}
	
	private void secondVisit(BinaryVertex vertex){
		for(BinaryVertex child : vertex.getChildren()){
			secondVisit(child);
		}
		
		//System.out.println(vertex.getNodeType());
		//System.out.println("Number of Childre: "+vertex.getChildren().size());
		
		if(vertex.isLeaf()){
			return;
		} else {
			BinaryVertex child0 = vertex.getChildren().get(0);
			//System.out.println("Child0: "+child0.getNodeType());
			BinaryVertex child1 = vertex.getChildren().get(1);
			
			matrix[secondPhaseIndex++] = new double[]{child0.getID(), child0.getDLtype(), child1.getID(), child1.getDLtype(), vertex.getID(), vertex.getDLtype()};
		}
	}
	
	
	public double[][] getMatrix(){
		return matrix;
	}

	public Map<String, Integer> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, Integer> mapping) {
		this.mapping = mapping;
	}
	
	
	public void printTree(){
		printTreeRecursive(tree.getRoot());
	}
	
	private void printTreeRecursive(BinaryVertex vertex){
		for(BinaryVertex child : vertex.getChildren()){
			printTreeRecursive(child);
		}
		
		System.out.println("ID: "+vertex.getID()+"; leaf: "+vertex.isLeaf()+"; DLtype: "+vertex.getDLtype()+"; type: "+NonTerminalFrequency.getNodeType(vertex));
	}
}
