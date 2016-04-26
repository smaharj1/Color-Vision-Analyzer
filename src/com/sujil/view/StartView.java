package com.sujil.view;

import com.sujil.robot.Robot;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class StartView {

	
	
	public StartView() {
		initMenu();
	}
	
	public void initMenu() {
		LCD.clear();
		
			
		LCD.drawString("Welcome to K Means",0, 0);
		LCD.drawString("Press Enter", 0, 1);
		

		int button = Button.waitForAnyPress();
		
		if (button == Button.ID_ENTER) {
			// Start the scanning from the robot
			Scan scan = new Scan();
		}
		else if (button == Button.ID_ESCAPE) {
			LCD.clear();
			LCD.drawString("Bye", 0, 0);
		}
	}
	
	public static void main (String[] args) {
		StartView start = new StartView();
	}
}
