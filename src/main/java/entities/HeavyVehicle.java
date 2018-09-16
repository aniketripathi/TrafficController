package main.java.entities;

import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.java.map.Crossing;

public class HeavyVehicle extends Vehicle {
	
	private static double imageWidth = 40;
	private static double imageHeight = 40;
	
	private Image image;

	public HeavyVehicle(Road currentRoad, ForwardLane sourceLane, Road destinationRoad, BackwardLane destinationLane, Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		super(currentRoad, sourceLane, destinationRoad, destinationLane, crossing, mapWidth, mapHeight);
		// TODO Auto-generated constructor stub
	}


	
	public static double getImageWidth() {
		return imageWidth;
	}

	public static void setImageWidth(double imageWidth) {
		HeavyVehicle.imageWidth = imageWidth;
	}

	public static double getImageHeight() {
		return imageHeight;
	}

	public static void setImageHeight(double imageHeight) {
		HeavyVehicle.imageHeight = imageHeight;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}



	@Override
	public void draw(GraphicsContext gc) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void computeRegions() {
		// TODO Auto-generated method stub
		
	}
	
	
}
