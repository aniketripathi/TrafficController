package main.java.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import main.java.map.Crossing;
import main.java.util.MathEngine;
import main.java.util.Scale;

public class Car extends Vehicle {

	private static final String IMAGE_TOP_TO_BOTTOM_URL = "/main/resources/images/car_top_to_bottom.png";
	private static final String IMAGE_BOTTOM_TO_TOP_URL = "/main/resources/images/car_bottom_to_top.png";
	private static final String IMAGE_LEFT_TO_RIGHT_URL = "/main/resources/images/car_left_to_right.png";
	private static final String IMAGE_RIGHT_TO_LEFT_URL = "/main/resources/images/car_right_to_left.png";

	public static DoubleProperty imageWidth = new SimpleDoubleProperty(
			Scale.CAR_LENGTH_METERS * Scale.pixelToMeterRatio);
	public static DoubleProperty imageHeight = new SimpleDoubleProperty(
			Scale.CAR_WIDTH_METERS * Scale.pixelToMeterRatio);

	private Image image_top_to_bottom = new Image(IMAGE_TOP_TO_BOTTOM_URL, Car.getImageWidth(), Car.getImageHeight(),
			false, false);
	private Image image_bottom_to_top = new Image(IMAGE_BOTTOM_TO_TOP_URL, Car.getImageWidth(), Car.getImageHeight(),
			false, false);
	private Image image_left_to_right = new Image(IMAGE_LEFT_TO_RIGHT_URL, Car.getImageWidth(), Car.getImageHeight(),
			false, false);
	private Image image_right_to_left = new Image(IMAGE_RIGHT_TO_LEFT_URL, Car.getImageWidth(), Car.getImageHeight(),
			false, false);

	public Car(Road currentRoad, ForwardLane sourceLane, Road destinationRoad, BackwardLane destinationLane,
			Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		super(currentRoad, sourceLane, destinationRoad, destinationLane, crossing, mapWidth, mapHeight);

		this.getRegion().setX(sourceLane.getCarSpawnPoint().getX());
		this.getRegion().setY(sourceLane.getCarSpawnPoint().getY());

		computeRegions();

		// TODO Auto-generated constructor stub
	}

	public static double getImageWidth() {
		return imageWidth.get();
	}

	public static double getImageHeight() {
		return imageHeight.get();
	}

	@Override
	public void draw(GraphicsContext gc) {

		double angle = 0;

		angle = Math.toDegrees(this.getAngle());

		switch (this.getDirection()) {

		case TOP_TO_BOTTOM:

			Car.drawRotated(image_top_to_bottom, gc, 0 + angle, this.getX(), this.getY(), Car.getImageHeight(),
					Car.getImageWidth());
			break;
		case BOTTOM_TO_TOP:
			Car.drawRotated(image_top_to_bottom, gc, MathEngine.NINETY_ANGLE_CLOCKWISE * 2 + angle, this.getX(),
					this.getY(), Car.getImageHeight(), Car.getImageWidth());
			// gc.drawImage(image_bottom_to_top, this.getX() - Car.getImageHeight()/2,
			// this.getY() - Car.getImageWidth()/2, Car.getImageHeight(),
			// Car.getImageWidth());
			break;
		case LEFT_TO_RIGHT:
			Car.drawRotated(image_top_to_bottom, gc, MathEngine.NINETY_ANGLE_ANTICLOCKWISE + angle, this.getX(),
					this.getY(), Car.getImageHeight(), Car.getImageWidth());
			// gc.drawImage(image_left_to_right, this.getX() - Car.getImageHeight()/2,
			// this.getY() - Car.getImageWidth()/2, Car.getImageHeight(),
			// Car.getImageWidth());
			break;
		case RIGHT_TO_LEFT:
			Car.drawRotated(image_top_to_bottom, gc, MathEngine.NINETY_ANGLE_CLOCKWISE + angle, this.getX(),
					this.getY(), Car.getImageHeight(), Car.getImageWidth());
			// gc.drawImage(image_right_to_left, this.getX() - Car.getImageHeight()/2,
			// this.getY() - Car.getImageWidth()/2, Car.getImageHeight(),
			// Car.getImageWidth());
			break;

		}
	}

	@Override
	public void computeRegions() {

		if (this.getDirection() == Vehicle.Direction.TOP_TO_BOTTOM
				|| this.getDirection() == Vehicle.Direction.BOTTOM_TO_TOP) {
			this.getRegion().setHeight(Scale.CAR_LENGTH_METERS * Scale.pixelToMeterRatio);
			this.getRegion().setWidth(Scale.CAR_WIDTH_METERS * Scale.pixelToMeterRatio);
		}

		else {
			this.getRegion().setWidth(Scale.CAR_LENGTH_METERS * Scale.pixelToMeterRatio);
			this.getRegion().setHeight(Scale.CAR_WIDTH_METERS * Scale.pixelToMeterRatio);

		}

	}

}
