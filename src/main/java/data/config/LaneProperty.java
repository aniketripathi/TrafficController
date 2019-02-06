package main.java.data.config;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import main.java.exceptions.ProbabilityException;
import main.java.util.MathEngine;

public class LaneProperty {

	private FloatProperty carProbability;
	private FloatProperty twoWheelerProbability;
	private FloatProperty heavyVehicleProbability;
	private FloatProperty rate;

	public static final float DEFAULT_CAR_PROBABILITY = 0.6f;
	public static final float DEFAULT_TWO_WHEELER_PROBABILITY = 0.3f;
	public static final float DEFAULT_HEAVY_VEHICLE_PROBABILITY = 0.1f;
	public static final float DEFAULT_RATE = 0.4f;

	public LaneProperty() {

		carProbability = new SimpleFloatProperty(DEFAULT_CAR_PROBABILITY);
		twoWheelerProbability = new SimpleFloatProperty(DEFAULT_TWO_WHEELER_PROBABILITY);
		heavyVehicleProbability = new SimpleFloatProperty(DEFAULT_HEAVY_VEHICLE_PROBABILITY);
		rate = new SimpleFloatProperty(DEFAULT_RATE);
	}

	public LaneProperty(LaneProperty laneProperty) {
		carProbability = new SimpleFloatProperty(laneProperty.getCarProbability());
		twoWheelerProbability = new SimpleFloatProperty(laneProperty.getTwoWheelerProbability());
		heavyVehicleProbability = new SimpleFloatProperty(laneProperty.getHeavyVehicleProbability());
		rate = new SimpleFloatProperty(laneProperty.getRate());
	}

	private static boolean probabilityValidate(float value) {
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

	public float getCarProbability() {
		return carProbability.get();
	}

	public void setCarProbability(float carProbability) {
		this.carProbability.set(carProbability);
	}

	public float getTwoWheelerProbability() {
		return twoWheelerProbability.get();
	}

	public void setTwoWheelerProbability(float twoWheelerProbability) {
		this.twoWheelerProbability.set(twoWheelerProbability);
	}

	public float getHeavyVehicleProbability() {
		return heavyVehicleProbability.get();
	}

	public void setHeavyVehicleProbability(float heavyVehicleProbability) {
		this.heavyVehicleProbability.set(heavyVehicleProbability);
	}

	public float getRate() {
		return rate.get();
	}

	public void setRate(float rate) {
		this.rate.set(rate);
	}

	public FloatProperty getCarProbabilityProperty() {
		return carProbability;
	}

	public FloatProperty getTwoWheelerProbabilityProperty() {
		return twoWheelerProbability;
	}

	public FloatProperty getHeavyVehicleProbabilityProperty() {
		return heavyVehicleProbability;
	}

	public FloatProperty getRateProbabilityProperty() {
		return rate;
	}

}
