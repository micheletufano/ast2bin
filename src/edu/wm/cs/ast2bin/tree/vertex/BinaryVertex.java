package edu.wm.cs.ast2bin.tree.vertex;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

public class BinaryVertex {
	
	private ASTNode node;
	private ASTNode terminal;
	private String type;
	private BinaryVertex parent;
	private List<BinaryVertex> mergedNodes = new ArrayList<BinaryVertex>();
	private boolean artificialNode = false;
	private boolean leaf = false;
	private List<BinaryVertex> children = new ArrayList<BinaryVertex>();
	private int ID;
	private int DLtype;
	private String nodeType = "";
	
	
	public ASTNode getNode() {
		return node;
	}

	public void setNode(ASTNode node) {
		this.node = node;
	}


	public ASTNode getTerminal() {
		return terminal;
	}

	public void setTerminal(ASTNode terminal) {
		this.terminal = terminal;
	}

	public List<BinaryVertex> getMergedNodes() {
		return mergedNodes;
	}

	public void setMergedNodes(List<BinaryVertex> mergedNodes) {
		this.mergedNodes = mergedNodes;
	}

	
	public void addMergedNode(BinaryVertex node){
		this.mergedNodes.add(node);
	}
	
	public BinaryVertex getParent() {
		return parent;
	}

	public void setParent(BinaryVertex parent) {
		this.parent = parent;
	}

	@Override
	public boolean equals(Object obj) {
		BinaryVertex v = (BinaryVertex) obj;
		ASTNode n = v.getNode();
		
		if(node != null && n != null){
			if(node.equals(n)){
				return true;
			} else{
				return false;
			}
		}
		
		return false;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.artificialNode = true;
		this.type = type;
	}

	public boolean isArtificialNode() {
		return artificialNode;
	}

	public void setArtificialNode(boolean artificialNode) {
		this.artificialNode = artificialNode;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public List<BinaryVertex> getChildren() {
		return children;
	}

	public void setChildren(List<BinaryVertex> children) {
		this.children = children;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getDLtype() {
		return DLtype;
	}

	public void setDLtype(int dLtype) {
		DLtype = dLtype;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
}
