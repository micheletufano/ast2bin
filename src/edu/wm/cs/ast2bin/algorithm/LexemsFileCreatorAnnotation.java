package edu.wm.cs.ast2bin.algorithm;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

import edu.wm.cs.ast2bin.tree.vertex.BinaryVertex;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class LexemsFileCreatorAnnotation {

	public @interface NotNull {
	}

	public static void createLexemsFile(List<BinaryVertex> vertices, String outputFile){

		String outputContent = extractLexemsCorpus(vertices);

		FileUtility.writeFile(outputContent, outputFile);
	}


	public static String extractLexemsCorpus(List<BinaryVertex> vertices){
		String outputContent = "";

		for(BinaryVertex vertex : vertices){
			ASTNode node = getLeafNode(vertex);

			if(node instanceof SimpleName){
				if(NonTerminalFrequency.getNodeType(vertex.getParent()).toLowerCase().contains("annotation")){
					if(vertex.getParent().getChildren().get(0).equals(vertex)){
						outputContent += "@";
					}
				}
			}


			//Check special case of literals
			if(node instanceof StringLiteral){
				outputContent += "<STRING> ";
			} else if(node instanceof CharacterLiteral){
				outputContent += "<CHAR> ";
			} else if(node instanceof NumberLiteral){
				if(node.toString().contains(".")){
					outputContent += "<FLOAT> ";
				} else {
					outputContent += "<INT> ";
				}
			} else if(node instanceof MarkerAnnotation || NonTerminalFrequency.getNodeType(vertex).toLowerCase().contains("annotation")){
				outputContent += "@"+extractNodeLexeme(node)+" ";
			} else { //General case
				outputContent += extractNodeLexeme(node)+" ";
			}
		}

		return checkLine(outputContent);
	}

	private static String checkLine(String line){
		line = line.substring(0, line.length()-1);
		line.replaceAll("  ", " ");

		while(line.charAt(0)==' '){
			System.out.println("********************* SPACE START LINE *********************");
			line = line.substring(1);
		}

		if(line.equals("")){
			System.out.println("********************* EMPTY LINE *********************");
		}

		return line;
	}


	private static String extractNodeLexeme(ASTNode node){
		String lexeme = node.toString();

		lexeme = lexeme.replace("{", "");
		lexeme = lexeme.replace("}", "");
		lexeme = lexeme.replace(";", "");
		lexeme = lexeme.replace(":", "");
		lexeme = lexeme.replace(",", "");
		lexeme = lexeme.replace("(", "");
		lexeme = lexeme.replace(")", "");
		lexeme = lexeme.replace("[", "");
		lexeme = lexeme.replace("]", "");
		lexeme = lexeme.replace(" ", "");
		lexeme = lexeme.replace("\n", "");

		return lexeme;
	}

	private static ASTNode getLeafNode(BinaryVertex vertex){

		if(vertex.getTerminal() != null){
			return vertex.getTerminal();
		} else if(vertex.getNode() != null){
			return vertex.getNode();
		}

		return null;
	}
}
