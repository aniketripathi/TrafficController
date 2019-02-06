package main.java.map;

import java.util.LinkedList;
import java.util.ListIterator;
import main.java.entities.Vehicle;
import main.java.util.Region;

public class Crossing {

	private Region region;
	private LinkedList<Vehicle> queue;

	public Crossing() {
		this(0, 0);
	}

	public Crossing(double width, double height) {
		region = new Region(0, 0, width, height);
		queue = new LinkedList<Vehicle>();
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public int getQueueSize() {
		return queue.size();
	}

	public void addVehicle(Vehicle vehicle) {
		queue.addLast(vehicle);
	}

	public void removeVehicle(Vehicle vehicle) {
		queue.remove(vehicle);
	}

	public void clearQueue() {
		queue.clear();
	}

	public ListIterator<Vehicle> listIterator() {
		return queue.listIterator();
	}

	public ListIterator<Vehicle> listIterator(int index) {
		return queue.listIterator(index);
	}

	public int getVehicleIndex(Vehicle vehicle) {
		return queue.indexOf(vehicle);
	}

	public Vehicle getVehicleAt(int index) {
		return queue.get(index);
	}
}
