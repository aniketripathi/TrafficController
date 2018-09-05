package util;

import javafx.geometry.Point2D;

public class QuadraticPath extends Path{
	
	private Point2D p0;
	private Point2D p1;
	private Point2D p2;
	
	
	
	/** Bezier Curve B(r) = (1-r)^2 * P0 + 2*r*(1-r)*P1 + r^2 * P2 **/
	
	
	public QuadraticPath(Point2D p0, Point2D p1, Point2D p2) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
	}
	
	@Override
	public double getX(double r) {
		return  ((1-r)*(1-r)* p0.getX() + 2*r*(1-r)*p1.getX() + r*r* p2.getX());
	}
	
	@Override
	public double getY(double r) {
		return ((1-r)*(1-r)*p0.getY() + 2*r*(1-r)*p1.getY() + r*r* p2.getY());
	}
	

	public double getVelocityX(double r, double velocityR) {
		return (2*(1-r)*(p1.getX() - p0.getX()) + 2*r*(p2.getX() - p1.getX())) * velocityR;
	}
	
	
	public double getVelocityY(double r, double velocityR) {
		return (2*(1-r)*(p1.getY() - p0.getY()) + 2*r*(p2.getY() - p1.getY())) * velocityR;
	}
	
	
	public double getAccelerationX(double r, double velocityR, double accelerationR) {
		return   (2*velocityR * velocityR * (p2.getX() - 2*p1.getX() + p0.getX())) + (accelerationR * (2*(1-r)*(p1.getX() - p0.getX()) + 2*r*(p2.getX() - p1.getX())));
	}
	
	
	public double getAccelerationY(double r, double velocityR, double accelerationR) {
		return (2*velocityR * velocityR * (p2.getY() - 2*p1.getY() + p0.getY())) + (accelerationR * (2*(1-r)*(p1.getY() - p0.getY()) + 2*r*(p2.getY() - p1.getY())));
	}

}
