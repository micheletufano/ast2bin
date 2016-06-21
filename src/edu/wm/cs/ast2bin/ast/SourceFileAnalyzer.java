package edu.wm.cs.ast2bin.ast;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.jgraph.graph.DefaultEdge;

import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.tree.vertex.Vertex;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class SourceFileAnalyzer {

	private String lexems;
	private List<ASTNode> nodes;
	private List<ASTNode> leaves;

	public static List<ASTNode> extractAllASTNodes(String sourceFilePath, int javaVersion){
		return extractASTNodes(sourceFilePath, javaVersion);
	}
	
	
	public static CompilationUnit extractCompilationUnit(String sourceFilePath, int javaVersion){
		String content = "";
		
		try {
			content = FileUtility.readFileToString(sourceFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ASTNodeVisitor.extractCompilationUnit(content, javaVersion);
	}
	
	
	public Tree analyzeSourceFile(String sourceFilePath, int javaVersion){
		
		//Visit the AST and extract all the ASTNodes
		nodes = extractASTNodes(sourceFilePath, javaVersion);
		
		//Filter the leaves nodes
		leaves = extractLeafNodes(nodes);
		
		//Exclude trivial leaves (Javadoc, empty Blocks, CU, etc..)
		removeTrivialLeaves();
		
		//Extracts and filters the parent's chains for each leaf node
		Map<ASTNode, List<ASTNode>> nodesMap = extractParentChains(leaves);
		
		
		//Build the pruned AST tree
		Tree tree = buildTree(leaves, nodesMap);

		return tree;
	}
	
	
	public List<Tree> analyzeMethodsSourceFile(String sourceFilePath, int javaVersion, File f, String prefix, PrintWriter writerPaths, int minLen, int maxLen){
		List<Tree> methodTrees = new ArrayList<Tree>();
		
		//Extract commpilation unit
		CompilationUnit cu = extractCompilationUnit(sourceFilePath, javaVersion);
		
		//Method visitor
		MethodVisitor methodVisitor = new MethodVisitor();
		methodVisitor.setFilterBySize(true);
		methodVisitor.setMinLen(minLen);
		methodVisitor.setMaxLen(maxLen);
		cu.accept(methodVisitor);
		List<MethodDeclaration> methods = methodVisitor.getMethods();
		
		for(MethodDeclaration method : methods){
			//Visit the method node and extract all ASTNodes
			nodes = ASTNodeVisitor.visitMethod(method);
			
			//Filter the leaves nodes
			leaves = extractLeafNodes(nodes);
			
			//Exclude trivial leaves (Javadoc, empty Blocks, CU, etc..)
			removeTrivialLeaves();
			
			//Extracts and filters the parent's chains for each leaf node
			Map<ASTNode, List<ASTNode>> nodesMap = extractParentChains(leaves);
			
			
			//Build the pruned AST tree
			Tree tree = buildTree(leaves, nodesMap);
			
			methodTrees.add(tree);
			
			printMethodPath(f, prefix, writerPaths, method);
		}

		return methodTrees;
	}
	
	
	private static void printMethodPath(File f, String pathPrefix, PrintWriter writerPaths, MethodDeclaration method){
		if(writerPaths != null){
			String toPrint = f.getAbsolutePath().replace(pathPrefix, "");
			toPrint += "."+method.getName();
			writerPaths.println(toPrint);
			writerPaths.flush();
		}
	}
	
	
	
	
	
	public Tree analyzeSourceFile_oldApproach(String sourceFilePath){
		
		//Visit the AST and extract the leaf nodes
		nodes = extractLeafNodes_oldApproach(sourceFilePath);
		
		
		//Extracts and filters the parent's chains for each leaf node
		Map<ASTNode, List<ASTNode>> nodesMap = extractParentChains(nodes);

		
		//Build the pruned AST tree
		Tree tree = buildTree(nodes, nodesMap);

		return tree;
	}

	
	private static Tree buildTree(List<ASTNode> nodes, Map<ASTNode, List<ASTNode>> nodesMap) {
		Tree tree = new Tree(DefaultEdge.class);
		
		for(ASTNode node : nodes){
			Vertex v = new Vertex();
			v.setNode(node);
			
			tree.addVertex(v);
			
			List<ASTNode> parents = nodesMap.get(node);
			Vertex child = v;
			
			for(ASTNode n : parents){
				Vertex parent = getVertex(n, tree);
				
				tree.addEdge(child, parent);
				
				child = parent;
			}
		}
		
		Vertex root = null;
		for(Vertex v : tree.vertexSet()){
			String s = ""+ ASTNode.nodeClassForType(v.getNode().getNodeType());
			if(s.contains("CompilationUnit")){
				root = v;
			}
		}
		
		tree.setRoot(root);
		return tree;
	}

	
	private static Map<ASTNode, List<ASTNode>> extractParentChains(List<ASTNode> nodes) {
		Map<ASTNode, List<ASTNode>> nodesMap = new HashMap<ASTNode, List<ASTNode>>();

		for(ASTNode n : nodes){
			List<ASTNode> parents = new ArrayList<ASTNode>();
			
			ASTNode p = n.getParent();
			
			while(p != null){
				if(isToBeSelected(p)){
					parents.add(p);
				}
				p = p.getParent();
			}
						
			nodesMap.put(n, parents);
			continue;
		}
		return nodesMap;
	}
	
	
	private void removeTrivialLeaves(){
		List<ASTNode> toRemove = new ArrayList<ASTNode>();
		
		//Identify
		for(ASTNode node : leaves){
			if(		   node instanceof EmptyStatement
					|| node instanceof Javadoc	
					|| node instanceof CompilationUnit
					|| node instanceof AnonymousClassDeclaration
					|| node instanceof ArrayInitializer
					|| node instanceof Block){
				
				toRemove.add(node);
			}
		}
		
		//Remove
		for(ASTNode node : toRemove){
			leaves.remove(node);
		}
	}
	
	
	
	private static boolean isToBeSelected(ASTNode p) {
		// TODO Auto-generated method stub
	
		
		return true;
	}

	
	private static List<ASTNode> extractASTNodes(String sourceFilePath, int javaVersion) {
		String content = "";
		
		try {
			content = FileUtility.readFileToString(sourceFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<ASTNode> nodes = ASTNodeVisitor.visit(content, javaVersion);
		
		return nodes;
	}	
	
	
	private List<ASTNode> extractLeafNodes(List<ASTNode> nodes) {
		List<ASTNode> leaves = new ArrayList<ASTNode>(nodes);
		
		for(ASTNode node : nodes){
			leaves.remove(node.getParent());
		}
		
		return leaves;
	}
	
	
	
	

	private List<ASTNode> extractLeafNodes_oldApproach(String sourceFilePath) {
		String content = "";
		
		try {
			content = FileUtility.readFileToString(sourceFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LeafVisitor visitor = new LeafVisitor();
		List<ASTNode> nodes = visitor.visit(content);
		lexems = visitor.getLexems();
		
		return nodes;
	}
	
	
	private static Vertex getVertex(ASTNode n, Tree t){
		Vertex v = new Vertex();
		v.setNode(n);
		
		for(Vertex v1 : t.vertexSet()){
			if(v1.equals(v)){
				return v1;
			}
		}
		
		t.addVertex(v);
		
		return v;
	}
	
	
	
	public String getLexems() {
		return lexems;
	}

	public void setLexems(String lexems) {
		this.lexems = lexems;
	}

	public List<ASTNode> getNodes() {
		return nodes;
	}
	
	public void setNodes(List<ASTNode> nodes) {
		this.nodes = nodes;
	}

	public List<ASTNode> getLeaves() {
		return leaves;
	}

	public void setLeaves(List<ASTNode> leaves) {
		this.leaves = leaves;
	}
	
}