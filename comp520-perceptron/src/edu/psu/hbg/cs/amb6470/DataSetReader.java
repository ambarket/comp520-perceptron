package edu.psu.hbg.cs.amb6470;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.princeton.cs.introcs.StdOut;

public class DataSetReader {
	
	
	public static double[][] readX(String basePath, String fileName) {
		double[][] retval = new double[1000][2];
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(basePath + fileName)));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				line = line.trim().replaceAll(" +|\t+", " ");
				String[] attributes = line.split(" ");
				if (attributes.length != 2) {
					StdOut.println("X file provided has " + attributes.length + " columns, was expecting 2");
				}
				retval[i][0] = Double.valueOf(attributes[0]);
				retval[i][1] = Double.valueOf(attributes[1]);
				i++;
			}
			br.close();
			if (i != 1001) {
				StdOut.println("X file provided does not have 1000 rows.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return retval;
	}
	
	public static int[] readY(String basePath, String fileName) {
		int[] retval = new int[1000];
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(basePath + fileName)));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				line = line.trim().replaceAll(" +|\t+", " ");
				String[] attributes = line.split(" ");
				if (attributes.length != 1) {
					StdOut.println("Y file provided has " + attributes.length + " columns, was expecting 1");
				}
				retval[i] = Double.valueOf(attributes[0]).intValue();
				i++;
			}
			br.close();
			if (i != 1001) {
				StdOut.println("Y file provided does not have 1000 rows.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return retval;
	}
}
