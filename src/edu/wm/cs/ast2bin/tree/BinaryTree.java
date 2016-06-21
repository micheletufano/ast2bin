package edu.wm.cs.ast2bin.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.DefaultDirectedGraph;

import edu.wm.cs.ast2bin.algorithm.NonTerminalFrequency;
import edu.wm.cs.ast2bin.ast.ASTNodeVisitor;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class BinaryTree extends DefaultDirectedGraph<BinaryVertex, DefaultEdge>{

	private static final long serialVersionUID = -3702065248472850686L;
	private BinaryVertex root;
	
	
	public BinaryTree(Class<? extends DefaultEdge> edgeClass) {
		super(edgeClass);
	}
	
	
	public BinaryVertex getRoot() {
		return root;
	}

	public void setRoot(BinaryVertex root) {
		this.root = root;
	}
	
	public List<BinaryVertex> getChildren(BinaryVertex v){
		List<BinaryVertex> children = new ArrayList<BinaryVertex>();
		Set<DefaultEdge> childEdges = incomingEdgesOf(v);
		
		for(DefaultEdge e : childEdges){
			children.add(getEdgeSource(e));
		}
		
		return children;
	}
	
	public boolean isLeaf(BinaryVertex v){
		return getChildren(v).size() == 0;
	}
	
	
	public void annotateNodes(){
		for(BinaryVertex vertex : vertexSet()){
			vertex.setLeaf(this.isLeaf(vertex));
			vertex.setChildren(this.getChildren(vertex));
			vertex.setNodeType(NonTerminalFrequency.getNodeType(vertex));
		}
	}

	public List<BinaryVertex> getLeaves(){
		List<BinaryVertex> leaves = new ArrayList<BinaryVertex>();
		
		getLeaves(getRoot(), leaves);
		
		return leaves;
	}
	
	
	private void getLeaves(BinaryVertex vertex, List<BinaryVertex> leaves){
		for(BinaryVertex child : vertex.getChildren()){
			getLeaves(child, leaves);
		}
		
		if(vertex.isLeaf()){
			leaves.add(vertex);
		}
	}
}
