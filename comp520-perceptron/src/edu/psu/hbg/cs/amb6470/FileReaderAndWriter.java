package edu.psu.hbg.cs.amb6470;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileReaderAndWriter {
	
	
	public static double[][] readX(String basePath, String fileName) {
		double[][] retval = new double[Main.currNumOfExamples][2];
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(basePath + fileName)));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				line = line.trim().replaceAll(" +|\t+", " ");
				String[] attributes = line.split(" ");
				if (attributes.length != 2) {
					System.out.println("X file provided has " + attributes.length + " columns, was expecting 2");
				}
				retval[i][0] = Double.valueOf(attributes[0]);
				retval[i][1] = Double.valueOf(attributes[1]);
				i++;
			}
			br.close();
			if (i != Main.currNumOfExamples) {
				System.out.println("X file provided has " + i + " rows, expected " + Main.currNumOfExamples + " rows.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return retval;
	}
	
	public static int[] readY(String basePath, String fileName) {
		int[] retval = new int[Main.currNumOfExamples];
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(basePath + fileName)));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				line = line.trim().replaceAll(" +|\t+", " ");
				String[] attributes = line.split(" ");
				if (attributes.length != 1) {
					System.out.println("Y file provided has " + attributes.length + " columns, was expecting 1");
				}
				retval[i] = Double.valueOf(attributes[0]).intValue();
				i++;
			}
			br.close();
			if (i != Main.currNumOfExamples) {
				System.out.println("Y file provided has " + i + " rows, expected " +  + Main.currNumOfExamples + " rows.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return retval;
	}
	
	public static void writeTrainingAndValidationErrors(double[] trainingErrorByIteration, double[] validationErrorByIteration) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Main.basePath + Main.errorOutputByIteration)));
			
			for (int i = 0; i < trainingErrorByIteration.length; i++) {
				bw.write(trainingErrorByIteration[i] + "\t" + validationErrorByIteration[i] + "\n");
			}

			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
