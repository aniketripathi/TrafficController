package main.java.data.recorder;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import main.java.entities.Vehicle;

public class VehicleCount implements ChangeListener<Number> {
	private final LongProperty car;
	private final LongProperty twoWheeler;
	private final LongProperty heavyVehicle;
	private final LongProperty total;

	public VehicleCount() {
		car = new SimpleLongProperty(0);
		twoWheeler = new SimpleLongProperty(0);
		heavyVehicle = new SimpleLongProperty(0);
		total = new SimpleLongProperty(0);

		car.addListener(this);
		twoWheeler.addListener(this);
		heavyVehicle.addListener(this);
	}

	public void reset() {
		car.set(0);
		twoWheeler.set(0);
		heavyVehicle.set(0);
		total.set(0);
	}

	protected LongProperty getTotalProperty() {
		return total;
	}

	public long getCarCount() {
		return car.get();
	}

	public long getTwoWheelerCount() {
		return twoWheeler.get();
	}

	public long getHeavyVehicleCount() {
		return heavyVehicle.get();
	}

	public long getTotalCount() {
		return total.get();
	}

	public void incrementCarCount() {
		car.set(car.get() + 1);
	}

	public void decrementCarCount() {
		car.set(car.get() - 1);

	}

	public void incrementTwoWheelerCount() {
		twoWheeler.set(twoWheeler.get() + 1);

	}

	public void decrementTwoWheelerCount() {
		twoWheeler.set(twoWheeler.get() - 1);
	}

	public void incrementHeavyVehicleCount() {
		heavyVehicle.set(heavyVehicle.get() + 1);
	}

	public void decrementHeavyVehicleCount() {
		heavyVehicle.set(heavyVehicle.get() + 1);
	}

	public void incrementCount(Vehicle.Type vehicleType) {
		switch (vehicleType) {
		case CAR:
			incrementCarCount();
			break;
		case TWO_WHEELER:
			incrementTwoWheelerCount();
			break;
		case HEAVY_VEHICLE:
			incrementHeavyVehicleCount();
			break;
		}
	}

	public void decrementCount(Vehicle.Type vehicleType) {
		switch (vehicleType) {
		case CAR:
			decrementCarCount();
			break;
		case TWO_WHEELER:
			decrementTwoWheelerCount();
			break;
		case HEAVY_VEHICLE:
			decrementHeavyVehicleCount();
			break;
		}
	}

	@Override
	public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
		total.set(car.get() + twoWheeler.get() + heavyVehicle.get());

	}
}
