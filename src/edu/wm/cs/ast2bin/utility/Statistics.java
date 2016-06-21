package edu.wm.cs.ast2bin.utility;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

public class Statistics {
	
	public static void main(String[] args) {
		Statistics.createStatistics("/home/michele/Desktop/nonTerminalAST", "/home/michele/Desktop/stats/");
	}
	
	public static void createStatistics(String inputDirectory, String outputDirectory){
		File dir = new File(inputDirectory);
		File[] files = dir.listFiles();
		
		
		for(File f : files){
			String content = "";
			String outputContent = "";
			String outputFile = outputDirectory+f.getName();
			try {
				content = FileUtility.readFile(f.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String lines[] = content.split("\\r?\\n");
			
			for(String l : lines){
				int children = StringUtils.countMatches(l, ",")+1;
				outputContent += children+"\n";
			}
			
			FileUtility.writeFile(outputContent, outputFile);
		}
	}

}
