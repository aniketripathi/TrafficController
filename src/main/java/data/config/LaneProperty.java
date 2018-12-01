package main.java.data.config;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import main.java.exceptions.ProbabilityException;
import main.java.util.MathEngine;

public class LaneProperty {

	private DoubleProperty carProbability;
	private DoubleProperty twoWheelerProbability;
	private DoubleProperty heavyVehicleProbability;
	private DoubleProperty rate;

	public static final double DEFAULT_CAR_PROBABILITY = 0.5;
	public static final double DEFAULT_TWO_WHEELER_PROBABILITY = 0.4;
	public static final double DEFAULT_HEAVY_VEHICLE_PROBABILITY = 0.1;
	public static final double DEFAULT_RATE = 0.2;

	public LaneProperty() {

		carProbability = new SimpleDoubleProperty(DEFAULT_CAR_PROBABILITY);
		twoWheelerProbability = new SimpleDoubleProperty(DEFAULT_TWO_WHEELER_PROBABILITY);
		heavyVehicleProbability = new SimpleDoubleProperty(DEFAULT_HEAVY_VEHICLE_PROBABILITY);
		rate = new SimpleDoubleProperty(DEFAULT_RATE);
	}

	public LaneProperty(LaneProperty laneProperty) {
		carProbability = new SimpleDoubleProperty(laneProperty.getCarProbability());
		twoWheelerProbability = new SimpleDoubleProperty(laneProperty.getTwoWheelerProbability());
		heavyVehicleProbability = new SimpleDoubleProperty(laneProperty.getHeavyVehicleProbability());
		rate = new SimpleDoubleProperty(laneProperty.getRate());
	}

	private static boolean probabilityValidate(double value) {
		return MathEngine.inRange(value, 0, 1);
	}

	public void validate() throws ProbabilityException {
		if (!probabilityValidate(carProbability.get()) || !probabilityValidate(twoWheelerProbability.get())
				|| !probabilityValidate(heavyVehicleProbability.get()) || Math.abs(carProbability.get()
						+ twoWheelerProbability.get() + heavyVehicleProbability.get() - 1) > MathEngine.THRESHOLD) {
			throw new ProbabilityException(ProbabilityException.CODE.BEYOND_RANGE);
		}
	}

	public void reset() {
		setCarProbability(DEFAULT_CAR_PROBABILITY);
		setTwoWheelerProbability(DEFAULT_TWO_WHEELER_PROBABILITY);
		setHeavyVehicleProbability(DEFAULT_HEAVY_VEHICLE_PROBABILITY);
		setRate(DEFAULT_RATE);
	}

	public double getCarProbability() {
		return carProbability.get();
	}

	public void setCarProbability(double carProbability) {
		this.carProbability.set(carProbability);
	}

	public double getTwoWheelerProbability() {
		return twoWheelerProbability.get();
	}

	public void setTwoWheelerProbability(double twoWheelerProbability) {
		this.twoWheelerProbability.set(twoWheelerProbability);
	}

	public double getHeavyVehicleProbability() {
		return heavyVehicleProbability.get();
	}

	public void setHeavyVehicleProbability(double heavyVehicleProbability) {
		this.heavyVehicleProbability.set(heavyVehicleProbability);
	}

	public double getRate() {
		return rate.get();
	}

	public void setRate(double rate) {
		this.rate.set(rate);
	}

	public DoubleProperty getCarProbabilityProperty() {
		return carProbability;
	}

	public DoubleProperty getTwoWheelerProbabilityProperty() {
		return twoWheelerProbability;
	}

	public DoubleProperty getHeavyVehicleProbabilityProperty() {
		return heavyVehicleProbability;
	}

	public DoubleProperty getRateProbabilityProperty() {
		return rate;
	}

}
