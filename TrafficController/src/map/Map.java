package map;

import entities.Road;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Shape;

public class Map {
	
	private double width;
	private double height;
	
	private int numberOfLanes;
	public static final int DEFAULT_NUMBER_OF_LANES = 3;
	private Road topRoad;
	private Road bottomRoad;
	private Road leftRoad;
	private Road rightRoad;
	
	private Shape crossing;
	private GraphicsContext graphicsContext;
	private boolean areLanesEqual;

	private Map() {}
	
	
	private Map createMap(double width, double height) {
	 
		Map map = null;
		
	//set number of lanes
		numberOfLanes = DEFAULT_NUMBER_OF_LANES;
		
	// set height	
		this.width = width;
		this.height = height;
		
	// call constructor for roads
		topRoad = new Road(numberOfLanes);
		bottomRoad = new Road(numberOfLanes);
		leftRoad = new Road(numberOfLanes);
		rightRoad = new Road(numberOfLanes);
	
		
		
		return null;
		
		
	}
	
	
	
	public Map getMap() {
		
		
		return null;
	}
	
	
	public Road getTopRoad() {
		return topRoad;
	}
	public void setTopRoad(Road topRoad) {
		this.topRoad = topRoad;
	}
	public Road getBottomRoad() {
		return bottomRoad;
	}
	public void setBottomRoad(Road bottomRoad) {
		this.bottomRoad = bottomRoad;
	}
	public Road getLeftRoad() {
		return leftRoad;
	}
	public void setLeftRoad(Road leftRoad) {
		this.leftRoad = leftRoad;
	}
	public Road getRightRoad() {
		return rightRoad;
	}
	public void setRightRoad(Road rightRoad) {
		this.rightRoad = rightRoad;
	}
	public Shape getCrossing() {
		return crossing;
	}
	public void setCrossing(Shape crossing) {
		this.crossing = crossing;
	}
	public GraphicsContext getGraphicsContext() {
		return graphicsContext;
	}
	public void setGraphicsContext(GraphicsContext graphicsContext) {
		this.graphicsContext = graphicsContext;
	}
	public boolean isAreLanesEqual() {
		return areLanesEqual;
	}
	public void setAreLanesEqual(boolean areLanesEqual) {
		this.areLanesEqual = areLanesEqual;
	}
	
	
	
}
