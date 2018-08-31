package data;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class VehicleCount {
	private LongProperty car;
	private LongProperty twoWheeler;
	private LongProperty heavyVehicle;
	private LongProperty total;

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

	public LongProperty getCarProperty() {
		return car;
	}

	public LongProperty getTwoWheelerProperty() {
		return car;
	}

	public LongProperty getHeavyVehicleProperty() {
		return car;
	}

	public void incrementCarCount() {
		car.set(car.get() + 1);
		total.set(total.get() + 1);
	}

	public void decrementCarCount() {
		car.set(car.get() - 1);
		total.set(total.get() - 1);

	}

	public void incrementTwoWheelerCount() {
		twoWheeler.set(twoWheeler.get() + 1);
		total.set(total.get() + 1);
	}

	public void decrementTwoWheelerCount() {
		twoWheeler.set(twoWheeler.get() - 1);
		total.set(total.get() - 1);
	}

	public void incrementheavyVehicleCount() {
		heavyVehicle.set(heavyVehicle.get() + 1);
		total.set(total.get() + 1);
	}

	public void decrementHeavyVehicleCount() {
		heavyVehicle.set(heavyVehicle.get() + 1);
		total.set(total.get() - 1);
	}
}
