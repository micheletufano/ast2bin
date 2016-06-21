package edu.wm.cs.ast2bin.algorithm.deep.learner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLCell;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;

public class MATFileExporter {
	
	
	public static void exportData(List<Object> matrices, Set<String> types, String outputFilePath){
		
		
		//Triplets
		MLCell triplets = new MLCell("triplets", new int[] {matrices.size(), 1} );
		int tripletIndex = 0;
		for(Object obj : matrices){
			double[][] matrix = (double[][]) obj;
			MLDouble matrixRow = new MLDouble("file", matrix);
	        triplets.set(matrixRow, tripletIndex++);
		}

		
		//Mapping
		MLCell mapping = new MLCell("mapping", new int[] {types.size(),1} );
		
		int mappingIndex = 0;
		for(String type : types){
	        MLChar singleMap = new MLChar( "type", type);
			mapping.set(singleMap, mappingIndex++);

		}		
		
		//Prepare data
		List<MLArray> data = new ArrayList<MLArray>(); 
		data.add(triplets);
		data.add(mapping); 

		//Write data
	    try {
			MatFileWriter mfw = new MatFileWriter(outputFilePath, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
