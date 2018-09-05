package entities;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import util.Region;

public class ForwardLane extends Lane {

	/**
	 * The line coordinates where this lane ends
	 */
	private Line exitBarrier;
	
	private Point2D carSpawnPoint;
	private Point2D twoWheelerSpawnPoint;
	private Point2D heavyVehicleSpawnPoint;

	public ForwardLane() {
		super();
		exitBarrier = new Line();
		carSpawnPoint = new Point2D(0,0);
		twoWheelerSpawnPoint = new Point2D(0,0);
		heavyVehicleSpawnPoint = new Point2D(0,0);
		
	}


	public ForwardLane(Region region) {
		super(region);
		exitBarrier = new Line();
		carSpawnPoint = new Point2D(0,0);
		twoWheelerSpawnPoint = new Point2D(0,0);
		heavyVehicleSpawnPoint = new Point2D(0,0);
	}

	public Line getExitBarrier() {
		return exitBarrier;
	}

	public void setExitBarrier(Line exitBarrier) {
		this.exitBarrier = exitBarrier;
	}

	public Point2D getCarSpawnPoint() {
		return carSpawnPoint;
	}

	public void setCarSpawnPoint(Point2D carSpawnPoint) {
		this.carSpawnPoint = carSpawnPoint;
	}

	public Point2D getTwoWheelerSpawnPoint() {
		return twoWheelerSpawnPoint;
	}

	public void setTwoWheelerSpawnPoint(Point2D twoWheelerSpawnPoint) {
		this.twoWheelerSpawnPoint = twoWheelerSpawnPoint;
	}

	public Point2D getHeavyVehicleSpawnPoint() {
		return heavyVehicleSpawnPoint;
	}

	public void setHeavyVehicleSpawnPoint(Point2D heavyVehicleSpawnPoint) {
		this.heavyVehicleSpawnPoint = heavyVehicleSpawnPoint;
	}
	
	

}
