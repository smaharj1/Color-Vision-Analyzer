package com.sujil.model;

import java.util.ArrayList;
import java.util.Random;

public class Kmeans {
	// This holds the total number of clusters that the user wants
	private int totalClusters;
	
	// It holds all of the clusters present
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		
	// It holds all of the pixels in the board that will later be assigned to various clusters.
	private ArrayList<Pixel> allPixels = new ArrayList<Pixel>();
	
	// It holds the single cluster if the user provides the working set of data. It is important only for filling in all the pixels we will manipulate
	private Cluster workingCluster = new Cluster();
	
	// It holds the value if we are doing it for distance or color. False means color.
	private boolean isWorkingCluster = false;
	
	/**
	 * It initiates the algorithms with K number of clusters.
	 * @param K It holds the total number of clusters to make
	 * @param cluster It holds the cluster if it is already provided. Else, it is NULL
	 */
	public Kmeans(int K, ArrayList<Pixel> pixels, boolean isDistance) {
		// Assigns the value to totalClusters
		totalClusters = K;

		// If the cluster user provided is not null, it means we want to calculate algorithm for distance
		if (isDistance) {
			//workingCluster = cluster;
			isWorkingCluster = true;
			allPixels = pixels;
		}		
		else {
			// Gets all of the given pixels
			allPixels = pixels;
		}
	}
	
	/**
	 * Initiates the K means clustering algorithm by plugging random value for the centroid of each cluster
	 */
	public void init() {
		// If it is the distance-based Kmeans, then, randomizes the coordinates of centroid in the given range
		if (isWorkingCluster) {
			// Randomizes the centroid according to the coordinates and adds to the cluster
			for (int index = 0; index < totalClusters; ++index) {
				Pixel initCentroid = Cluster.getRandomCentroid(allPixels);
				
				Cluster c = new Cluster();
				c.setCentroid(initCentroid);
				
				clusters.add(c);

			}
			
		}
		// Randomize the color value if it is color-based K means
		else {
			// Calculates the range of the color to consider
			double range = (double) 7 / totalClusters;
			
			// Loops through each and every cluster and adds the randomized centroid
			for (int i=0; i < totalClusters; ++i) {
				// Get the random value for each range i.e. add i*range for each random value got.
				Random r = new Random();
				//double randomValue = (i*range) + r.nextDouble() * (range);
				
				double randomValue = i+1;
				//System.out.println("CENTROID: " + randomValue);
				Pixel initCentroid = new Pixel(-1,-1, randomValue);
				
				// Puts the initial pixel in the cluster as the centroid
				Cluster c = new Cluster();
				c.setCentroid(initCentroid);
				
				// Now, adds the cluster to the list of clusters
				clusters.add(c);
			}	
		}
		
	}

	/**
	 * Updates the clusters until the pixels are perfectly grouped together
	 */
	public void update() {
		// It holds the boolean if the clusters are done
		boolean isDone = false;
		
		// Loops until the clusters are perfectly grouped
		while (!isDone) {
			// First of all, clear all of the existing values in the cluster
			for (Cluster currentCluster : clusters) {
				currentCluster.erase();
			}
			
			ArrayList<Pixel> previousCentroids = getAllCentroids();
			
			// Assign the pixels to the clusters
			assignToClusters();
			
			// Recalculate the centroid values 
			recalculateCentroid();
			
			// Check if the new values are same as old. If they are all same in all clusters, then break from the loop
			boolean allCorrect = true;
			for (int index =0; index < totalClusters; ++index) {
				
				if (isWorkingCluster) {
					if (clusters.get(index).getCentroid().getX() != previousCentroids.get(index).getX() || clusters.get(index).getCentroid().getY() != previousCentroids.get(index).getY()) {
						allCorrect = false;
					}
				}
				else {
//					System.out.println("Cluster #: " + index);
//					System.out.println("Previous centroid: " + previousCentroids.get(index).getColorValue());
//					System.out.println("New centroid: " + clusters.get(index).getCentroid().getColorValue());
//					System.out.println();
					if (clusters.get(index).getCentroid().getColorValue() != previousCentroids.get(index).getColorValue()) {
						allCorrect = false;
					}
				}
			}
			
			if (allCorrect) {
				isDone = true;
			}
			
		}
				
		for (Cluster cluster : clusters) {
			cluster.setSideValues();
		}
		
		
		
		// Now, generalize the clusters and make them individually distinct
		generalizeCluster();
		
		// At this point, everything is correctly clustered into their own circle.
		//printClusters();

	}
	
	/**
	 * It generalizes each of the clusters with the initial coordinates (0,0)
	 * Eg. if the starting coordinate of the cluster is (5,5), it changes to (0,0).
	 */
	public void generalizeCluster() {
		// Loops through each clusters and generalizes the coordinates for each pixels in those clusters
		for (Cluster cluster : clusters) {
			float minX = Float.MAX_VALUE;
			float minY = Float.MAX_VALUE;
			
			// Calculate the minimum x coordinate and y coordinates in current list of pixels
			for (Pixel pixel : cluster.getPixels()) {
				if (pixel.getX() < minX) {
					minX = pixel.getX();
				}
				if (pixel.getY() < minY) {
					minY = pixel.getY();
				}
			}
			
			// Now, substract the X and Y coord minimums to get a coordinates in terms of (0,0)
			for (Pixel pixel : cluster.getPixels()) {
				pixel.setXY(pixel.getX() - minX, pixel.getY() - minY);
			}
		}
	}
	
	/**
	 * It gets all of the centroids of the clusters
	 * @return Returns the arraylist of all of the existing centroids
	 */
	public ArrayList<Pixel> getAllCentroids() {
		ArrayList<Pixel> centroids = new ArrayList<Pixel>();
		for (Cluster cluster : clusters) {
			centroids.add(cluster.getCentroid());
		}
		
		return centroids;
	}
	
	/**
	 * It recalculates the centroid value for each clusters
	 */
	public void recalculateCentroid() {
		// Loops to each clusters and gets a new centroid value by taking the average of existing pixels in that cluster
		for (Cluster cluster : clusters) {
			
			// Now, goes through each and every pixel in that cluster
			// Then, it averages the centroid value
			
			if (isWorkingCluster) {
				// If it is distance based K means, then, find the average of X and Y coordinates and set the new centroid value
				float sumX = 0;
				float sumY = 0;
				
				for (Pixel pixel:cluster.getPixels()) {
					sumX += pixel.getX();
					sumY += pixel.getY();
				}
				
				float avgX = sumX / cluster.getPixels().size();
				float avgY = sumY / cluster.getPixels().size();
				
				cluster.setCentroid(new Pixel (avgX, avgY, 0.0));
			}
			else {
				// If it is color based K means, then, set the color value as average of existing pixel's color values
				double sum = 0;
				
				for (Pixel pixel : cluster.getPixels()) {
					sum += pixel.getColorValue();
				}
				
				double avg = sum / cluster.getPixels().size();
				
				
				cluster.setCentroid(new Pixel(-1,-1,avg));
			}
			
		}
	}
	
	/**
	 * It assigns the pixels to its respective coordinates according to the nearest centroids
	 */
	public void assignToClusters() {
		// Loops through all the pixels and finds the nearest cluster to each of them
		for (Pixel pixel : allPixels) {
			//System.out.println("Assigning: " + pixel.getX() + "  " + pixel.getY());
			double minDistance = Double.MAX_VALUE;
			Cluster actualCluster = clusters.get(0);
			//System.out.println(pixel.getX() + "  " + pixel.getY() + "  " + pixel.getColorValue());
			// Loops through each cluster and checks for the nearest centroid from the pixel
			for (Cluster cluster : clusters) {
				Pixel centroid = cluster.getCentroid();
				
				if (isWorkingCluster) {
					double distance = Pixel.getDistance(pixel, centroid);
					if (distance < minDistance) {
						minDistance = distance;
						actualCluster = cluster;
					}
				}
				else  {
					double colorDistance = Pixel.getColorDistance(pixel, centroid); 
					if ( colorDistance < minDistance) {
						minDistance = colorDistance;
						actualCluster = cluster;
					}
				}
			}
			
			//System.out.println(("Assigning to " + actualCluster.getCentroid().getColorValue()));
			actualCluster.addPixel(pixel);
			
		}
	}
	
	/**
	 * Prints the cluster values
	 */
	public void printClusters() {
		for (int i=0; i < totalClusters; i++) {
			System.out.println("Cluster: " + i);
			System.out.println("Centroid: " + clusters.get(i).getCentroid().getColorValue());
			
			ArrayList<Pixel> pixels = clusters.get(i).getPixels();
			for (int j = 0; j < clusters.get(i).getPixels().size(); ++j) {
				System.out.println("Pixels: " + pixels.get(j).getY() + "  " + pixels.get(j).getX() + "  " + pixels.get(j).getColorValue());
			}
			
			System.out.println();
			System.out.println();
		}
	}
	
	/**
	 * Gets the clusters
	 * @return Returns all of the clusters
	 */
	public ArrayList<Cluster> getClusters () {
		return clusters;
	}
	
	public int getTotalClusters() {
		return totalClusters;
	}
	
	
	/**
	 * This is the prototype to run in the Eclipse. This prototype should be applied to the concept of Lejos
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Kmeans kmeans = new Kmeans(6, null);
//		kmeans.init();
//		kmeans.update();
//		
//		System.out.println("------------------XXXXXXXXXXXXXX-------------------");
//		System.out.println("Entering distance mode");
//		System.out.println("------------------XXXXXXXXXXXXXX-------------------");
//		// Now get all of the clusters and then, do the K means on each cluster
//		for (Cluster cluster : kmeans.getClusters()) {
//			Kmeans kForDistance = new Kmeans(2, cluster);
//			kForDistance.init();
//			kForDistance.update();
//		}
	}

}
