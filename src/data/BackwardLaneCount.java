package data;

public class BackwardLaneCount {

	
	private  VehicleCount destroyed;
	private VehicleCount onLane;
	
	public BackwardLaneCount() {
		destroyed = new VehicleCount();
		onLane = new VehicleCount();
	}
	
	
	public VehicleCount getDestroyedCount() {
		return destroyed;
	}
	public VehicleCount getPresentCount() {
		return onLane;
	}
	
	public void reset() {
		destroyed.reset();
		onLane.reset();
	}
}
