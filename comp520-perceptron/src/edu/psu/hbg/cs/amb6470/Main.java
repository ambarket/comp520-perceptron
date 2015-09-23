package edu.psu.hbg.cs.amb6470;

public class Main {
	public static final String basePath = "C:/Users/ambar_000/Documents/GitHub/comp520-perceptron/comp520-perceptron/datasetFiles/";
	public static final String linear_x = "linear_x.dat";
	public static final String linear_y = "linear_y.dat";
	public static final String nonlinear_x = "nonlinear_x.dat";
	public static final String nonlinear_y = "nonlinear_y.dat";
	
	public static final int linearNumOfExamples = 488;
	public static final int nonlinearNumOfExamples = 99;
	public static final int linearNumOfValidationExamples = (int)Math.floor(0.2 * Main.linearNumOfExamples);
	public static final int nonlinearNumOfValidationExamples = (int)Math.floor(0.2 * Main.nonlinearNumOfExamples);
	
	public static String errorOutputByIteration;
	public static int currNumOfExamples;
	public static int currNumOfValidationExamples;
	
	public static final int MAX_ITERATIONS = 1000;
	public static final double[] LEARNING_RATES = {.01, .05, .1, .15, .2, .3, .5, .8, 1};
	public static void main(String[] args) {

		

		
		currNumOfExamples = linearNumOfExamples;
		currNumOfValidationExamples = linearNumOfValidationExamples;
		final double[][] linearX = FileReaderAndWriter.readX(basePath, linear_x);
		final int[] linearY = FileReaderAndWriter.readY(basePath, linear_y);
		for (double learningRate : LEARNING_RATES) {
			errorOutputByIteration = "errorByIteration" + learningRate + "LearningRateLinear.txt";
			Perceptron perceptron = new Perceptron(linearX, linearY, learningRate);
			perceptron.learnWeights_MaxIterationsOnly(MAX_ITERATIONS);
		}
		
		
		currNumOfExamples = nonlinearNumOfExamples;
		currNumOfValidationExamples = nonlinearNumOfValidationExamples;
		final double[][] nonlinearX = FileReaderAndWriter.readX(basePath, nonlinear_x);
		final int[] nonlinearY = FileReaderAndWriter.readY(basePath, nonlinear_y);
		for (double learningRate : LEARNING_RATES) {
			errorOutputByIteration = "errorByIteration" + learningRate + "LearningRate-NonLinear.txt";
			Perceptron perceptron = new Perceptron(nonlinearX, nonlinearY, learningRate);
			perceptron.learnWeights_MaxIterationsOnly(MAX_ITERATIONS);
		}
		
		
	}
}
