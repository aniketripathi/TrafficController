package main.java.data;

import main.java.map.Map;

public class Recorder {

	private RoadCount roads[];
	private CrossingCount crossing;
	private int numberOfLanes;

	public Recorder(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;

		roads = new RoadCount[Map.NUMBER_OF_ROADS];
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			roads[i] = new RoadCount(numberOfLanes);
		}
		crossing = new CrossingCount(numberOfLanes);
	}

	public long getGenerated() {
		long sum = 0;
		for (RoadCount road : roads) {
			sum += road.getTotalGeneratedProperty().get();
		}
		return sum;
	}

	public long getDestroyed() {
		long sum = 0;
		for (RoadCount road : roads) {
			sum += road.getTotalDestroyedProperty().get();
		}
		return sum;
	}

	public long onMap() {
		return (getGenerated() - getDestroyed());
	}

	public RoadCount getRoadCount(int index) {
		return roads[index];
	}

	public CrossingCount getCrossingCount() {
		return crossing;
	}

	public void reset() {
		crossing.reset();
		for (RoadCount road : roads) {
			road.reset();
		}
	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

}
