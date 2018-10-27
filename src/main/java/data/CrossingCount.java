package main.java.data;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import main.java.map.Map;

public class CrossingCount {

	/**
	 * Index logic: i = source road j = source lane k = destination road l =
	 * destination lane
	 * 
	 * topRoad = 0, bottomRoad = 1, leftRoad = 2, rightRoad = 3; lane closest the
	 * divider have lowest index;
	 * 
	 * No restrictions on any vehicle to go in any specific direction(except itself)
	 */
	private VehicleCount[][][][] crossingDetail;

	/**
	 * No binding due to performance reasons. Update the properties individually
	 * without violating
	 **/
	private VehicleCount totalCrossed;
	private VehicleCount inCrossing;
	private int numberOfLanes;

	public CrossingCount(int numberOfLanes) {
		totalCrossed = new VehicleCount();
		inCrossing = new VehicleCount();
		this.numberOfLanes = numberOfLanes;

		crossingDetail = new VehicleCount[Map.NUMBER_OF_ROADS][numberOfLanes][Map.NUMBER_OF_ROADS - 1][numberOfLanes];

		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < numberOfLanes; j++) {
				for (int k = 0; k < Map.NUMBER_OF_ROADS - 1; k++) {
					for (int l = 0; l < numberOfLanes; l++) {
						crossingDetail[i][j][k][l] = new VehicleCount();
					}
				}
			}
		}

	}

	public void reset() {
		totalCrossed.reset();
		inCrossing.reset();

		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < numberOfLanes; j++) {
				for (int k = 0; k < Map.NUMBER_OF_ROADS - 1; k++) {
					for (int l = 0; l < numberOfLanes; l++) {
						crossingDetail[i][j][k][l].reset();
					}
				}
			}
		}

	}

	public VehicleCount getInCrossingCount() {
		return inCrossing;
	}

	public VehicleCount getTotalCrossedCount() {
		return totalCrossed;
	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	public VehicleCount getCrossingDetailCount(int sourceRoad, int sourceLane, int destinationRoad,
			int destinationLane) {
		return crossingDetail[sourceRoad][sourceLane][destinationRoad][destinationLane];
	}

}
