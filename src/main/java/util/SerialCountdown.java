package main.java.util;

public class SerialCountdown extends Timer {

	private long countdownTime;
	private long beginCountdownTime;
	private long countdownRemaining;
	private CountdownTimerEvent event;
	private boolean countdownInitialized;
	private final int cycleCount;
	private int currentCycleCount;
	private boolean terminateCountdown;

	public static final int INFINITE = -1;
	public static final int DEFAULT_CYCLE_COUNT = 1;

	public SerialCountdown(int cycleCount) {
		super();
		this.cycleCount = cycleCount;
		countdownTime = 0;
		countdownRemaining = 0;
		event = null;
		countdownInitialized = false;
		terminateCountdown = false;
		currentCycleCount = 0;
	}

	public SerialCountdown() {
		this(DEFAULT_CYCLE_COUNT);
	}

	public void setCountdown(long countdown, CountdownTimerEvent event) {
		this.countdownTime = countdown;
		this.countdownRemaining = countdown;
		this.event = event;
		this.countdownInitialized = true;
	}

	public void resetCountdown() {
		this.clearTimer();
		countdownTime = 0;
		countdownRemaining = 0;
		this.beginCountdownTime = 0;
		event = null;
		countdownInitialized = false;
		terminateCountdown = false;
		currentCycleCount = 0;
	}

	@Override
	public void start() {
		if (this.countdownInitialized && this.isStopped() && !this.isCountdownTerminated()) {
			super.start();
			this.beginCountdownTime = 0;
			this.terminateCountdown = false;

		}
	}

	public boolean isCountdownInitialized() {
		return this.countdownInitialized;
	}

	public boolean isCountdownTerminated() {
		return this.terminateCountdown;
	}

	private void calculateCountdownRemaining() {

		if (this.isCountdownInitialized() && !isStopped()) {
			long temp = this.getElapsedNanos();
			this.countdownRemaining = (this.beginCountdownTime + this.countdownTime) - temp;
		}
	}

	public long getCountdownRemaining() {

		if (this.isCountdownInitialized() && !this.isCountdownTerminated()) {
			boolean cycleFinished = false;
			calculateCountdownRemaining();
			if (this.countdownRemaining <= 0) {
				this.currentCycleCount++;
				this.clearTimer();
				cycleFinished = true;
				if (event != null)
					event.run();
			}

			if (cycleFinished && this.cycleCount != INFINITE && this.currentCycleCount >= this.cycleCount) {
				this.stop();
				this.terminateCountdown = true;
				this.countdownRemaining = 0;
			}

			if (cycleFinished && !this.isCountdownTerminated()) {
				this.start();
				this.beginCountdownTime += (-this.countdownRemaining);
			}
		}

		return this.countdownRemaining;
	}

}
