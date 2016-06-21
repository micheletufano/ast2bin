package edu.wm.cs.ast2bin.visualization;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class BinaryTreeVisualizer extends JFrame{

	private static final long serialVersionUID = -8301627352019252341L;
	private JTree tree;

	public BinaryTreeVisualizer(BinaryTree bTree){
		this(bTree, "Pruned Binary Tree");
	}

	public BinaryTreeVisualizer(BinaryTree bTree, String title){

		BinaryVertex ASTroot = bTree.getRoot();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(getName(ASTroot));

		List<BinaryVertex> children = bTree.getChildren(ASTroot);

		populateTree(root, children, bTree, ASTroot);

		tree = new JTree(root);

		JPanel p = new JPanel(new BorderLayout());
		JScrollPane sp = new JScrollPane(tree);
		p.add(BorderLayout.CENTER, sp);
		add(p);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(title);       
		this.pack();
		this.setVisible(true);
	}


	private void populateTree(DefaultMutableTreeNode parent, List<BinaryVertex> children, BinaryTree bTree, BinaryVertex parentVertex){
		if(children.size()==0){
			DefaultMutableTreeNode node = null;

			if(parentVertex.getTerminal()==null){
				ASTNode nodeToPrint = parentVertex.getNode();
				if(nodeToPrint != null){
					node = new DefaultMutableTreeNode(nodeToPrint.toString());
					parent.add(node);
				}
			} else {
				node = new DefaultMutableTreeNode(parentVertex.getTerminal().toString());
				parent.add(node);
			}

		}

		for(BinaryVertex c : children){
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(getName(c));
			parent.add(node);
			populateTree(node, bTree.getChildren(c), bTree, c);
		}
	}


	private String getName(BinaryVertex vertex){
		ASTNode node = vertex.getNode();

		if(node != null){

			String name = ""+ASTNode.nodeClassForType(node.getNodeType());
			String info[] = name.split("\\.");
			return info[info.length-1];

		} else {
			return vertex.getType();
		}

	}     
}