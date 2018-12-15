package main.java.data.recorder;

import java.util.LinkedList;

import main.java.entities.Vehicle;

public class ForwardLaneCount {

	private VehicleCount generated;
	private VehicleCount inLane;
	private LinkedList<Vehicle> bufferQueue;

	private final int BUFFER_LIMIT = 50;

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
