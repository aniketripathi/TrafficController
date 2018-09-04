package entities;

import javafx.scene.paint.Color;
import util.Region;

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
	private LightColor color;
	private double timer;
	private Region region;
	public static final LightColor DEFAULT_COLOR = LightColor.RED;

	public TrafficLight(boolean enabled) {
		this.enabled = enabled;
		this.timer = 0;
		this.color = DEFAULT_COLOR;
		this.region = new Region();
	}

	public TrafficLight() {
		this(false);
	}

	public TrafficLight(Region region, boolean enabled) {
		this.enabled = enabled;
		this.timer = 0;
		this.color = DEFAULT_COLOR;
		this.region = region;
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

	public void setColor(LightColor color) {
		this.color = color;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

}
