package edu.wm.cs.ast2bin.algorithm.deep.learner;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.wm.cs.ast2bin.algorithm.binary.builder.BinaryTreeBuilder;
import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class ASTTraningCorpus {
	public static final boolean LEAVES_LIMIT = false;
	public static final int LEAVES_LIMIT_TRESHOLD = 4000;
	public static final int NODES_LIMIT_TRESHOLD = LEAVES_LIMIT_TRESHOLD*2;

	public static void generateCorpus(String sourceFilePath, String outputMATFile, boolean merging, int javaVersion){
		
		Vector<File> javaFiles = FileUtility.listJavaFiles(new File(sourceFilePath));
		List<Object> matrices = new ArrayList<Object>();
		Map<String, Integer> mapping = new LinkedHashMap<String, Integer>();
		String filesPaths = "";
		
		int total = javaFiles.size();
		int skippedFiles = 0;
		int index = 0;
		for(File f : javaFiles){
			System.out.println("File: "+index+++"/"+total);
			filesPaths += f.getPath()+"\n";
			
			SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
			Tree tree = analyzer.analyzeSourceFile(f.getAbsolutePath(), javaVersion);
			
			if(tree.vertexSet().size()==0){
				skippedFiles++;
				continue;
			}
			
			BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree, merging, false);
			
			if(LEAVES_LIMIT){
				if(bTree.vertexSet().size() > NODES_LIMIT_TRESHOLD){
					skippedFiles++;
					continue;
				}
			}
			
			//Generate matrix
			BinaryTreeToDeepLearner BTanalyzer = new BinaryTreeToDeepLearner(bTree);
			BTanalyzer.setMapping(mapping);
			BTanalyzer.analyzeBinaryTree();
			mapping = BTanalyzer.getMapping();
			matrices.add(BTanalyzer.getMatrix());
		}
		
		System.out.println("Skipped files:" + skippedFiles);
		
		MATFileExporter.exportData(matrices, mapping.keySet(), outputMATFile);
		FileUtility.writeFile(filesPaths, outputMATFile+".paths");
	}
	
	
	public static void generateMethodCorpus(String sourceFilePath, String outputMATFile, boolean merging, int javaVersion, int minLen, int maxLen){
		
		Vector<File> javaFiles = FileUtility.listJavaFiles(new File(sourceFilePath));
		List<Object> matrices = new ArrayList<Object>();
		Map<String, Integer> mapping = new LinkedHashMap<String, Integer>();
		String filesPaths = "";
		
		int total = javaFiles.size();
		int skippedFiles = 0;
		int index = 0;
		for(File f : javaFiles){
			System.out.println("File: "+index+++"/"+total);
			filesPaths += f.getPath()+"\n";
			
			SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
			List<Tree> methodTrees = analyzer.analyzeMethodsSourceFile(f.getAbsolutePath(), javaVersion, f, null, null, minLen, maxLen);
			//find empty methods
			List<Tree> emptyMethods = new ArrayList<>();
			for(Tree tree : methodTrees){
				if(tree.vertexSet().size()==0){
					System.out.println("************* Missing method ****************");
					skippedFiles++;
					emptyMethods.add(tree);
				}
			}
			
			//Remove empty methods
			for(Tree tree : emptyMethods){
				methodTrees.remove(tree);
			}
			
			
			//Generate corpus for methods
			for(Tree tree : methodTrees){
				BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree, merging, false);
				bTree.annotateNodes();

				//List<ASTNode> leaves = getLeafNodes(leafVertices);

				//Generate matrix
				BinaryTreeToDeepLearner BTanalyzer = new BinaryTreeToDeepLearner(bTree);
				BTanalyzer.setMapping(mapping);
				BTanalyzer.analyzeBinaryTree();
				mapping = BTanalyzer.getMapping();
				matrices.add(BTanalyzer.getMatrix());
			}

		}
		
		System.out.println("Skipped files:" + skippedFiles);
		
		MATFileExporter.exportData(matrices, mapping.keySet(), outputMATFile);
		//FileUtility.writeFile(filesPaths, outputMATFile+".paths");
	}
}
