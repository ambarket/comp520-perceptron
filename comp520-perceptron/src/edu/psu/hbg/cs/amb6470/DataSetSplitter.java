package edu.psu.hbg.cs.amb6470;

import edu.gmu.cs.MersenneTwisterFast;
import edu.princeton.cs.introcs.StdArrayIO;

public class DataSetSplitter {
	static MersenneTwisterFast rand = new MersenneTwisterFast();
	
	/**
	 * Returns an int array where the first Main.currNumOfValidationExamples; elements are randomly selected integers from [0, currNumOfExamples-1], the
	 * remaining 800 elements are the remaining numbers from [0, currNumOfExamples-1]
	 * @return
	 */
	public static int[] basicSelection() {
		boolean[] used = new boolean[Main.currNumOfExamples];

		int[] retval = new int[Main.currNumOfExamples];
		int validationPtr = 0, trainingPtr = Main.currNumOfValidationExamples;
		while (validationPtr < Main.currNumOfValidationExamples) {
			int next = rand.nextInt(Main.currNumOfExamples);
			if (!used[next]) {
				retval[validationPtr] = next;
				used[next] = true;
				validationPtr++;
			}
		}
		
		for (int i = 0; i < Main.currNumOfExamples; i++) {
			if (!used[i]) {
				retval[trainingPtr] = i;
				trainingPtr++;
			}
		}
		assert(trainingPtr == Main.currNumOfExamples);
		return retval;
	}
	
	/**
	 * Source: https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
	 * @return A random permutation of the numbers 0 through currNumOfExamples-1. 
	 * So reading the first Main.currNumOfValidationExamples; of these numbers should be the same as random
	 * uniform sampling.
	 */
	public static int[] fisherYatesShuffle() {
		int[] retval = new int[Main.currNumOfExamples];
		for (int i = 0; i < Main.currNumOfExamples; i++) {
			retval[i] = i;
		}

		for (int i = Main.currNumOfExamples-1, j = 0, tmp = 0; i > 0; i--) {
			j = rand.nextInt(i);
			tmp = retval[i];
			retval[i] = retval[j];
			retval[j] = tmp;
		}
		return retval;
	}
}
