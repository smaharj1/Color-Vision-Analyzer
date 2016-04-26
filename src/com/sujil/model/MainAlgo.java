package com.sujil.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sujil.robot.Robot;
import com.sujil.view.Scan;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class MainAlgo {
	
	private ArrayList<Pixel> boardPixels = new ArrayList<Pixel>();
	
	
	private final int TOTAL_ROW_COL = 24;
	// Holds all of the possible rules given certain pixel as ON or OFF
	private ArrayList<RuleNode> possibleRules = new ArrayList<RuleNode>();
	private ArrayList<RuleNode> rejectedRules = new ArrayList<RuleNode>();
	
	// Holds the previous state of the node if in case we get a null value while scanning
	private ArrayList<RuleNode> previousState = new ArrayList<RuleNode>();
	
	
	private FileAccess fileAccess;
	private Robot robot;
	private ArrayList<Pixel> scannedTable = new ArrayList<Pixel>();
	private int Kval =0;
	
	private static ArrayList<RuleNode> rules = new ArrayList<RuleNode> ();
	
	Kmeans kmeans;
	Kmeans kForDistance;
	
	public MainAlgo(String filename, Robot r, Table table, int Kval) {
		fileAccess = new FileAccess(filename);
		
		robot = r;

		//getBoard();
		
		//scannedTable = table.getTable();
		scannedTable = table.getTable();
		
		this.Kval = Kval;

	}
	
	public void performLejos() {
		// Now run the first K means clustering for color
		kmeans = new Kmeans(Kval, scannedTable, false);
		kmeans.init();
		kmeans.update();
		
		// Then print out the total # of clusters and total pixels in it
		//System.out.println("Total # of clusters: " + kmeans.getTotalClusters());
		LCD.drawString("Total # of clusters: " + kmeans.getTotalClusters(),0,0);
		
		robot.delay(2000);
		
		for (Cluster cluster : kmeans.getClusters()) {
			LCD.clear();
			
			Pixel topLeft = cluster.getTopLeft();
			Pixel bottomRight = cluster.getBottomRight();
			
			//System.out.println("TopLeft: " + topLeft.getX() + "  " + topLeft.getY());
			//System.out.println("BottomRight: " + bottomRight.getX() + "  " + bottomRight.getY());
			
			
			LCD.drawString("Total # of clusters: " + kmeans.getTotalClusters(), 0, 0);
			LCD.drawString("TL: " + topLeft.getX() + "  " + topLeft.getY(), 0, 1);
			LCD.drawString("BR: " + bottomRight.getX() + "  " + bottomRight.getY(), 0, 2);
			
			int button = Button.waitForAnyPress();
			
			if (button == Button.ID_ENTER) {
				// Start the scanning from the robot
				continue;
			}
			else if (button == Button.ID_ESCAPE) {
				return;
			}
		}
		
		// Pause the LCD in the robot for some time (10 sec)
		
		// Runs the K means for distance
		//System.out.println("------------------XXXXXXXXXXXXXX-------------------");
		//System.out.println("Entering distance mode");
		//System.out.println("------------------XXXXXXXXXXXXXX-------------------");
		LCD.clear();
		LCD.drawString("DISTANCE MODE ON", 0, 0);
		
		Delay.msDelay(2000);
		// Now get all of the clusters and then, do the K means on each cluster
		for (Cluster cluster : kmeans.getClusters()) {
			if (cluster.getPixels().size() <=1) {
				continue;
			}
			kForDistance = new Kmeans(2, cluster.getPixels(), true);
			kForDistance.init();
			kForDistance.update();
			
			// Prints out the total # of clusters and total pixels in it
			//System.out.println("Total # of clusters: " + kForDistance.getTotalClusters());
			LCD.drawString(kForDistance.getTotalClusters() + " # of clusters: " , 0, 0);
			
			
			for (Cluster cls : kForDistance.getClusters()) {
				LCD.clear();
				Pixel topLeft = cls.getTopLeft();
				Pixel bottomRight = cls.getBottomRight();
				int row = 0;
				
				LCD.drawString("TL: " + topLeft.getX() + "  " + topLeft.getY(), 0, 0);
				LCD.drawString("BR: " + bottomRight.getX() + "  " + bottomRight.getY(), 0, 1);
				
				// Runs forward search
				startForwardSearch(cls);
				
				int button = Button.waitForAnyPress();
				
				if (button == Button.ID_ENTER) {
					// Start the scanning from the robot
					continue;
				}
				else if (button == Button.ID_ESCAPE) {
					return;
				}
			}
			LCD.drawString("Cluster over", 0, 4);
			Delay.msDelay(2000);
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
			//System.out.println("Empty");
			LCD.drawString("Empty", 0, 2);
		}
		else {
			//System.out.println("Letter: " + possibleRules.get(0).getRHS());
			LCD.drawString("Letter: " + possibleRules.get(0).getRHS(), 0, 2);
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
		//MainAlgo algo = new MainAlgo("rules.txt", /*new Robot(),*/ new Table(), 8);
	}

	
}
