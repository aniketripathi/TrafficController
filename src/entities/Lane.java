package entities;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Path;
import util.Region;

public abstract class Lane {
	

	private Region region;


	// 1- close to divider, 2 - away from divider
	
	private List<Vehicle> queue;
	private Path path;
	
   // private static final int queueSize = 100;	
	
	
	public Lane() {
		region = new Region();
		queue = new LinkedList<Vehicle>();
		path = new Path();
	}
	
	
	public Lane(Region region) {
		this.region = new Region();
		queue = new LinkedList<Vehicle>();
		path = new Path();
	}
	
	
	public Lane(Region region, Path path) {
		this.region = region;
		this.path = path;
		queue = new LinkedList<Vehicle>();
	}
	
	
	
	/**
	 * 
	 * @param graphics
	 */
	public void draw(GraphicsContext graphicsContext) {
		
	}
	
	
	
	public ListIterator<Vehicle> iterator(){
		return queue.listIterator();
	}


	public boolean isLaneEmpty() {
		return queue.isEmpty();
	}


	public Region getRegion() {
		return region;
	}


	public void setRegion(Region region) {
		this.region = region;
	}


	public Path getPath() {
		return path;
	}


	public void setPath(Path path) {
		this.path = path;
	}
	
	
}
