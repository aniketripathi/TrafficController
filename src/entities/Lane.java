package entities;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javafx.scene.canvas.GraphicsContext;
import util.LinearPath;
import util.Path;
import util.Region;

public abstract class Lane {

	private Region region;

	// 1- close to divider, 2 - away from divider
	private double laneLength;
	private double remainingLength;
	private LinkedList<Vehicle> queue;
	private Path carPath;
	private Path twoWheelerPath;
	private Path heavyVehiclePath;

	// private static final int queueSize = 100;

	public Lane() {
		region = new Region();
		queue = new LinkedList<Vehicle>();
		carPath = new LinearPath();
		twoWheelerPath = new LinearPath();
		heavyVehiclePath = new LinearPath();
		this.laneLength = 0;
		this.remainingLength = 0;
		
	}

	public Lane(Region region) {
		this.region = new Region();
		queue = new LinkedList<Vehicle>();
		carPath = new LinearPath();
		twoWheelerPath = new LinearPath();
		heavyVehiclePath = new LinearPath();
		this.laneLength = 0;
		this.remainingLength = 0;
		
	}

	

	
	
	
	
	/**
	 * 
	 * @param graphics
	 */
	public void draw(GraphicsContext graphicsContext) {

	}


	public boolean isLaneEmpty() {
		return queue.isEmpty();
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	
	public double getLaneLength() {
		return laneLength;
	}

	public void setLaneLength(double laneLength) {
		this.laneLength = laneLength;
	}

	public double getRemainingLength() {
		return remainingLength;
	}
	
	/** Always lies between 0 and laneLength **/

	public void setRemainingLength(double remainingLength) {
		this.remainingLength = Math.max(0, remainingLength);
		this.remainingLength = Math.min(this.remainingLength, laneLength);
	}

	public Path getCarPath() {
		return carPath;
	}

	public void setCarPath(Path carPath) {
		this.carPath = carPath;
	}

	
	public Path getTwoWheelerPath() {
		return twoWheelerPath;
	}

	public void setTwoWheelerPath(Path twoWheelerPath) {
		this.twoWheelerPath = twoWheelerPath;
	}

	public Path getHeavyVehiclePath() {
		return heavyVehiclePath;
		
	}

	public void setHeavyVehiclePath(Path heavyVehiclePath) {
		this.heavyVehiclePath = heavyVehiclePath;
	}

	
	public LinkedList<Vehicle> getQueue() {
		return queue;
	}
	
	
	
}
