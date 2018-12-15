package main.java.algorithm;

public class PriorityTuple implements Comparable<PriorityTuple> {

	public enum Priority {
		LOW(0), MEDIUM(1), HIGH(2);
		private int value;

		Priority(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	private Priority queueLength;
	private Priority waitingTime;
	private Priority rate;

	public static final int QUEUE_LENGTH_WEIGHT = 5;
	public static final int WAITING_TIME_WEIGHT = 4;
	public static final int RATE_WEIGHT = 3;

	public int getWeight() {
		return (queueLength.getValue() * QUEUE_LENGTH_WEIGHT + waitingTime.getValue() * WAITING_TIME_WEIGHT
				+ rate.getValue() * RATE_WEIGHT);
	}

	public Priority getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(Priority qeueLength) {
		this.queueLength = qeueLength;
	}

	public Priority getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Priority waitingTime) {
		this.waitingTime = waitingTime;
	}

	public Priority getRate() {
		return rate;
	}

	public void setRate(Priority rate) {
		this.rate = rate;
	}

	@Override
	public int compareTo(PriorityTuple tuple) {
		int compare = 0;
		compare = (this.getWeight() - tuple.getWeight());
		if (compare == 0) {
			compare = this.getQueueLength().getValue() - tuple.getQueueLength().getValue();
			if (compare == 0) {
				compare = this.getWaitingTime().getValue() - tuple.getWaitingTime().getValue();
			}

		}
		return compare;
	}

	public String toString() {
		return "[" + queueLength + "," + waitingTime + "," + rate + "]";
	}

}
