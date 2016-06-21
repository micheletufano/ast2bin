package edu.wm.cs.ast2bin.algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeLiteral;

import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class CheckTerminalTypes {
	
	private List<ASTNode> allLeaves = new ArrayList<ASTNode>();
	private List<ASTNode> leavesNotRecognized = new ArrayList<ASTNode>();
	private PrintWriter writerAll;
	private PrintWriter writerNotRecognized;
	private List<String> allLeavesNames = new ArrayList<String>();
	private List<String> leavesNotRecognizedNames = new ArrayList<String>();
	private String special;
	private String specialContent = "";
	
	public void checkProject(String sourceFilePath, String allLeavesOutput, String notRecognizedLeavesOutput, String special, int javaVersion){
		FileWriter fileWriterAll = null;
		FileWriter fileWriterNotRecognized = null;
		this.special = special;
		
		try {
			fileWriterAll = new FileWriter(allLeavesOutput, true);
			writerAll = new PrintWriter(fileWriterAll);
			
			fileWriterNotRecognized = new FileWriter(notRecognizedLeavesOutput, true);
			writerNotRecognized = new PrintWriter(fileWriterNotRecognized);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		Vector<File> javaFiles = FileUtility.listJavaFiles(new File(sourceFilePath));
		
		int total = javaFiles.size();
		int index = 0;
		for(File f : javaFiles){
			System.out.println("File: "+index+++"/"+total);
			checkSourceFile(f.getAbsolutePath(), javaVersion);
		}
		
		
		
		writerNotRecognized.flush();
		writerNotRecognized.close();
		
		writerAll.flush();
		writerAll.close();
		FileUtility.writeFile(specialContent, special);
	}
	
	
	public void checkSourceFile(String sourceFilePath, int javaVersion){
		List<ASTNode> nodes = SourceFileAnalyzer.extractAllASTNodes(sourceFilePath, javaVersion);
		List<ASTNode> leaves = new ArrayList<ASTNode>(nodes);

		for(ASTNode node : nodes){
			leaves.remove(node.getParent());
		}
		
		
		for(ASTNode leaf : leaves){
			if(		   leaf instanceof SimpleName
					|| leaf instanceof BooleanLiteral	
					|| leaf instanceof StringLiteral
					|| leaf instanceof CharacterLiteral
					|| leaf instanceof NumberLiteral
					|| leaf instanceof NullLiteral
					|| leaf instanceof TypeLiteral
					|| leaf instanceof Modifier
					|| leaf instanceof ThisExpression
					|| leaf instanceof ContinueStatement
					|| leaf instanceof BreakStatement
					|| leaf instanceof PrimitiveType
					|| leaf instanceof SuperConstructorInvocation
					){
				//We got them
			} else {
				writerNotRecognized.println(ASTNode.nodeClassForType(leaf.getNodeType()));
				addString(ASTNode.nodeClassForType(leaf.getNodeType())+"", leavesNotRecognizedNames);
				
				if(leaf instanceof ConstructorInvocation){
					specialContent += "\n<<<<\n"+leaf.toString()+"\n>>>>>\n";
				}
			}
			
			writerAll.println(ASTNode.nodeClassForType(leaf.getNodeType()));
			addString(ASTNode.nodeClassForType(leaf.getNodeType())+"", allLeavesNames);
		}

	}
	
	private void addString(String s, List<String> list){
		for(String ss: list){
			if(ss.equalsIgnoreCase(s)){
				return;
			}
		}
		
		list.add(s);
	}

	
	public void printAllLeavesList(String outputFilePath){
		String content = "";
		
		for(String s: allLeavesNames){
			content += s+"\n";
		}
		
		FileUtility.writeFile(content, outputFilePath);
	}
	
	public void printNotRecognizedLeavesList(String outputFilePath){
		String content = "";
		
		for(String s: leavesNotRecognizedNames){
			content += s+"\n";
		}
		
		FileUtility.writeFile(content, outputFilePath);
	}
	
	public void printAllLeaves(String outputFilePath){
		String content = "";
		
		for(ASTNode leaf : allLeaves){
			content += ASTNode.nodeClassForType(leaf.getNodeType())+"\n";
		}
		
		FileUtility.writeFile(content, outputFilePath);
	}
	
	
	public void printNotRecognizedLeaves(String outputFilePath){
		String content = "";
		
		for(ASTNode leaf : leavesNotRecognized){
			content += ASTNode.nodeClassForType(leaf.getNodeType())+"\n";
		}
		
		FileUtility.writeFile(content, outputFilePath);
	}
	

	public List<ASTNode> getAllLeaves() {
		return allLeaves;
	}


	public void setAllLeaves(List<ASTNode> allLeaves) {
		this.allLeaves = allLeaves;
	}


	public List<ASTNode> getLeavesNotRecognized() {
		return leavesNotRecognized;
	}


	public void setLeavesNotRecognized(List<ASTNode> leavesNotRecognized) {
		this.leavesNotRecognized = leavesNotRecognized;
	}

}
