package entities;

import javafx.scene.shape.Shape;

public class TrafficLight {
	
	public static enum Color {
		RED,
		GREEN;
	}
	private Shape light;
	private boolean enabled = false;
	private Color color;
	private double timer;
	public static final Color DEFAULT_COLOR = Color.RED;
	
	
	
	public TrafficLight(Shape light, boolean enabled) {
		super();
		this.light = light;
		this.enabled = enabled;
		this.timer = 0;
		this.color = DEFAULT_COLOR;
	}
	
	
	public double getTimer() {
		return timer;
	}
	public void setTimer(double timer) {
		this.timer = timer;
	}
	public Shape getLight() {
		return light;
	}
	public void setLight(Shape light) {
		this.light = light;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	
}
