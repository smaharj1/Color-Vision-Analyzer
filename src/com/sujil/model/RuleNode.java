package com.sujil.model;

import java.util.ArrayList;

public class RuleNode {
	// It holds the LHS, RHS, and if LHS has characters
	private ArrayList<Pixel> lhs = new ArrayList<Pixel>();
	private String lhsCharacter;
	private String rhs = "";
	private boolean hasLHSChar = false;
	
	/**
	 * Constructor
	 * @param singleRule String equivalent of the rule
	 */
	public RuleNode(String singleRule) {
		parseString(singleRule);
	}
	
	/**
	 * It parsees the string and stores it into the relative variables
	 * @param singleRule
	 */
	public void parseString(String singleRule) {
		boolean recordMode = false;
		
		// Splits the string with ->
		String[] str = singleRule.split("->");
		
		// Gets the right hand side of the rule
		for (int index = 0 ; index < str[1].length(); ++index) {
			if (str[1].charAt(index) == ' ') {
				continue;
			}
			else {
				rhs += str[1].charAt(index);
			}
		}
		//System.out.println(rhs);
		
		int lhsIndex =0;
		String lhsStr = str[0];
		
		// Loops through the string and eliminates anything with spaces or comma.
		// Makes pixels and stores in the LHS
		while (lhsIndex < lhsStr.length()) {
			if ( lhsStr.charAt(lhsIndex) == ' ' || lhsStr.charAt(lhsIndex) == ',') {
				lhsIndex++;
				continue;
			}
			if ( lhsStr.charAt(lhsIndex) == '(') {
				// Record the next indexes
				lhsIndex ++;
				int[] coord = new int[2];
				int tempIndex =0;
				while (lhsStr.charAt(lhsIndex) != ')') {
					// At this point, we have to record numbers
					if (lhsStr.charAt(lhsIndex) != ' ' && lhsStr.charAt(lhsIndex) != ',') {
						coord[tempIndex] = Integer.parseInt(lhsStr.charAt(lhsIndex)+"");
						tempIndex++;
					}
					lhsIndex++;
				}
				
				// compute the coordinates and put it in the node
				lhs.add(new Pixel(coord[0], coord[1], 0.0));
				
				//System.out.println(coord[0] + " and " + coord[1]);
				continue;
			}
			else {
				// At this point, it consists of characters
				if (lhsStr.charAt(lhsIndex) != ')') {
					lhsCharacter = Character.toString(lhsStr.charAt(lhsIndex));
					
					lhsIndex++;
					while (lhsIndex < lhsStr.length() && (lhsStr.charAt(lhsIndex) != ' ' && lhsStr.charAt(lhsIndex) != ',')) {
						lhsCharacter += lhsStr.charAt(lhsIndex);
						lhsIndex++;
					}

					ArrayList<Pixel> temp = MainAlgo.getRules(lhsCharacter);
					
					lhs.addAll(temp);
					
					hasLHSChar = true;

					continue;
				}
			}
						
			lhsIndex++;
		}
		
		//System.out.println("The total size is " + lhs.size()+ " for " + rhs);
	}
	
	/**
	 * Gets the RHS string
	 * @return Returns the RHS
	 */
	public String getRHS() {
		return rhs;
	}
	
	/**
	 * Returns the pixels in LHS
	 * @return Returns the pixels in LHS
	 */
	public ArrayList<Pixel> getLHSPixels () {
		return lhs;
	}
	
	/**
	 * Returns the LHS Character
	 * @return Returns the LHS Character
	 */
	public String getLHSCharacter() {
		return lhsCharacter;
	}
	
	public boolean lhsHasChar () {
		return hasLHSChar;
	}
	
	/**
	 * Checks if the pixel exists in a RuleNode. It is a static function since it does not depend of the class
	 * @param pixel It is the pixel to be searched
	 * @param ruleNode It is the rule node to be looked into
	 * @return Returns true if the pixel exists in the ruleNode. Else, false
	 */
	public static boolean checkIfExists(Pixel pixel, RuleNode ruleNode) {
		ArrayList<Pixel> lhsRules = ruleNode.getLHSPixels();
		
		for (int index=0; index < lhsRules.size(); ++index){
			if (pixel.getX() == lhsRules.get(index).getX() && pixel.getY() == lhsRules.get(index).getY()) {
				//System.out.println(pixel.getRow() + "   " + pixel.getColumn()+ " in " + ruleNode.getRHS());
				return true;
			}
		}
		
		return false;
	
	}
}


