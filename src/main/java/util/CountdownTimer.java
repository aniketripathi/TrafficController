package main.java.util;

/**
 * Not Thread Safe
 * 
 * @author Aniket Kumar Tripathi
 *
 */
public class CountdownTimer extends Timer {

	private Thread thread;
	private volatile long countdown;
	private CountdownTimerEvent event;
	private volatile boolean endCountdown;
	private volatile boolean startCountdown;
	private volatile boolean countdownInitialized;
	private final int cycleCount;
	private volatile int currentCycleCount;
	/** Indicates when the countdown thread is ready for termination **/
	private volatile boolean terminateCountdown;

	public static final int INFINITE = -1;
	private static final int UPDATE_DURATION_MILIS = 1;
	public static final int DEFAULT_CYCLE_COUNT = 1;

	/** Dummy Object for thread waiting **/
	private Object dummyObject;

	public CountdownTimer() {
		this(DEFAULT_CYCLE_COUNT);
	}

	public CountdownTimer(int cycleCount) {
		super();
		this.cycleCount = cycleCount;
		countdown = 0;
		event = null;
		thread = null;
		endCountdown = false;
		startCountdown = false;
		countdownInitialized = false;
		terminateCountdown = false;
		currentCycleCount = 0;
		dummyObject = new Object();
	}

	private void terminateCountdown() {

		while (!getTerminateCountdown()) {
			thread.interrupt();
		}

		try {
			thread.join();
		} catch (InterruptedException e) {
		}

	}

	private synchronized boolean getTerminateCountdown() {
		return this.terminateCountdown;
	}

	private synchronized void setTerminateCountdown(boolean terminateCountdown) {
		this.terminateCountdown = terminateCountdown;
	}

	public synchronized boolean isCountdownInitialized() {
		return this.countdownInitialized;
	}

	private synchronized void setCountdownInitialized(boolean countdownInitialized) {
		this.countdownInitialized = countdownInitialized;
	}

	public synchronized long getCountdown() {
		return this.countdown;
	}

	private synchronized void setCountdown(long countdown) {
		this.countdown = countdown;
	}

	private synchronized boolean getStartCountdown() {
		return this.startCountdown;
	}

	private synchronized void setStartCountdown(boolean startCountdown) {
		this.startCountdown = startCountdown;
	}

	private synchronized boolean getEndCountdown() {
		return this.endCountdown;
	}

	private synchronized void setEndCountdown(boolean endCountdown) {
		this.endCountdown = endCountdown;
	}

	private synchronized void setCurrentCycleCount(int currentCycleCount) {
		this.currentCycleCount = currentCycleCount;
	}

	public int getCycleCount() {
		return this.cycleCount;
	}

	public synchronized int getCurrentCycleCount() {
		return this.currentCycleCount;
	}

	public void setCountdown(long countdown, CountdownTimerEvent event) {
		this.setCountdown(countdown);
		this.event = event;
		this.setCountdownInitialized(true);

	}

	public void startCountdown() {
		if (isCountdownInitialized() && !isCountdownStarted()) {
			setStartCountdown(true);
			thread = new Thread(() -> update());
			thread.start();
		}
	}

	public void clearCountdown() {

		if (this.isCountdownInitialized()) {
			this.clearCountdownCycle();
			setCurrentCycleCount(0);
			setCountdownInitialized(false);
			setStartCountdown(false);
			setEndCountdown(false);
			this.terminateCountdown();
		}

	}

	protected void clearCountdownCycle() {
		super.clearTimer();
		super.start();
	}

	public boolean isCountdownFinished() {
		return (getStartCountdown() && getEndCountdown());
	}

	private boolean isAllCyclesFinished() {
		return (this.getCycleCount() != INFINITE) && (this.getCurrentCycleCount() >= this.getCycleCount());
	}

	public boolean isCountdownStarted() {
		return (getStartCountdown());
	}

	@Override
	public void start() {
		super.start();
		if (this.getStartCountdown() && !this.getEndCountdown()) {
			synchronized (dummyObject) {
				dummyObject.notify();
			}
		}
	}

	private void update() {

		try {

			if (getStartCountdown() && !getEndCountdown()) {
				while (!isAllCyclesFinished() && !thread.isInterrupted()) {
					while (this.getElapsedNanos() < this.getCountdown()) {
						synchronized (dummyObject) {
							if (this.isStopped()) {
								dummyObject.wait();
							}
						}
						Thread.sleep(UPDATE_DURATION_MILIS);

					}
					event.run();
					this.setCurrentCycleCount(this.getCurrentCycleCount() + 1);
					clearCountdownCycle();
					Thread.sleep(UPDATE_DURATION_MILIS);
				}
				setEndCountdown(true);
			}

		} catch (InterruptedException e) {

			e.printStackTrace();
			this.setTerminateCountdown(true);
		}

	}

}
