package edu.wm.cs.ast2bin.algorithm;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;

import edu.wm.cs.ast2bin.utility.FileUtility;

public class LexemsFileCreator {

	
	
	public static void createLexemsFile(List<ASTNode> nodes, String outputFile){
		
		String outputContent = extractLexemsCorpus(nodes);
		
		FileUtility.writeFile(outputContent, outputFile);
	}
	

	public static String extractLexemsCorpus(List<ASTNode> nodes){
		String outputContent = "";
		
		for(ASTNode node : nodes){
			
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
}
