package entities;

import javafx.scene.image.Image;

public class Car extends Vehicle {

	
	private static double imageWidth = 40;
	private static double imageHeight = 40;
	
	private Image image;
	
	public Car(double width, double height) {
		super(width, height);
		
		// TODO Auto-generated constructor stub
	}

	public Car() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static double getImageWidth() {
		return imageWidth;
	}

	public static void setImageWidth(double imageWidth) {
		Car.imageWidth = imageWidth;
	}

	public static double getImageHeight() {
		return imageHeight;
	}

	public static void setImageHeight(double imageHeight) {
		Car.imageHeight = imageHeight;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	

}
