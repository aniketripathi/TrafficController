package main.java.util;

/**
 * 
 * @author Aniket Kumar Tripathi
 *
 *         Timer class is utility class that is used to keep track of time
 *         passed since it was initialized. The rate of flow of time can be
 *         varied by using doubleRate() and halfRate() methods. The timer by
 *         default does not include the time passed during when it was stopped.
 *         To get actual time use getFromStartNanos() method.
 */
public final class Timer {

	private long initialTime;
	private long lastCalculatedTime;
	private long elapsedTime;
	private float rate;
	private boolean stopped;
	private boolean initialised;
	public static float MAX_RATE = 4f;
	public static float MIN_RATE = 0.25f;
	public static float DEFAULT_RATE = 1;

	/**
	 * A Constructor for initialization of timer.
	 */
	public Timer() {
		stopped = true;
		initialised = false;
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
	public void stop() {

		calculateElapsedNanos();
		stopped = true;
	}

	/**
	 * Starts the timer.
	 */
	public void start() {

		if (stopped) {
			long now = System.nanoTime();
			if (!initialised) {
				initialTime = now;
				initialised = true;
			}

			lastCalculatedTime = now;
			stopped = false;
		}
	}

	/**
	 * Resets the timer. All the values are cleared and the rate is set to
	 * DEFAULT_RATE. If the value of isStopped() is false when this is invoked, this
	 * method will first stop the timer and then reset it.
	 */
	public void reset() {
		this.stop();
		initialised = false;
		initialTime = 0;
		lastCalculatedTime = 0;
		elapsedTime = 0;
		rate = DEFAULT_RATE;
	}

	/**
	 * Doubles the speed of timer. The rate cannot exceed MAX_RATE.
	 */
	public void doubleRate() {
		calculateElapsedNanos();
		rate = Math.min(MAX_RATE, rate * 2);
	}

	/**
	 * Set the rate of timer to its half of original rate. The rate cannot be less
	 * than MIN_RATE.
	 */
	public void halfRate() {
		calculateElapsedNanos();
		rate = Math.max(MIN_RATE, rate / 2);

	}

	/**
	 * Calculates the elapsedTime after a specific event. The elapsedTime attribute
	 * stores the total nanoseconds passed from the beginning of timer till now(when
	 * this is called). This is used to keep track of time passed before an event is
	 * triggered which changes the natural flow of time like stop(), halfRate() and
	 * doubleRate().
	 */
	protected void calculateElapsedNanos() {
		if (!stopped) {
			long now = System.nanoTime();
			elapsedTime += ((now - lastCalculatedTime) * rate);
			lastCalculatedTime = now;
		}
	}

	/**
	 * Returns whether the timer is stopped
	 * 
	 * @return True if timer is stopped otherwise false.
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * Return the current rate of timer. The default value is given by DEFAULT_RATE.
	 * The rate can only be changed using the methods doubleRate() and halfRate().
	 * 
	 * @return A float value between MAX_RATE and MIN_RATE.
	 */
	public float getRate() {
		return rate;
	}

	/**
	 * Returns the total time passed till now in nanoseconds. The time is dependent
	 * on rate and the duration. The stop durations of the timer is not included.
	 * 
	 * @return A long value indicating total nanoseconds passed since the timer was
	 *         first started.
	 */
	public long getElapsedNanos() {
		if (!stopped)
			calculateElapsedNanos();
		return elapsedTime;
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
		return (System.nanoTime() - initialTime);
	}
}
