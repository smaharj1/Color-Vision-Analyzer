package com.sujil.model;

import java.util.ArrayList;
import java.util.Random;

public class Cluster {
	private ArrayList<Pixel> correctPixels = new ArrayList<Pixel>();
	private Pixel centroid;
	
	private Pixel topLeft;
	private Pixel bottomRight;
	
	public Cluster() {
		
	}
	
	public ArrayList<Pixel> getPixels() {
		return correctPixels;
	}
	
	public void addPixel(Pixel temp) {
		correctPixels.add(temp);
	}
	
	public void setCentroid(Pixel temp) {
		centroid = temp;
	}
	
	public Pixel getCentroid() {
		return centroid;
	}
	
	public void erase() {
		correctPixels.clear();
	}
	
	public void setSideValues() {
		float maxX = (float) -1.0;
		float maxY = (float) -1.0;
		
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		
		for (int index  = 0; index < correctPixels.size(); ++index) {
			if (correctPixels.get(index).getX() > maxX) maxX = correctPixels.get(index).getX();
			if (correctPixels.get(index).getY() > maxY) maxY = correctPixels.get(index).getY();
			
			if (correctPixels.get(index).getX() < minX) minX = correctPixels.get(index).getX();
			if (correctPixels.get(index).getY() < minY) minY = correctPixels.get(index).getY();
		}
		
		bottomRight =  new Pixel (maxX, maxY, 0.0);
		topLeft = new Pixel (minX, minY, 0.0);
	}
	
	public Pixel getTopLeft() {
		return topLeft;
	}
	
	public Pixel getBottomRight() {
		return bottomRight;
	}
	
	public static Pixel getRandomCentroid(ArrayList<Pixel> pixels) {
		float minCoord = 0;
		
		float maxX = (float) -1.0;
		float maxY = (float) -1.0;
		
		for (Pixel pixel : pixels) {
			//System.out.println("X: " + pixel.getX() + " Y: " + pixel.getY());
			if (pixel.getX() > maxX) {
				maxX = pixel.getX();
			}
			
			if (pixel.getY() > maxY) {
				maxY = pixel.getY();
			}
			
		}
		
		Random r = new Random();
		float randomX = r.nextFloat() * maxX;
		float randomY = r.nextFloat() * maxY;
		
		
		return new Pixel(randomX, randomY, 0.0);
	}
	
	
}
