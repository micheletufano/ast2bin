package edu.wm.cs.ast2bin.algorithm.ast.unstructured;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.tree.vertex.Vertex;

public class PreOrderTraversal {
 
	private List<Vertex> orderedVertices;
	
	
	public List<Vertex> preOrderTraversal(Tree tree){
		orderedVertices = new ArrayList<Vertex>();
		
		preOrderVisit(tree.getRoot(), tree);
		return orderedVertices;
	}
	
	
	
	private void preOrderVisit(Vertex v, Tree t){
		orderedVertices.add(v);
		
		List<Vertex> children = t.getChildren(v);
		
		for(Vertex vv : children){
			preOrderVisit(vv, t);
		}
	}


}
