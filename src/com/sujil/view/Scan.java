package com.sujil.view;

import com.sujil.model.MainAlgo;
import com.sujil.model.Pixel;
import com.sujil.model.Table;
import com.sujil.robot.Robot;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.TextMenu;

// This class should be present always since 
public class Scan {
	private final int TOTAL = 10;
	private Robot robot;
	
	private Table table;
		
	// It holds the cursor position and the actual cursor
	private final int CURSOR_X = 10;
	private final String CURSOR = "<";
	
	private int K =0;
	private MainAlgo algo;
	
	public Scan() {
		robot = new Robot();
		table = new Table();
		startScan();
		
		while(!Button.ESCAPE.isDown()) {
			LCD.clear();
			
			// Ask for the K value
			K = initiateKMenu();
			
			// Now, call MainAlgo.java which will handle the rest.
			algo = new MainAlgo("rules.txt", robot, table, K);
		}
	}
	
	public int initiateKMenu() {
		LCD.drawString(CURSOR, CURSOR_X, 0);
		
		String [] options = new String[8];
		
		for (int i = 0; i < 8; i++) {
			options[i] = Integer.toString(i+2);
		}
		
		int testsNumber =0;
		
		while (!Button.ENTER.isDown()) {
			TextMenu testsMenu = new TextMenu(options, 1, "Select a test");
			testsNumber = testsMenu.select();		
		}
		
		return testsNumber+2;
	}
	
	
	// Traverse through the robot and then store the pixels in the table
	public void startScan() {
		int row = 0;
		float color;
		
		boolean loop = true;
		boolean isRightDirection = true;
		while (loop) {
			if (isRightDirection) {
				for (int index = 0; index < TOTAL; ++index ) {
					robot.travelOne();
					color = robot.getColor();
					
					table.addPixel( row,  index, color);
					
					//System.out.println(row + "  " + index);
				}
				
				robot.rotateRight();
			}
			else {
				for (int index = TOTAL-1; index >= 0; --index) {
					robot.travelOne();
					color = robot.getColor();
					
					table.addPixel( row,  index, color);
					//System.out.println(row + "  " + index);
				}
				robot.rotateLeft();
			}
			
			isRightDirection = !isRightDirection;
			row++;
			if (row == TOTAL) loop = false;
		}
	}
	
	public static void main (String[] args) {
		Scan scan = new Scan();
		scan.startScan();
	}
}
