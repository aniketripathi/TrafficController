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
	/*
	 * private Map createMap(double width, double height, double laneWidth, double
	 * crossingRadius, double sideLaneRadius, double dividerWidth) {
	 * 
	 * Map map = null;
	 * 
	 * //set number of lanes numberOfLanes = DEFAULT_NUMBER_OF_LANES;
	 * 
	 * // set height this.width = width; this.height = height;
	 * 
	 * // call constructor for roads topRoad = new Road(numberOfLanes); bottomRoad =
	 * new Road(numberOfLanes); leftRoad = new Road(numberOfLanes); rightRoad = new
	 * Road(numberOfLanes);
	 * 
	 *//**** Create Shapes ****/
	/*
	 * 
	 * //1. create 8 horizontal lane shapes and 8 vertical lane shapes double
	 * topLeftEndX = (height/2) - (3 * laneWidth) + (dividerWidth/2); double
	 * topLeftEndY = (width/2) - (3 * laneWidth) + (dividerWidth/2); double
	 * horizontalLaneWidth = (width/2) - laneWidth; double verticalLaneWidth =
	 * (height/2) - laneWidth;
	 * 
	 * Shape leftRoadLaneForward2 = new Rectangle(0, topLeftEndY + laneWidth,
	 * horizontalLaneWidth, laneWidth ); Shape leftRoadLaneForward1 = new
	 * Rectangle(0, topLeftEndY + 2*laneWidth, horizontalLaneWidth, laneWidth );
	 * Shape leftRoadLaneBackward1 = new Rectangle(0, topLeftEndY + 3*laneWidth +
	 * dividerWidth , horizontalLaneWidth, laneWidth ); Shape leftRoadLaneBackward2
	 * = new Rectangle(0, topLeftEndY + 4*laneWidth + dividerWidth,
	 * horizontalLaneWidth, laneWidth );
	 * 
	 * Shape rightRoadLaneForward2 = new Rectangle(width/2 + laneWidth +
	 * dividerWidth/2, topLeftEndY + laneWidth, horizontalLaneWidth, laneWidth );
	 * Shape rightRoadLaneForward1 = new Rectangle(width/2 + laneWidth +
	 * dividerWidth/2, topLeftEndY + 2*laneWidth, horizontalLaneWidth, laneWidth );
	 * Shape rightRoadLaneBackward1 = new Rectangle(width/2 + laneWidth +
	 * dividerWidth/2, topLeftEndY + 3*laneWidth + dividerWidth ,
	 * horizontalLaneWidth, laneWidth ); Shape rightRoadLaneBackward2 = new
	 * Rectangle(width/2 + laneWidth + dividerWidth/2, topLeftEndY + 4*laneWidth +
	 * dividerWidth, horizontalLaneWidth, laneWidth );
	 * 
	 * 
	 * Shape topRoadLaneForward2 = new Rectangle(topLeftEndX + laneWidth, 0,
	 * laneWidth, verticalLaneWidth ); Shape topRoadLaneForward1 = new
	 * Rectangle(topLeftEndX + 2*laneWidth, 0, laneWidth, verticalLaneWidth ); Shape
	 * topRoadLaneBackward1 = new Rectangle(topLeftEndX + 3*laneWidth +
	 * dividerWidth, 0, laneWidth, verticalLaneWidth ); Shape topRoadLaneBackward2 =
	 * new Rectangle(topLeftEndX + 4*laneWidth + dividerWidth, 0, laneWidth,
	 * verticalLaneWidth );
	 * 
	 * Shape bottomRoadLaneForward2 = new Rectangle(topLeftEndX + laneWidth,
	 * height/2 + laneWidth + dividerWidth/2, laneWidth, verticalLaneWidth ); Shape
	 * bottomRoadLaneForward1 = new Rectangle(topLeftEndX + 2*laneWidth, height/2 +
	 * laneWidth + dividerWidth/2, laneWidth, verticalLaneWidth ); Shape
	 * bottomRoadLaneBackward1 = new Rectangle(topLeftEndX + 3*laneWidth +
	 * dividerWidth, height/2 + laneWidth + dividerWidth/2, laneWidth,
	 * verticalLaneWidth ); Shape bottomRoadLaneBackward2 = new
	 * Rectangle(topLeftEndX + 4*laneWidth + dividerWidth, height/2 + laneWidth +
	 * dividerWidth/2, laneWidth, verticalLaneWidth );
	 * 
	 * 
	 * //2.Create 8 curved lane shapes Shape leftRoadLaneForward3 = new
	 * ShapeCurvedLane(new Rectangle(), new Arc(), laneWidth); Shape
	 * leftRoadLaneBackward3 = new ShapeCurvedLane(new Rectangle(), new Arc(),
	 * laneWidth); Shape rightRoadLaneForward3 = new ShapeCurvedLane(new
	 * Rectangle(), new Arc(), laneWidth); Shape rightRoadLaneBackward3 = new
	 * ShapeCurvedLane(new Rectangle(), new Arc(), laneWidth); Shape
	 * topRoadLaneForward3 = new ShapeCurvedLane(new Rectangle(), new Arc(),
	 * laneWidth); Shape topRoadLaneBackward3 = new ShapeCurvedLane(new Rectangle(),
	 * new Arc(), laneWidth); Shape bottomRoadLaneForward3 = new ShapeCurvedLane(new
	 * Rectangle(), new Arc(), laneWidth); Shape bottomRoadLaneBackward3 = new
	 * ShapeCurvedLane(new Rectangle(), new Arc(), laneWidth);
	 * 
	 * 
	 * 
	 * //3. Shape for area crossing Shape crossing = new Circle(width/2, height/2,
	 * crossingRadius);
	 * 
	 *//*** End creating shapes ***/
	/*	
	
	
	*//** Create Lanes **/

	/*
	 * topRoad.createAllLanes(); bottomRoad.createAllLanes();
	 * leftRoad.createAllLanes(); rightRoad.createAllLanes();
	 * 
	 * 
	 *//*** Set shapes to their respective objects **//*
														 * topRoad.setLaneShapes(LaneDirection.FORWARD,
														 * topRoadLaneForward1, topRoadLaneForward2,
														 * topRoadLaneForward3);
														 * bottomRoad.setLaneShapes(LaneDirection.FORWARD,
														 * bottomRoadLaneForward1, bottomRoadLaneForward2,
														 * bottomRoadLaneForward3);
														 * leftRoad.setLaneShapes(LaneDirection.FORWARD,
														 * leftRoadLaneForward1, leftRoadLaneForward2,
														 * leftRoadLaneForward3);
														 * rightRoad.setLaneShapes(LaneDirection.FORWARD,
														 * rightRoadLaneForward1, rightRoadLaneForward2,
														 * rightRoadLaneForward3);
														 * 
														 * topRoad.setLaneShapes(LaneDirection.BACKWARD,
														 * topRoadLaneBackward1, topRoadLaneBackward2,
														 * topRoadLaneBackward3);
														 * bottomRoad.setLaneShapes(LaneDirection.BACKWARD,
														 * bottomRoadLaneBackward1, bottomRoadLaneBackward2,
														 * bottomRoadLaneBackward3);
														 * leftRoad.setLaneShapes(LaneDirection.BACKWARD,
														 * leftRoadLaneBackward1, leftRoadLaneBackward2,
														 * leftRoadLaneBackward3);
														 * rightRoad.setLaneShapes(LaneDirection.BACKWARD,
														 * rightRoadLaneBackward1, rightRoadLaneBackward2,
														 * rightRoadLaneBackward3);
														 * 
														 * 
														 * 
														 * return map;
														 * 
														 * 
														 * }
														 */

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
