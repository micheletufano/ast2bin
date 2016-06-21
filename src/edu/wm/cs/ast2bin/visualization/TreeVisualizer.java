package edu.wm.cs.ast2bin.visualization;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.tree.vertex.Vertex;

public class TreeVisualizer extends JFrame{
	
	private static final long serialVersionUID = -8301627352019252341L;
	private JTree tree;
	
	
	public TreeVisualizer(Tree ASTtree){
		
		Vertex ASTroot = ASTtree.getRoot();
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(getName(ASTroot));
		
		List<Vertex> children = ASTtree.getChildren(ASTroot);
		
		populateTree(root, children, ASTtree, ASTroot);

		tree = new JTree(root);

		JPanel p = new JPanel(new BorderLayout());
		JScrollPane sp = new JScrollPane(tree);
		p.add(BorderLayout.CENTER, sp);
		add(p);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Original AST Tree");       
		this.pack();
		this.setVisible(true);
	}
	
	
	private void populateTree(DefaultMutableTreeNode parent, List<Vertex> children, Tree ASTtree, Vertex parentVertex){
		if(children.size()==0){
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(parentVertex.getNode().toString());
			parent.add(node);
		}
		
		for(Vertex c : children){
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(getName(c));
			parent.add(node);
			populateTree(node, ASTtree.getChildren(c), ASTtree, c);
		}
	}
	
	
	private String getName(Vertex vertex){
		ASTNode node = vertex.getNode();
		
		String name = ""+ASTNode.nodeClassForType(node.getNodeType());
		String info[] = name.split("\\.");

		return info[info.length-1];
	}     
}