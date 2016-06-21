package edu.wm.cs.ast2bin.algorithm;

import java.io.File;
import java.util.Vector;

import edu.wm.cs.ast2bin.algorithm.binary.builder.BinaryTreeBuilder;
import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class Analysis {

	
	public static void run(String projectPath, int javaVersion){
		Vector<File> javaFiles = FileUtility.listJavaFiles(new File(projectPath));

		int total = javaFiles.size();
		int index = 0;
		for(File f : javaFiles){
			System.out.println("File: "+index+++"/"+total);
			
			SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
			Tree tree = analyzer.analyzeSourceFile(f.getAbsolutePath(), javaVersion);
			BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree);
		}
	}
}
