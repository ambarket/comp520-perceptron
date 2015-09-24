

import static org.math.plot.plotObjects.Base.LINEAR;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;
import org.math.plot.plotObjects.BaseLabel;

public class FileReaderAndWriter {
	
	
	public static double[][] readX(String basePath, String fileName) {
		double[][] retval = new double[Main.currNumOfExamples][3];
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
				retval[i][0] = 1.0;
				retval[i][1] = Double.valueOf(attributes[0]);
				retval[i][2] = Double.valueOf(attributes[1]);
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
	
	public static void writeTrainingAndValidationErrors(String dataFileName, double[] trainingErrorByIteration, double[] validationErrorByIteration) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Main.errorDataFiles + dataFileName)));
			
			for (int i = 0; i < trainingErrorByIteration.length; i++) {
				bw.write(trainingErrorByIteration[i] + "\t" + validationErrorByIteration[i] + "\n");
			}

			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void plotTrainingAndValidationErrors(String plotFileName, String titleStr, double[] iterationNumber, double[] trainingErrorByIteration, double[] validationErrorByIteration) {

		Plot2DPanel plot = new Plot2DPanel(new double[] {0 , 0 }, new double[] { 1, 1 },  new String[] { LINEAR, LINEAR }, new String[] { "Iteration Number", "Error" });
		plot.addLegend("SOUTH");
		plot.addLinePlot("Training Error", iterationNumber, trainingErrorByIteration);
		plot.addLinePlot("Validation Error", iterationNumber, validationErrorByIteration);

		plot.setFixedBounds(1, -0.1, 1);
		plot.setFixedBounds(0, 0, 1000);
		plot.setLinearSlicing(1, 11);
		plot.setLinearSlicing(0, 10);
		
	    BaseLabel title = new BaseLabel(titleStr, Color.BLUE, 0.5, 1.1);
        title.setFont(new Font("Courier", Font.BOLD, 15));
        plot.addPlotable(title);
		plot.setLinearSlicing(1, 11);
		
		JFrame frame = new JFrame("a plot panel");
		frame.setContentPane(plot);
		frame.setVisible(true);
		frame.setBounds(0, 0, 1000, 500);
		
		  
		BufferedImage bufferedImage = new BufferedImage(1000, 500, BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.createGraphics();
		try {
			Thread.sleep(Main.msBeforeClosingEachGraph);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		frame.paint(g);
	
		g.dispose();
		frame.dispose();
		try {
			ImageIO.write((RenderedImage) bufferedImage, "PNG", new File(Main.errorPlotFiles + plotFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
