package edu.wm.cs.ast2bin.statistics;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.wm.cs.ast2bin.algorithm.NonTerminalFrequency;
import edu.wm.cs.ast2bin.algorithm.binary.builder.BinaryTreeBuilder;
import edu.wm.cs.ast2bin.ast.ASTNodeVisitor;
import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;
import edu.wm.cs.ast2bin.tree.vertex.Vertex;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class TreeStatistics {

	//AST stats files
	private String ASTterminal;
	private String ASTNonTerminal;
	private String ASTNonTerminalMoreThan2;
	private String ASTNonTerminal1Child;
	private String ASTUniqueNonTerminal;
	private String ASTChildren;
	private String ASTChildrenMoreThan2;
	private String ASTMaxDepth;

	//Full Binary Tree stats files
	private String FBTTerminal;
	private String FBTNonTerminal;
	private String FBTUniqueNonTerminal;
	private String FBTArtificialNonTerminal;
	private String FBTMaxDepth;

	private int ASTMaxDepthValue = 0;
	private int FBTMaxDepthValue = 0;


	public void computeTreeStatistics(String projectPath, String statsDir){
		int javaVersion = 4;

		initOutputFiles(statsDir);

		Vector<File> javaFiles = FileUtility.listJavaFiles(new File(projectPath));

		int total = javaFiles.size();
		int index = 0;
		for(File f : javaFiles){
			System.out.println("File: "+index+++"/"+total);

			SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
			Tree tree = analyzer.analyzeSourceFile(f.getAbsolutePath(), javaVersion);

			//AST Stats
			computeASTStatistics(tree);
			
			if(tree.vertexSet().size()==0){
				System.out.println("************* Missing file ****************");
				System.out.println(f);
				continue;
			}

			BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree, true, false);

			//FBT Stats
			computeFTBStatistics(bTree);
			
		}
	}


	private void initOutputFiles(String statsDir){
		//AST stats files
		ASTterminal = statsDir+"ASTterminal";
		ASTNonTerminal = statsDir+"ASTNonTerminal";
		ASTNonTerminalMoreThan2 = statsDir+"ASTNonTerminalMoreThan2";
		ASTNonTerminal1Child = statsDir+"ASTNonTerminal1Child";
		ASTUniqueNonTerminal = statsDir+"ASTUniqueNonTerminal";
		ASTChildren = statsDir+"ASTChildren";
		ASTChildrenMoreThan2 = statsDir+"ASTChildrenMoreThan2";
		ASTMaxDepth = statsDir+"ASTMaxDepth";

		//Full Binary Tree stats files
		FBTTerminal = statsDir+"FBTTerminal";
		FBTNonTerminal = statsDir+"FBTNonTerminal";
		FBTUniqueNonTerminal = statsDir+"FBTUniqueNonTerminal";
		FBTArtificialNonTerminal = statsDir+"FBTArtificialNonTerminal";
		FBTMaxDepth = statsDir+"FBTMaxDepth";
	}


	private void computeASTStatistics(Tree tree){
		int numASTterminal = 0;
		int nonTermina1Child = 0;
		List<Integer> totalChildren = new ArrayList<Integer>();
		List<Integer> childrenMoreThan2 = new ArrayList<Integer>();
		Map<String, Integer> nonTerminals = new HashMap<String, Integer>();

		for(Vertex v : tree.vertexSet()){
			int numChildren = tree.getChildren(v).size();

			if(numChildren == 0){
				numASTterminal++;
			} else {
				String type = ASTNodeVisitor.getASTNodeType(v.getNode());
				Integer instances = nonTerminals.get(type);
				int newInstances = 1;

				if(instances != null){
					newInstances = instances+1;
				}

				nonTerminals.put(type, newInstances);

				if(numChildren > 2){
					childrenMoreThan2.add(numChildren);
				} else if(numChildren == 1){
					nonTermina1Child++;
				}
				totalChildren.add(numChildren);
			}
		}
		
		int maxDepth = comuteASTMaxDepth(tree);
		String totalChildrenToPrint = "";
		for(Integer val : totalChildren){
			totalChildrenToPrint += val+"\n";
		}
		String childrenMoreThan2ToPrint = "";
		for(Integer val : childrenMoreThan2){
			childrenMoreThan2ToPrint += val+"\n";
		}
		
		//Print values
		FileUtility.appendToFile(new File(ASTterminal), ""+numASTterminal);
		FileUtility.appendToFile(new File(ASTNonTerminal), ""+totalChildren.size());
		FileUtility.appendToFile(new File(ASTNonTerminalMoreThan2), ""+childrenMoreThan2.size());
		FileUtility.appendToFile(new File(ASTNonTerminal1Child), ""+nonTermina1Child);
		FileUtility.appendToFile(new File(ASTUniqueNonTerminal), ""+nonTerminals.keySet().size());
		FileUtility.appendToFile(new File(ASTChildren), totalChildrenToPrint);
		FileUtility.appendToFile(new File(ASTChildrenMoreThan2), childrenMoreThan2ToPrint);
		FileUtility.appendToFile(new File(ASTMaxDepth), ""+maxDepth);
	}

	
	private void computeFTBStatistics(BinaryTree tree){
		tree.annotateNodes();
		
		int numTerminals = 0;
		Map<String, Integer> nonTerminals = new HashMap<String, Integer>();
		int numArtificialNonTerminals = 0;

		
		for(BinaryVertex v : tree.vertexSet()){
			if(v.isLeaf()){
				numTerminals++;
			} else {
				String type = NonTerminalFrequency.getNodeType(v);
				Integer instances = nonTerminals.get(type);
				int newInstances = 1;

				if(instances != null){
					newInstances = instances+1;
				}

				nonTerminals.put(type, newInstances);

				if(v.isArtificialNode()){
					numArtificialNonTerminals++;
				}
			}
		}
		
		int maxDepht = comuteFBTMaxDepth(tree);
		int numNonTerminal = 0;
		for(Integer val : nonTerminals.values()){
			numNonTerminal += val;
		}
		
		//Print values
		FileUtility.appendToFile(new File(FBTTerminal), ""+numTerminals);
		FileUtility.appendToFile(new File(FBTNonTerminal), ""+numNonTerminal);
		FileUtility.appendToFile(new File(FBTUniqueNonTerminal), ""+nonTerminals.keySet().size());
		FileUtility.appendToFile(new File(FBTArtificialNonTerminal), ""+numArtificialNonTerminals);
		FileUtility.appendToFile(new File(FBTMaxDepth), ""+maxDepht);
		
	}
	
	
	
	
	private int comuteASTMaxDepth(Tree tree){
		ASTMaxDepthValue = 0;
		depthASTVisit(tree.getRoot(), 0, tree);
		
		return ASTMaxDepthValue;
	}

	
	private void depthASTVisit(Vertex v, int level, Tree tree){
		for(Vertex c : tree.getChildren(v)){
			depthASTVisit(c, level+1, tree);
		}
		
		if(level > ASTMaxDepthValue){
			ASTMaxDepthValue = level;
		}
	}
	
	
	private int comuteFBTMaxDepth(BinaryTree tree){
		FBTMaxDepthValue = 0;
		deptFBTVisit(tree.getRoot(), 0, tree);
		
		return FBTMaxDepthValue;
	}

	
	private void deptFBTVisit(BinaryVertex v, int level, BinaryTree tree){
		for(BinaryVertex c : v.getChildren()){
			deptFBTVisit(c, level+1, tree);
		}
		
		if(level > FBTMaxDepthValue){
			FBTMaxDepthValue = level;
		}
	}
	

}
