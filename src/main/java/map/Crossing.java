package main.java.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

import javafx.scene.shape.Path;
import main.java.entities.BackwardLane;
import main.java.entities.ForwardLane;
import main.java.entities.Vehicle;
import main.java.util.Region;

public class Crossing {

	private class LanePair {

		private ForwardLane forwardLane;
		private BackwardLane backwardLane;

		public LanePair(ForwardLane forwardLane, BackwardLane backwardLane) {
			super();
			this.forwardLane = forwardLane;
			this.backwardLane = backwardLane;
		}

		public ForwardLane getForwardLane() {
			return forwardLane;
		}

		public BackwardLane getBackwardLane() {
			return backwardLane;
		}

	}

	private Region region;
	private int numberOfLanePairs;
	private HashMap<LanePair, Path> paths;
	private Set<LanePair> lanePairs;
	private LinkedList<Vehicle> queue;

	public static final int DEFAULT_NUMBER_OF_LANE_PAIRS = 16;

	public Crossing() {
		this(0, 0);
	}

	public Crossing(double width, double height) {
		region = new Region(0, 0, width, height);
		paths = new HashMap<LanePair, Path>();
		lanePairs = new HashSet<LanePair>(numberOfLanePairs);
		queue = new LinkedList<Vehicle>();
		numberOfLanePairs = DEFAULT_NUMBER_OF_LANE_PAIRS;
	}

	public boolean addPath(ForwardLane forwardLane, BackwardLane backwardLane, Path path) {

		boolean successful = false;
		if (getLanePair(forwardLane, backwardLane) == null) {
			LanePair lanePair = new LanePair(forwardLane, backwardLane);
			lanePairs.add(lanePair);
			paths.put(lanePair, path);
			successful = true;
		}

		return successful;
	}

	private LanePair getLanePair(ForwardLane forwardLane, BackwardLane backwardLane) {
		LanePair lanePair = null;
		Iterator<LanePair> iterator = lanePairs.iterator();
		while (iterator.hasNext()) {
			LanePair temp = iterator.next();
			if (temp.getForwardLane().equals(forwardLane) && temp.getBackwardLane().equals(backwardLane)) {
				lanePair = temp;
			}
		}
		return lanePair;
	}

	public Path getPath(ForwardLane forwardLane, BackwardLane backwardLane) {
		return paths.get(getLanePair(forwardLane, backwardLane));
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public int getNumberOfLanePairs() {
		return numberOfLanePairs;
	}

	public void setNumberOfLanePairs(int numberOfLanePairs) {
		this.numberOfLanePairs = numberOfLanePairs;
	}

	public void addVehicle(Vehicle vehicle) {
		queue.add(vehicle);
	}

	public void removeVehicle(Vehicle vehicle) {
		queue.remove(vehicle);
	}

	public void clearQueue() {
		queue.clear();
	}

	public ListIterator<Vehicle> iterator() {
		return queue.listIterator();
	}

	public ListIterator<Vehicle> iterator(int index) {
		return queue.listIterator(index);
	}

	public int getVehicleIndex(Vehicle vehicle) {
		return queue.indexOf(vehicle);
	}
}
