package util;

public class Region {
	private double x;				//x- coordinate of center
	private double y;				//y-coordinate of center
	private double width;			//width of region
	private double height;			// height of region
	
	
	public Region(double x, double y, double width, double height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	
	public Region(){
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}
	
	// copy constructor
	public Region(Region region){
		this.x = region.x;
		this.y = region.y;
		this.width = region.width;
		this.height = region.height;
	}
	
	
	
	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public double getWidth() {
		return width;
	}


	public void setWidth(double width) {
		this.width = width;
	}


	public double getHeight() {
		return height;
	}


	public void setHeight(double height) {
		this.height = height;
	}


	public double getArea(){
		return (this.width * this.height);
	}
	
	
	public double getArea(int percentage){
		return (percentage /100) * this.getArea();
	}
	
	public Region getRegion(int width_percentage, int height_percentage){
		return new Region(this.x, this.y, this.width * width_percentage/100, this.height * height_percentage /100);
	}
	
	
	public boolean isCoinciding(Region region2){
		return (Math.round(this.x) == Math.round(region2.x) && Math.round(this.y) == Math.round(region2.y));
	}
	
	
	public boolean isColliding(Region region2){
		return ( Math.abs(this.getX() - region2.x) <= (this.width/2 + region2.width/2)  &&
	 			 Math.abs(this.getY() - region2.y) <= (this.height/2 + region2.height/2));
	}
	
	
	public boolean isInsideOf(Region region){
		return (this.getX() + this.width/2 <= region.x + region.width/2 && this.getY() + this.height/2 <= region.y + region.height/2
			&&  this.getX() - this.width/2 >= region.x - region.width/2 && this.getY() - this.height/2 >= region.y - region.height/2);
	}
	
	public boolean isOutsideOf(Region region){
		return (!isInsideOf(region));
	}
	
	public boolean isInside(int x, int y){
		return (x <= this.getX() + this.width/2  && x >= this.getX() - this.width/2		&&
				y <= this.getY() + this.height/2 && y >= this.getY() - this.height/2);
	}
	
	public boolean isOutside(int x, int y){
		return (!isInside(x, y));
}

}
