package edu.wm.cs.ast2bin.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLCell;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;

public class MATFileTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double[][] matrix = new double[3][];
		matrix[0] = new double[] {1,2,3,4,5,6};
		matrix[1] = new double[] {1,2,3,4,5,6};
		matrix[2] = new double[] {1,2,3,4,5,6};

		MLDouble row1 = new MLDouble("row1", matrix);
		MLDouble row2 = new MLDouble("row2", matrix);
		MLDouble row3 = new MLDouble("row3", matrix);
		MLDouble row4 = new MLDouble("row4", matrix);

		MLCell triplets = new MLCell("triplets", new int[] {4,1} );
        triplets.set(row1, 0);
        triplets.set(row2, 1);
        triplets.set(row3, 2);
        triplets.set(row4, 3);

        //Mapping
        MLChar map1 = new MLChar( "1", "CompilationUnit");
        MLChar map2 = new MLChar( "2", "ForStatement");

		MLCell mapping = new MLCell("mapping", new int[] {2,1} );
		mapping.set(map1, 0);
		mapping.set(map2, 1);


		List<MLArray> data = new ArrayList<MLArray>(); 
		data.add(triplets);
		data.add(mapping); 


	    try {
			MatFileWriter mfw = new MatFileWriter("/home/michele/Desktop/data.mat", data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
