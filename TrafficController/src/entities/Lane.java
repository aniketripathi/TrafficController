package entities;

import java.util.LinkedList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;

public class Lane {
	

	private Shape shape;

	public static enum LaneDirection {

		FORWARD, BACKWARD;
	};

	private TrafficLight light;
	private List<Vehicle> queue;
	private LaneDirection direction;
   // private static final int queueSize = 100;	
	
	
	public Lane(Shape shape, TrafficLight light, LaneDirection direction) {
		this.shape = shape;
		this.light = light;
		this.direction = direction;
		queue = new LinkedList<Vehicle>();
	}



	/**
	 * 
	 * @param graphics
	 */
	public void draw(GraphicsContext graphicsContext) {
	
		
	}
	
	
	
	public TrafficLight getLight() {
		return light;
	}

	public void setLight(TrafficLight light) {
		this.light = light;
	}

	public List<Vehicle> getQueue() {
		return queue;
	}

	public void setQueue(ObservableList<Vehicle> queue) {
		this.queue = queue;
	}

	public LaneDirection getDirection() {
		return direction;
	}

	public void setDirection(LaneDirection direction) {
		this.direction = direction;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

}
