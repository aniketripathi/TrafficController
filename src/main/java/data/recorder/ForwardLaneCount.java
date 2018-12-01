package main.java.data.recorder;

public class ForwardLaneCount {

	private VehicleCount generated;
	private VehicleCount inLane;

	public ForwardLaneCount() {
		generated = new VehicleCount();
		inLane = new VehicleCount();
	}

	public VehicleCount getGeneratedCount() {
		return generated;
	}

	public VehicleCount getinLaneCount() {
		return inLane;
	}

	public void reset() {
		generated.reset();
		inLane.reset();
	}

}
