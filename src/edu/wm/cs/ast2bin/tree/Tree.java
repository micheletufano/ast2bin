package edu.wm.cs.ast2bin.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.DefaultDirectedGraph;

import edu.wm.cs.ast2bin.tree.vertex.Vertex;

public class Tree extends DefaultDirectedGraph<Vertex, DefaultEdge>{

	private static final long serialVersionUID = -3702065248472850686L;
	private Vertex root;
	
	
	public Tree(Class<? extends DefaultEdge> edgeClass) {
		super(edgeClass);
	}
	
	
	public Vertex getRoot() {
		return root;
	}

	public void setRoot(Vertex root) {
		this.root = root;
	}
	
	public List<Vertex> getChildren(Vertex v){
		List<Vertex> children = new ArrayList<Vertex>();
		Set<DefaultEdge> childEdges = incomingEdgesOf(v);
		
		for(DefaultEdge e : childEdges){
			children.add(getEdgeSource(e));
		}
		
		return children;
	}
	
	public boolean isLeaf(Vertex v){
		return getChildren(v).size() == 0;
	}

}
