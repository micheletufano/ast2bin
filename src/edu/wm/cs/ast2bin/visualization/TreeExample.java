package edu.wm.cs.ast2bin.visualization;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeExample extends JFrame{

	private static final long serialVersionUID = -8923838005955043156L;
	private JTree tree;
	public TreeExample(){
		//create the root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		//create the child nodes
		DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
		DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");

		//add the child nodes to the root node
		root.add(vegetableNode);
		root.add(fruitNode);

		//create the tree by passing in the root node
		tree = new JTree(root);
		add(tree);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("JTree Example");       
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TreeExample();
			}
		});
	}       
}