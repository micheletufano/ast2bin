package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class CompilationUnit extends BinaryBuilderComponent{

	public static void convertToBinary(BinaryVertex vertex, BinaryTree tree) {

		//Siblings
		List<BinaryVertex> siblings = tree.getChildren(vertex);

		disconnectChildren(vertex, siblings, tree);

		//Header
		buildHeader(vertex, siblings, tree);
		
		//Types
		buildTypes(vertex, siblings, tree);
	}


	private static void buildHeader(BinaryVertex parent, List<BinaryVertex> siblings, BinaryTree tree){
		org.eclipse.jdt.core.dom.CompilationUnit node = (org.eclipse.jdt.core.dom.CompilationUnit) parent.getNode();

		//Create new artificial NonTerminal node
		BinaryVertex header = new BinaryVertex();
		header.setType(Type.COMPILATION_UNIT_HEADER);
		tree.addVertex(header);

		//Add edge to parent
		tree.addEdge(header, parent);
		header.setParent(parent);

		PackageDeclaration pack = node.getPackage();

		if(pack != null){
			BinaryVertex vPack = siblings.remove(0);

			//Add edge to parent
			tree.addEdge(vPack, header);
			vPack.setParent(header);
		}


		//Create new artificial NonTerminal node
		BinaryVertex importList = new BinaryVertex();
		importList.setType(Type.IMPORT_DECLARATION_LIST);
		tree.addVertex(importList);

		//Add edge to parent
		tree.addEdge(importList, header);
		importList.setParent(header);

		List<BinaryVertex> imports = getImports(siblings);

		if(imports.size() > 0){
			//FirstChild
			BinaryVertex child = imports.remove(0);

			//Recursion
			buildList(importList, child, imports, tree, Type.IMPORT_DECLARATION_LIST);
		}

	}

	private static void buildTypes(BinaryVertex parent, List<BinaryVertex> siblings, BinaryTree tree){
		//Create new artificial NonTerminal node
		BinaryVertex typeList = new BinaryVertex();
		typeList.setType(Type.TYPE_DECLARATION_LIST);
		tree.addVertex(typeList);

		//Add edge to parent
		tree.addEdge(typeList, parent);
		typeList.setParent(parent);

		if(siblings.size() > 0){
			//FirstChild
			BinaryVertex child = siblings.remove(0);

			//Recursion
			buildList(typeList, child, siblings, tree, Type.TYPE_DECLARATION_LIST);
		}
	}


	private static List<BinaryVertex> getImports(List<BinaryVertex> siblings){
		List<BinaryVertex> imports = new ArrayList<BinaryVertex>();

		for(BinaryVertex v : siblings){
			if(v.getNode() instanceof ImportDeclaration){
				imports.add(v);
			}
		}

		for(BinaryVertex v : imports){
			siblings.remove(v);
		}

		return imports;
	}
}
