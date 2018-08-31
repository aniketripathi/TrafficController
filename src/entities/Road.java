package entities;

public class Road {

	private int numberOfLanes;
	private ForwardLane[] forwardLanes;
	private BackwardLane[] backwardLanes;
	private TrafficLight trafficLight;

	public Road(int numberOfLanes) {
		if (numberOfLanes < 1)
			throw new IllegalArgumentException("The numberOfLanes must be greater than 0");

		this.numberOfLanes = numberOfLanes;
		forwardLanes = new ForwardLane[numberOfLanes];
		backwardLanes = new BackwardLane[numberOfLanes];
		trafficLight = new TrafficLight();

	}

	public void createAllLanes() {

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

}
