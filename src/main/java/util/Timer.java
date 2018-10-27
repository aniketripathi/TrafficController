package main.java.util;

/**
 * 
 * @author Aniket Kumar Tripathi
 *
 *         Timer class is utility class that is used to keep track of time
 *         passed since it was initialized. The rate of flow of time can be
 *         varied by using doubleRate() and halfRate() methods. The timer by
 *         default does not include the time passed during when it was stopped.
 *         To get actual time use getFromStartNanos() method. The class is
 *         thread safe.
 */
public class Timer {

	private volatile long initialTime;
	private volatile long lastCalculatedTime;
	private volatile long elapsedTime;
	private volatile float rate;
	private volatile boolean stopped;
	private volatile boolean initialized;
	public static final float MAX_RATE = 4f;
	public static final float MIN_RATE = 0.25f;
	public static final float DEFAULT_RATE = 1;
	public static final long SECOND_TO_NANOS = 1000_000_000L;

	/**
	 * A Constructor for initialization of timer.
	 */
	public Timer() {
		stopped = true;
		initialized = false;
		initialTime = 0;
		lastCalculatedTime = 0;
		elapsedTime = 0;
		rate = 1;

	}

	/**
	 * Stops the current timer. The timer can be started again by calling start()
	 * method. The time passed during the stop duration will be ignored when elapsed
	 * time is calculated using getElapsedNanos() method.
	 */
	public synchronized void stop() {

		calculateElapsedNanos();
		this.setStopped(true);

	}

	/**
	 * Starts the timer.
	 */
	public synchronized void start() {

		if (this.isStopped()) {
			long now = System.nanoTime();
			if (!this.isInitialized()) {
				this.setInitialTime(now);
				this.setInitialized(true);
			}

			setLastCalculatedTime(now);
			setStopped(false);

		}
	}

	/**
	 * Resets the timer. All the values are cleared and the rate is set to
	 * DEFAULT_RATE. If the value of isStopped() is false when this is invoked, this
	 * method will first stop the timer and then reset it.
	 */
	public synchronized void reset() {

		clearTimer();
		setRate(DEFAULT_RATE);
	}

	/** This method is similar to reset but it does not changes the rate **/
	protected synchronized void clearTimer() {
		this.stop();
		setInitialized(false);
		setInitialTime(0);
		setLastCalculatedTime(0);
		setElapsedTime(0);

	}

	/**
	 * Doubles the speed of timer. The rate cannot exceed MAX_RATE.
	 */
	public synchronized void doubleRate() {
		calculateElapsedNanos();
		setRate(Math.min(MAX_RATE, rate * 2));
	}

	/**
	 * Set the rate of timer to its half of original rate. The rate cannot be less
	 * than MIN_RATE.
	 */
	public synchronized void halfRate() {
		calculateElapsedNanos();
		setRate(Math.max(MIN_RATE, rate / 2));

	}

	/**
	 * Calculates the elapsedTime after a specific event. The elapsedTime attribute
	 * stores the total nanoseconds passed from the beginning of timer till now(when
	 * this is called). This is used to keep track of time passed before an event is
	 * triggered which changes the natural flow of time like stop(), halfRate() and
	 * doubleRate().
	 */
	private void calculateElapsedNanos() {
		if (!isStopped()) {
			long now = System.nanoTime();
			synchronized (this) {
				setElapsedTime(getElapsedTime() + (long) ((now - getLastCalculatedTime()) * rate));
				setLastCalculatedTime(now);
			}
		}
	}

	/**
	 * Returns whether the timer is stopped
	 * 
	 * @return True if timer is stopped otherwise false.
	 */
	public synchronized boolean isStopped() {
		return stopped;
	}

	/**
	 * Return the current rate of timer. The default value is given by DEFAULT_RATE.
	 * The rate can only be changed using the methods doubleRate() and halfRate().
	 * 
	 * @return A float value between MAX_RATE and MIN_RATE.
	 */
	public synchronized float getRate() {
		return this.rate;
	}

	/**
	 * Returns the total time passed till now in nanoseconds. The time is dependent
	 * on rate and the duration. The stop durations of the timer is not included.
	 * 
	 * @return A long value indicating total nanoseconds passed since the timer was
	 *         first started.
	 */
	public long getElapsedNanos() {
		if (!isStopped())
			calculateElapsedNanos();
		return getElapsedTime();
	}

	/**
	 * Returns the total time elapsed from the beginning of time when the timer was
	 * first started. This time returned is not dependent on stop() or rate. It
	 * returns the total time elapsed from the first call of start() method after
	 * creation of timer or reset() method.
	 * 
	 * @return A long value indicating total nanoseconds passed since the timer was
	 *         created or reset().
	 */
	public long getFromStartNanos() {
		return (System.nanoTime() - getInitialTime());
	}

	private synchronized void setRate(float rate) {
		this.rate = rate;
	}

	public synchronized boolean isInitialized() {
		return this.initialized;
	}

	private synchronized void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	private synchronized void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	private synchronized long getInitialTime() {
		return this.initialTime;
	}

	private synchronized void setInitialTime(long initalTime) {
		this.initialTime = initialTime;
	}

	private synchronized long getElapsedTime() {
		return elapsedTime;
	}

	private synchronized void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	private synchronized long getLastCalculatedTime() {
		return this.lastCalculatedTime;
	}

	private synchronized void setLastCalculatedTime(long lastCalculatedTime) {
		this.lastCalculatedTime = lastCalculatedTime;
	}

}
