package util;

import javafx.geometry.Point2D;

public class LinearPath extends Path {

	private Point2D initialPoint;
	private Point2D finalPoint;

	/** Bezier Curve B(r) = (1-r)P0 + P1 **/

	public LinearPath(double initialX, double initialY, double finalX, double finalY) {
		initialPoint = new Point2D(initialX, initialY);
		finalPoint = new Point2D(finalX, finalY);
	}

	public LinearPath(Point2D p0, Point2D p1) {
		this.initialPoint = p0;
		this.finalPoint = p1;
	}
	
	public LinearPath() {
		this.initialPoint = new Point2D(0,0);
		this.initialPoint = new Point2D(0,0);
	}
	
	public void setPoints(double initialX, double initialY, double finalX, double finalY) {
		initialPoint = new Point2D(initialX, initialY);
		finalPoint = new Point2D(finalX, finalY);
	}

	@Override
	public double getX(double r) {
		return ((1 - r) * initialPoint.getX() + finalPoint.getX());
	}

	@Override
	public double getY(double r) {
		return ((1 - r) * initialPoint.getY() + finalPoint.getY());
	}

	
	public double getVelocityX(double r, double velocityR) {
		return (finalPoint.getX() - initialPoint.getX()) * velocityR;
	}

	
	public double getVelocityY(double r, double velocityR) {
		return (finalPoint.getY() - initialPoint.getY()) * velocityR;
	}

	
	public double getAccelerationX(double r, double accelerationR) {
		return (finalPoint.getX() - initialPoint.getX())* accelerationR;
	}

	
	public double getAccelerationY(double r, double accelerationR) {
		return (finalPoint.getY() - initialPoint.getY())* accelerationR;
	}
}
