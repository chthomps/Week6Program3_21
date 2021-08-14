//Christopher Thompson
//Date: 081321
//Programming Fundamentals
//Summer 2021
//Program 3- Machine Learning

import java.io.*;
import java.util.Scanner;

public class NearestNeighbor {

	// Variables- arrays where double data or Strings will be stored 
	static double[][] testAttributeArray;
	static double[][] trainAttributeArray;
	static String[] testClassLabelArray;
	static String[] trainClassLabelArray;

	
	
	// Main Method
	public static void main(String[] args) throws IOException {

		
	        //headers
		System.out.println("Programming Fundamentals");
		System.out.println("Name: Christopher Thompson");
		System.out.println("PROGRAMMING ASSIGNMENT 3");
		System.out.println();

		
		
		// User interface- determining which csv file is passed
		Scanner scan = new Scanner(System.in);

		System.out.print("Enter the name of the training file: ");
		String inTrainFile = scan.nextLine(); // inTrainFile object passed to makeAttributeArray method
		inTrainFile = ("/Users/thompsonc/Desktop/" + inTrainFile); // update for GitHub path pre-submission

		// Call makeAttributeArray method and makeClassArray method on specified training file- save output to array
		trainAttributeArray = NearestNeighbor.makeAttributeArray(inTrainFile);
		trainClassLabelArray = NearestNeighbor.makeClassArray(inTrainFile);

		
		
		System.out.print("Enter the name of the testing file: ");
		String inTestFile = scan.nextLine(); // inTestFile object passed to makeAttributeArray method
		inTestFile = ("/Users/thompsonc/Desktop/" + inTestFile);
		scan.close();

		
		System.out.println();
		System.out.println("EX#: TRUE LABEL, PREDICTED LABEL");
		
		
		// Call makeAttributeArray method and makeClassArray method on specified import file - save output to array
		testAttributeArray = NearestNeighbor.makeAttributeArray(inTestFile);
		testClassLabelArray = NearestNeighbor.makeClassArray(inTestFile);

		
		// evalDistance method acts on inputs from the following arrays:
		NearestNeighbor.evalDistance(testAttributeArray, trainAttributeArray, testClassLabelArray, trainClassLabelArray);

	}

	// 2D Array Loading Method (employed for both train & test)
	public static double[][] makeAttributeArray(String inAttributes) { // Data passed in from u.i. (as a string)

		File inFile = new File(inAttributes); // assigns inAttributes as "infile" for the purposes of parsing data below

		// Variables
		double[][] attributeArray = new double[75][4]; // sets output array
		String line = "";
		int row = 0;

		try {
			Scanner scanFile = new Scanner(inFile);

			while (scanFile.hasNextLine()) { // reads csv file line-by-line

				line = scanFile.nextLine();
				String[] tempLine = line.split(","); // tempLine array, splitting each line of input file

				for (int col = 0; col < 4; col++) { // process each line (element by element), parsing into attributeArray
					attributeArray[row][col] = Double.parseDouble(tempLine[col]);
				}
				row++; // iterate row

			}
			scanFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return attributeArray; // 2D array output as doubles

	}

	// 1D Array Loading Method (employed for both train & test)

	public static String[] makeClassArray(String inClass) {

		File inFile = new File(inClass); // assigns inClass as "infile" for the purposes of copying in iris species data

		// Variables
		String[] classArray = new String[75]; // sets output array
		String line = "";
		int row = 0;

		try {
			Scanner scanFile = new Scanner(inFile);

			while (scanFile.hasNextLine()) { // reads single csv file line (should just be one line)

				line = scanFile.nextLine();
				String[] tempLine = line.split(","); // tempLine array, splitting each line of input file

				classArray[row] = tempLine[tempLine.length - 1]; // only write in col index[4]
				row++;
			}

			scanFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return classArray; // 1D array String output of species 

	}

	// Calculating Distances Method (apply NNN algorithm)
	public static double distanceCalc(double[] testA, double[] trainA) {

		double dist = 0;
		double descriminant = 0;

		descriminant = (Math.pow(testA[0] - trainA[0], 2) + Math.pow(testA[1] - trainA[1], 2)
				+ Math.pow(testA[2] - trainA[2], 2) + Math.pow(testA[3] - trainA[3], 2));

		dist = Math.sqrt(descriminant);

		return dist;
	}

	// Class Compare method- compare class labels from testing file to class names in training file seeking a match
	public static boolean classCompare(int testRow, int trainRow, String[] testClass, String[] trainClass) {

		if (testClass[testRow].equals(trainClass[trainRow])) {
			return true;
		} else {
			return false;
		}
	}

	// Looping Method for calculating distance- looping through training file to find NNN (smallest dist)
	public static void evalDistance(double[][] testAttributes, double[][] trainAttributes, String[] testClass,
			String[] trainClass) {

		String[] predictClass = new String[75]; // predictClass array will house the predicted training class label

		int count = 0;

		// loop taking one testing row (species) at a time
		for (int testRow = 0; testRow < 75; testRow++) {
			int currentMinTrainRow = 0;  // variable tracking minimum training row below
			double currentMinDist = 100; // initializing with a "high" number that will exceed any dist return
			

			for (int trainRow = 0; trainRow < 75; trainRow++) { // nested for loop pitting the above test row against the entirety of the training file
				// call distanceCalc method
				double currentDist = NearestNeighbor.distanceCalc(testAttributes[testRow], trainAttributes[trainRow]); 																						
				if (currentDist < currentMinDist) { // updates minimum distance
					currentMinDist = currentDist;
					currentMinTrainRow = trainRow;
				}

			}
			// print NNN training class species names across from the testing species at issue
			predictClass[testRow] = trainClass[currentMinTrainRow];
			System.out.println((testRow + 1) + ":" + " " + testClass[testRow] + " " + predictClass[testRow]);

			// number of matches- calls classCompare method on each line of the above evaluation
			if (NearestNeighbor.classCompare(testRow, currentMinTrainRow, testClass, trainClass)) {
				count += 1;
			}

		}

		// % match
		System.out.println("ACCURACY: " + (double) count / 75);
	}

}
