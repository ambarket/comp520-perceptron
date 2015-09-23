package edu.psu.hbg.cs.amb6470;

import edu.princeton.cs.introcs.Matrix;

public class Perceptron {


	public final double[][] x;
	public final int[] y;
	public final double learningRate;
	
	/**
	 * First 200 entries are the indices of the validation set, remaining 800 are indices of the training set 
	 */
	public final int[] dataSplit = DataSetSplitter.fisherYatesShuffle();
	//public final int[] dataSplit = DataSetSplitter.basicSelection();
	/**
	 * The weight vector defining this perceptron
	 */
	public double[] w = new double[2];
	
	public Perceptron(double[][] x, int[] y, double learningRate) {
		this.x = x;
		this.y = y;
		this.learningRate = learningRate;
		
		//this.w[0] = 3; 
		//this.w[1] = -3;
	}
	

	/**
	 * @param n
	 */
	public void learnWeights_MaxIterationsOnly(int n) {
		double[] trainingErrorByIteration = new double[n];
		double[] validationErrorByIteration = new double[n];
		for (int i = 0; i < n; i ++) {
			learnWeights_SingleEpoch();
			trainingErrorByIteration[i] = evaluateTrainingError();
			validationErrorByIteration[i] = evaluateValidationError();
		}
		FileReaderAndWriter.writeTrainingAndValidationErrors(trainingErrorByIteration, validationErrorByIteration);
	}

	private int predict(double[] instance) {
		return (int)Math.signum(Matrix.dot(w, instance));
	}

	private void learnWeights_SingleEpoch() {
		double trainingError = 1;
		for (int t = Main.currNumOfValidationExamples; t < Main.currNumOfExamples; t++) {
			double[] instance = x[dataSplit[t]];
			int label = y[dataSplit[t]];
			int prediction = predict(instance);
			if (prediction != label) {
				double[] nextW = Matrix.add(w, Matrix.multiplyScalar(learningRate * label, instance));
				
				//double nextTrainingError = evaluateTrainingError();
				//if (nextTrainingError <= trainingError) {
					this.w = nextW;
				//	trainingError = nextTrainingError;
				//} 
			}
		}
	}
	
	private double evaluateTrainingError() {
		double error = 0.0;
		for (int t = Main.currNumOfValidationExamples; t < Main.currNumOfExamples; t++) {
			double[] instance = x[dataSplit[t]];
			int label = y[dataSplit[t]];
			int prediction = predict(instance);
			error += (label == prediction) ? 0 : 1;
		}
		return error / (Main.currNumOfExamples - Main.currNumOfValidationExamples);
	}
	
	private double evaluateValidationError() {
		double error = 0.0;
		for (int t = 0; t < Main.currNumOfValidationExamples; t++) {
			double[] instance = x[dataSplit[t]];
			int label = y[dataSplit[t]];
			int prediction = predict(instance);
			error += (label == prediction) ? 0 : 1;
		}
		return error / (Main.currNumOfValidationExamples);
	}
}
