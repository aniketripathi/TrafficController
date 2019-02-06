package main.java.entities;

import java.util.LinkedList;
import java.util.ListIterator;

import javafx.geometry.Point2D;
import main.java.util.MathEngine;

public class ForwardLane extends Lane {

	/**
	 * The line coordinates where this lane ends
	 */

	private Point2D carSpawnPoint;
	private Point2D twoWheelerSpawnPoint;
	private Point2D heavyVehicleSpawnPoint;

	private LinkedList<Vehicle> bufferQueue;

	private final int BUFFER_LIMIT = 300;

	public ForwardLane(Road road, int index) {
		super(road, index);
		carSpawnPoint = new Point2D(0, 0);
		twoWheelerSpawnPoint = new Point2D(0, 0);
		heavyVehicleSpawnPoint = new Point2D(0, 0);
		bufferQueue = new LinkedList<Vehicle>();
	}

	@Override
	public boolean addVehicle(Vehicle vehicle) {
		boolean added = false;
		if (this.isEnoughSpace(vehicle)) {
			added = super.addVehicle(vehicle);
		} else if (bufferQueue.size() <= BUFFER_LIMIT) {
			bufferQueue.addLast(vehicle);
			added = true;
		}

		return added;
	}

	public void updateQueues() {
		ListIterator<Vehicle> iterator = bufferQueue.listIterator();
		while (iterator.hasNext()) {
			Vehicle vehicle = iterator.next();
			if (this.isEnoughSpace(vehicle)) {
				super.addVehicle(vehicle);
				iterator.remove();
			} else {
				break;
			}
		}
	}

	@Override
	public void clearQueue() {

		super.clearQueue();
		bufferQueue.clear();
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && bufferQueue.isEmpty();
	}

	@Override
	public int getQueueSize() {
		return super.getQueueSize() + bufferQueue.size();
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
