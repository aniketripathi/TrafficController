package main.java.util;

public class Compare {
	
	/**
	 * The maximum difference value tolerated when comparing doubles. Thus if x and
	 * y are not equal only if they differ more than the threshold value.
	 */

	public static final double THRESHOLD = 0.00001;
	
	
	public static boolean isLarger(double a, double b) {
		return ((a - b) > THRESHOLD);

	}

	public static boolean isSmaller(double a, double b) {
		return ((b-a) > THRESHOLD);
	}

	public static boolean isEqual(double a, double b) {
		return (Math.abs(a - b) < THRESHOLD);
	}

	public static boolean isSmallerEquals(double a, double b) {
		return (isSmaller(a,b) || isEqual(a,b));
	}
	
	public static boolean isLargerEqual(double a, double b) {
		return (isLarger(a,b) || isEqual(a,b));
	}
	
}
