package edu.wm.cs.ast2bin.main;

import edu.wm.cs.ast2bin.algorithm.CheckTerminalTypes;

public class ExecuteCheckTerminalTypes {

	public static void main(String[] args) {

		CheckTerminalTypes checker = new CheckTerminalTypes();
		int javaVersion = 4;
		
		checker.checkProject(args[0], args[1], args[2], args[2]+"_special", javaVersion);
		
		checker.printAllLeavesList(args[1]+"_single");
		checker.printNotRecognizedLeavesList(args[2]+"_single");

	}

}
