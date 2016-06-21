package edu.wm.cs.ast2bin.algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.wm.cs.ast2bin.algorithm.binary.builder.BinaryTreeBuilder;
import edu.wm.cs.ast2bin.ast.ASTNodeVisitor;
import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;
import edu.wm.cs.ast2bin.tree.vertex.Vertex;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class GenerateTrainingCorpus {

	public static final boolean LEAVES_LIMIT = true;
	public static final int LEAVES_LIMIT_TRESHOLD = 4000;

	public static void generateTrainingCorpus(String projectPath, String corpusPath, String pathsPath, boolean mergingLiterals, int javaVersion){
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
			List<BinaryVertex> leafVertices = bTree.getLeaves();
			List<ASTNode> leaves = getLeafNodes(leafVertices);

			String lexemsCorpus = LexemsFileCreator.extractLexemsCorpus(leaves);

			writerCorpus.println(lexemsCorpus);
			writerCorpus.flush();

			printPath(f, prefix, writerPaths);	
		}


		//Close files
		writerCorpus.flush();
		writerCorpus.close();

		writerPaths.flush();
		writerPaths.close();
	}



	public static void generateTrainingCorpusWithAnnotation(String projectPath, String corpusPath, String pathsPath, boolean mergingLiterals, int javaVersion){
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

			SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
			Tree tree = analyzer.analyzeSourceFile(f.getAbsolutePath(), javaVersion);

			if(tree.vertexSet().size()==0){
				System.out.println("************* Missing file ****************");
				skippedFiles++;
				System.out.println(f);
				continue;
			}

			BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree, mergingLiterals, false);
			bTree.annotateNodes();
			List<BinaryVertex> leafVertices = bTree.getLeaves();

			if(LEAVES_LIMIT){
				if(leafVertices.size() > LEAVES_LIMIT_TRESHOLD){
					skippedFiles++;
					continue;
				}
			}

			bTree.annotateNodes();
			//List<ASTNode> leaves = getLeafNodes(leafVertices);

			String lexemsCorpus = LexemsFileCreatorAnnotation.extractLexemsCorpus(leafVertices);

			writerCorpus.println(lexemsCorpus);
			writerCorpus.flush();

			printPath(f, prefix, writerPaths);	
		}

		System.out.println("Skipped files: "+skippedFiles);


		//Close files
		writerCorpus.flush();
		writerCorpus.close();

		writerPaths.flush();
		writerPaths.close();
	}




	public static int getNumberofLeaves(String javaFile){
		int javaVersion = 4;
		File f = new File(javaFile);

		SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
		Tree tree = analyzer.analyzeSourceFile(f.getAbsolutePath(), javaVersion);

		BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree, true, false);
		bTree.annotateNodes();
		List<BinaryVertex> leafVertices = bTree.getLeaves();

		return leafVertices.size();
	}


	public static void generateMethodsTrainingCorpus(String projectPath, String corpusPath, String pathsPath, boolean mergingLiterals, int javaVersion, int minLen, int maxLen){
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

			SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
			List<Tree> methodTrees = analyzer.analyzeMethodsSourceFile(f.getAbsolutePath(), javaVersion, f, prefix, writerPaths, minLen, maxLen);

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
				BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree, mergingLiterals, false);
				bTree.annotateNodes();
				List<BinaryVertex> leafVertices = bTree.getLeaves();

				bTree.annotateNodes();
				//List<ASTNode> leaves = getLeafNodes(leafVertices);

				String lexemsCorpus = LexemsFileCreatorAnnotation.extractLexemsCorpus(leafVertices);

				writerCorpus.println(lexemsCorpus);
				writerCorpus.flush();
			}

		}

		System.out.println("Skipped files: "+skippedFiles);


		//Close files
		writerCorpus.flush();
		writerCorpus.close();

		writerPaths.flush();
		writerPaths.close();
	}





	public static void generateTrainingCorpusForMutant(String projectPath, String corpusPath, String pathsPath, String reportFile, boolean mergingLiterals, int javaVersion){
		String prefix = extractPathPrefix(projectPath);
		String reportOutput = "file,syntaxError,error\n";

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

			SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
			Tree tree = analyzer.analyzeSourceFile(f.getAbsolutePath(), javaVersion);

			if(tree.vertexSet().size()==0){
				System.out.println("************* Missing file ****************");
				skippedFiles++;
				System.out.println(f);
				continue;
			}

			BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree, mergingLiterals, false);
			bTree.annotateNodes();
			List<BinaryVertex> leafVertices = bTree.getLeaves();

			/*
			if(LEAVES_LIMIT){
				if(leafVertices.size() > LEAVES_LIMIT_TRESHOLD){
					skippedFiles++;
					continue;
				}
			}
			 */

			bTree.annotateNodes();
			//List<ASTNode> leaves = getLeafNodes(leafVertices);

			String lexemsCorpus = LexemsFileCreatorAnnotation.extractLexemsCorpus(leafVertices);

			writerCorpus.println(lexemsCorpus);
			writerCorpus.flush();

			printPath(f, prefix, writerPaths);

			//Error Report
			boolean syntaxError = containsErrors(f, true);
			boolean anyError = containsErrors(f, false);
			String errorLine = f.getAbsolutePath()+","+syntaxError+","+anyError+"\n";
			reportOutput += errorLine;

		}

		System.out.println("Skipped files: "+skippedFiles);

		FileUtility.writeFile(reportOutput, reportFile);

		//Close files
		writerCorpus.flush();
		writerCorpus.close();

		writerPaths.flush();
		writerPaths.close();


	}





	private static boolean containsErrors(File f, boolean syntax){
		String content = "";

		try {
			content = FileUtility.readFileToString(f.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ASTNodeVisitor.containsError(content, syntax);
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

	private static List<ASTNode> getLeafNodes(List<BinaryVertex> leafVertices){
		List<ASTNode> leafNodes = new ArrayList<ASTNode>();

		for(BinaryVertex vertex : leafVertices){
			if(vertex.getTerminal() != null){
				leafNodes.add(vertex.getTerminal());
			} else if(vertex.getNode() != null){
				leafNodes.add(vertex.getNode());
			}
		}

		return leafNodes;
	}
}
