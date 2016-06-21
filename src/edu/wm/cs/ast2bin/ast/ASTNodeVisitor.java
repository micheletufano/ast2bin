package edu.wm.cs.ast2bin.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class ASTNodeVisitor {


	public static List<ASTNode> visit(String str, int javaVersion) {
		List<ASTNode> nodes = new ArrayList<ASTNode>();

		final CompilationUnit cu = extractCompilationUnit(str, javaVersion);


		cu.accept(new ASTVisitor() {

			public void preVisit(ASTNode node){
				nodes.add(node);		
			}

		});

		return nodes;
	}

	
	public static List<ASTNode> visitMethod(MethodDeclaration method){
		List<ASTNode> nodes = new ArrayList<ASTNode>();
		
		method.accept(new ASTVisitor() {

			public void preVisit(ASTNode node){
				nodes.add(node);		
			}

		});

		return nodes;
		
	}


	public static boolean containsError(String str, boolean syntax){
		CompilationUnit cu1 = extractCompilationUnit(str, 4);
		CompilationUnit cu2 = extractCompilationUnit(str, 5);

		IProblem[] problems1 = cu1.getProblems();
		IProblem[] problems2 = cu2.getProblems();

		List<IProblem> errors1 = getErrors(problems1, syntax);
		List<IProblem> errors2 = getErrors(problems2, syntax);

		if(errors1.isEmpty() || errors2.isEmpty()){
			return false;
		}

		return true;
	}

	private static List<IProblem> getErrors(IProblem[] problems, boolean syntax){
		List<IProblem> errors = new ArrayList<IProblem>();

		for(IProblem p : problems){
			if(p.isError()){
				if(syntax){
					if(p.getMessage().contains("Syntax")){
						errors.add(p);
					}
				} else {
					errors.add(p);
				}
			}
		}

		return errors;
	}

	public static CompilationUnit extractCompilationUnit(String str, int javaVersion){	
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setBindingsRecovery(true);


		String javaCoreJavaVersion = "";
		if(javaVersion == 4){
			javaCoreJavaVersion = JavaCore.VERSION_1_4;
		} else if(javaVersion == 5){
			javaCoreJavaVersion = JavaCore.VERSION_1_5;
		} else if(javaVersion == 6){
			javaCoreJavaVersion = JavaCore.VERSION_1_6;
		} else if(javaVersion == 7){
			javaCoreJavaVersion = JavaCore.VERSION_1_7;
		} else if(javaVersion == 8){
			javaCoreJavaVersion = JavaCore.VERSION_1_8;
		}

		Map options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, javaCoreJavaVersion);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, javaCoreJavaVersion);
		options.put(JavaCore.COMPILER_SOURCE, javaCoreJavaVersion);
		parser.setCompilerOptions(options);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		return cu;
	}


	public static String getASTNodeType(ASTNode node){
		String fullType = ""+ASTNode.nodeClassForType(node.getNodeType());

		String[] info = fullType.split("\\.");

		return info[info.length-1];
	}

}