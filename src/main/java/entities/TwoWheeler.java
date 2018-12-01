package main.java.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.java.map.Crossing;
import main.java.util.MathEngine;
import main.java.util.Scale;

public class TwoWheeler extends Vehicle {

	private static final String IMAGE_TOP_TO_BOTTOM_URL = "/main/resources/images/two_wheeler_top_to_bottom.png";
	public static DoubleProperty imageWidth = new SimpleDoubleProperty(Scale.toPixels(Scale.TWO_WHEELER_LENGTH_METERS));
	public static DoubleProperty imageHeight = new SimpleDoubleProperty(Scale.toPixels(Scale.TWO_WHEELER_WIDTH_METERS));

	private Image image_top_to_bottom = new Image(IMAGE_TOP_TO_BOTTOM_URL, TwoWheeler.getImageWidth(),
			TwoWheeler.getImageHeight(), false, false);

	public TwoWheeler(Road sourceRoad, ForwardLane sourceLane, Road destinationRoad, BackwardLane destinationLane,
			Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		super(sourceRoad, sourceLane, destinationRoad, destinationLane, crossing, mapWidth, mapHeight);
		this.setType(Type.TWO_WHEELER);
		this.getRegion().setX(sourceLane.getTwoWheelerSpawnPoint().getX());
		this.getRegion().setY(sourceLane.getTwoWheelerSpawnPoint().getY());

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

			Vehicle.drawRotated(image_top_to_bottom, gc, 0 + angle, this.getX(), this.getY(),
					TwoWheeler.getImageHeight(), TwoWheeler.getImageWidth());
			break;

		case BOTTOM_TO_TOP:
			Vehicle.drawRotated(image_top_to_bottom, gc, MathEngine.NINETY_ANGLE_CLOCKWISE * 2 + angle, this.getX(),
					this.getY(), TwoWheeler.getImageHeight(), TwoWheeler.getImageWidth());
			break;

		case LEFT_TO_RIGHT:
			Vehicle.drawRotated(image_top_to_bottom, gc, MathEngine.NINETY_ANGLE_ANTICLOCKWISE + angle, this.getX(),
					this.getY(), TwoWheeler.getImageHeight(), TwoWheeler.getImageWidth());
			break;

		case RIGHT_TO_LEFT:
			Vehicle.drawRotated(image_top_to_bottom, gc, MathEngine.NINETY_ANGLE_CLOCKWISE + angle, this.getX(),
					this.getY(), TwoWheeler.getImageHeight(), TwoWheeler.getImageWidth());
			break;

		}

	}

	@Override
	public void computeRegions() {
		if (this.getDirection() == Vehicle.Direction.TOP_TO_BOTTOM
				|| this.getDirection() == Vehicle.Direction.BOTTOM_TO_TOP) {
			this.getRegion().setHeight(Scale.TWO_WHEELER_LENGTH_METERS * Scale.pixelToMeterRatio);
			this.getRegion().setWidth(Scale.TWO_WHEELER_WIDTH_METERS * Scale.pixelToMeterRatio);
		}

		else {
			this.getRegion().setWidth(Scale.TWO_WHEELER_LENGTH_METERS * Scale.pixelToMeterRatio);
			this.getRegion().setHeight(Scale.TWO_WHEELER_WIDTH_METERS * Scale.pixelToMeterRatio);

		}

	}

}
