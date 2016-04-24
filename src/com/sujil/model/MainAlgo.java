package com.sujil.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sujil.robot.Robot;

public class MainAlgo {
	
	private ArrayList<Pixel> boardPixels = new ArrayList<Pixel>();
	
	
	private final int TOTAL_ROW_COL = 24;
	// Holds all of the possible rules given certain pixel as ON or OFF
	private ArrayList<RuleNode> possibleRules = new ArrayList<RuleNode>();
	private ArrayList<RuleNode> rejectedRules = new ArrayList<RuleNode>();
	
	// Holds the previous state of the node if in case we get a null value while scanning
	private ArrayList<RuleNode> previousState = new ArrayList<RuleNode>();
	
	
	private FileAccess fileAccess;
	//private Robot robot;
	private ArrayList<Pixel> scannedTable = new ArrayList<Pixel>();
	
	private static ArrayList<RuleNode> rules = new ArrayList<RuleNode> ();
	
	Kmeans kmeans;
	Kmeans kForDistance;
	
	public MainAlgo(String filename, /*Robot r,*/ Table table) {
		fileAccess = new FileAccess(filename);
		
		//robot = r;

		getBoard();
		
		//scannedTable = table.getTable();
		scannedTable = boardPixels;
		
		// Now run the first K means clustering for color
		kmeans = new Kmeans(7, scannedTable, false);
		kmeans.init();
		kmeans.update();
		
		// Then print out the total # of clusters and total pixels in it
		System.out.println("Total # of clusters: " + kmeans.getTotalClusters());
		
		for (Cluster cluster : kmeans.getClusters()) {
			Pixel topLeft = cluster.getTopLeft();
			Pixel bottomRight = cluster.getBottomRight();
			
			System.out.println("TopLeft: " + topLeft.getX() + "  " + topLeft.getY());
			System.out.println("BottomRight: " + bottomRight.getX() + "  " + bottomRight.getY());
		}
		
		// Pause the LCD in the robot for some time (10 sec)
		
		// Runs the K means for distance
		System.out.println("------------------XXXXXXXXXXXXXX-------------------");
		System.out.println("Entering distance mode");
		System.out.println("------------------XXXXXXXXXXXXXX-------------------");
		// Now get all of the clusters and then, do the K means on each cluster
		for (Cluster cluster : kmeans.getClusters()) {
			kForDistance = new Kmeans(2, cluster.getPixels(), true);
			kForDistance.init();
			kForDistance.update();
			
			// Prints out the total # of clusters and total pixels in it
			System.out.println("Total # of clusters: " + kForDistance.getTotalClusters());
			
			for (Cluster cls : kForDistance.getClusters()) {
				Pixel topLeft = cls.getTopLeft();
				Pixel bottomRight = cls.getBottomRight();
				
				System.out.println("TopLeft: " + topLeft.getY() + "  " + topLeft.getX());
				System.out.println("BottomRight: " + bottomRight.getY() + "  " + bottomRight.getX());
				// Runs forward search
				startForwardSearch(cls);
			}
		}

	}
	
	public void startForwardSearch(Cluster cluster) {
		initiateForwardCondition();
		
		// Gets all of the pixels that are ON (Cluster only has ON pixels by default)
		ArrayList<Pixel> boxes = cluster.getPixels();
		
		//System.out.println("Size of possible Rules is " + possibleRules.size());
		
		for (int index= 0; index < boxes.size(); ++index) {
			Pixel currentPixel = boxes.get(index);
			
			updatePossibilities(currentPixel);
			
			// If the possibleRules is not empty, then save it as previousState too
			if (!possibleRules.isEmpty()) {
				previousState = possibleRules;
			}
			else {
				possibleRules = previousState;
			}
		}
		
		//System.out.println("Size of possible Rules is " + possibleRules.size());
		if (possibleRules.isEmpty()) {
			
		}
		else {
			System.out.println("Possible rule: " + possibleRules.get(0).getRHS());
		}
		
	}
	
	private void updatePossibilities(Pixel pixel) {
		for (int index = 0; index < possibleRules.size(); ++index) {
			//System.out.println(pixel.getY() + "  " + pixel.getX() + " in " + possibleRules.get(index).getRHS());
			if (!RuleNode.checkIfExists(pixel, possibleRules.get(index))) {
				//System.out.println("Doesn't exist");
				possibleRules.remove(index);
				index--;
			}
			else {
				//System.out.println("Exists");
			}
		}
	}
	
	private void initiateForwardCondition() {
		possibleRules = new ArrayList<RuleNode>();
		previousState = new ArrayList<RuleNode>();
		possibleRules.addAll(rules);
		previousState.addAll(rules);
	}
	
	public static void addRule(String str) {
		RuleNode temp = new RuleNode(str);
		rules.add(temp);
		
		//System.out.println(rules.size() + "is the size");
		// Adds the RuleNode to the rule
		//rhsRules.add(temp.getRHS());
	}
	
	/**
	 * Gets the rule coordinates of left hand side given the string on the right hand side
	 * @param value It holds the string value of RHS
	 * @return Returns the arraylist of pixels in LHS
	 */
	public static ArrayList<Pixel> getRules(String value) {
		if (rules.isEmpty()) {
			return null;
		}
		
		for (int i =0; i < rules.size(); ++i){
			//System.out.println(rules.get(i).getRHS());
			if (value.equals(rules.get(i).getRHS())) {
				return rules.get(i).getLHSPixels();
			}
		}
		
		return null;
	}
	


	private void getBoard() {
		//boardPixels = new ArrayList<Pixel>();
		try {
			FileReader fr = new FileReader("test");
			BufferedReader reader = new BufferedReader(fr);
			String line;
			try {
				int i =0;
					while ((line = reader.readLine()) != null) {
						String [] str = line.split(" ");
						//System.out.println(str.length);
						for (int m = 0; m < str.length; m++) {
							//System.out.println(str[m]);
							if (!str[m].isEmpty()) {
								if (Float.parseFloat(str[m]) == 0.0) continue;
								boardPixels.add(new Pixel((float) i, (float) m, Float.parseFloat(str[m])));
							}
						}
						
						i++;
					}
							
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	public static void main (String[] args) {
		MainAlgo algo = new MainAlgo("rules.txt", /*new Robot(),*/ new Table());
	}

	
}
