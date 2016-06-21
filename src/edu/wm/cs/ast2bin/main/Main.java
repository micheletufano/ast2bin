package edu.wm.cs.ast2bin.main;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.wm.cs.ast2bin.algorithm.Analysis;
import edu.wm.cs.ast2bin.algorithm.GenerateTrainingCorpus;
import edu.wm.cs.ast2bin.algorithm.LexemsFileCreator;
import edu.wm.cs.ast2bin.algorithm.NonTerminalFrequency;
import edu.wm.cs.ast2bin.algorithm.binary.builder.BinaryTreeBuilder;
import edu.wm.cs.ast2bin.algorithm.deep.learner.ASTTraningCorpus;
import edu.wm.cs.ast2bin.ast.SourceFileAnalyzer;
import edu.wm.cs.ast2bin.statistics.TreeStatistics;
import edu.wm.cs.ast2bin.tree.BinaryTree;
import edu.wm.cs.ast2bin.tree.Tree;
import edu.wm.cs.ast2bin.visualization.BinaryTreeVisualizer;
import edu.wm.cs.ast2bin.visualization.TreeVisualizer;

public class Main {

	private static final String GENERATE_TRAINING_CORPUS = "generate-training-corpus";
	private static final String GREEDY_CORPUS = "greedy-corpus"; 
	private static final String GENERATE_TRAINING_CORPUS_MUTANT = "generate-training-corpus-mutant"; 
	private static final String NON_TERMINAL_FREQUENCY = "non-terminal-frequency"; 
	private static final String RUN = "run"; 
	private static final String AST_BASED_CORPUS = "ast-based-corpus"; 
	private static final String STATISTICS = "statistics"; 
	private static final String GENERATE_METHOD_CORPUS = "generate-method-corpus"; 

	public static void main(String[] args) {

		int numberOfParameters = args.length;

		if(numberOfParameters > 0){
			String mode = args[0];

			if(mode.equalsIgnoreCase("-g")){
				graphicalMode(args, numberOfParameters);

			} else if(mode.equalsIgnoreCase("-t")){
				terminalMode(args, numberOfParameters);

			} else{

				printErrorNotRecognizedMode();
			}

		} else {
			printErrorNumberOfParameter();
		}

	}


	private static void graphicalMode(String[] args, int numberOfParameters){
		if(numberOfParameters == 3){
			analyzeSourceCode(args, false);

		} else if(numberOfParameters == 4){
			analyzeSourceCode(args, true);

		} else {
			printErrorNumberOfParameter();
		}
	}


	private static void terminalMode(String[] args, int numberOfParameters){
		String task = args[1];

		if(task.equalsIgnoreCase(GENERATE_TRAINING_CORPUS)){
			String projectPath = args[2];
			String corpusPath = args[3];
			String pathsPath = args[4];
			int javaVersion = Integer.parseInt(args[5]);
			
			GenerateTrainingCorpus.generateTrainingCorpus(projectPath, corpusPath+".txt", pathsPath+".txt", false, javaVersion);
			GenerateTrainingCorpus.generateTrainingCorpus(projectPath, corpusPath+"_merged.txt", pathsPath+"_merged.txt", true, javaVersion);


		} else if(task.equalsIgnoreCase(NON_TERMINAL_FREQUENCY)){
			String projectPath = args[2];
			String workingDirectory = args[3];
			int javaVersion = Integer.parseInt(args[4]);
			//String workingDirectoryPositionInfo = args[4];

			NonTerminalFrequency.calculateFrequencyDistributionBinary(projectPath, workingDirectory, false, javaVersion);
			//NonTerminalFrequency.calculateFrequencyDistributionBinary(projectPath, workingDirectoryPositionInfo, true);
			
			//NonTerminalFrequency.calculateFrequencyDistribution(projectPath, workingDirectory);

		} else if(task.equalsIgnoreCase(RUN)){
			String projectPath = args[2];
			int javaVersion = Integer.parseInt(args[3]);
			
			Analysis.run(projectPath, javaVersion);
		} else if(task.equalsIgnoreCase(AST_BASED_CORPUS)){
			String projectPath = args[2];
			String outputFile = args[3];
			int javaVersion = Integer.parseInt(args[4]);

			ASTTraningCorpus.generateCorpus(projectPath, outputFile+".mat", false, javaVersion);
			ASTTraningCorpus.generateCorpus(projectPath, outputFile+"_merged.mat", true, javaVersion);
			
		}else if(task.equalsIgnoreCase(GREEDY_CORPUS)){ //GENERATE_TRAINING_CORPUS_ANNOTAION
			String projectPath = args[2];
			String corpusPath = args[3];
			String pathsPath = args[4];
			int javaVersion = Integer.parseInt(args[5]);
			
			GenerateTrainingCorpus.generateTrainingCorpusWithAnnotation(projectPath, corpusPath+".txt", pathsPath+".txt", false, javaVersion);
			GenerateTrainingCorpus.generateTrainingCorpusWithAnnotation(projectPath, corpusPath+"_merged.txt", pathsPath+"_merged.txt", true, javaVersion);
			
		}else if(task.equalsIgnoreCase(GENERATE_TRAINING_CORPUS_MUTANT)){
			String projectPath = args[2];
			String corpusPath = args[3];
			String pathsPath = args[4];
			String reportFile = args[5];
			int javaVersion = 4;
			
			GenerateTrainingCorpus.generateTrainingCorpusForMutant(projectPath, corpusPath+"_merged.txt", pathsPath+"_merged.txt", reportFile, true, javaVersion);
			
		}else if(task.equalsIgnoreCase(STATISTICS)){
			String projectPath = args[2];
			String statsDir = args[3];
			
			TreeStatistics stats = new TreeStatistics();
			stats.computeTreeStatistics(projectPath, statsDir);
			
		}else if(task.equalsIgnoreCase(GENERATE_METHOD_CORPUS)){
			String projectPath = args[2];
			String corpusPath = args[3];
			String pathsPath = args[4];
			int javaVersion = Integer.parseInt(args[5]);
			int minLen = Integer.parseInt(args[6]);
			int maxLen = Integer.parseInt(args[7]);
			
			//Supervised
			ASTTraningCorpus.generateMethodCorpus(projectPath, corpusPath+"ast_merged.method.mat", true, javaVersion, minLen, maxLen);
			
			//Unsupervised
			GenerateTrainingCorpus.generateMethodsTrainingCorpus(projectPath, corpusPath+"corpus_merged.method.txt", pathsPath+"paths_merged.method.txt", true, javaVersion, minLen, maxLen);

			
		}else{

			printErrorWrongTask();
		}

	}




	private static void analyzeSourceCode(String[] args, boolean createLexemsFile){
		String filePath = args[1];
		int javaVersion = Integer.parseInt(args[2]);
		
		SourceFileAnalyzer analyzer = new SourceFileAnalyzer();
		Tree tree = analyzer.analyzeSourceFile(filePath, javaVersion);

		if(createLexemsFile){
			String lexemsFileOutput = args[3];

			List<ASTNode> leaves = analyzer.getLeaves();
			LexemsFileCreator.createLexemsFile(leaves, lexemsFileOutput);
		}

		if(tree.vertexSet().size()>0){
			TreeVisualizer tv = new TreeVisualizer(tree);
			BinaryTree bTree = BinaryTreeBuilder.convertASTToBinaryTree(tree, true, true);
			BinaryTreeVisualizer bTv = new BinaryTreeVisualizer(bTree);
		} else {
			printErrorEmptyCode();
		}
	}




	private static void printErrorNotRecognizedMode() {
		System.out.println("\n !!! WRONG USAGE !!!");
		System.out.println("\n\n Not recognized mode.");
		System.out.println("Please use:");
		System.out.println("'-g' for the Graphical mode");
		System.out.println("'-t' for the Terminal mode");
	}


	private static void printErrorNumberOfParameter(){
		System.out.println("\n !!! WRONG USAGE !!!");
		System.out.println("\n\n Wrong number of parameters!");
	}

	private static void printErrorWrongTask() {
		System.out.println("\n !!! WRONG USAGE !!!");
		System.out.println("\n\n Wrong task!");		
	}
	
	private static void printErrorEmptyCode() {
		System.out.println("\n !!! EMPTY SOURCE CODE !!!");
	}

}
