package map;

import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ShapeCurvedLane extends Shape{
	private Rectangle rectangle;
	private Arc arc;
	private double arcWidth;
	
	public ShapeCurvedLane(Rectangle rectangle, Arc arc, double arcWidth) {
		this.rectangle = rectangle;
		this.arc = arc;
		this.arcWidth = arcWidth;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public Arc getArc() {
		return arc;
	}

	public void setArc(Arc arc) {
		this.arc = arc;
	}

	public double getArcWidth() {
		return arcWidth;
	}

	public void setArcWidth(double arcWidth) {
		this.arcWidth = arcWidth;
	}
	
	
	
	
	
}
