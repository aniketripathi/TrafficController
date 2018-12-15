package main.java.util;

public final class Scale {

	private static double pixelToMeterRatio = 6;

	/**
	 * The car width and length is not taken according to project but real world
	 * meanings The car length is the width according to Cartesian coordinate system
	 * The car width is the height according to Cartesian coordinate system
	 */
	public static final double CAR_WIDTH_METERS = 2.0;
	public static final double CAR_LENGTH_METERS = 4.4;
	public static final double TWO_WHEELER_WIDTH_METERS = 1.5;
	public static final double TWO_WHEELER_LENGTH_METERS = 3;
	public static final double HEAVY_VEHICLE_WIDTH_METERS = 2.5;
	public static final double HEAVY_VEHICLE_LENGTH_METERS = 12;
	public static final double LANE_WIDTH_METERS = 3.9;
	public static final double DIVIDER_WIDTH_METERS = 1;
	public static final double VEHICLE_SPEED_METERPERSEC = 15;

	private Scale() {
		throw new UnsupportedOperationException();
	};

	public static double toMeters(double pixels) {
		return (pixels / pixelToMeterRatio);

	}

	public static double toPixels(double meters) {
		return (meters * pixelToMeterRatio);
	}

}
