package com.sujil.view;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class StartView {

	// It holds the cursor position and the actual cursor
	private final int CURSOR_X = 10;
	private final String CURSOR = "<";
	
	public StartView() {
		initMenu();
	}
	
	public void initMenu() {
		LCD.clear();
		
		int x_coord = 0;
		int y_coord = 0;
		
		LCD.drawString(CURSOR, CURSOR_X, 1); 
		
		LCD.drawString("Welcome to K Means",0, 0);
		
		
		
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
}
