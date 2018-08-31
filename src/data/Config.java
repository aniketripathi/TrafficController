package data;

import java.io.IOException;
import java.nio.file.Path;

import exceptions.InvalidConfigException;
import exceptions.ProbabilityException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Config {

	public class LaneProperty {

		private DoubleProperty carProbability;
		private DoubleProperty twoWheelerProbability;
		private DoubleProperty heavyVehicleProbability;
		private DoubleProperty rate;

		private static final double THRESHOLD = 0.000001;
		public static final double DEFAULT_CAR_PROBABILITY = 0.5;
		public static final double DEFAULT_TWO_WHEELER_PROBABILITY = 0.2;
		public static final double DEFAULT_HEAVY_VEHICLE_PROBABILITY = 0.3;
		public static final double DEFAULT_RATE = 0.0;

		private LaneProperty() {

			carProbability = new SimpleDoubleProperty(DEFAULT_CAR_PROBABILITY);
			twoWheelerProbability = new SimpleDoubleProperty(DEFAULT_TWO_WHEELER_PROBABILITY);
			heavyVehicleProbability = new SimpleDoubleProperty(DEFAULT_HEAVY_VEHICLE_PROBABILITY);
			rate = new SimpleDoubleProperty(DEFAULT_RATE);
		}

		private void validate(double changedValue, double otherValue1, double otherValue2) throws ProbabilityException {
			if (changedValue < 0)
				throw new ProbabilityException(ProbabilityException.CODE.NEGATIVE);

			if (Math.abs(changedValue + otherValue1 + otherValue2 - 1) > THRESHOLD) {
				throw new ProbabilityException(ProbabilityException.CODE.BEYOND_RANGE);
			}
		}

		public double getCarProbability() {
			return carProbability.get();
		}

		public void setCarProbability(double carProbability) {
			validate(carProbability, twoWheelerProbability.get(), heavyVehicleProbability.get());
			this.carProbability.set(carProbability);
		}

		public double getTwoWheelerProbability() {
			return twoWheelerProbability.get();
		}

		public void setTwoWheelerProbability(double twoWheelerProbability) {
			validate(twoWheelerProbability, carProbability.get(), heavyVehicleProbability.get());
			this.twoWheelerProbability.set(twoWheelerProbability);
		}

		public double getHeavyVehicleProbability() {
			return heavyVehicleProbability.get();
		}

		public void setHeavyVehicleProbability(double heavyVehicleProbability) {
			validate(heavyVehicleProbability, twoWheelerProbability.get(), carProbability.get());
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

	public class RoadProperty implements ChangeListener<Number> {

		private int numberOfLanes;
		private LaneProperty[] lanesProperty;
		private DoubleProperty rate;

		public static final double DEFAULT_RATE = 0.0;

		public static final int DEFAULT_NUMBER_OF_LANES = 3;

		public RoadProperty(int numberOfLanes) {

			rate = new SimpleDoubleProperty(DEFAULT_RATE);
			lanesProperty = new LaneProperty[numberOfLanes];

			for (int i = 0; i < lanesProperty.length; i++) {
				LaneProperty laneProperty = new LaneProperty();
				laneProperty.getRateProbabilityProperty().addListener(this);
			}

		}

		public double getRate() {
			return rate.get();
		}

		public void setRate(double rate, double... laneRatios) {

			if (laneRatios.length != numberOfLanes)
				throw new IllegalArgumentException(
						"Number of elements in laneRation must be equal to " + numberOfLanes);

			double sum = 0;
			for (double ratio : laneRatios) {
				sum += ratio;
			}

			final double THRESHOLD = 0.00001;
			if ((sum - 1) > THRESHOLD) {
				throw new IllegalArgumentException("Sum of laneRatios must be equal to 1");
			}

			for (int i = 0; i < laneRatios.length; i++) {
				lanesProperty[i].setRate(rate * laneRatios[i]);
			}

			this.rate.set(rate);

		}

		public LaneProperty getLaneProperty(int index) {
			return lanesProperty[index];
		}

		public int getNumberOfLanes() {
			return numberOfLanes;
		}

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			rate.set(rate.get() - oldValue.doubleValue() + newValue.doubleValue());

		}

	}

	private RoadProperty topRoadProperty;
	private RoadProperty bottomRoadProperty;
	private RoadProperty leftRoadProperty;
	private RoadProperty rightRoadProperty;

	public Config(int numberOfLanes) {
		this(numberOfLanes, numberOfLanes, numberOfLanes, numberOfLanes);
	}

	public Config(int numberOfLanesTopRoad, int numberOfLanesBottomRoad, int numberOfLanesLeftRoad,
			int numberOfLanesRightRoad) {
		topRoadProperty = new RoadProperty(numberOfLanesTopRoad);
		bottomRoadProperty = new RoadProperty(numberOfLanesBottomRoad);
		leftRoadProperty = new RoadProperty(numberOfLanesLeftRoad);
		rightRoadProperty = new RoadProperty(numberOfLanesRightRoad);
	}

	public RoadProperty getTopRoadProperty() {
		return topRoadProperty;
	}

	public RoadProperty getBottomRoadProperty() {
		return bottomRoadProperty;
	}

	public RoadProperty getLeftRoadProperty() {
		return leftRoadProperty;
	}

	public RoadProperty getRightRoadProperty() {
		return rightRoadProperty;
	}

	public void loadFromFile(Path file) throws IOException, InvalidConfigException {
		ConfigReader.readFromFile(file, this);

	}

	public void saveToFile(Path file) throws IOException {
		ConfigWriter.writeToPath(file, this);
	}

}
