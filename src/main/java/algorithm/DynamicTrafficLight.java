package main.java.algorithm;

import javafx.util.Pair;
import main.java.algorithm.PriorityTuple.Priority;
import main.java.entities.Road;
import main.java.map.Map;
import main.java.util.MathEngine;
import main.java.util.Timer;

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

		public String toString() {
			return "[" + queueLength + "," + waitingTime + "," + rate + "]";
		}

	}

	private final int QUEUE_LENGTH = 50;
	private final float WAITING_TIME = 45;
	private final float RATE = 2.5f;
	private final float MAX_TIME = 120;
	private final float MIN_TIME = 10;
	private final float DEFAULT_TIME = 30;
	private int round = 0;

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
	}

	public void updateRoadsInfo(int index, int queueLength, float waitingTime, float rate) {
		info[index].setQueueLength(queueLength);
		info[index].setRate(rate);
		info[index].setWaitingTime(waitingTime);

		updateTuples(index, queueLength, waitingTime, rate);
	}

	private void updateTuples(int index, int queueLength, float waitingTime, float rate) {

		Priority qL = (queueLength == 0) ? Priority.LOW
				: (queueLength <= QUEUE_LENGTH) ? Priority.MEDIUM : Priority.HIGH;
		Priority wT = (MathEngine.isEqual(waitingTime, 0)) ? Priority.LOW
				: (MathEngine.isSmallerEquals(waitingTime, WAITING_TIME)) ? Priority.MEDIUM : Priority.HIGH;
		Priority r = (MathEngine.isEqual(rate, 0)) ? Priority.LOW
				: (MathEngine.isSmallerEquals(rate, RATE)) ? Priority.MEDIUM : Priority.HIGH;

		roads[index].setQueueLength(qL);
		roads[index].setWaitingTime(wT);
		roads[index].setRate(r);
	}

	private int getMax() {
		int max = 0;
		for (int i = 0; i < roads.length; i++) {
			System.out.println(info[i]);
			if (roads[i].compareTo(roads[max]) >= 0 && !roadSelected[i])
				max = i;
		}
		System.out.println();
		return max;

	}

	public Pair<Integer, Float> dynamicTrafficLightAlgorithm() {
		int road = 0;
		float time = DEFAULT_TIME;
		// Selection of the road

		road = getMax();
		round++;
		roadSelected[road] = true;
		if (round >= 4) {
			round = 0;
			for (int i = 0; i < Map.NUMBER_OF_ROADS; i++)
				roadSelected[i] = false;
		}

		// selection of time
		float rate = info[road].getRate();
		int queueLength = info[road].getQueueLength();

		if (MathEngine.isSmaller(rate, RATE)) {
			time = Math.max(MIN_TIME, (((float) queueLength) / RATE * (1 + rate / RATE)));
			time = Math.min(MAX_TIME, time);
		} else {
			time = MAX_TIME;
		}
		return new Pair<Integer, Float>(road, time);
	}

	public Pair<Integer, Float> staticTrafficLightAlgorithm() {
		return new Pair<Integer, Float>((round++) % 4, DEFAULT_TIME);
	}

}
