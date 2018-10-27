package main.java.entities;

import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.java.entities.Vehicle.Type;
import main.java.map.Crossing;

public class TwoWheeler extends Vehicle {

	private Type type = Type.TWO_WHEELER;

	private static double imageWidth = 40;
	private static double imageHeight = 40;

	private Image image;

	public TwoWheeler(Road sourceRoad, ForwardLane sourceLane, Road destinationRoad, BackwardLane destinationLane,
			Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		super(sourceRoad, sourceLane, destinationRoad, destinationLane, crossing, mapWidth, mapHeight);
		// TODO Auto-generated constructor stub
	}

	public static double getImageWidth() {
		return imageWidth;
	}

	public static void setImageWidth(double imageWidth) {
		TwoWheeler.imageWidth = imageWidth;
	}

	public static double getImageHeight() {
		return imageHeight;
	}

	public static void setImageHeight(double imageHeight) {
		TwoWheeler.imageHeight = imageHeight;
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

	public Type getType() {
		return this.type;
	}
}
