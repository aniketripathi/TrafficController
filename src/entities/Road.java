package entities;

import util.Region;

public class Road {

	private int numberOfLanes;

	/**
	 * The notation of lane - index The lane closes to divider will have lowest
	 * index i.e 0 The lane most far away from divider will have highest index i.e.
	 * numberOfLanes - 1 The lanes in between will follow non decreasing order. The
	 * curved lane is included in this indexing scheme.
	 */

	private ForwardLane[] forwardLanes;
	private BackwardLane[] backwardLanes;
	private TrafficLight trafficLight;
	private Region divider;

	public Road(int numberOfLanes) {
		if (numberOfLanes < 1)
			throw new IllegalArgumentException("The numberOfLanes must be greater than 0");

		this.numberOfLanes = numberOfLanes;
		forwardLanes = new ForwardLane[numberOfLanes];
		backwardLanes = new BackwardLane[numberOfLanes];
		divider = new Region();
		trafficLight = new TrafficLight();
		createAllLanes();
	}

	private void createAllLanes() {

		for (int i = 0; i < numberOfLanes; i++) {
			forwardLanes[i] = new ForwardLane();
			backwardLanes[i] = new BackwardLane();
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

	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
	}

}
