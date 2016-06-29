package edu.wm.cs.ast2bin.tree.vertex;

import org.eclipse.jdt.core.dom.ASTNode;

public class Vertex {
	
	private ASTNode node;
	
	public ASTNode getNode() {
		return node;
	}

	public void setNode(ASTNode node) {
		this.node = node;
	}


	@Override
	public boolean equals(Object obj) {
		Vertex v = (Vertex) obj;
		ASTNode n = v.getNode();
		
		if(node.equals(n)){
			return true;
		} else{
			return false;
		}
	}

	
	public String getName(){		
		String name = ""+ASTNode.nodeClassForType(node.getNodeType());
		String info[] = name.split("\\.");

		return info[info.length-1];
	} 
}
