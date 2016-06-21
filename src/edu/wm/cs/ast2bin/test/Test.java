package edu.wm.cs.ast2bin.test;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.wm.cs.ast2bin.algorithm.CheckTerminalTypes;
import edu.wm.cs.ast2bin.algorithm.LexemsFileCreator;
import edu.wm.cs.ast2bin.algorithm.binary.builder.BinaryTreeBuilder;
import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.utility.FileUtility;
import edu.wm.cs.ast2bin.visualization.BinaryTreeVisualizer;
import edu.wm.cs.ast2bin.visualization.TreeVisualizer;

public class Test {

	public static void main(String[] args) {
		
		String filePath = "/home/michele/Desktop/Test.java";
		int javaVersion = 4;
			
		SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
		Tree tree = analyzer.analyzeSourceFile(filePath, javaVersion);
		String lexems = analyzer.getLexems();
		List<ASTNode> leaves = analyzer.getNodes();

		LexemsFileCreator.createLexemsFile(leaves, "/home/michele/Desktop/lexems");
		
		TreeVisualizer tv = new TreeVisualizer(tree);
		
		BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree);
		
		BinaryTreeVisualizer bTv = new BinaryTreeVisualizer(bTree);
		
		FileUtility.writeFile(lexems, "/home/michele/Desktop/Test.lexems");
		
		CheckTerminalTypes checker = new CheckTerminalTypes();
		
		
		
		checker.checkSourceFile("/home/michele/Desktop/Test.java", javaVersion);
		checker.printAllLeaves("/home/michele/Desktop/allLeaves");
		checker.printNotRecognizedLeaves("/home/michele/Desktop/notRecognizedLeaves");
	}

}
