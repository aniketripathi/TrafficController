package util;

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
	private double x;

	/**
	 * Y coordinate of this region's center
	 */
	private double y;

	/**
	 * Width of this region
	 */
	private double width;

	/**
	 * Height of this region
	 */
	private double height;

	/**
	 * The maximum difference value tolerated when comparing doubles. Thus if x and
	 * y are not equal only if they differ more than the threshold value.
	 */
	private static final double THRESHOLD = 0.000001;

	public Region(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Region() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}

	// copy constructor
	public Region(Region region) {
		this.x = region.x;
		this.y = region.y;
		this.width = region.width;
		this.height = region.height;
	}

	private boolean isLarger(double a, double b) {
		return ((a - b) > THRESHOLD);

	}

	private boolean isSmaller(double a, double b) {
		return !(isLarger(a, b) || isEqual(a, b));
	}

	private boolean isEqual(double a, double b) {
		return (Math.abs(a - b) < THRESHOLD);
	}

	/**
	 * Returns the center x coordinate of the region
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the center x coordinate of the region
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Returns the center x coordinate of the region
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the center y coordinate of the region
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Returns the width of the region
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Sets the width of the region
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Returns the height of the region
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Sets the height of the region
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	public double getArea() {
		return (this.width * this.height);
	}

	public double getArea(double percentage) {
		return (percentage / 100.0) * this.getArea();
	}

	public Region getRegion(double width_percentage, double height_percentage) {
		return new Region(this.x, this.y, this.width * width_percentage / 100, this.height * height_percentage / 100);
	}

	public boolean isCoinciding(Region region) {
		return (isEqual(this.x, region.x) && isEqual(this.y, region.y));
	}

	public boolean isColliding(Region region) {
		return (isLarger((this.width / 2 + region.width / 2), (this.getX() - region.x))
				&& isLarger((this.height / 2 + region.height / 2), (this.getY() - region.y)));
	}

	public boolean isInsideOf(Region region) {
		return (isSmaller(this.getX() + this.width / 2, region.x + region.width / 2)
				&& isSmaller(this.getY() + this.height / 2, region.y + region.height / 2)
				&& isLarger(this.getX() - this.width / 2, region.x - region.width / 2)
				&& isLarger(this.getY() - this.height / 2, region.y - region.height / 2));
	}

	public boolean isOutsideOf(Region region) {
		return (!isInsideOf(region));
	}

	public boolean isInside(double x, double y) {
		return (isSmaller(x, this.getX() + this.width / 2) && isLarger(x, this.getX() - this.width / 2)
				&& isSmaller(y, this.getY() + this.height / 2) && isLarger(y, this.getY() - this.height / 2));
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
	public void draw(GraphicsContext graphic, Paint fill, Paint stroke, double strokeLineWidth) {
		graphic.save();
		graphic.rect(this.x - this.width, this.y - this.height, this.width, this.height);
		graphic.setFill(fill);
		graphic.setStroke(stroke);
		graphic.setLineWidth(strokeLineWidth);
		graphic.closePath();
		graphic.fill();
		graphic.stroke();
		graphic.restore();
	}

}
