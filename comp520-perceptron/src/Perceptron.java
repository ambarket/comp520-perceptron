

import java.util.Arrays;

/**
 * 
 * @author Austin Barket
 *
 */
public class Perceptron {
	public final double[][] x;
	public final int[] y;
	public final double learningRate;
	
	/**
	 * First 20% entries are the indices of the validation set, remaining 80% are indices of the training set 
	 */
	public final int[] dataSplit;
	
	/**
	 * The weight vector defining this perceptron
	 */
	public double[] w;
	
	public Perceptron(double[][] x, int[] y, int[] dataSplit, double[] startingW, double learningRate) {
		this.x = x;
		this.y = y;
		this.learningRate = learningRate;
		this.dataSplit = dataSplit;
		this.w = startingW;
	}
	
	//----------------------------Separate public method for each termination condition----------------------------------------
	
	/**
	 * @param n
	 */
	public void learnWeights_MaxIterationsOnly(int n) {
		double[] trainingErrorByIteration = new double[n];
		double[] validationErrorByIteration = new double[n];
		double[] iterationNumber = new double[n];
		for (int i = 0; i < n; i ++) {
			learnWeights_SingleEpoch_Original();
			trainingErrorByIteration[i] = evaluateTrainingError();
			validationErrorByIteration[i] = evaluateValidationError();
			iterationNumber[i] = i+1;
		}

		recordErrors("MaxIterationsOnly", iterationNumber, trainingErrorByIteration, validationErrorByIteration);
	}
	

	/**
	 * If the learner goes for maxNoImprovIter iterations without improving upon the lowestErrorSeenSoFar
	 *  AND the lowestErrorSeenSoFar <= maxErrorToTerminate with, the learning process terminates early.
	 * 
	 * If the learner reaches maxTotalIter iterations, it will end regardless of other parameters.
	 * 
	 * Keep track of the weights that gave the lowest validation error seen, and always set these optimal
	 * weights as the perceptron's weights for the final iteration.
	 * 
	 * @param maxTotalIter
	 * @param maxNoImprovIter
	 * @param maxErrorToTerminateWith 
	 */
	public void learnWeights_SuperSmartTermination(int maxTotalIter, int maxNoImprovIter, double maxErrorToTerminateWith) {
		double[] trainingErrorByIteration = new double[maxTotalIter];
		double[] validationErrorByIteration = new double[maxTotalIter];
		double[] iterationNumber = new double[maxTotalIter];
		int iterationsOfNoImprovement = 0;
		double[] bestW = this.w;
		double lowestErrorSeen = evaluateValidationError();
		
		int i = 0;
		while(i < maxTotalIter-1) {
			learnWeights_SingleEpoch_Original();
			trainingErrorByIteration[i] = evaluateTrainingError();
			validationErrorByIteration[i] = evaluateValidationError();
			iterationNumber[i] = i+1;
			
			if (validationErrorByIteration[i] < lowestErrorSeen) {
				lowestErrorSeen = validationErrorByIteration[i];
				bestW = this.w;
				iterationsOfNoImprovement = 0;
			} else {
				iterationsOfNoImprovement++;
			}
			i++;
			// Break early if we've gone k iterations without improving best seen training error, and the error isn't too high
			if (iterationsOfNoImprovement >= maxNoImprovIter && lowestErrorSeen <= maxErrorToTerminateWith) {
				break;
			}
		}
		this.w = bestW;
		trainingErrorByIteration[i] = evaluateTrainingError();
		validationErrorByIteration[i] = evaluateValidationError();
		iterationNumber[i] = i+1;
		
		recordErrors("SuperSmartTermination", Arrays.copyOf(iterationNumber, i+1), Arrays.copyOf(trainingErrorByIteration, i+1), Arrays.copyOf(validationErrorByIteration, i+1));
	}
	
	private void recordErrors(String termConditionUsed, double[] iterationNumber, double[] trainingErrorByIteration, double[] validationErrorByIteration) {
		
		String dataFileName = Main.currDataSet + "-" + termConditionUsed + "-ErrorData-" + String.format("%.2f", learningRate) + "-LR.txt";
		String plotFileName = Main.currDataSet + "-" + termConditionUsed + "-ErrorPlot-" + String.format("%.2f", learningRate) + "-LR.png";
		String plotTitle    = Main.currDataSet + "-" + termConditionUsed + ": Iterations vs Mean Squared Error (Learning Rate: " + String.format("%.2f", learningRate) + ")";
		
		FileReaderAndWriter.writeTrainingAndValidationErrors(dataFileName, trainingErrorByIteration, validationErrorByIteration);
		FileReaderAndWriter.plotTrainingAndValidationErrors(plotFileName, plotTitle, iterationNumber, trainingErrorByIteration, validationErrorByIteration);
	}
	

	//----------------------------Core Learning and Prediction Methods----------------------------------------
	/**
	 * Matrix Magic, thanks Princeton
	 * @param instance
	 * @return
	 */
	private int predict(double[] instance) {
		return (int)Math.signum(Matrix.dot(w, instance));
	}
	
	private void learnWeights_SingleEpoch_Original() {
		for (int t = Main.currNumOfValidationExamples; t < Main.currNumOfExamples; t++) {
			double[] instance = x[dataSplit[t]];
			int label = y[dataSplit[t]];
			int prediction = predict(instance);
			if (prediction != label) {
				this.w = Matrix.add(w, Matrix.multiplyScalar(learningRate * label, instance));
			}
		}
	}

	/**
	 * The suggestion Dr. Chang gave in class to remember the best w seen so far. 
	 * Makes for very uninteresting graphs so will just use the basic algorithm for analysis.
	 */
	private void learnWeights_SingleEpoch_OnlyImprovingWeights() {
		double trainingError = evaluateTrainingError();
		double bestTrainingError = trainingError;
		double[] bestW = this.w;
		for (int t = Main.currNumOfValidationExamples; t < Main.currNumOfExamples; t++) {
			double[] instance = x[dataSplit[t]];
			int label = y[dataSplit[t]];
			int prediction = predict(instance);
			if (prediction != label) {
				this.w = Matrix.add(w, Matrix.multiplyScalar(learningRate * label, instance));
				
				trainingError = evaluateTrainingError();
				if (trainingError < bestTrainingError) {
					bestW = this.w;
					bestTrainingError = trainingError;
				} 
			}
		}
		if (bestTrainingError < trainingError) {
			this.w = bestW;
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
