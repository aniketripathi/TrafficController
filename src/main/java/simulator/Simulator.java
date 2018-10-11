package main.java.simulator;

import javafx.animation.AnimationTimer;
import main.java.engine.Updater;

public class Simulator {

	public static double FPS = 30;
	private Updater updater;

	private class AnimationController extends AnimationTimer {

		private Updater updater;
		private long prev = 0;
		private final long[] frameTimes = new long[25];
		private int frameTimeIndex = 0;
		private boolean arrayFilled = false;
		private long elapsedFrameNanos;
		private int rate = Simulator.NORMAL_RATE;

		public AnimationController(Updater updater) {
			this.updater = updater;

		}

		public void setRate(int rate) {
			this.rate = rate;
		}

		public int getRate() {
			return rate;
		}

		@Override
		public void handle(long now) {

			long oldFrameTime = frameTimes[frameTimeIndex];
			frameTimes[frameTimeIndex] = now;
			frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
			if (frameTimeIndex == 0) {
				arrayFilled = true;
			}

			if (arrayFilled) {
				long elapsedNanos = now - oldFrameTime;
				long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;

				if (elapsedNanosPerFrame * rate < elapsedFrameNanos) {

					updater.update();
					updater.draw();
					prev = now;
				}
				elapsedFrameNanos = now - prev;
				// double frameRate = 1_000_000_000/elapsedNanosPerFrame
				// double updateRate = 1_000_000_000/elapsedFrameNanos
			}

		}
	}

	private AnimationController animationController;

	/**
	 * Here RATE represents the delay factor between updates i.e. higher rate means
	 * slow speed
	 **/
	public static final int MAX_RATE = 16;
	public static final int MIN_RATE = 1;
	public static final int NORMAL_RATE = 4;

	public Simulator(Updater updater) {
		animationController = new AnimationController(updater);
		this.updater = updater;
	}

	public void pause() {
		animationController.stop();
		updater.getTimer().stop();
	}

	public void play() {
		animationController.start();
		updater.getTimer().start();
	}

	public void stop() {
		animationController.stop();
		updater.getTimer().start();
	}

	public void doubleSpeed() {
		animationController.setRate(Math.max(MIN_RATE, Math.round(animationController.getRate() / 2)));
		updater.getTimer().doubleRate();
	}

	public void halfSpeed() {
		animationController.setRate(Math.min(MAX_RATE, animationController.getRate() * 2));
		updater.getTimer().halfRate();
	}

}
