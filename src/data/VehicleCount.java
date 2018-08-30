package data;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class VehicleCount {
		private LongProperty car;
		private LongProperty twoWheeler;
		private LongProperty heavyVehicle;
		private LongProperty total;
		private LongBinding tempBinding1, tempBinding2;
		
		public VehicleCount() {
			car = new SimpleLongProperty(0);
			twoWheeler = new SimpleLongProperty(0);
			heavyVehicle = new SimpleLongProperty(0);
			total = new SimpleLongProperty(0);
			tempBinding1 = (LongBinding) Bindings.add(car, twoWheeler);
			tempBinding2 = (LongBinding) Bindings.add(tempBinding1, heavyVehicle);
			total.bind(tempBinding2);
		}
		
		
		public void reset() {
			car.set(0);
			twoWheeler.set(0);
			heavyVehicle.set(0);
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
}
