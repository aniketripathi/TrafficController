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

	// 1- close to divider, 2 - away from divider
	
	private List<Vehicle> queue;
	private LaneDirection direction;
   // private static final int queueSize = 100;	
	
	
	public Lane(Shape shape, LaneDirection direction) {
		this.shape = shape;
		this.direction = direction;
		queue = new LinkedList<Vehicle>();
	}


   public Lane(LaneDirection direction) {
	   this(null, direction);
   }
	
	
	
	/**
	 * 
	 * @param graphics
	 */
	public void draw(GraphicsContext graphicsContext) {
	
		
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
