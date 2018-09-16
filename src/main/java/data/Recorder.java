package main.java.data;

public class Recorder {

	
	private RoadCount topRoad;
	private RoadCount bottomRoad;
	private RoadCount leftRoad;
	private RoadCount rightRoad;
	private CrossingCount crossing;
	private int numberOfLanes;
	
	public Recorder(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
		
		topRoad = new RoadCount(numberOfLanes);
		bottomRoad = new RoadCount(numberOfLanes);
		leftRoad = new RoadCount(numberOfLanes);
		rightRoad = new RoadCount(numberOfLanes);
		crossing = new CrossingCount(numberOfLanes);
	}
	
	
	public long getGenerated() {
		return (topRoad.getTotalGeneratedProperty().get() +
				bottomRoad.getTotalGeneratedProperty().get() +
				leftRoad.getTotalGeneratedProperty().get() +
				rightRoad.getTotalGeneratedProperty().get());
	}
	
	
	public long getDestroyed() {
		return (topRoad.getTotalDestroyedProperty().get() +
				bottomRoad.getTotalDestroyedProperty().get() +
				leftRoad.getTotalDestroyedProperty().get() +
				rightRoad.getTotalDestroyedProperty().get());
	}
	
	
	public long onMap() {
		return (getGenerated() - getDestroyed());
	}


	public RoadCount getTopRoadCount() {
		return topRoad;
	}


	public RoadCount getBottomRoadCount() {
		return bottomRoad;
	}


	public RoadCount getLeftRoadCount() {
		return leftRoad;
	}


	public RoadCount getRightRoadCount() {
		return rightRoad;
	}


	public CrossingCount getCrossingCountb() {
		return crossing;
	}


	public int getNumberOfLanes() {
		return numberOfLanes;
	}
	
	
	
}
