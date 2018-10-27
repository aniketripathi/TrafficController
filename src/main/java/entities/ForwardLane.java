package main.java.entities;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import main.java.util.MathEngine;

public class ForwardLane extends Lane {

	/**
	 * The line coordinates where this lane ends
	 */
	private Line exitBarrier;

	private Point2D carSpawnPoint;
	private Point2D twoWheelerSpawnPoint;
	private Point2D heavyVehicleSpawnPoint;

	public ForwardLane(Road road, int index) {
		super(road, index);
		exitBarrier = new Line();
		carSpawnPoint = new Point2D(0, 0);
		twoWheelerSpawnPoint = new Point2D(0, 0);
		heavyVehicleSpawnPoint = new Point2D(0, 0);

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

	public boolean isEnoughSpace(Vehicle vehicle) {
		boolean enoughSpace = false;
		double remainingLength = 0;
		Vehicle lastVehicle = this.getLast();

		if (lastVehicle == null) {
			enoughSpace = true;
		} else {
			switch (lastVehicle.getDirection()) {

			case TOP_TO_BOTTOM: {
				remainingLength = ((lastVehicle.getY() - lastVehicle.getHeight() / 2)
						- (this.getY() - this.getHeight() / 2));
				enoughSpace = MathEngine.isLargerEquals(remainingLength + Vehicle.getVehiclePadding(),
						vehicle.getHeight() / 2);
				break;
			}

			case BOTTOM_TO_TOP: {
				remainingLength = ((this.getY() + this.getHeight() / 2)
						- (lastVehicle.getY() + lastVehicle.getHeight() / 2));
				enoughSpace = MathEngine.isLargerEquals(remainingLength + Vehicle.getVehiclePadding(),
						vehicle.getHeight() / 2);
				break;
			}

			case LEFT_TO_RIGHT: {
				remainingLength = ((lastVehicle.getX() - lastVehicle.getWidth() / 2)
						- (this.getX() - this.getWidth() / 2));
				enoughSpace = MathEngine.isLargerEquals(remainingLength + Vehicle.getVehiclePadding(),
						vehicle.getWidth() / 2);
				break;
			}

			case RIGHT_TO_LEFT: {
				remainingLength = ((this.getX() + this.getWidth() / 2)
						- (lastVehicle.getX() + lastVehicle.getWidth() / 2));
				enoughSpace = MathEngine.isLargerEquals(remainingLength + Vehicle.getVehiclePadding(),
						vehicle.getWidth() / 2);
				break;
			}

			}
		}

		return enoughSpace;
	}

}
