package edu.wm.cs.ast2bin.statistics;

import edu.wm.cs.ast2bin.algorithm.GenerateTrainingCorpus;
import edu.wm.cs.ast2bin.utility.FileUtility;

public class TokenCounter {

	public static void main(String[] args) {
	
		TokenCounter.countTokens("antlr-4");
		TokenCounter.countTokens("apache-ant-1.9.6");
		TokenCounter.countTokens("argouml");
		TokenCounter.countTokens("carol-2.0.5-src");
		TokenCounter.countTokens("dnsjava-code");
		TokenCounter.countTokens("hibernate-2.1");
		TokenCounter.countTokens("J2SDK_1.4.2_19");
		TokenCounter.countTokens("jhotdraw-svn");
		
	}

	private static final int FILE_A = 1;
	private static final int FILE_B = 2;
	private static final int TYPE = 3;
	
	private static final String projectsRoot = "/home/scratch/mtufano/projects/deepLearningCloneDetection/SubjectSystemSource/";
	private static final String truePositiveRoot = "/home/scratch/mtufano/projects/deepLearningCloneDetection/DeckardComparison/TruePositives/";
	private static final String truePositive50Root = "/home/scratch/mtufano/projects/deepLearningCloneDetection/DeckardComparison/TruePositives50/";
	private static final String truePositive50Type3Root = "/home/scratch/mtufano/projects/deepLearningCloneDetection/DeckardComparison/TruePositives50Type3/";

	public static void countTokens(String project){
		String truePositive50Content = "";
		String truePositives50Type3Content = "";
		String truePositive50Output = truePositive50Root+"/"+project+".csv";
		String truePositive50Type3Output = truePositive50Type3Root+"/"+project+".csv";

		String file = truePositiveRoot+"/"+project+".csv";
		
		String[] truePositives = FileUtility.getLines(file);

		//Checking positives
		boolean firstLine = true;
		for(String positive : truePositives){
			if(firstLine){
				firstLine = false;
				truePositive50Content += positive+"\n";
				truePositives50Type3Content += positive+"\n";
				continue;
			}

			String[] positiveInfo = positive.split(",");
			String fileA = projectsRoot+positiveInfo[FILE_A];
			String fileB = projectsRoot+positiveInfo[FILE_B];
			int cloneType = Integer.parseInt(positiveInfo[TYPE]);
			
			int tokensA = GenerateTrainingCorpus.getNumberofLeaves(fileA);
			int tokensB = GenerateTrainingCorpus.getNumberofLeaves(fileB);

			//System.out.println("Tokens: "+tokensA+" - "+tokensB);
			
			if(tokensA < 50 || tokensB < 50){
				System.out.println("Removed: "+tokensA+" - "+tokensB);
			} else{
				truePositive50Content += positive+"\n";
				
				if(cloneType > 2){
					truePositives50Type3Content += positive+"\n";
				}
			}
		}

		FileUtility.writeFile(truePositive50Content, truePositive50Output);
		FileUtility.writeFile(truePositives50Type3Content, truePositive50Type3Output);

	}

}
