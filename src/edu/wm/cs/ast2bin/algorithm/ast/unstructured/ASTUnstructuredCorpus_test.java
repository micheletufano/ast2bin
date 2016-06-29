package edu.wm.cs.ast2bin.algorithm.ast.unstructured;

import java.io.File;
import java.util.List;

import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.tree.vertex.Vertex;
import edu.wm.cs.ast2bin.visualization.TreeVisualizer;

public class ASTUnstructuredCorpus_test {

	public static void main(String[] args) {
		ASTUnstructuredCorpus_test tester = new ASTUnstructuredCorpus_test();
		File file = new File("/home/scratch/mtufano/workspaces/java/ASTClone/AST2BIN/Case4_Test/IfStatement_Test.java");
		tester.analyzeFile(file);
	}
	
	
	public static void analyzeFile(File file){
		
		SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
		Tree tree = analyzer.analyzeSourceFile(file.getAbsolutePath(), 4);
		extractASTCorpus(tree);
		TreeVisualizer tv = new TreeVisualizer(tree);
	}
	
	private static void extractASTCorpus(Tree tree){
		String corpus = "";
		
		PreOrderTraversal visitor = new PreOrderTraversal();
		
		List<Vertex> vertices = visitor.preOrderTraversal(tree);
		
		for(Vertex vertexNode : vertices){
            corpus += vertexNode.getName() + " ";
			System.out.println( vertexNode.getName() + " - " + vertexNode.getNode().toString());
		}
		
		corpus.trim();
		System.out.println(corpus);
		
	}
}
