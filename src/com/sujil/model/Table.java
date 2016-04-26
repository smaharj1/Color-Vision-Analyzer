package com.sujil.model;

import java.util.ArrayList;

public class Table {

	
	public ArrayList<Pixel> data = new ArrayList<Pixel> ();
	public Pixel[] pixel = new Pixel[25];
	
	public Table() {

	}
	
	public void addPixel(float f , float g, double color) {
		data.add(new Pixel((float) f, (float) g, color));
	}
	
	public ArrayList<Pixel> getTable() {
		return data;
	}
}
