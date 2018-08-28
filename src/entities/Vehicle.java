package entities;

import entities.Lane.LaneDirection;
import util.Region;

public  class Vehicle {
	
	
	
	private double velocityX;
    private double velocityY;
    private double accelerationX;
    private double accelerationY;
    private Road currentRoad;
    private Road destinationRoad;
    private Lane currentLane;
    private Lane destinationLane;
    private LaneDirection currentDirection;
    private Region region;
    
    
    
    
    public Vehicle(double width, double height) {
    	velocityX = 0;
    	velocityY = 0;
    	accelerationX = 0;
    	accelerationY = 0;
    	currentRoad = null;
    	destinationRoad = null;
    	currentLane = null;
    	destinationLane = null;
    	currentDirection = null;
    	region = new Region(0,0, width, height);
    }
    
    
    
    public void reset() {
    	
    	velocityX = 0;
    	velocityY = 0;
    	accelerationX = 0;
    	accelerationY = 0;
    	currentRoad = null;
    	destinationRoad = null;
    	currentLane = null;
    	destinationLane = null;
    	currentDirection = null;
    	region.setX(0);
    	region.setY(0);
    	
    }
    
	
	public double getVelocityX() {
		return velocityX;
	}
	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}
	public double getVelocityY() {
		return velocityY;
	}
	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}
	public double getAccelerationX() {
		return accelerationX;
	}
	public void setAccelerationX(double accelerationX) {
		this.accelerationX = accelerationX;
	}
	public double getAccelerationY() {
		return accelerationY;
	}
	public void setAccelerationY(double accelerationY) {
		this.accelerationY = accelerationY;
	}
	public Road getCurrentRoad() {
		return currentRoad;
	}
	public void setCurrentRoad(Road currentRoad) {
		this.currentRoad = currentRoad;
	}
	public Road getDestinationRoad() {
		return destinationRoad;
	}
	public void setDestinationRoad(Road destinationRoad) {
		this.destinationRoad = destinationRoad;
	}
	public Lane getCurrentLane() {
		return currentLane;
	}
	public void setCurrentLane(Lane currentLane) {
		this.currentLane = currentLane;
	}
	public Lane getDestinationLane() {
		return destinationLane;
	}
	public void setDestinationLane(Lane destinationLane) {
		this.destinationLane = destinationLane;
	}
	public LaneDirection getCurrentDirection() {
		return currentDirection;
	}
	public void setCurrentDirection(LaneDirection currentDirection) {
		this.currentDirection = currentDirection;
	}
    
    

}
