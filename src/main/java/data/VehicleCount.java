package main.java.data;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class VehicleCount implements ChangeListener<Number>{
	private final LongProperty car;
	private final LongProperty twoWheeler;
	private final LongProperty heavyVehicle;
	private final LongProperty total;

	public VehicleCount() {
		car = new SimpleLongProperty(0);
		twoWheeler = new SimpleLongProperty(0);
		heavyVehicle = new SimpleLongProperty(0);
		total = new SimpleLongProperty(0);

	}

	public void reset() {
		car.set(0);
		twoWheeler.set(0);
		heavyVehicle.set(0);
		total.set(0);
	}

	//public LongProperty getCarProperty() {
	//	return car;
//	}

	public LongProperty getTwoWheelerProperty() {
		return car;
	}

	public LongProperty getHeavyVehicleProperty() {
		return car;
	}

	public LongProperty getTotalProperty() {
		return total;
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

	public void incrementheavyVehicleCount() {
		heavyVehicle.set(heavyVehicle.get() + 1);
	}

	public void decrementHeavyVehicleCount() {
		heavyVehicle.set(heavyVehicle.get() + 1);
	}

	@Override
	public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
		total.set(car.get() + twoWheeler.get() + heavyVehicle.get());
		
	}
}
