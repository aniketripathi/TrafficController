package main.java.entities;

import javafx.scene.paint.Color;
import main.java.util.CountdownTimerEvent;
import main.java.util.Region;

public class TrafficLight {

	public static enum LightColor {
		RED(Color.RED), GREEN(Color.GREEN);

		private Color color;

		LightColor(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return color;
		}
	}

	private boolean enabled = false;
	private volatile LightColor color;
	private double timer;
	private Region region;
	private CountdownTimerEvent event;
	public static final LightColor DEFAULT_COLOR = LightColor.RED;

	public TrafficLight(boolean enabled) {
		this(new Region(), enabled);
	}

	public TrafficLight() {
		this(true);
	}

	public TrafficLight(Region region, boolean enabled) {
		this.enabled = enabled;
		this.timer = 0;
		this.color = DEFAULT_COLOR;
		this.region = region;
	}

	public CountdownTimerEvent getCountdownTimerEvent() {
		return this.event;
	}

	public void setCountdownTimerEvent(CountdownTimerEvent event) {
		this.event = event;
	}

	public double getTimer() {
		return timer;
	}

	public void setTimer(double timer) {
		this.timer = timer;
	}

	public void resetTimer() {
		timer = 0;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public LightColor getColor() {
		return color;
	}

	public synchronized void setColor(LightColor color) {
		this.color = color;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public boolean isRed() {
		return this.color == LightColor.RED;
	}

	public boolean isGreen() {

		return this.color == LightColor.GREEN;
	}

	public double getX() {
		return region.getX();
	}

	public double getY() {
		return region.getY();
	}

	public double getHeight() {
		return region.getHeight();
	}

	public double getWidth() {
		return region.getWidth();
	}

}
