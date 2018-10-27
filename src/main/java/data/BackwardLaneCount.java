package main.java.data;

public class BackwardLaneCount {

	private VehicleCount destroyed;
	private VehicleCount inLane;

	public BackwardLaneCount() {
		destroyed = new VehicleCount();
		inLane = new VehicleCount();
	}

	public VehicleCount getDestroyedCount() {
		return destroyed;
	}

	public VehicleCount getInLaneCount() {
		return inLane;
	}

	public void reset() {
		destroyed.reset();
		inLane.reset();
	}
}
