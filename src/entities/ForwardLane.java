package entities;

import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import util.Region;

public class ForwardLane extends Lane{
	
	/**
	 * The line coordinates where this lane ends
	 */
	private Line exitBarrier;

	public ForwardLane() {
		super();
		exitBarrier = new Line();
	}

	public ForwardLane(Region region, Path path) {
		super(region, path);
		exitBarrier = new Line();
	}

	public ForwardLane(Region region) {
		super(region);
		exitBarrier = new Line();
	}

	public Line getExitBarrier() {
		return exitBarrier;
	}

	public void setExitBarrier(Line exitBarrier) {
		this.exitBarrier = exitBarrier;
	}
 
	
}
