package edu.wm.cs.ast2bin.algorithm;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.wm.cs.ast2bin.algorithm.binary.builder.BinaryTreeBuilder;
import edu.wm.cs.ast2bin.ast.ASTNodeVisitor;
import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;
import edu.wm.cs.ast2bin.tree.vertex.Vertex;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class NonTerminalFrequency {


	public static void calculateFrequencyDistribution(String projectPath, String workingDirectory, int javaVersion){
		Vector<File> javaFiles = FileUtility.listJavaFiles(new File(projectPath));

		int total = javaFiles.size();
		int index = 0;
		for(File f : javaFiles){
			System.out.println("File: "+index+++"/"+total);

			SourceFileAnalyzer analyzer = new SourceFileAnalyzer();

			Tree tree = analyzer.analyzeSourceFile(f.getAbsolutePath(), javaVersion);

			for(Vertex v : tree.vertexSet()){
				if(!tree.isLeaf(v)){
					List<Vertex> children = tree.getChildren(v);
					
					if(children.size()>2){
						printNonTerminal(v, children, workingDirectory);
					}
				}
			}

		}
	}

	
	private static void printNonTerminal(Vertex v, List<Vertex> children, String workingDirectory){
		String filePath = workingDirectory+ASTNodeVisitor.getASTNodeType(v.getNode());			
		File file = new File(filePath);
		
		String childrenTypes = "";
		boolean first = true;
		for(Vertex c : children){
			if(first){
				childrenTypes += ASTNodeVisitor.getASTNodeType(c.getNode());
				first = false;
			} else {
				childrenTypes += ","+ASTNodeVisitor.getASTNodeType(c.getNode());
			}
		}
		
		FileUtility.appendToFile(file, childrenTypes);
	}
	
	
	
	
	
	public static void calculateFrequencyDistributionBinary(String projectPath, String workingDirectory, boolean position, int javaVersion){
		Vector<File> javaFiles = FileUtility.listJavaFiles(new File(projectPath));

		int total = javaFiles.size();
		int index = 0;
		for(File f : javaFiles){
			System.out.println("File: "+index+++"/"+total);
			System.out.println("File: "+f.getAbsolutePath());
			
			SourceFileAnalyzer analyzer = new SourceFileAnalyzer();

			Tree tree = analyzer.analyzeSourceFile(f.getAbsolutePath(), javaVersion);
			
			if(tree.vertexSet().size()==0){
				continue;
			}
			
			BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree);
			
			for(BinaryVertex v : bTree.vertexSet()){
				if(!bTree.isLeaf(v)){
					List<BinaryVertex> children = bTree.getChildren(v);
					
					if(children.size()>2 || children.size()==1){
						printNonTerminal(v, children, workingDirectory, f, position, javaVersion);
					}
				}
			}

		}
	}

	
	private static void printNonTerminal(BinaryVertex v, List<BinaryVertex> children, String workingDirectory, File sourceCodefile, boolean position, int javaVersion){
		String filePath = workingDirectory + getNodeType(v);
		File file = new File(filePath);
		
		String childrenTypes = "";
		boolean first = true;
		for(BinaryVertex c : children){
			if(first){
				childrenTypes += getNodeType(c);
				first = false;
			} else {
				childrenTypes += ","+getNodeType(c);
			}
		}
		
		if(position){
			String positionInfo = getNodePosition(sourceCodefile, v.getNode(), javaVersion);
			childrenTypes += ","+positionInfo;
		}
		
		FileUtility.appendToFile(file, childrenTypes);
	}
	
	public static String getNodeType(BinaryVertex v){
		if(v.getNode() != null){
			return ASTNodeVisitor.getASTNodeType(v.getNode());
		} else {
			return v.getType();
		}
	}
	
	private static String getNodePosition(File file, ASTNode node, int javaVersion){
		CompilationUnit cu = SourceFileAnalyzer.extractCompilationUnit(file.getAbsolutePath(), javaVersion);
		
		int lineNumber = cu.getLineNumber(node.getStartPosition());
		
		String info = file.getAbsolutePath()+":"+lineNumber;
		
		return info;
	}
	
	
	

}
