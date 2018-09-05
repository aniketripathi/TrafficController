package engine;

import java.util.LinkedList;

import entities.Car;
import entities.HeavyVehicle;
import entities.TwoWheeler;
import entities.Vehicle;
import util.Compare;

public class VehicleManager {

	private LinkedList<Car> carPool;
	private LinkedList<TwoWheeler> twoWheelerPool;
	private LinkedList<HeavyVehicle> heavyVehiclePool;
	
	public VehicleManager() {

		carPool = new LinkedList<Car>();
		twoWheelerPool = new LinkedList<TwoWheeler>();
		heavyVehiclePool = new LinkedList<HeavyVehicle>();


	}
	
	public  Vehicle getVehicle(double carProbability, double twoWheelerProbability, double heavyVehicleProbability, long carCount, long twoWheelerCount, long heavyVehicleCount){
		Vehicle vehicle = null;
		
		if(carCount == 0 && twoWheelerCount == 0 && heavyVehicleCount == 0) {
			vehicle = getCar();
		}
		else {
			long sum =  carCount + twoWheelerCount + heavyVehicleCount;
			double carStat = carProbability - ((double)carCount)/sum, twoWheelerStat = twoWheelerProbability - ((double)twoWheelerCount)/sum, heavyVehicleStat = heavyVehicleProbability - ((double)heavyVehicleCount)/sum;
			double max = Math.max(carStat, Math.max(twoWheelerStat, heavyVehicleStat));
			
			if(Compare.isEqual(max, carStat)) {
				vehicle = getCar();
			}
			
			else if(Compare.isEqual(max, twoWheelerStat)) {
				vehicle = getTwoWheeler();
			}
			
			else if(Compare.isEqual(max, heavyVehicleStat)) {
				vehicle = getHeavyVehicle();
			}
		}
		
		return vehicle;
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

	public Car getCar() {
		Car car;

		if (isCarPoolEmpty())
			car = new Car();

		else {
			car = carPool.pollFirst();
		}

		return car;
	}

	public TwoWheeler getTwoWheeler() {
		TwoWheeler twoWheeler;

		if (isCarPoolEmpty())
			twoWheeler = new TwoWheeler();

		else {
			twoWheeler = twoWheelerPool.pollFirst();
		}

		return twoWheeler;
	}

	public HeavyVehicle getHeavyVehicle() {
		HeavyVehicle heavyVehicle;

		if (isCarPoolEmpty())
			heavyVehicle = new HeavyVehicle();

		else {
			heavyVehicle = heavyVehiclePool.pollFirst();
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
