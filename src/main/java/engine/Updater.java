package main.java.engine;

import java.util.ListIterator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import main.java.data.Config;
import main.java.data.Recorder;
import main.java.entities.BackwardLane;
import main.java.entities.ForwardLane;
import main.java.entities.Road;
import main.java.entities.Vehicle;
import main.java.entities.Vehicle.Type;
import main.java.map.Map;
import main.java.util.CountdownTimer;
import main.java.util.CountdownTimerEvent;
import main.java.util.MathEngine;
import main.java.util.Timer;

public class Updater implements ChangeListener<Number> {

	private Map map;
	private Canvas canvas;
	private VehicleManager vehicleManager;
	private Config config;
	private Recorder recorder;
	private RuleSet ruleSet;
	private CountdownTimer trafficTimer;
	private Timer timer;
	private CountdownTimer[][] vehicleGenerationTimer;
	private boolean[][] generateVehicle;
	private GenerateVehicleEvent[][] generateVehicleEvent;

	private boolean initialized;
	private boolean isStopped;
	private boolean isPaused;

	private Label label;

	double canvasWidth;
	double canvasTranslateY = 0;

	private class GenerateVehicleEvent implements CountdownTimerEvent {

		private final int i;
		private final int j;

		public GenerateVehicleEvent(int road, int lane) {
			super();
			i = road;
			j = lane;
		}

		@Override
		public void run() {
			setGenerateVehicle(i, j, true);

		}
	}

	public Updater(Map map, Canvas canvas, VehicleManager vehicleManager, Config config, Recorder recorder,
			Label timerValueLabel) {

		this.label = timerValueLabel;
		this.timer = new Timer();
		trafficTimer = new CountdownTimer(1);
		vehicleGenerationTimer = new CountdownTimer[Map.NUMBER_OF_ROADS][map.getNumberOfLanes()];
		generateVehicle = new boolean[Map.NUMBER_OF_ROADS][map.getNumberOfLanes()];
		generateVehicleEvent = new GenerateVehicleEvent[Map.NUMBER_OF_ROADS][map.getNumberOfLanes()];

		this.config = config;
		this.recorder = recorder;
		this.map = map;
		this.canvas = canvas;
		this.vehicleManager = vehicleManager;
		this.ruleSet = new RuleSet(Map.NUMBER_OF_ROADS, map.getNumberOfLanes());
		canvas.widthProperty().addListener(this);
		canvas.heightProperty().addListener(this);

		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				setGenerateVehicle(i, j, true);
				vehicleGenerationTimer[i][j] = new CountdownTimer(CountdownTimer.INFINITE);
				generateVehicleEvent[i][j] = new GenerateVehicleEvent(i, j);
				vehicleGenerationTimer[i][j].setCountdown(
						(long) config.getRoadProperty(i).getLaneProperty(j).getRate() * CountdownTimer.SECOND_TO_NANOS,
						generateVehicleEvent[i][j]);
			}
		}

		trafficTimer.setCountdown(50 * Timer.SECOND_TO_NANOS,
				map.getRoad(2).getTrafficLight().getCountdownTimerEvent());
		initialize();

	}

	private synchronized boolean getGenerateVehicle(int i, int j) {
		return generateVehicle[i][j];
	}

	private synchronized void setGenerateVehicle(int i, int j, boolean value) {
		generateVehicle[i][j] = value;
	}

	public void doubleRate() {
		timer.doubleRate();
		trafficTimer.doubleRate();
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				vehicleGenerationTimer[i][j].doubleRate();
			}
		}
	}

	public void halfRate() {
		timer.halfRate();
		trafficTimer.halfRate();
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				vehicleGenerationTimer[i][j].doubleRate();
			}
		}
	}

	public void update() {

		/** Vehicle Generation **/
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				if (getGenerateVehicle(i, j)) {
					generateVehicle(i, j);
					setGenerateVehicle(i, j, false);
				}
			}
		}
		updateVehicles();

		label.setText(Double.toString(timer.getElapsedNanos() / (double) 1_000_000_000).toString());

	}

	private void updateVehicles() {
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {

			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				BackwardLane lane = map.getRoad(i).getBackwardLane(j);
				ListIterator<Vehicle> iterator = lane.listIterator();
				Vehicle nextVehicle = null;
				if (iterator.hasNext()) {
					Vehicle vehicle = iterator.next();
					boolean beDestroyed = vehicle.update(nextVehicle, true);
					nextVehicle = vehicle;
					if (beDestroyed) {
						iterator.remove();
						vehicleManager.addToPool(vehicle);
						recorder.getRoadCount(i).getBackwardLaneCount(j).getDestroyedCount()
								.incrementCount(vehicle.getType());
						recorder.getRoadCount(i).getBackwardLaneCount(j).getInLaneCount()
								.decrementCount(vehicle.getType());
					}
				}
				while (iterator.hasNext()) {
					Vehicle vehicle = iterator.next();
					boolean beDestroyed = vehicle.update(nextVehicle, false);
					nextVehicle = vehicle;
					if (beDestroyed) {
						iterator.remove();
						vehicleManager.addToPool(vehicle);
						recorder.getRoadCount(i).getBackwardLaneCount(j).getDestroyedCount()
								.incrementCount(vehicle.getType());
						recorder.getRoadCount(i).getBackwardLaneCount(j).getInLaneCount()
								.decrementCount(vehicle.getType());
					}
				}
			}

		}

		for (int i = 0; i < map.getCrossing().getQueueSize(); i++) {
			ListIterator<Vehicle> iterator = map.getCrossing().listIterator();
			while (iterator.hasNext()) {
				Vehicle vehicle = iterator.next();
				boolean leftCrossing = vehicle.update(null, false);
				if (leftCrossing) {
					iterator.remove();
					vehicle.getDestinationLane().addVehicle(vehicle);
					recorder.getCrossingCount().getInCrossingCount().decrementCount(vehicle.getType());
					recorder.getCrossingCount().getTotalCrossedCount().incrementCount(vehicle.getType());
					recorder.getCrossingCount()
							.getCrossingDetailCount(vehicle.getSourceRoad().getIndex(),
									vehicle.getSourceLane().getIndex(), vehicle.getDestinationRoad().getIndex(),
									vehicle.getDestinationLane().getIndex())
							.incrementCount(vehicle.getType());

				}
			}

		}

		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {

			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				ForwardLane lane = map.getRoad(i).getForwardLane(j);
				ListIterator<Vehicle> iterator = lane.listIterator();
				Vehicle nextVehicle = null;
				if (iterator.hasNext()) {
					Vehicle vehicle = iterator.next();
					boolean enterCrossing = vehicle.update(nextVehicle, true);
					nextVehicle = vehicle;
					if (enterCrossing) {
						recorder.getCrossingCount().getInCrossingCount().decrementCount(vehicle.getType());
						iterator.remove();
						map.getCrossing().addVehicle(vehicle);
					}
				}

				while (iterator.hasNext()) {
					Vehicle vehicle = iterator.next();
					boolean enterCrossing = vehicle.update(nextVehicle, false);
					nextVehicle = vehicle;
					if (enterCrossing) {
						recorder.getCrossingCount().getInCrossingCount().decrementCount(vehicle.getType());
						iterator.remove();
						map.getCrossing().addVehicle(vehicle);
					}
				}
			}

		}
	}

	public void draw() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		map.draw(gc);

		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) { // for all roads
			for (int j = 0; j < map.getNumberOfLanes(); j++) { // for all lanes
				ListIterator<Vehicle> forwardIterator = map.getRoad(i).getForwardLane(j).listIterator();
				while (forwardIterator.hasNext())
					forwardIterator.next().draw(gc);

				ListIterator<Vehicle> backwardIterator = map.getRoad(i).getBackwardLane(j).listIterator();
				while (backwardIterator.hasNext())
					backwardIterator.next().draw(gc);
			}
		}

		ListIterator<Vehicle> crossingIterator = map.getCrossing().listIterator();
		while (crossingIterator.hasNext())
			crossingIterator.next().draw(gc);
	}

	private void generateVehicle(int sourceRoad, int sourceLane) {
		Vehicle vehicle = null;

		long carCount = recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getGeneratedCount()
				.getCarCount();
		long twoWheelerCount = recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getGeneratedCount()
				.getTwoWheelerCount();
		long heavyVehicleCount = recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getGeneratedCount()
				.getHeavyVehicleCount();
		double sum = carCount + twoWheelerCount + heavyVehicleCount;

		if (MathEngine.isEqual(sum, 0)) {
			sum = 1; // some dummy value;
		}

		double carFrac = config.getRoadProperty(sourceLane).getLaneProperty(sourceLane).getCarProbability()
				- carCount / sum;
		double twoWheelerFrac = config.getRoadProperty(sourceLane).getLaneProperty(sourceLane)
				.getTwoWheelerProbability() - twoWheelerCount / sum;
		double heavyVehicleFrac = config.getRoadProperty(sourceLane).getLaneProperty(sourceLane)
				.getHeavyVehicleProbability() - heavyVehicleCount / sum;

		double max = Math.max(Math.max(carFrac, twoWheelerFrac), heavyVehicleFrac);

		if (MathEngine.isEqual(max, carFrac)) {
			vehicle = this.getVehicle(sourceRoad, sourceLane, Type.CAR);
			if (vehicle != null && map.getRoad(sourceRoad).getForwardLane(sourceLane).isEnoughSpace(vehicle)) {
				map.getRoad(sourceRoad).getForwardLane(sourceLane).addVehicle(vehicle);
				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getGeneratedCount()
						.incrementCarCount();
				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getinLaneCount().incrementCarCount();
			}
		} else if (MathEngine.isEqual(max, twoWheelerFrac)) {
			vehicle = this.getVehicle(sourceRoad, sourceLane, Type.CAR);
			if (vehicle != null && map.getRoad(sourceRoad).getForwardLane(sourceLane).isEnoughSpace(vehicle)) {
				map.getRoad(sourceRoad).getForwardLane(sourceLane).addVehicle(vehicle);
				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getGeneratedCount()
						.incrementTwoWheelerCount();
				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getinLaneCount()
						.incrementTwoWheelerCount();
			}

		} else if (MathEngine.isEqual(max, heavyVehicleFrac)) {
			vehicle = this.getVehicle(sourceRoad, sourceLane, Type.HEAVY_VEHICLE);
			if (vehicle != null && map.getRoad(sourceRoad).getForwardLane(sourceLane).isEnoughSpace(vehicle)) {
				map.getRoad(sourceRoad).getForwardLane(sourceLane).addVehicle(vehicle);
				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getGeneratedCount()
						.incrementHeavyVehicleCount();
				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getinLaneCount()
						.incrementHeavyVehicleCount();
			}
		}

	}

	private Vehicle getVehicle(int roadIndex, int laneIndex, Type vehicleType) {
		Road sourceRoad = map.getRoad(roadIndex);
		ForwardLane sourceLane = sourceRoad.getForwardLane(laneIndex);
		int destinationRoadIndex = ruleSet.getDestinationRoad(roadIndex, laneIndex);
		int destinationLaneIndex = (int) Math.round(Math.random() * (map.getNumberOfLanes() - 1)); // randomly select
																									// lane from 0 -2
		Road destinationRoad = map.getRoad(destinationRoadIndex);
		BackwardLane destinationLane = destinationRoad.getBackwardLane(destinationLaneIndex);
		Vehicle vehicle;
		switch (vehicleType) {
		case CAR:
			vehicle = vehicleManager.getCar(sourceRoad, sourceLane, destinationRoad, destinationLane, map.getCrossing(),
					map.getWidthProperty(), map.getHeightProperty());
			break;

		case TWO_WHEELER:
			vehicle = vehicleManager.getTwoWheeler(sourceRoad, sourceLane, destinationRoad, destinationLane,
					map.getCrossing(), map.getWidthProperty(), map.getHeightProperty());
			break;

		case HEAVY_VEHICLE:
			vehicle = vehicleManager.getHeavyVehicle(sourceRoad, sourceLane, destinationRoad, destinationLane,
					map.getCrossing(), map.getWidthProperty(), map.getHeightProperty());
			break;

		default:
			vehicle = null;
		}

		return vehicle;
	}

	public void computeVehicleRegions() {

	}

	@Override
	public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
		map.getWidthProperty().set(canvas.getWidth());
		map.getHeightProperty().set(canvas.getHeight());

	}

	public void reset() {
		clearVehicles();
		recorder.reset();
		isPaused = true;
		isStopped = false;
		initialized = false;

	}

	private void clearVehicles() {
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				map.getRoad(i).getForwardLane(j).clearQueue();
				map.getRoad(i).getBackwardLane(j).clearQueue();
			}
		}
		map.getCrossing().clearQueue();
	}

	public boolean isInitialized() {
		return this.initialized;
	}

	public boolean isStopped() {
		return this.isStopped;
	}

	public boolean isPaused() {
		return this.isPaused;
	}

	public void initialize() {
		trafficTimer.startCountdown();
		;
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				vehicleGenerationTimer[i][j].startCountdown();
			}
		}
		this.isStopped = false;
		this.isPaused = true;
		initialized = true;
	}

	public void stop() {

		if (isInitialized() && !isStopped()) {
			timer.stop();
			trafficTimer.stop();
			trafficTimer.clearCountdown();
			for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
				for (int j = 0; j < map.getNumberOfLanes(); j++) {
					vehicleGenerationTimer[i][j].stop();
					vehicleGenerationTimer[i][j].clearCountdown();
				}
			}
			isStopped = true;
		}
	}

	public void start() {

		if (isInitialized() && !isStopped() && isPaused()) {
			timer.start();
			trafficTimer.start();
			for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
				for (int j = 0; j < map.getNumberOfLanes(); j++) {
					vehicleGenerationTimer[i][j].start();
				}
			}
			isPaused = false;
		}
	}

	public void pause() {

		if (isInitialized() && !isStopped() && !isPaused()) {
			timer.stop();
			trafficTimer.stop();
			for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
				for (int j = 0; j < map.getNumberOfLanes(); j++) {
					vehicleGenerationTimer[i][j].stop();
				}
			}
			isPaused = true;
		}
	}

}
