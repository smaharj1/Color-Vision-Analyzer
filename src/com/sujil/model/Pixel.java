package com.sujil.model;

public class Pixel {
	private float xVal;
	private float yVal;
	private float colorVal;
	
	public Pixel(float x, float y, double d) {
		xVal = x;
		yVal = y;
		colorVal = (float) d;		
	}
	
	public void setXY(float x, float y) {
		xVal = x;
		yVal = y;
	}
	
	public float getX() {
		return xVal;
	}
	
	public float getY() {
		return yVal;
	}
	
	public void setColorValue(float data) {
		colorVal = data;
	}
	
	public double getColorValue() {
		return colorVal;
	}
	
	public static float getDistance(Pixel first, Pixel second) {
		return (float) Math.sqrt(Math.pow((second.getX() - first.getX()), 2) + Math.pow((second.getY() - first.getY()), 2));
	}
	
	public static double getColorDistance(Pixel first, Pixel second) {
		return Math.abs(second.getColorValue()-first.getColorValue());
	}
	
	// temporarily created
	public boolean isOn() {
		return true;
	}
	
}
