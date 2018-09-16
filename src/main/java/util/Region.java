package main.java.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * 
 * @author Aniket Kumar Tripathi
 *
 */
public class Region {

	/**
	 * X coordinate of this region's center
	 */
	private DoubleProperty x;

	/**
	 * Y coordinate of this region's center
	 */
	private DoubleProperty y;

	/**
	 * Width of this region
	 */
	private DoubleProperty width;

	/**
	 * Height of this region
	 */
	private DoubleProperty height;


	public Region(double x, double y, double width, double height) {
		
		this.x = new SimpleDoubleProperty(x);
		this.y = new SimpleDoubleProperty(y);
		this.width = new SimpleDoubleProperty(width);
		this.height = new SimpleDoubleProperty(height);
	}

	public Region() {
		this(0,0,0,0);
	}

	// copy constructor
	public Region(Region region) {
		this(region.x.get(), region.y.get(), region.width.get(), region.height.get());
	}

	
	public void setAll(double x, double y, double width, double height) {
		this.x.set(x);
		this.y.set(y);
		this.width.set(width);
		this.height.set(height);
	}

	/**
	 * Returns the center x coordinate of the region
	 */
	public double getX() {
		return x.get();
	}

	/**
	 * Sets the center x coordinate of the region
	 */
	public void setX(double x) {
		this.x.set(x);;
	}

	/**
	 * Returns the center x coordinate of the region
	 */
	public double getY() {
		return y.get();
	}

	/**
	 * Sets the center y coordinate of the region
	 */
	public void setY(double y) {
		this.y.set(y);
	}

	/**
	 * Returns the width of the region
	 */
	public double getWidth() {
		return width.get();
	}

	/**
	 * Sets the width of the region
	 */
	public void setWidth(double width) {
		this.width.set(width);;
	}

	/**
	 * Returns the height of the region
	 */
	public double getHeight() {
		return height.get();
	}

	/**
	 * Sets the height of the region
	 */
	public void setHeight(double height) {
		this.height.set(height);
	}

	public double getArea() {
		return (this.getWidth() * this.getHeight());
	}

	public double getArea(double percentage) {
		return (percentage / 100.0) * this.getArea();
	}

	public Region getRegion(double width_percentage, double height_percentage) {
		return new Region(this.getX(), this.getY(), this.getWidth() * width_percentage / 100, this.getHeight() * height_percentage / 100);
	}

	public boolean isCoinciding(Region region) {
		return (Compare.isEqual(this.getX(), region.getX()) && Compare.isEqual(this.getY(), region.getY()));
	}

	public boolean isColliding(Region region) {
		return (Compare.isLarger((this.getWidth() / 2 + region.getWidth() / 2), (this.getX() - region.getX()))
				&& Compare.isLarger((this.getHeight() / 2 + region.getHeight() / 2), (this.getY() - region.getY())));
	}

	public boolean isInsideOf(Region region) {
		return (Compare.isSmaller(this.getX() + this.getWidth() / 2, region.getX() + region.getWidth() / 2)
				&& Compare.isSmaller(this.getY() + this.getHeight() / 2, region.getY() + region.getHeight() / 2)
				&& Compare.isLarger(this.getX() - this.getWidth() / 2, region.getX() - region.getWidth() / 2)
				&& Compare.isLarger(this.getY() - this.getHeight() / 2, region.getY() - region.getHeight() / 2));
	}

	public boolean isOutsideOf(Region region) {
		return (!isInsideOf(region));
	}

	public boolean isInside(double x, double y) {
		return (Compare.isSmaller(x, this.getX() + this.getWidth() / 2) && Compare.isLarger(x, this.getX() - this.getWidth() / 2)
				&& Compare.isSmaller(y, this.getY() + this.getHeight() / 2) && Compare.isLarger(y, this.getY() - this.getHeight() / 2));
	}

	public boolean isOutside(int x, int y) {
		return (!isInside(x, y));
	}

	/**
	 * The draw method is used to draw the region using the specified
	 * GraphicContext.
	 * 
	 * The method first saves the state of the graphic instance, applies the given
	 * attributes of Paint, Stroke and Stroke line width, draws the region (both
	 * fill and stroke) and then restores the original state. Note that the
	 * rendering is done by creating path of the region's rectangle. You can
	 * override this method to use more advanced drawing scheme or draw the region
	 * separately.
	 * 
	 * @param graphic         GraphicContext instance used for drawing
	 * @param fill            Paint attribute for filling the region
	 * @param stroke          Paint attribute for stroke
	 * @param strokeLineWidth Width of the stroke line
	 */
	public void draw(GraphicsContext graphic, Paint fill, Paint strokePaint, double strokeLineWidth, boolean stroke) {
		graphic.save();
		graphic.beginPath();
		graphic.rect(this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2, this.getWidth(), this.getHeight());
		graphic.setFill(fill);
		graphic.fill();
		if (stroke) {
			graphic.setStroke(strokePaint);
			graphic.setLineWidth(strokeLineWidth);
			graphic.stroke();
		}
		graphic.closePath();
		graphic.restore();

	}
	
	
	
	public DoubleProperty getXProperty() {
		return this.x;
	}
	
	
	public DoubleProperty getYProperty() {
		return this.y;
	}
	
	
	public DoubleProperty getWidthProperty() {
		return this.width;
	}
	
	
	public DoubleProperty getHeightProperty() {
		return this.height;
	}

}
