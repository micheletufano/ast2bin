package edu.wm.cs.ast2bin.algorithm.binary.builder.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Modifier;

import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;

public class TypeDeclaration extends BinaryBuilderComponent{

	public static void convertToBinary(BinaryVertex vertex, BinaryTree tree) {

		//Siblings
		List<BinaryVertex> siblings = tree.getChildren(vertex);

		disconnectChildren(vertex, siblings, tree);

		//Header
		buildHeader(vertex, siblings, tree);

		//Body
		buildBody(vertex, siblings, tree);

	}


	private static void buildHeader(BinaryVertex parent, List<BinaryVertex> siblings, BinaryTree tree){
		org.eclipse.jdt.core.dom.TypeDeclaration node = (org.eclipse.jdt.core.dom.TypeDeclaration) parent.getNode();

		//Create new artificial NonTerminal node
		BinaryVertex header = new BinaryVertex();
		header.setType(Type.TYPE_DECLARATION_HEADER);
		tree.addVertex(header);

		//Add edge to parent
		tree.addEdge(header, parent);
		header.setParent(parent);

		//Modifiers
		BinaryVertex modifierList = new BinaryVertex();
		modifierList.setType(Type.MODIFIER_LIST);
		tree.addVertex(modifierList);
		
		//Add edge to parent
		tree.addEdge(modifierList, header);
		modifierList.setParent(header);

		List<BinaryVertex> modifiers = getModifiers(siblings);
		
		if(modifiers.size() > 0){
			//FirstChild
			BinaryVertex child = modifiers.remove(0);
			
			//Recursion
			buildList(modifierList, child, modifiers, tree, Type.MODIFIER_LIST);
		}

		
		//Signature
		BinaryVertex signature = new BinaryVertex();
		signature.setType(Type.TYPE_SIGNATURE);
		tree.addVertex(signature);
		
		//Add edge to parent
		tree.addEdge(signature, header);
		signature.setParent(header);
		
		//Identifier
		BinaryVertex identifier = siblings.remove(0);
		tree.addEdge(identifier, signature);
		identifier.setParent(signature);
		
		
		//Types
		List<BinaryVertex> types = getTypes(node, siblings);

		if(types.size()>0){
			//Create new artificial NonTerminal node
			BinaryVertex typeList = new BinaryVertex();
			typeList.setType(Type.TYPE_LIST);
			tree.addVertex(typeList);

			//Add edge to parent
			tree.addEdge(typeList, signature);
			typeList.setParent(signature);

			//FirstChild
			BinaryVertex child = types.remove(0);

			//Recursion
			buildList(typeList, child, types, tree, Type.TYPE_LIST);
		}
	}



	private static void buildBody(BinaryVertex parent, List<BinaryVertex> siblings, BinaryTree tree){
		//Create new artificial NonTerminal node
		BinaryVertex nextParent = new BinaryVertex();
		nextParent.setType(Type.CLASS_BODY_ELEMENT_LIST);
		tree.addVertex(nextParent);
		
		//Add edge to parent
		tree.addEdge(nextParent, parent);
		nextParent.setParent(parent);

		if(siblings.size() > 0){
			//FirstChild
			BinaryVertex child = siblings.remove(0);
			
			//Recursion
			buildList(nextParent, child, siblings, tree, Type.CLASS_BODY_ELEMENT_LIST);
		}
	}


	private static List<BinaryVertex> getTypes(org.eclipse.jdt.core.dom.TypeDeclaration node, List<BinaryVertex> siblings){
		List<BinaryVertex> types = new ArrayList<BinaryVertex>();

		for(Object obj : node.typeParameters()){
			ASTNode n = (ASTNode) obj;
			BinaryVertex vn = getAsociatedBinaryVertex(n, siblings);
			types.add(vn);
			siblings.remove(vn);
		}
		
		//SuperClass
		ASTNode superclass = node.getSuperclassType();
		if(superclass != null){
			BinaryVertex vSuperclass = siblings.remove(0);
			types.add(vSuperclass);
		}
		
		//Interfaces
		List interfaces = node.superInterfaceTypes();
		for(Object o : interfaces){
			BinaryVertex vInterface = siblings.remove(0);
			types.add(vInterface);
		}

		return types;
	}

	
	private static List<BinaryVertex> getModifiers(List<BinaryVertex> siblings){
		List<BinaryVertex> modifiers = new ArrayList<BinaryVertex>();

		for(BinaryVertex v : siblings){
			if(v.getNode() instanceof Modifier){
				modifiers.add(v);
			}
		}

		for(BinaryVertex v : modifiers){
			siblings.remove(v);
		}

		return modifiers;
	}
}
