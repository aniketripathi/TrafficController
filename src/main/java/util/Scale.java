package main.java.util;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public final class Scale {

	public  static double pixelToMeterRatio = 8;
	
	
	/** The car width  and length is not taken according to project but real world meanings 
	 * 
	 */
	public static final double CAR_WIDTH_METERS = 2.0;	
	public static final double CAR_LENGTH_METERS = 4.4;
	public static final double LANE_WIDTH_METERS = 3.5;
	public static final double DIVIDER_WIDTH_METERS = 1;
	public static final double VEHICLE_SPEED_METERPERSEC = 36;
	
	
	private Scale() {
		throw new UnsupportedOperationException();
	};

	public static double toMeters(double pixels) {
		return (pixels * pixelToMeterRatio);

	}

	public static double toPixels(double meters) {
		return (meters / pixelToMeterRatio);
	}
	
	public static Image rotateImage(Image image, double angle) {
	    ImageView iv = new ImageView(image);
	    SnapshotParameters params = new SnapshotParameters();
	    params.setFill(Color.TRANSPARENT);
	    params.setTransform(new Rotate(angle, image.getHeight() / 2, image.getWidth() / 2));
	    params.setViewport(new Rectangle2D(0, 0, image.getHeight(), image.getWidth()));
	    return iv.snapshot(params, null);
	}
}
