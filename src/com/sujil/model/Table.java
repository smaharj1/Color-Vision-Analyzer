package com.sujil.model;

import java.util.ArrayList;

public class Table {

	
	public ArrayList<Pixel> data = new ArrayList<Pixel> ();

	public Table() {

	}
	
	public void addPixel(int row , int col, double color) {
		data.add(new Pixel((float) row, (float) col, color));
	}
	
	public ArrayList<Pixel> getTable() {
		return data;
	}

}
