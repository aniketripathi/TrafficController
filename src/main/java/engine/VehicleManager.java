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

	public Car getCar(Road currentRoad, ForwardLane sourceLane, Road destinationRoad, BackwardLane destinationLane,
			Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		Car car;

		if (isCarPoolEmpty())
			car = new Car(currentRoad, sourceLane, destinationRoad, destinationLane, crossing, mapWidth, mapHeight);

		else {
			car = carPool.pollFirst();
			car.setCurrentRoad(currentRoad);
			car.setCurrentLane(sourceLane);
			car.setDestinationRoad(destinationRoad);
			car.setDestinationLane(destinationLane);
			car.setCrossing(crossing);
		}

		return car;
	}

	public TwoWheeler getTwoWheeler(Road currentRoad, ForwardLane sourceLane, Road destinationRoad,
			BackwardLane destinationLane, Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		TwoWheeler twoWheeler;

		if (isCarPoolEmpty())
			twoWheeler = new TwoWheeler(currentRoad, sourceLane, destinationRoad, destinationLane, crossing, mapWidth,
					mapHeight);

		else {
			twoWheeler = twoWheelerPool.pollFirst();
			twoWheeler.setCurrentRoad(currentRoad);
			twoWheeler.setCurrentLane(sourceLane);
			twoWheeler.setDestinationRoad(destinationRoad);
			twoWheeler.setDestinationLane(destinationLane);
			twoWheeler.setCrossing(crossing);
		}

		return twoWheeler;
	}

	public HeavyVehicle getHeavyVehicle(Road currentRoad, ForwardLane sourceLane, Road destinationRoad,
			BackwardLane destinationLane, Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {
		HeavyVehicle heavyVehicle;

		if (isCarPoolEmpty())
			heavyVehicle = new HeavyVehicle(currentRoad, sourceLane, destinationRoad, destinationLane, crossing,
					mapWidth, mapHeight);

		else {
			heavyVehicle = heavyVehiclePool.pollFirst();
			heavyVehicle.setCurrentRoad(currentRoad);
			heavyVehicle.setCurrentLane(sourceLane);
			heavyVehicle.setDestinationRoad(destinationRoad);
			heavyVehicle.setDestinationLane(destinationLane);
			heavyVehicle.setCrossing(crossing);
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

}
