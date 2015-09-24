A. Compilation and Execution

	From command line...

		1. cd to the root of directory I sent 
			(Should have src, datasetFiles, and outputFiles folders, along with this README)
		2. javac -cp src/plotStuff.jar src/*.java
		3. java -cp src/plotStuff.jar;src Main
	
	Or From Eclipse...
 
		1. Import eclipse Project, ensure that the plotStuff.jar file is referenced in the build path.
		2. Open the Main.java file
		3. Click Run
		
	What happens when it runs...
		
		1. Reads the datasets from the datasetFiles directory.
		2. Randomly splits the datasets into 80% training, 20% validation
		3. Randomly selects a weight vector with components in the range [-500, 500]. 
			This big range was chosen to give the perceptrons some work to do and to make
			for more interesting graphs. Setting to a vector close to zero allows it to converge
			instantly, and that's not interesting.
		4. For each dataset, 16 graphs will be generated.
			2a. Test the perceptron with 8 different learning rates 0.01, 0.02, 0.04, 0.08, 0.16, 0.32, 0.64, 1.28
			2b. Test each learning rate against two termination criteria. Only max iterations, and one that will stop early
					if its not improving for 50 iterations and the error rate is below 0.2.
		
		
		5. For each of the 32 total tests, it will flash the graph on the screen then close it after half a second.
		It is saving these graphs to .png files in the outputFiles/plots directory. The corresponding raw data 
		for these plots is saved to .txt files in outputFiles/data directory.
		
	Once it's done...
	
		After the program exists go to the outputFiles directory and examine the results.
	

B.Other Notes:

The plotStuff.jar file must be included in the classpath for the plotting to work. This jar consists of the 
following two packages, with some very minor tweaks I made to make things work for me.

https://github.com/yannrichet/jmathplot
https://github.com/yannrichet/jmathio

If you see 'Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException', just ignore it,
it doesn't seem to have any impact on the rendering of the graphs so it doesn't really matter. It's a
known issue with jmathplot that they don't want to fix. I think I got it to stop happening but it could
still occur.
See https://sites.google.com/site/mulabsltd/products/jmathplot/i-often-get-exception-in-thread-awt-eventqueue-0-java-lang-nullpointerexception




