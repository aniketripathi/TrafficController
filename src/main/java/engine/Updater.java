package main.java.engine;

import java.util.ListIterator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.util.Pair;
import main.java.algorithm.DynamicTrafficLight;
import main.java.data.config.Config;
import main.java.data.recorder.Recorder;
import main.java.entities.BackwardLane;
import main.java.entities.ForwardLane;
import main.java.entities.Road;
import main.java.entities.TrafficLight.LightColor;
import main.java.entities.Vehicle;
import main.java.entities.Vehicle.Type;
import main.java.map.Map;
import main.java.simulator.Simulator;
import main.java.util.CountdownTimerEvent;
import main.java.util.MathEngine;
import main.java.util.SerialCountdown;
import main.java.util.Timer;

public class Updater implements ChangeListener<Number> {

	private Map map;
	private Canvas canvas;
	private VehicleManager vehicleManager;
	private Config config;
	private Recorder recorder;
	private RuleSet ruleSet;
	private SerialCountdown trafficTimer;
	private Timer timer;
	private SerialCountdown switchTimer;
	private SerialCountdown[][] vehicleGenerationTimer;
	private boolean[][] generateVehicle;
	private GenerateVehicleEvent[][] generateVehicleEvent;
	private DynamicTrafficLight algo;
	private int greenRoad;

	private float crossingRate;
	private float averageQueueLength;
	private float averageWaitingTime;
	private long roadsWaitingTime[];
	private float frameRate = Simulator.EXPECTED_FPS;
	private final long switchTime = (long) (Timer.SECOND_TO_NANOS * 1.5);

	private boolean initialized;
	private boolean isStopped;
	private boolean isPaused;

	private Label timerValueLabel;
	private Label generatedValueLabel;
	private Label crossedValueLabel;
	private Label fpsValueLabel;
	private Label greenTimeValueLabel;
	private Label AQLValueLabel;
	private Label AWTValueLabel;
	private Label crossingRateValueLabel;

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

	private class TrafficTimerEvent implements CountdownTimerEvent {
		private final int i;

		public TrafficTimerEvent(int i) {
			this.i = i;
		}

		@Override
		public void run() {
			map.getRoad(i).getTrafficLight().setColor(LightColor.RED);
			roadsWaitingTime[i] = timer.getElapsedNanos();
		}
	}

	public Updater(Map map, Canvas canvas, VehicleManager vehicleManager, Config config, Recorder recorder) {

		this.averageQueueLength = 0;
		this.averageWaitingTime = 0;
		this.timer = new Timer();

		trafficTimer = new SerialCountdown(1);
		switchTimer = new SerialCountdown(1);
		vehicleGenerationTimer = new SerialCountdown[Map.NUMBER_OF_ROADS][map.getNumberOfLanes()];
		generateVehicle = new boolean[Map.NUMBER_OF_ROADS][map.getNumberOfLanes()];
		generateVehicleEvent = new GenerateVehicleEvent[Map.NUMBER_OF_ROADS][map.getNumberOfLanes()];
		roadsWaitingTime = new long[Map.NUMBER_OF_ROADS];

		this.config = config;
		this.recorder = recorder;
		this.map = map;
		this.canvas = canvas;
		this.vehicleManager = vehicleManager;
		this.ruleSet = new RuleSet(Map.NUMBER_OF_ROADS, map.getNumberOfLanes());
		canvas.widthProperty().addListener(this);
		canvas.heightProperty().addListener(this);
		greenRoad = -1;
		this.isPaused = true;
		this.isStopped = false;

		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				setGenerateVehicle(i, j, true);
				vehicleGenerationTimer[i][j] = new SerialCountdown(SerialCountdown.INFINITE);
				generateVehicleEvent[i][j] = new GenerateVehicleEvent(i, j);
				vehicleGenerationTimer[i][j].setCountdown(
						(long) (Timer.SECOND_TO_NANOS / config.getRoadProperty(i).getLaneProperty(j).getRate()),
						generateVehicleEvent[i][j]);
			}

			map.getRoad(i).getTrafficLight().setCountdownTimerEvent(new TrafficTimerEvent(i));
		}

	}

	public void reloadTimers() {
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				setGenerateVehicle(i, j, true);
				vehicleGenerationTimer[i][j].setCountdown(
						(long) (Timer.SECOND_TO_NANOS / config.getRoadProperty(i).getLaneProperty(j).getRate()),
						generateVehicleEvent[i][j]);
			}
		}
	}

	public void setAlgo(DynamicTrafficLight algo) {
		this.algo = algo;
	}

	private boolean getGenerateVehicle(int i, int j) {
		return generateVehicle[i][j];
	}

	private void setGenerateVehicle(int i, int j, boolean value) {
		generateVehicle[i][j] = value;
	}

	public void setFrameRate(float fps) {
		this.frameRate = fps;
	}

	public float getFrameRate() {
		return this.frameRate;
	}

	public void doubleRate() {
		timer.doubleRate();
		trafficTimer.doubleRate();
		switchTimer.doubleRate();
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				vehicleGenerationTimer[i][j].doubleRate();
			}
		}
	}

	public void halfRate() {
		timer.halfRate();
		trafficTimer.halfRate();
		switchTimer.halfRate();
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				vehicleGenerationTimer[i][j].halfRate();
			}
		}
	}

	public void update() {

		/** Vehicle Generation **/
		int temp = 0;
		float waitingTime = 0;
		int count = 0;
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				if (getGenerateVehicle(i, j)) {
					generateVehicle(i, j);
					setGenerateVehicle(i, j, false);
				}
				temp += map.getRoad(i).getForwardLane(j).getQueueSize();
				map.getRoad(i).getForwardLane(j).updateQueues();
			}
			if (greenRoad != i) {
				waitingTime += (timer.getElapsedNanos() - this.roadsWaitingTime[i]) / Timer.SECOND_TO_NANOS;
				++count;
			}
		}
		this.averageQueueLength = (temp) / ((float) Map.NUMBER_OF_ROADS * map.getNumberOfLanes());
		this.averageWaitingTime = waitingTime / (count);
		if ((timer.getElapsedNanos() / (float) 1_000_000_000) > 0)
			this.crossingRate = ((recorder.getCrossingCount().getTotalCrossedCount().getTotalCount()))
					/ (timer.getElapsedNanos() / (float) 1_000_000_000);
		updateVehicles();
		updateLabels();
		updateTuples();
		updateTimers();

	}

	public void updateTuples() {
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			int lane = 0;
			for (int j = 1; j < map.getNumberOfLanes(); j++) {
				if (map.getRoad(i).getForwardLane(j).getQueueSize() > map.getRoad(i).getForwardLane(lane)
						.getQueueSize()) {
					lane = j;
				}
			}
			float waitingTime;
			if (i == this.greenRoad) {
				waitingTime = 0;
			} else {
				waitingTime = (timer.getElapsedNanos() - this.roadsWaitingTime[i]) * algo.getPauseTime()
						/ Timer.SECOND_TO_NANOS;
			}

			algo.updateRoadsInfo(i, map.getRoad(i).getForwardLane(lane).getQueueSize(), waitingTime,
					config.getRoadProperty(i).getLaneProperty(lane).getRate());
		}
	}

	public void updateTimers() {

		// update tuples

		trafficTimer.getCountdownRemaining();
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				this.vehicleGenerationTimer[i][j].getCountdownRemaining();
			}
			if (greenRoad == i) {
				this.roadsWaitingTime[i] = timer.getElapsedNanos();
			}
		}

		if (!trafficTimer.isCountdownInitialized() || trafficTimer.isCountdownTerminated()) {
			if (switchTimer.isCountdownInitialized()) {
				switchTimer.start();
			}
			switchTimer.getCountdownRemaining();

			if (switchTimer.isCountdownTerminated()) {
				switchTimer.resetCountdown();
				switchTimer.setCountdown(switchTime, null);
				Pair<Integer, Float> pair = (algo.isDynamic()) ? algo.dynamicTrafficLightAlgorithm()
						: algo.staticTrafficLightAlgorithm();
				greenRoad = pair.getKey();
				map.getRoad(pair.getKey()).getTrafficLight().setColor(LightColor.GREEN);
				trafficTimer.resetCountdown();
				trafficTimer.setCountdown((long) (pair.getValue() * Timer.SECOND_TO_NANOS),
						map.getRoad(pair.getKey()).getTrafficLight().getCountdownTimerEvent());
				trafficTimer.start();
			}
		}
	}

	private void updateLabels() {

		this.AQLValueLabel.setText(String.format("%.2f", this.averageQueueLength));
		this.AWTValueLabel.setText(String.format("%.2f", this.averageWaitingTime));

		crossingRateValueLabel.setText(String.format("%.2f", crossingRate));
		timerValueLabel.setText(String.format("%.2f", timer.getElapsedNanos() / (float) 1_000_000_000));
		generatedValueLabel.setText(Long.toString(recorder.getGenerated()));
		crossedValueLabel.setText(Long.toString(recorder.getCrossingCount().getTotalCrossedCount().getTotalCount()));
		this.fpsValueLabel.setText(String.format("%.2f", this.getFrameRate()));
		this.greenTimeValueLabel
				.setText(String.format("%.2f", trafficTimer.getCountdownRemaining() / (float) 1_000_000_000));
	}

	private void updateVehicles() {
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {

			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				BackwardLane lane = map.getRoad(i).getBackwardLane(j);
				ListIterator<Vehicle> iterator = lane.listIterator();
				while (iterator.hasNext()) {
					Vehicle vehicle = iterator.next();
					boolean beDestroyed = vehicle.update();
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

		{
			ListIterator<Vehicle> iterator = map.getCrossing().listIterator();
			while (iterator.hasNext()) {
				Vehicle vehicle = iterator.next();
				boolean leftCrossing = vehicle.update();
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
				while (iterator.hasNext()) {
					Vehicle vehicle = iterator.next();
					boolean enterCrossing = vehicle.update();
					if (enterCrossing) {
						recorder.getRoadCount(i).getForwardLaneCount(j).getinLaneCount()
								.decrementCount(vehicle.getType());
						recorder.getCrossingCount().getInCrossingCount().incrementCount(vehicle.getType());
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

		double carFrac = config.getRoadProperty(sourceRoad).getLaneProperty(sourceLane).getCarProbability()
				- carCount / sum;
		double twoWheelerFrac = config.getRoadProperty(sourceRoad).getLaneProperty(sourceLane)
				.getTwoWheelerProbability() - twoWheelerCount / sum;
		double heavyVehicleFrac = config.getRoadProperty(sourceRoad).getLaneProperty(sourceLane)
				.getHeavyVehicleProbability() - heavyVehicleCount / sum;

		double max = Math.max(Math.max(carFrac, twoWheelerFrac), heavyVehicleFrac);

		if (MathEngine.isEqual(max, carFrac)) {
			vehicle = this.getVehicle(sourceRoad, sourceLane, Type.CAR);
			if (vehicle != null && map.getRoad(sourceRoad).getForwardLane(sourceLane).addVehicle(vehicle)) {
				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getGeneratedCount()
						.incrementCarCount();
				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getinLaneCount().incrementCarCount();
			} else {
				vehicleManager.addToPool(vehicle);
			}

		} else if (MathEngine.isEqual(max, twoWheelerFrac)) {
			vehicle = this.getVehicle(sourceRoad, sourceLane, Type.TWO_WHEELER);
			if (vehicle != null && map.getRoad(sourceRoad).getForwardLane(sourceLane).addVehicle(vehicle)) {

				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getGeneratedCount()
						.incrementTwoWheelerCount();
				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getinLaneCount()
						.incrementTwoWheelerCount();
			} else {
				vehicleManager.addToPool(vehicle);
			}

		} else if (MathEngine.isEqual(max, heavyVehicleFrac)) {
			vehicle = this.getVehicle(sourceRoad, sourceLane, Type.HEAVY_VEHICLE);
			if (vehicle != null && map.getRoad(sourceRoad).getForwardLane(sourceLane).addVehicle(vehicle)) {

				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getGeneratedCount()
						.incrementHeavyVehicleCount();
				recorder.getRoadCount(sourceRoad).getForwardLaneCount(sourceLane).getinLaneCount()
						.incrementHeavyVehicleCount();
			} else {
				vehicleManager.addToPool(vehicle);
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

		if (!initialized) {
			this.isStopped = false;
			this.isPaused = true;
			initialized = true;

			updateTuples();
			switchTimer.resetCountdown();
			switchTimer.setCountdown(switchTime, null);

		}
	}

	public void stop() {

		if (isInitialized() && !isStopped()) {
			timer.stop();
			trafficTimer.stop();
			trafficTimer.reset();
			switchTimer.stop();
			switchTimer.reset();
			for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
				for (int j = 0; j < map.getNumberOfLanes(); j++) {
					vehicleGenerationTimer[i][j].stop();
					vehicleGenerationTimer[i][j].resetCountdown();
				}
			}
			isStopped = true;
		}
	}

	public void start() {

		initialize();

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
			switchTimer.stop();
			for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
				for (int j = 0; j < map.getNumberOfLanes(); j++) {
					vehicleGenerationTimer[i][j].stop();
				}
			}
			isPaused = true;
		}
	}

	public void setFpsValueLabel(Label fpsValueLabel) {
		this.fpsValueLabel = fpsValueLabel;
	}

	public void setCrossingRateValueLabel(Label crossingRateValueLabel) {
		this.crossingRateValueLabel = crossingRateValueLabel;
	}

	public void setTimerValueLabel(Label timerValueLabel) {
		this.timerValueLabel = timerValueLabel;
	}

	public void setGeneratedValueLabel(Label generatedValueLabel) {
		this.generatedValueLabel = generatedValueLabel;
	}

	public void setCrossedValueLabel(Label crossedValueLabel) {
		this.crossedValueLabel = crossedValueLabel;
	}

	public void setGreenTimeValueLabel(Label greenTimeValueLabel) {
		this.greenTimeValueLabel = greenTimeValueLabel;
	}

	public void setAQLValueLabel(Label aQLValueLabel) {
		AQLValueLabel = aQLValueLabel;
	}

	public void setAWTValueLabel(Label aWTValueLabel) {
		AWTValueLabel = aWTValueLabel;
	}

	public void setConfig(Config config) {
		this.config = config;
		for (int i = 0; i < Map.NUMBER_OF_ROADS; i++) {
			for (int j = 0; j < map.getNumberOfLanes(); j++) {
				vehicleGenerationTimer[i][j].setCountdown(
						(long) (Timer.SECOND_TO_NANOS / config.getRoadProperty(i).getLaneProperty(j).getRate()),
						generateVehicleEvent[i][j]);
			}
		}
	}
}
