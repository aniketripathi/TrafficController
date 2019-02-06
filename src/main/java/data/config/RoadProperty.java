package main.java.data.config;

import main.java.exceptions.ProbabilityException;

public class RoadProperty {

	private int numberOfLanes;
	private LaneProperty[] lanesProperty;

	public RoadProperty(int numberOfLanes) {

		this.numberOfLanes = numberOfLanes;
		lanesProperty = new LaneProperty[numberOfLanes];

		for (int i = 0; i < lanesProperty.length; i++) {
			LaneProperty laneProperty = new LaneProperty();
			lanesProperty[i] = laneProperty;
		}

	}

	public RoadProperty(RoadProperty roadProperty) {
		this.numberOfLanes = roadProperty.numberOfLanes;
		lanesProperty = new LaneProperty[numberOfLanes];
		for (int i = 0; i < lanesProperty.length; i++) {
			LaneProperty laneProperty = new LaneProperty(roadProperty.getLaneProperty(i));
			lanesProperty[i] = laneProperty;
		}
	}

	public void reset() {

		for (int i = 0; i < lanesProperty.length; i++) {
			getLaneProperty(i).reset();
		}
	}

	public void validate() throws ProbabilityException {

		for (LaneProperty lane : lanesProperty) {
			lane.validate();
		}

	}

	public LaneProperty getLaneProperty(int index) {
		return lanesProperty[index];
	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}
}
