package edu.psu.hbg.cs.amb6470;

import edu.princeton.cs.introcs.Matrix;

public class Perceptron {

	/**
	 * First 200 entries are the indices of the validation set, remaining 800 are indices of the training set 
	 */
	int[] dataSplit;
	double[][] x;
	int[] y;
	double learningRate;
	
	double[] w;
	double trainingError;
	double validationError;
	
	public Perceptron(double[][] x, int[] y, double learningRate) {
		this.dataSplit = DataSetSplitter.fisherYatesShuffle();
		this.x = x;
		this.y = y;
		this.learningRate = learningRate;
		
		this.w = new double[2];
		trainingError = 1;
		validationError = 1;
	}
	
	public int predict(double[] instance) {
		return (int)Math.signum(Matrix.dot(w, instance));
	}
	
	/**
	 * 
	 * @param dataSplit 
	 * @param x
	 * @param y
	 * @param learningRate
	 * @return
	 */
	public double[] learnWeights(int[] dataSplit, double[][] x, int[] y, double learningRate) {
		
		for (int t = 200; t < 1000; t++) {
			double[] instance = x[dataSplit[t]];
			int label = y[dataSplit[t]];
			int prediction = predict(instance);
			if (prediction != label) {
				double[] nextW = Matrix.add(w, Matrix.multiplyScalar(learningRate * label, instance));
				
				double nextTrainingError = evaluateTrainingError();
				if (nextTrainingError < trainingError) {
					this.w = nextW;
					this.trainingError = nextTrainingError;
				} 
			}
		}
		
		return w;
	}
	
	public double evaluateTrainingError() {
		double error = 0.0;
		for (int t = 200; t < 1000; t++) {
			double[] instance = x[dataSplit[t]];
			int label = y[dataSplit[t]];
			int prediction = predict(instance);
			error += (label == prediction) ? 0 : 1;
		}
		return error / 800;
	}
	
	public double evaluateValidationError() {
		double error = 0.0;
		for (int t = 0; t < 200; t++) {
			double[] instance = x[dataSplit[t]];
			int label = y[dataSplit[t]];
			int prediction = predict(instance);
			error += (label == prediction) ? 0 : 1;
		}
		return error / 200;
	}
}
