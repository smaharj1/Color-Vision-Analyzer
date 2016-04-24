package com.sujil.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



import lejos.hardware.Sound;

public class FileAccess {

	// It consists of the arraylist of rule nodes
	private ArrayList<RuleNode> rules = new ArrayList<RuleNode>();
	
	public FileAccess(String filename) {
		// Parses the file and saves the rules individually in line by line basis
		saveRules(filename);
	}
	
	/**
	 * Saves the rules line by line by parsing through the text file
	 * @param filename
	 */
	private void saveRules(String filename) {
		
		try {
			// Parses the rules
			  InputStream is = FileAccess.class.getResource(filename).openStream();
			  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			  String line;
		    while ((line = reader.readLine()) != null) {
		        //System.out.println(line);
		        MainAlgo.addRule(line);
		    }
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Sound.beep();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
		
	/**
	 * It returns all of the rules
	 * @return
	 */
	public ArrayList<RuleNode> getRules() {
		return rules;
	}
}

