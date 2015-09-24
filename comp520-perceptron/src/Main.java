

/**
 * 
 * @author Austin Barket
 *
 */
public class Main {
	/**
	 * Allows you to set the timeout for the graph to stay on the screen in the JPanel. Result of annoying thing with the 
	 * plot framework, I couldn't figure out how to get the legend to save to the file with the graph without rendering 
	 * it in a JPanel first.
	 */
	public static final int msBeforeClosingEachGraph = 500;
	
	/**
	 * Hopefully this will just work when you try to run it, otherwise may have to hard code some valid paths here.
	 */
	public static final String datasetFiles = System.getProperty("user.dir") + "/datasetFiles/";
	public static final String errorDataFiles = System.getProperty("user.dir") + "/outputFiles/data/";
	public static final String errorPlotFiles = System.getProperty("user.dir") + "/outputFiles/plots/";
	
	public static final String linear_x = "linear_x.dat";
	public static final String linear_y = "linear_y.dat";
	public static final String nonlinear_x = "nonlinear_x.dat";
	public static final String nonlinear_y = "nonlinear_y.dat";
	
	/** Used By FileReader to make arrays, and DataSplitter to select the correct number of validation examples, then
	 *	used by Perceptron to calculate training and validation error separately and only iterate over the training
	 *  examples while training.
	 */
	public static final int linearNumOfExamples = 488;
	public static final int nonlinearNumOfExamples = 99;
	public static final int linearNumOfValidationExamples = (int)Math.floor(0.2 * Main.linearNumOfExamples);
	public static final int nonlinearNumOfValidationExamples = (int)Math.floor(0.2 * Main.nonlinearNumOfExamples);
	public static String currDataSet;
	public static int currNumOfExamples;
	public static int currNumOfValidationExamples;
	
	/**
	 * Termination Condition Parameters
	 */
	public static final int MAX_ITERATIONS = 1000;
	public static final int MAX_ITERATIONS_NO_IMPROV = 50;
	public static final double MAX_TERMINATION_ERROR = 0.2;
	
	public static void main(String[] args) {
		/**
		 * Use the same starting weights across all tests to make fair comparison of learning rates. 
		 * 
		 * Select weights from a relatively large range of values to make graphs more interesting. Initializing it to the 
		 * zero vector (or anything close to the zero vector) allows the perceptron to reach its minimum error on first epoch for
		 * both linear and non linear, and that's not interesting at all.
		 * 
		 * The range [-500, 500] seems to be interesting enough
		 * 
		 * Obviously if this were actually to be used for a data mining application you'd use small random weights from a gaussian 
		 * distribution around 0 as discussed in class. 
		 */
		double[] startingW = new double[3];
		startingW[0] = DataSplitter.rand.nextDouble() * DataSplitter.rand.nextInt(500) * (DataSplitter.rand.nextBoolean() ? 1 : -1); 
		startingW[1] = DataSplitter.rand.nextDouble() * DataSplitter.rand.nextInt(500) * (DataSplitter.rand.nextBoolean() ? 1 : -1); 
		startingW[2] = DataSplitter.rand.nextDouble() * DataSplitter.rand.nextInt(500) * (DataSplitter.rand.nextBoolean() ? 1 : -1); 
		
		double[][] x = null;
		int[] y =  null;
		int[] dataSplit = null; 
		for (int test = 0; test <= 1; test++) {
			switch(test) {
				case 0: 
					currDataSet = "Linear";
					currNumOfExamples = linearNumOfExamples;
					currNumOfValidationExamples = linearNumOfValidationExamples;
					x = FileReaderAndWriter.readX(datasetFiles, linear_x);
					y = FileReaderAndWriter.readY(datasetFiles, linear_y);
					dataSplit = DataSplitter.fisherYatesShuffle(); // Use same partitioning for all learning rates for fair comparison
					break;
				case 1:
					currDataSet = "NonLinear";
					currNumOfExamples = nonlinearNumOfExamples;
					currNumOfValidationExamples = nonlinearNumOfValidationExamples;
					x = FileReaderAndWriter.readX(datasetFiles, nonlinear_x);
					y = FileReaderAndWriter.readY(datasetFiles, nonlinear_y);
					dataSplit = DataSplitter.fisherYatesShuffle(); // Use same partitioning for all learning rates for fair comparison
					break;
			}
			
			for (double learningRate = .01; learningRate < 1.29; learningRate = learningRate *= 2) {
				Perceptron perceptron = new Perceptron(x, y, dataSplit, startingW, learningRate);
				perceptron.learnWeights_MaxIterationsOnly(MAX_ITERATIONS);
				perceptron.w = startingW; // Reset weights to learn again
				perceptron.learnWeights_SuperSmartTermination(MAX_ITERATIONS, MAX_ITERATIONS_NO_IMPROV, MAX_TERMINATION_ERROR);
			}
		}
	}
}
