package com.sujil.view;

import com.sujil.model.MainAlgo;
import com.sujil.model.Pixel;
import com.sujil.model.Table;
import com.sujil.robot.Robot;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.utility.TextMenu;

// This class should be present always since 
public class Scan {
	private final int TOTAL = 6;
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
		//startScan();
		
		robot.startSensor();
		robot.getColor();
		for (int i =0; i < 6; i++) {
			robot.travelOne();
			float val = robot.getColor();
			
			LCD.drawString(val+"", 0, i);
			table.addPixel(0, i, val);
		}
		
		
		
		while(!Button.ESCAPE.isDown()) {
			LCD.clear();
			
			// Ask for the K value
			K = initiateKMenu();
			
			// Now, call MainAlgo.java which will handle the rest.
			algo = new MainAlgo("rules.txt", robot, table, K);
			
			LCD.clear();
			LCD.drawString("EXIT", 0, 0);
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
		robot.startSensor();
		
		boolean loop = true;
		boolean isRightDirection = true;
		while (loop) {
			
			if (isRightDirection) {
				LCD.clear();
				robot.forward();
				for (int index = 0; index < TOTAL; ++index ) {
					//robot.travelOne();
					Delay.msDelay(robot.getTime());
					color = robot.getColor();
					
					LCD.drawString(color+"", 0, index);
					table.addPixel( row,  index, color);
					
					//System.out.println(row + "  " + index);
				}
				robot.stop();
				robot.rotateRight();
			}
			else {
				LCD.clear();
				robot.forward();
				for (int index = TOTAL-1; index >= 0; --index) {
					//robot.travelOne();
					Delay.msDelay(robot.getTime());
					color = robot.getColor();
					
					LCD.drawString(color+"", 0, TOTAL - index);
					
					table.addPixel( row,  index, color);
					//System.out.println(row + "  " + index);
				}
				robot.stop();
				robot.rotateLeft();
			}
			
			robot.delay(5000);
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
