package main.java.algorithm;

import javafx.util.Pair;
import main.java.algorithm.PriorityTuple.Priority;
import main.java.map.Map;
import main.java.util.MathEngine;

public class DynamicTrafficLight {

	class RoadsInfo {

		private int queueLength;
		private float waitingTime;
		private float rate;

		public int getQueueLength() {
			return queueLength;
		}

		public void setQueueLength(int queueLength) {
			this.queueLength = queueLength;
		}

		public float getWaitingTime() {
			return waitingTime;
		}

		public void setWaitingTime(float waitingTime) {
			this.waitingTime = waitingTime;
		}

		public float getRate() {
			return rate;
		}

		public void setRate(float rate) {
			this.rate = rate;
		}

		@Override
		public String toString() {
			return "[" + queueLength + "," + waitingTime + "," + rate + "]";
		}

	}

	private int queueLength;
	private float waitingTime;
	private float rate;
	private float outRate;
	private float maxTime;
	private float minTime;
	private float defaultTime;
	private int round;
	private float pauseTime;

	private boolean dynamic = true;
	private PriorityTuple[] roads;
	private RoadsInfo info[];

	private boolean roadSelected[];

	public DynamicTrafficLight() {
		roads = new PriorityTuple[Map.NUMBER_OF_ROADS];
		info = new RoadsInfo[Map.NUMBER_OF_ROADS];
		roadSelected = new boolean[Map.NUMBER_OF_ROADS];
		for (int i = 0; i < roads.length; i++) {
			roads[i] = new PriorityTuple();
			info[i] = new RoadsInfo();
		}

		queueLength = 50;
		waitingTime = 45;
		rate = 0.4f;
		outRate = 1.6f;
		maxTime = 90;
		minTime = 10;
		defaultTime = 30;
		round = 0;
		pauseTime = 4;
		dynamic = true;

	}

	public void updateRoadsInfo(int index, int queueLength, float waitingTime, float rate) {
		info[index].setQueueLength(queueLength);
		info[index].setRate(rate);
		info[index].setWaitingTime(waitingTime);

		updateTuples(index, queueLength, waitingTime, rate);
	}

	private void updateTuples(int index, int queueLength, float waitingTime, float rate) {

		Priority qL = (queueLength == 0) ? Priority.LOW
				: (queueLength <= this.queueLength) ? Priority.MEDIUM : Priority.HIGH;
		Priority wT = (MathEngine.isEqual(waitingTime, 0)) ? Priority.LOW
				: (MathEngine.isSmallerEquals(waitingTime, this.waitingTime)) ? Priority.MEDIUM : Priority.HIGH;
		Priority r = (MathEngine.isEqual(rate, 0)) ? Priority.LOW
				: (MathEngine.isSmallerEquals(rate, this.rate)) ? Priority.MEDIUM : Priority.HIGH;

		roads[index].setQueueLength(qL);
		roads[index].setWaitingTime(wT);
		roads[index].setRate(r);
	}

	private int getMax() {
		int max = 0;
		for (int i = 1; i < roads.length; i++) {
			if (!roadSelected[i] && roadSelected[max])
				max = i;

			if (!roadSelected[i] && roads[i].compareTo(roads[max]) >= 0)
				max = i;
		}
		return max;

	}

	public Pair<Integer, Float> dynamicTrafficLightAlgorithm() {
		int road = 0;
		float time = defaultTime;
		// Selection of the road

		if (round >= Map.NUMBER_OF_ROADS) {
			round = 0;
			for (int i = 0; i < Map.NUMBER_OF_ROADS; i++)
				roadSelected[i] = false;
		}
		road = getMax();
		round++;
		roadSelected[road] = true;
		// selection of time
		float rate = info[road].getRate();
		float queueLength = info[road].getQueueLength();

		if (MathEngine.isSmaller(rate, outRate)) {
			time = Math.max(minTime, ((queueLength / outRate) + (queueLength / outRate) * (rate / outRate)));
			time = Math.min(maxTime, time);
		} else {
			time = maxTime;
		}
		return new Pair<Integer, Float>(road, time);
	}

	public Pair<Integer, Float> staticTrafficLightAlgorithm() {
		return new Pair<Integer, Float>((round++) % 4, defaultTime);
	}

	public int getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(int queueLength) {
		this.queueLength = queueLength;
	}

	public float getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(float waitingTime) {
		this.waitingTime = waitingTime;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public float getOutRate() {
		return outRate;
	}

	public void setOutRate(float outRate) {
		this.outRate = outRate;
	}

	public float getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(float maxTime) {
		this.maxTime = maxTime;
	}

	public float getMinTime() {
		return minTime;
	}

	public void setMinTime(float minTime) {
		this.minTime = minTime;
	}

	public float getDefaultTime() {
		return defaultTime;
	}

	public void setDefaultTime(float defaultTime) {
		this.defaultTime = defaultTime;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public boolean isDynamic() {
		return this.dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public float getPauseTime() {
		return pauseTime;
	}

	public void setPauseTime(float pauseTime) {
		this.pauseTime = pauseTime;
	}

}
