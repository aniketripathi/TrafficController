package main.java.entities;

import main.java.util.Region;

public class Road {

	public enum TYPE {
		TOP(0), BOTTOM(1), LEFT(2), RIGHT(3);

		int index;

		TYPE(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static TYPE getType(int index) {
			switch (index) {
			case 0:
				return TOP;
			case 1:
				return BOTTOM;
			case 2:
				return LEFT;
			case 3:
				return RIGHT;
			default:
				return null;
			}
		}
	}

	private final int numberOfLanes;

	/**
	 * The notation of lane - index The lane closes to divider will have lowest
	 * index i.e 0 The lane most far away from divider will have highest index i.e.
	 * numberOfLanes - 1 The lanes in between will follow non decreasing order. The
	 * curved lane is included in this indexing scheme.
	 */

	private final TYPE type;

	private ForwardLane[] forwardLanes;
	private BackwardLane[] backwardLanes;
	private TrafficLight trafficLight;
	private Region divider;

	public Road(int numberOfLanes, TYPE type) {
		if (numberOfLanes < 1)
			throw new IllegalArgumentException("The numberOfLanes must be greater than 0");

		this.type = type;
		this.numberOfLanes = numberOfLanes;
		forwardLanes = new ForwardLane[numberOfLanes];
		backwardLanes = new BackwardLane[numberOfLanes];
		divider = new Region();
		trafficLight = new TrafficLight();
		createAllLanes();
	}

	public TYPE getType() {
		return this.type;
	}

	public int getIndex() {
		return this.type.getIndex();
	}

	private void createAllLanes() {

		for (int i = 0; i < numberOfLanes; i++) {
			forwardLanes[i] = new ForwardLane(this, i);
			backwardLanes[i] = new BackwardLane(this, i);
		}

	}

	public void setForwardLane(ForwardLane lane, int index) {
		forwardLanes[index] = lane;

	}

	public void setBackwardLane(BackwardLane lane, int index) {
		backwardLanes[index] = lane;

	}

	public ForwardLane getForwardLane(int index) {
		return forwardLanes[index];
	}

	public BackwardLane getBackwardLane(int index) {
		return backwardLanes[index];
	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	public TrafficLight getTrafficLight() {
		return trafficLight;
	}

	public void setTrafficLight(TrafficLight trafficLight) {
		this.trafficLight = trafficLight;
	}

	public Region getDivider() {
		return divider;
	}

	public void setDivider(Region divider) {
		this.divider = divider;
	}

	public void computeRegions(double x, double y, double width, double height) {

		switch (this.getType()) {

		case TOP: {

		}

		case BOTTOM: {

		}

		case LEFT: {

		}

		case RIGHT: {

		}

		}

	}

}
