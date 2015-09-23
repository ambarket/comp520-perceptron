package edu.psu.hbg.cs.amb6470;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.gmu.cs.MersenneTwisterFast;
import edu.princeton.cs.introcs.StdOut;

public class DataSetSplitter {
	static MersenneTwisterFast rand = new MersenneTwisterFast();
	
	/**
	 * Returns an int array where the first 200 elements are randomly selected integers from [0, 999], the
	 * remaining 800 elements are the remaining numbers from [0, 999]
	 * @return
	 */
	public static int[] basicSelection() {
		boolean[] used = new boolean[1000];

		int[] retval = new int[1000];
		int validationPtr = 0, trainingPtr = 200;
		while (validationPtr < 200) {
			int next = rand.nextInt(1000);
			if (!used[next]) {
				retval[validationPtr] = next;
				used[next] = true;
				validationPtr++;
			}
		}
		
		for (int i = 0; i < 1000; i++) {
			if (!used[i]) {
				retval[trainingPtr] = i;
				trainingPtr++;
			}
		}
		assert(validationPtr == 200);
		assert(trainingPtr == 1000);
		return retval;
	}
	
	/**
	 * Source: https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
	 * @return A random permutation of the numbers 0 through 999. 
	 * So reading the first 200 of these numbers should be the same as random
	 * uniform sampling.
	 */
	public static int[] fisherYatesShuffle() {
		int[] retval = new int[1000];
		for (int i = 0; i < 1000; i++) {
			retval[i] = i;
		}

		for (int i = 999, j = 0, tmp = 0; i >= 0; i--) {
			j = rand.nextInt(i);
			tmp = retval[i];
			retval[i] = retval[j];
			retval[j] = tmp;
		}
		return retval;
	}
}
