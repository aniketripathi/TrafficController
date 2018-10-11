package main.java.data;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

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
	private LongProperty totalCrossed;
	private LongProperty onCrossingArea;
	private int numberOfLanes;

	private static final int NUMBER_OF_ROADS = 4;
	public static final int TOP_ROAD_INDEX = 0;
	public static final int BOTTOM_ROAD_INDEX = 1;
	public static final int LEFT_ROAD_INDEX = 2;
	public static final int RIGHT_ROAD_INDEX = 3;

	public CrossingCount(int numberOfLanes) {
		totalCrossed = new SimpleLongProperty();
		onCrossingArea = new SimpleLongProperty();
		this.numberOfLanes = numberOfLanes;

		crossingDetail = new VehicleCount[NUMBER_OF_ROADS][numberOfLanes][NUMBER_OF_ROADS - 1][numberOfLanes];

		for (int i = 0; i < NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < numberOfLanes; j++) {
				for (int k = 0; k < NUMBER_OF_ROADS - 1; k++) {
					for (int l = 0; l < numberOfLanes; l++) {
						crossingDetail[i][j][k][l] = new VehicleCount();
					}
				}
			}
		}

	}

	public void reset() {
		totalCrossed.set(0);
		onCrossingArea.set(0);

		for (int i = 0; i < NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < numberOfLanes; j++) {
				for (int k = 0; k < NUMBER_OF_ROADS - 1; k++) {
					for (int l = 0; l < numberOfLanes; l++) {
						crossingDetail[i][j][k][l].reset();
					}
				}
			}
		}

	}

	public LongProperty getPresentProperty() {
		return onCrossingArea;
	}

	public LongProperty getTotalCrossedProperty() {
		return totalCrossed;
	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	public VehicleCount getCrossingDetailCount(int sourceRoad, int sourceLane, int destinationRoad,
			int destinationLane) {
		return crossingDetail[sourceRoad][sourceLane][destinationRoad][destinationLane];
	}

	public VehicleCount getTopToBottomRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(TOP_ROAD_INDEX, sourceLane, BOTTOM_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getTopToLeftRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(TOP_ROAD_INDEX, sourceLane, LEFT_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getTopToRightRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(TOP_ROAD_INDEX, sourceLane, RIGHT_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getBottomToTopRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(BOTTOM_ROAD_INDEX, sourceLane, TOP_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getBottomToLeftRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(BOTTOM_ROAD_INDEX, sourceLane, LEFT_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getBottomToRightRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(BOTTOM_ROAD_INDEX, sourceLane, RIGHT_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getLeftToTopRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(LEFT_ROAD_INDEX, sourceLane, TOP_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getLeftToBottomRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(LEFT_ROAD_INDEX, sourceLane, BOTTOM_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getLeftToRightRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(LEFT_ROAD_INDEX, sourceLane, RIGHT_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getRightToTopRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(RIGHT_ROAD_INDEX, sourceLane, TOP_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getRightToBottomRoadCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(RIGHT_ROAD_INDEX, sourceLane, BOTTOM_ROAD_INDEX, destinationLane);
	}

	public VehicleCount getRightToLeftCrossingDetail(int sourceLane, int destinationLane) {
		return getCrossingDetailCount(RIGHT_ROAD_INDEX, sourceLane, LEFT_ROAD_INDEX, destinationLane);
	}

}
