package main.java.engine;

import java.util.LinkedList;

import javafx.beans.property.DoubleProperty;
import main.java.entities.BackwardLane;
import main.java.entities.Car;
import main.java.entities.ForwardLane;
import main.java.entities.HeavyVehicle;
import main.java.entities.Road;
import main.java.entities.TwoWheeler;
import main.java.entities.Vehicle;
import main.java.map.Crossing;

public class VehicleManager {

	private LinkedList<Car> carPool;
	private LinkedList<TwoWheeler> twoWheelerPool;
	private LinkedList<HeavyVehicle> heavyVehiclePool;

	public VehicleManager() {

		carPool = new LinkedList<Car>();
		twoWheelerPool = new LinkedList<TwoWheeler>();
		heavyVehiclePool = new LinkedList<HeavyVehicle>();

	}

	public boolean isCarPoolEmpty() {
		return carPool.isEmpty();
	}

	public boolean isTwoWheelerPoolEmpty() {
		return twoWheelerPool.isEmpty();
	}

	public boolean isHeavyVehiclePoolEmpty() {
		return heavyVehiclePool.isEmpty();
	}

	public Car getCar(Road sourceRoad, ForwardLane sourceLane, Road destinationRoad, BackwardLane destinationLane,
			Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		Car car;

		if (isCarPoolEmpty())
			car = new Car(sourceRoad, sourceLane, destinationRoad, destinationLane, crossing, mapWidth, mapHeight);

		else {
			car = carPool.pollFirst();
			car.setSourceRoad(sourceRoad);
			car.setSourceLane(sourceLane);
			car.setCurrentRoad(sourceRoad);
			car.setCurrentLane(sourceLane);
			car.setDestinationRoad(destinationRoad);
			car.setDestinationLane(destinationLane);
			car.setCrossing(crossing);
			car.getRegion().setX(sourceLane.getCarSpawnPoint().getX());
			car.getRegion().setY(sourceLane.getCarSpawnPoint().getY());
			car.updateDirection();
		}

		return car;
	}

	public TwoWheeler getTwoWheeler(Road sourceRoad, ForwardLane sourceLane, Road destinationRoad,
			BackwardLane destinationLane, Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		TwoWheeler twoWheeler;

		if (isTwoWheelerPoolEmpty())
			twoWheeler = new TwoWheeler(sourceRoad, sourceLane, destinationRoad, destinationLane, crossing, mapWidth,
					mapHeight);

		else {
			twoWheeler = twoWheelerPool.pollFirst();
			twoWheeler.setSourceRoad(sourceRoad);
			twoWheeler.setSourceLane(sourceLane);
			twoWheeler.setCurrentRoad(sourceRoad);
			twoWheeler.setCurrentLane(sourceLane);
			twoWheeler.setDestinationRoad(destinationRoad);
			twoWheeler.setDestinationLane(destinationLane);
			twoWheeler.setCrossing(crossing);
			twoWheeler.getRegion().setX(sourceLane.getTwoWheelerSpawnPoint().getX());
			twoWheeler.getRegion().setY(sourceLane.getTwoWheelerSpawnPoint().getY());
			twoWheeler.updateDirection();
		}

		return twoWheeler;
	}

	public HeavyVehicle getHeavyVehicle(Road sourceRoad, ForwardLane sourceLane, Road destinationRoad,
			BackwardLane destinationLane, Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		HeavyVehicle heavyVehicle;

		if (isHeavyVehiclePoolEmpty())
			heavyVehicle = new HeavyVehicle(sourceRoad, sourceLane, destinationRoad, destinationLane, crossing,
					mapWidth, mapHeight);

		else {
			heavyVehicle = heavyVehiclePool.pollFirst();
			heavyVehicle.setSourceRoad(sourceRoad);
			heavyVehicle.setSourceLane(sourceLane);
			heavyVehicle.setCurrentRoad(sourceRoad);
			heavyVehicle.setCurrentLane(sourceLane);
			heavyVehicle.setDestinationRoad(destinationRoad);
			heavyVehicle.setDestinationLane(destinationLane);
			heavyVehicle.setCrossing(crossing);
			heavyVehicle.getRegion().setX(sourceLane.getHeavyVehicleSpawnPoint().getX());
			heavyVehicle.getRegion().setY(sourceLane.getHeavyVehicleSpawnPoint().getY());
			heavyVehicle.updateDirection();
		}

		return heavyVehicle;
	}

	public void addToPool(Vehicle vehicle) {
		vehicle.reset();
		if (vehicle.getClass() == Car.class) {
			carPool.addLast((Car) vehicle);
		} else if (vehicle.getClass() == TwoWheeler.class) {
			twoWheelerPool.addLast((TwoWheeler) vehicle);
		} else if (vehicle.getClass() == HeavyVehicle.class) {
			heavyVehiclePool.addLast((HeavyVehicle) vehicle);
		}

	}

	public void clearCarPool() {
		carPool.clear();
	}

	public void clearTwoWheelerPool() {
		twoWheelerPool.clear();
	}

	public void clearHeavyVehiclePool() {
		heavyVehiclePool.clear();
	}

	public void clearAllPools() {
		clearCarPool();
		clearTwoWheelerPool();
		clearHeavyVehiclePool();
	}

}
