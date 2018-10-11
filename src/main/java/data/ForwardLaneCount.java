package main.java.data;

public class ForwardLaneCount {

	private VehicleCount generated;
	private VehicleCount onLane;

	public ForwardLaneCount() {
		generated = new VehicleCount();
		onLane = new VehicleCount();
	}

	public VehicleCount getGeneratedCount() {
		return generated;
	}

	public VehicleCount getPresentCount() {
		return onLane;
	}

	public void reset() {
		generated.reset();
		onLane.reset();
	}

}
