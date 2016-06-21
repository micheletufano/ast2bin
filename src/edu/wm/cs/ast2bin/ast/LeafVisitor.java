package edu.wm.cs.ast2bin.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeLiteral;

public class LeafVisitor {
	
	private String lexems = "";

	public List<ASTNode> visit(String str) {
		List<ASTNode> nodes = new ArrayList<ASTNode>();
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		cu.accept(new ASTVisitor() {
			
			public boolean visit(SimpleName node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			public boolean visit(BooleanLiteral node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			public boolean visit(StringLiteral node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			public boolean visit(CharacterLiteral node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			public boolean visit(NumberLiteral node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			public boolean visit(NullLiteral node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			public boolean visit(TypeLiteral node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			
			public boolean visit(Modifier node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			
			public boolean visit(ThisExpression node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			public boolean visit(ContinueStatement node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			public boolean visit(BreakStatement node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			public boolean visit(PrimitiveType node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
			public boolean visit(SuperConstructorInvocation node) {
				nodes.add(node);
				lexems += " "+node.toString();
				System.out.println(node.toString());
				return true;
			}
			
		});
		
		return nodes;
	}
	
	public String getLexems() {
		return lexems;
	}

	public void setLexems(String lexems) {
		this.lexems = lexems;
	}
	
	
	//Not used
	public static Object[] getChildren(ASTNode node) {
		List list= node.structuralPropertiesForType();
		for (int i= 0; i < list.size(); i++) {
			StructuralPropertyDescriptor curr= (StructuralPropertyDescriptor) list.get(i);
			Object child= node.getStructuralProperty(curr);
			if (child instanceof List) {
				System.out.println("List");
				return ((List) child).toArray();
			} else if (child instanceof ASTNode) {
				System.out.println("Node!");
				return new Object[] { child };
			}
		}
		return null;
	}
}