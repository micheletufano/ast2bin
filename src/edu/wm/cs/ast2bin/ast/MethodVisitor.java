package edu.wm.cs.ast2bin.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodVisitor extends ASTVisitor { 

	private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private boolean filterBySize = false;
	private int minLen;
	private int maxLen;

	@Override 
	public boolean visit(MethodDeclaration node) {
		//Filter by lines of code
		if(filterBySize){
			String code = node.toString();
			int loc = code.split(System.getProperty("line.separator")).length;
			System.out.println("LOC: "+loc);
			if(loc > minLen && loc < maxLen){
				methods.add(node); 
			}
		} else{
			//Filtering disabled
			methods.add(node); 
		}
		return super.visit(node);
	}

	public List<MethodDeclaration> getMethods(){
		return methods;
	}

	public void setFilterBySize(boolean filterBySize) {
		this.filterBySize = filterBySize;
	}

	public void setMinLen(int minLen) {
		this.minLen = minLen;
	}

	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}

}

