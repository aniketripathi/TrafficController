package map;

import entities.Road;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import util.Region;

public class Map {

	private double width;
	private double height;

	private int numberOfLanes;
	public static final int DEFAULT_NUMBER_OF_LANES = 3;
	private Road topRoad;
	private Road bottomRoad;
	private Road leftRoad;
	private Road rightRoad;

	private Region crossing;
	private GraphicsContext graphicsContext;
	private boolean areLanesEqual;

	/*
	 * private Map() {}
	 * 
	 *//**
		 * THIS METHOD IS CUSTOM FOR CREATING A MAP
		 * 
		 * 
		 * @param width
		 * @param height
		 * @param laneWidth
		 * @return
		 */
	
	  private Map createMap(double width, double height, double laneWidth, double crossingRadius, double sideLaneRadius, double dividerWidth) {
	  
	  Map map = null;
	  numberOfLanes = DEFAULT_NUMBER_OF_LANES;
	  
	  /** Create Roads **/
	  
	  Road topRoad = new Road(numberOfLanes);
	  Road bottomRoad = new Road(numberOfLanes);
	  Road leftRoad = new Road(numberOfLanes);
	  Road rightRoad = new Road(numberOfLanes);
	  
	  /** Set Regions for all the lanes for all roads **/
	  
	  double roadWidth = 6 * laneWidth + dividerWidth;
	  double verticalRoadHeight = (height - roadWidth)/2;
	  double horizontalRoadHeight = (width - roadWidth)/2;
	  
	  /** Set regions for curves explicitly.
	   *  Here width and height are considered with respect to canvas and not considering the orientation or direction of roads.
	   * **/
	  for(int i = 0; i < numberOfLanes; i++) {
		  
		  topRoad.getForwardLane(i).getRegion().setAll((width+dividerWidth+laneWidth)/2 + i*laneWidth, verticalRoadHeight/2, laneWidth, verticalRoadHeight);
		  topRoad.getBackwardLane(i).getRegion().setAll((width-dividerWidth-laneWidth)/2 - i*laneWidth, verticalRoadHeight/2, laneWidth, verticalRoadHeight);
		 
		  bottomRoad.getForwardLane(i).getRegion().setAll((width-dividerWidth-laneWidth)/2 - i*laneWidth, height - verticalRoadHeight/2, laneWidth, verticalRoadHeight);
		  bottomRoad.getBackwardLane(i).getRegion().setAll((width+dividerWidth+laneWidth)/2 + i*laneWidth, height - verticalRoadHeight/2, laneWidth, verticalRoadHeight);
		 
		  leftRoad.getForwardLane(i).getRegion().setAll(horizontalRoadHeight/2, (height - dividerWidth - laneWidth)/2 - i* laneWidth, horizontalRoadHeight, laneWidth);
		  leftRoad.getBackwardLane(i).getRegion().setAll(horizontalRoadHeight/2, (height + dividerWidth + laneWidth)/2 + i* laneWidth, horizontalRoadHeight, laneWidth);
		  
		  rightRoad.getForwardLane(i).getRegion().setAll(horizontalRoadHeight/2, (height + dividerWidth + laneWidth)/2 + i* laneWidth, horizontalRoadHeight, laneWidth);
		  rightRoad.getBackwardLane(i).getRegion().setAll(horizontalRoadHeight/2, (height - dividerWidth - laneWidth)/2 - i* laneWidth, horizontalRoadHeight, laneWidth);
		  
	  }
	  
	  /** TODO Set traffic light region for all roads **/
	  
	  Crossing crossing= new Crossing();
	  crossing.getRegion().setAll(width/2, height/2, roadWidth, roadWidth);
	  /** set paths for crossing TODO here**/
	  
	  
	  
	  
	return map;
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

	public Region getCrossing() {
		return crossing;
	}

	public void setCrossing(Region crossing) {
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

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
	}

}
