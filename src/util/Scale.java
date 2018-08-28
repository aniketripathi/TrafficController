package util;

public final class Scale {
	
	public static final double PIXEL_TO_METER_RATIO = 100;
	
	private Scale(){
		throw new UnsupportedOperationException();
	};
	
	public static double toMeters(double pixels) {
		return (pixels * PIXEL_TO_METER_RATIO);
	
	}
	
	public static double toPixels(double meters) {
		return (meters / PIXEL_TO_METER_RATIO);
	}

}
