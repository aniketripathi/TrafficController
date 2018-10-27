package main.java.engine;

import main.java.entities.Road;
import main.java.entities.Road.TYPE;

public class RuleSet {

	private int[][] laneToRoadMapping;

	public RuleSet(int numberOfRoads, int numberOfLanes) {

		laneToRoadMapping = new int[numberOfRoads][numberOfLanes];
		map();
	}

	// Needs to be hard - coded
	private void map() {
		// top Road
		int ti = Road.TYPE.TOP.getIndex();
		int bi = Road.TYPE.BOTTOM.getIndex();
		int li = Road.TYPE.LEFT.getIndex();
		int ri = Road.TYPE.RIGHT.getIndex();

		laneToRoadMapping[ti][0] = li;
		laneToRoadMapping[ti][1] = bi;
		laneToRoadMapping[ti][2] = ri;

		laneToRoadMapping[bi][0] = ri;
		laneToRoadMapping[bi][1] = ti;
		laneToRoadMapping[bi][2] = li;

		laneToRoadMapping[li][0] = bi;
		laneToRoadMapping[li][1] = ri;
		laneToRoadMapping[li][2] = ti;

		laneToRoadMapping[ri][0] = ti;
		laneToRoadMapping[ri][1] = li;
		laneToRoadMapping[ri][2] = bi;

	}

	public TYPE getDestinationRoadType(TYPE sourceRoadType, int sourceLane) {
		return TYPE.getType(getDestinationRoad(sourceRoadType, sourceLane));
	}

	public int getDestinationRoad(TYPE sourceRoadType, int sourceLane) {
		return laneToRoadMapping[sourceRoadType.getIndex()][sourceLane];
	}

	public int getDestinationRoad(int sourceRoad, int sourceLane) {
		return laneToRoadMapping[sourceRoad][sourceLane];
	}

	public int getNumberOfRoads() {
		return laneToRoadMapping.length;
	}

	public int getNumberOfLanes() {
		return laneToRoadMapping[0].length;
	}

}
