package main.java.entities;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import javafx.scene.canvas.GraphicsContext;
import main.java.util.LinearPath;
import main.java.util.Path;
import main.java.util.Region;

public abstract class Lane {

	private Region region;

	// 1- close to divider, 2 - away from divider

	private Road road;
	private double laneLength;
	private Path carPath;
	private Path twoWheelerPath;
	private Path heavyVehiclePath;
	private int index;

	/**
	 * The vehicle that entered first will be at the head of the queue. New vehicles
	 * should be added at the end of the queue.
	 */
	private LinkedList<Vehicle> queue;

	// private static final int queueSize = 100;

	public Lane(Road road, int index) {

		this.road = road;
		region = new Region();
		this.index = index;
		this.laneLength = 0;
		this.laneLength = 0;
		queue = new LinkedList<Vehicle>();

	}

	public int getIndex() {
		return this.index;
	}

	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public double getX() {
		return region.getX();
	}

	public double getY() {
		return region.getY();
	}

	public double getWidth() {
		return region.getWidth();
	}

	public double getHeight() {
		return region.getHeight();
	}

	/**
	 * 
	 * @param graphics
	 */
	public void draw(GraphicsContext graphicsContext) {

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

	protected LinkedList<Vehicle> getQueue() {
		return this.queue;
	}

	/** Vehicle will be added to the end of the list **/
	public void addVehicle(Vehicle vehicle) {
		queue.add(vehicle);
	}

	public void removeVehicle(Vehicle vehicle) {
		queue.remove(vehicle);
	}

	public ListIterator<Vehicle> listIterator() {
		return queue.listIterator();
	}

	public ListIterator<Vehicle> listIterator(int index) {
		return queue.listIterator(index);
	}

	public void clearQueue() {
		queue.clear();
	}

	public int getVehicleIndex(Vehicle vehicle) {
		return queue.indexOf(vehicle);
	}

	public Vehicle getVehicleAt(int index) {
		return queue.get(index);
	}

	/** Get the vehicle that was added least recently **/
	public Vehicle getFirst() {
		try {
			return queue.getFirst();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	/** Get the vehicle that was added most recently **/
	public Vehicle getLast() {
		try {
			return queue.getLast();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
