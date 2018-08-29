package engine;

import java.util.LinkedList;

import entities.Car;
import entities.HeavyVehicle;
import entities.TwoWheeler;
import entities.Vehicle;

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
	
	
	
	public Car getCar() {
		Car car;
		
		if(isCarPoolEmpty())
			car =  new Car();
		
		else {
			car = carPool.pollFirst();
		}
		
		return car;
	}
	
	
	public TwoWheeler getTwoWheeler() {
		TwoWheeler twoWheeler;
		
		if(isCarPoolEmpty())
			twoWheeler =  new TwoWheeler();
		
		else {
			twoWheeler = twoWheelerPool.pollFirst();
		}
		
		return twoWheeler;
	}
	
	
	public HeavyVehicle getHeavyVehicle() {
		HeavyVehicle heavyVehicle;
		
		if(isCarPoolEmpty())
			heavyVehicle =  new HeavyVehicle();
		
		else {
			heavyVehicle = heavyVehiclePool.pollFirst();
		}
		
		return heavyVehicle;
	}
	
	
	public void addToPool(Vehicle vehicle) {
		vehicle.reset();
		if(vehicle.getClass() == Car.class) {
			carPool.addLast((Car)vehicle);
		}
		else if(vehicle.getClass() == TwoWheeler.class) {
			twoWheelerPool.addLast((TwoWheeler) vehicle);
		}
		else if(vehicle.getClass() == HeavyVehicle.class) {
			heavyVehiclePool.addLast((HeavyVehicle)vehicle);
		}
		
		
	}
	
	
}
