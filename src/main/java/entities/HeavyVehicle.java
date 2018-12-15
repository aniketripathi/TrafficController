package main.java.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.java.map.Crossing;
import main.java.util.MathEngine;
import main.java.util.Scale;

public class HeavyVehicle extends Vehicle {

	private static final String IMAGE_TOP_TO_BOTTOM_URL = "/main/resources/images/heavy_vehicle_top_to_bottom.png";

	public static DoubleProperty imageWidth = new SimpleDoubleProperty(
			Scale.toPixels(Scale.HEAVY_VEHICLE_LENGTH_METERS));
	public static DoubleProperty imageHeight = new SimpleDoubleProperty(
			Scale.toPixels(Scale.HEAVY_VEHICLE_WIDTH_METERS));

	private Image image_top_to_bottom = new Image(IMAGE_TOP_TO_BOTTOM_URL, HeavyVehicle.getImageWidth(),
			HeavyVehicle.getImageHeight(), false, true);

	public HeavyVehicle(Road sourceRoad, ForwardLane sourceLane, Road destinationRoad, BackwardLane destinationLane,
			Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		super(sourceRoad, sourceLane, destinationRoad, destinationLane, crossing, mapWidth, mapHeight);
		this.setType(Type.HEAVY_VEHICLE);
		this.getRegion().setX(sourceLane.getHeavyVehicleSpawnPoint().getX());
		this.getRegion().setY(sourceLane.getHeavyVehicleSpawnPoint().getY());

		computeRegions();
	}

	public static double getImageWidth() {
		return imageWidth.get();
	}

	public static void setImageWidth(double imageWidth) {
		HeavyVehicle.imageWidth.set(imageWidth);
	}

	public static double getImageHeight() {
		return imageHeight.get();
	}

	public static void setImageHeight(double imageHeight) {
		HeavyVehicle.imageHeight.set(imageHeight);
	}

	@Override
	public void draw(GraphicsContext gc) {

		double angle = 0;

		angle = Math.toDegrees(this.getAngle());

		switch (this.getDirection()) {

		case TOP_TO_BOTTOM:

			drawRotated(image_top_to_bottom, gc, 0 + angle, this.getX(), this.getY(), HeavyVehicle.getImageHeight(),
					HeavyVehicle.getImageWidth());
			break;

		case BOTTOM_TO_TOP:
			drawRotated(image_top_to_bottom, gc, MathEngine.NINETY_ANGLE_CLOCKWISE * 2 + angle, this.getX(),
					this.getY(), HeavyVehicle.getImageHeight(), HeavyVehicle.getImageWidth());
			break;

		case LEFT_TO_RIGHT:
			drawRotated(image_top_to_bottom, gc, MathEngine.NINETY_ANGLE_ANTICLOCKWISE + angle, this.getX(),
					this.getY(), HeavyVehicle.getImageHeight(), HeavyVehicle.getImageWidth());
			break;

		case RIGHT_TO_LEFT:
			drawRotated(image_top_to_bottom, gc, MathEngine.NINETY_ANGLE_CLOCKWISE + angle, this.getX(), this.getY(),
					HeavyVehicle.getImageHeight(), HeavyVehicle.getImageWidth());
			break;

		}
	}

	@Override
	public void computeRegions() {

		if (this.getDirection() == Vehicle.Direction.TOP_TO_BOTTOM
				|| this.getDirection() == Vehicle.Direction.BOTTOM_TO_TOP) {
			this.getRegion().setHeight(Scale.toPixels(Scale.HEAVY_VEHICLE_LENGTH_METERS));
			this.getRegion().setWidth(Scale.toPixels(Scale.HEAVY_VEHICLE_WIDTH_METERS));
		}

		else {
			this.getRegion().setWidth(Scale.toPixels(Scale.HEAVY_VEHICLE_LENGTH_METERS));
			this.getRegion().setHeight(Scale.toPixels(Scale.HEAVY_VEHICLE_WIDTH_METERS));

		}

	}

}
