package entities;


public class TrafficLight {
	
	public static enum LightColor {
		RED,
		GREEN;
	}
	
	private boolean enabled = false;
	private LightColor color;
	private double timer;
	public static final LightColor DEFAULT_COLOR = LightColor.RED;
	
	
	
	public TrafficLight(boolean enabled) {
		this.enabled = enabled;
		this.timer = 0;
		this.color = DEFAULT_COLOR;
	}
	
	
	public TrafficLight() {
		this(false);
	}
	
	
	public double getTimer() {
		return timer;
	}
	public void setTimer(double timer) {
		this.timer = timer;
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
	
	
}
