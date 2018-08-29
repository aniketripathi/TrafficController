package entities;

import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import util.Region;

public class BackwardLane extends Lane{
	
	/**
	 * The line coordinates from where this lane starts
	 */
	private Line entryBarrier;

	public BackwardLane() {
		super();
		entryBarrier = new Line();
	}

	public BackwardLane(Region region, Path path) {
		super(region, path);
		entryBarrier = new Line();
	}

	public BackwardLane(Region region) {
		super(region);
		entryBarrier = new Line();
	}

	public Line getEntryBarrier() {
		return entryBarrier;
	}

	public void setEntryBarrier(Line entryBarrier) {
		this.entryBarrier = entryBarrier;
	}
	
	

}
	