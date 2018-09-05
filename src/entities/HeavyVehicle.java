package entities;

import javafx.scene.image.Image;

public class HeavyVehicle extends Vehicle {
	
	private static double imageWidth = 40;
	private static double imageHeight = 40;
	
	private Image image;

	public HeavyVehicle(double width, double height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public HeavyVehicle() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public static double getImageWidth() {
		return imageWidth;
	}

	public static void setImageWidth(double imageWidth) {
		HeavyVehicle.imageWidth = imageWidth;
	}

	public static double getImageHeight() {
		return imageHeight;
	}

	public static void setImageHeight(double imageHeight) {
		HeavyVehicle.imageHeight = imageHeight;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	
}
