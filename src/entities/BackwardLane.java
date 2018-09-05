package entities;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import util.Region;

public class BackwardLane extends Lane {

	private Point2D carDestroyPoint;
	private Point2D twoWheelerDestroyPoint;
	private Point2D heavyVehicleDestroyPoint;
	
	/**
	 * The line coordinates from where this lane starts
	 */
	private Line entryBarrier;

	public BackwardLane() {
		super();
		entryBarrier = new Line();
		carDestroyPoint = new Point2D(0,0);
		twoWheelerDestroyPoint = new Point2D(0,0);
		heavyVehicleDestroyPoint = new Point2D(0,0);
	}

	
	public BackwardLane(Region region) {
		super(region);
		entryBarrier = new Line();
		carDestroyPoint = new Point2D(0,0);
		twoWheelerDestroyPoint = new Point2D(0,0);
		heavyVehicleDestroyPoint = new Point2D(0,0);
	}

	public Line getEntryBarrier() {
		return entryBarrier;
	}

	public void setEntryBarrier(Line entryBarrier) {
		this.entryBarrier = entryBarrier;
		
	}


	public Point2D getCarDestroyPoint() {
		return carDestroyPoint;
	}


	public void setCarDestroyPoint(Point2D carDestroyPoint) {
		this.carDestroyPoint = carDestroyPoint;
	}


	public Point2D getTwoWheelerDestroyPoint() {
		return twoWheelerDestroyPoint;
	}


	public void setTwoWheelerDestroyPoint(Point2D twoWheelerDestroyPoint) {
		this.twoWheelerDestroyPoint = twoWheelerDestroyPoint;
	}


	public Point2D getHeavyVehicleDestroyPoint() {
		return heavyVehicleDestroyPoint;
	}


	public void setHeavyVehicleDestroyPoint(Point2D heavyVehicleDestroyPoint) {
		this.heavyVehicleDestroyPoint = heavyVehicleDestroyPoint;
	}
	
	
	}
