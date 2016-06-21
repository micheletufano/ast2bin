package edu.wm.cs.ast2bin.statistics;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.wm.cs.ast2bin.algorithm.binary.builder.BinaryTreeBuilder;
import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class RootStats {

	public static void main(String[] args) {
		RootStats stats = new RootStats();
		//stats.computeRootStats("/home/scratch/mtufano/projects/deepLearningCloneDetection/SubjectSystemSource/antlr-4/", true);
		//stats.computeRootStats("/home/scratch/mtufano/projects/deepLearningCloneDetection/SubjectSystemSource/apache-ant-1.9.6/", true);
		//stats.computeRootStats("/home/scratch/mtufano/projects/deepLearningCloneDetection/SubjectSystemSource/argouml/", true);
		//stats.computeRootStats("/home/scratch/mtufano/projects/deepLearningCloneDetection/SubjectSystemSource/carol-2.0.5-src/", true);
		//stats.computeRootStats("/home/scratch/mtufano/projects/deepLearningCloneDetection/SubjectSystemSource/dnsjava-code/", true);
		//stats.computeRootStats("/home/scratch/mtufano/projects/deepLearningCloneDetection/SubjectSystemSource/hibernate-2.1/", true);
		//stats.computeRootStats("/home/scratch/mtufano/projects/deepLearningCloneDetection/SubjectSystemSource/J2SDK_1.4.2_19/", true);
		stats.computeRootStats("/home/scratch/mtufano/projects/deepLearningCloneDetection/SubjectSystemSource/jhotdraw-svn/", true);
		
		stats.printStats();
	}
	
	
	private HashMap<String, Integer> roots;
	
	
	public RootStats(){
		roots = new HashMap<>();
	}
	


	public void computeRootStats(String projectPath, boolean mergingLiterals){

		int javaVersion = 4;
		Vector<File> javaFiles = FileUtility.listJavaFiles(new File(projectPath));

		int total = javaFiles.size();
		int index = 0;
		for(File f : javaFiles){
			System.out.println("File: "+index+++"/"+total);

			SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
			Tree tree = analyzer.analyzeSourceFile(f.getAbsolutePath(), javaVersion);

			if(tree.vertexSet().size()==0){
				System.out.println("************* Missing file ****************");
				System.out.println(f);
				continue;
			}

			BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree, mergingLiterals, false);
			bTree.annotateNodes();

			addRoot(bTree);

		}

	}

	private void addRoot(BinaryTree bTree){
		ASTNode root = bTree.getRoot().getNode();
		String type = ASTNode.nodeClassForType(root.getNodeType()).toString();
		
		int occurrence = 1;
		if(roots.containsKey(type)){
			occurrence = roots.get(type)+1;
		}
		
		roots.put(type, occurrence);
	}
	
	public void printStats(){
		for(Entry<String, Integer> e : roots.entrySet()){
			System.out.println(e.getKey()+" : "+e.getValue());
		}
	}

}
