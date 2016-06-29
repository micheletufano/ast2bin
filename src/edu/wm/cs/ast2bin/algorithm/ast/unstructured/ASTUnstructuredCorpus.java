package edu.wm.cs.ast2bin.algorithm.ast.unstructured;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.tree.vertex.Vertex;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class ASTUnstructuredCorpus {

	public static void main(String[] args) {
		ASTUnstructuredCorpus tester = new ASTUnstructuredCorpus();
		String projectPath = "/home/scratch/mtufano/projects/deepLearningCloneDetection/SubjectSystemSource/antlr-4";
		String corpusPath = "/scratch/mtufano.scratch/tmp/corpus.txt";
		String pathsPath = "/scratch/mtufano.scratch/tmp/paths.txt";
		int javaVersion = 4;
		
		tester.generateProjectCorpus(projectPath, corpusPath, pathsPath, javaVersion);
		
	}
	
	
	
	public static void generateProjectCorpus(String projectPath, String corpusPath, String pathsPath, int javaVersion){
		String prefix = extractPathPrefix(projectPath);

		FileWriter fileWriterCorpus = null;
		FileWriter fileWriterPaths = null;
		PrintWriter writerCorpus = null;
		PrintWriter writerPaths = null;

		try {
			fileWriterCorpus = new FileWriter(corpusPath, true);
			writerCorpus = new PrintWriter(fileWriterCorpus);

			fileWriterPaths = new FileWriter(pathsPath, true);
			writerPaths = new PrintWriter(fileWriterPaths);

		} catch (Exception e) {
			e.printStackTrace();
		}	

		Vector<File> javaFiles = FileUtility.listJavaFiles(new File(projectPath));

		int total = javaFiles.size();
		int skippedFiles = 0;
		int index = 0;
		for(File f : javaFiles){
			System.out.println("File: "+index+++"/"+total);

			String fileCorpus = generateFileCorpus(f);
			
			if(fileCorpus.isEmpty()){
				skippedFiles++;
				continue;
			} else{
				writerCorpus.println(fileCorpus);
				writerCorpus.flush();
				printPath(f, prefix, writerPaths);	
			}
		}

		System.out.println("Skipped files: "+skippedFiles);

		//Close files
		writerCorpus.flush();
		writerCorpus.close();

		writerPaths.flush();
		writerPaths.close();
	}
	
	
	public static String generateFileCorpus(File file){
		
		SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
		Tree tree = analyzer.analyzeSourceFile(file.getAbsolutePath(), 4);
		
		if(tree.vertexSet().size()!=0){
			String corpus = extractASTCorpus(tree);
			return corpus;
		} else {
			System.out.println("************* Empty file ****************");
			System.out.println(file);
			return "";
		}
	}
	
	private static String extractASTCorpus(Tree tree){
		String corpus = "";
		
		PreOrderTraversal visitor = new PreOrderTraversal();
		List<Vertex> vertices = visitor.preOrderTraversal(tree);
		
		for(Vertex vertexNode : vertices){
            corpus += vertexNode.getName() + " ";
			//System.out.println( vertexNode.getName() + " - " + vertexNode.getNode().toString());
		}
		corpus.trim();
		
		return corpus;
	}
	
	
	
	
	
	private static String extractPathPrefix(String fullPath) {
		String[] dirs = fullPath.split("/");
		String prefix = "";

		boolean first = true;
		for(int i=0; i<dirs.length-1; i++){
			if(first){
				first = false;
				prefix += dirs[i];
			} else {
				prefix += "/"+dirs[i];
			}
		}
		prefix += "/";

		System.out.println("Prefix: "+prefix);
		return prefix;
	}
	
	private static void printPath(File f, String pathPrefix, PrintWriter writerPaths){
		String toPrint = f.getAbsolutePath().replace(pathPrefix, "");
		writerPaths.println(toPrint);
		writerPaths.flush();
	}

}
