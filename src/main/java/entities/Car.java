package main.java.entities;

import java.io.File;
import java.io.InputStream;

import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.java.map.Crossing;
import main.java.util.Scale;

public class Car extends Vehicle {
	
	static InputStream  is = Car.class.getResourceAsStream("images/car_yellow.png") ; 
	private static final String IMAGE_URL = "/main/resources/images/car_yellow.png";
	public static final double  IMAGE_WIDTH = 110;;
	public static final double IMAGE_HEIGHT = 50;;
	Image IMAGE  = new Image( IMAGE_URL,Car.IMAGE_WIDTH, Car.IMAGE_HEIGHT, false, false);
	
	private Image image = IMAGE;
	
	public Car(Road currentRoad, ForwardLane sourceLane, Road destinationRoad, BackwardLane destinationLane, Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		super(currentRoad, sourceLane, destinationRoad, destinationLane, crossing, mapWidth, mapHeight );
		
		this.getRegion().setX(sourceLane.getCarSpawnPoint().getX());
		this.getRegion().setY(sourceLane.getCarSpawnPoint().getY());
		
		
		
		// TODO Auto-generated constructor stub
	}
	
	
	

	
	public Image getImage() {
		return image;
	}

	@Override
	public void draw(GraphicsContext gc) {
		
		switch(this.getDirection()) {
		
		case TOP_TO_BOTTOM:
			gc.drawImage(Scale.rotateImage(IMAGE, 90), this.getX() - this.getWidth()/2, this.getY() - this.getHeight()/2, this.getWidth(), this.getHeight());
			break;
		case BOTTOM_TO_TOP:
			gc.drawImage(Scale.rotateImage(IMAGE, -90), this.getX() - this.getWidth()/2, this.getY() - this.getHeight()/2, this.getWidth(), this.getHeight());
			break;
		case LEFT_TO_RIGHT:
			gc.drawImage(IMAGE, this.getX() - this.getWidth()/2, this.getY() - this.getHeight()/2, this.getWidth(), this.getHeight());
			break;
		case RIGHT_TO_LEFT:
			gc.drawImage(Scale.rotateImage(IMAGE, 180), this.getX() - this.getWidth()/2, this.getY() - this.getHeight()/2, this.getWidth(), this.getHeight());
			break;
		}
	}


	@Override
	public void computeRegions() {
		
		if(this.getDirection() == Vehicle.Direction.TOP_TO_BOTTOM || this.getDirection() == Vehicle.Direction.BOTTOM_TO_TOP) {
			this.getRegion().setHeight(Scale.CAR_WIDTH_METERS * Scale.pixelToMeterRatio);
			this.getRegion().setWidth(Scale.CAR_LENGTH_METERS * Scale.pixelToMeterRatio);
	}
	
	else {
		this.getRegion().setWidth(Scale.CAR_WIDTH_METERS * Scale.pixelToMeterRatio);
		this.getRegion().setHeight(Scale.CAR_LENGTH_METERS * Scale.pixelToMeterRatio);
		
		}
		
	}


}
