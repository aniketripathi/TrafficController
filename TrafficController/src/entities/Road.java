package entities;

import entities.Lane.LaneDirection;
import javafx.scene.shape.Shape;

public class Road {

	private int numberOfLanes;
	private Lane[] forwardLanes;
	private Lane[] backwardLanes;
	private TrafficLight trafficLight;
	
	
	public Road(int numberOfLanes) {
	if(numberOfLanes < 1)
		throw new IllegalArgumentException("The numberOfLanes must be greater than 0");
	
		this.numberOfLanes = numberOfLanes;
		forwardLanes = new Lane[numberOfLanes];
		backwardLanes = new Lane[numberOfLanes];
		trafficLight = new TrafficLight();
		
	}
	
	
	
	public void createAllLanes() {
		
		for(int i = 0; i < numberOfLanes; i++) {
			forwardLanes[i] = new Lane(LaneDirection.FORWARD);
			backwardLanes[i] = new Lane(LaneDirection.BACKWARD);
		}
		
		
	}
	
	
	public void setLane(LaneDirection direction, Lane lane, int index) {
		if(direction == LaneDirection.FORWARD) {
			forwardLanes[index] = lane;
		}
		
		else if(direction == LaneDirection.BACKWARD) {
			backwardLanes[index] = lane;
		}
	}
	
	
	public Lane getLane(LaneDirection direction, int index) {
		Lane lane = null;
		if(direction == LaneDirection.FORWARD) {
			lane = forwardLanes[index];
		}
		
		else if(direction == LaneDirection.BACKWARD) {
			lane = backwardLanes[index];
		}
		return lane;
	}
	
	
	
	public int getNumberOfLanes() {
		return numberOfLanes;
	}
	
	
	public void setLaneShapes(LaneDirection direction, Shape ... shapes) {
		
		if(direction == LaneDirection.FORWARD) {
		
			for(int i = 0; i < shapes.length; i++)	
				forwardLanes[i].setShape(shapes[i]);
			
		}
			
			else if(direction == LaneDirection.BACKWARD) {
		
			for(int i = 0; i < shapes.length; i++)
				backwardLanes[i].setShape(shapes[i]);
			}
		
		
		}
	
	
	
	public void setLaneShapeAt(LaneDirection direction, Shape shape, int index) {
		
		if(direction == LaneDirection.FORWARD) {
			forwardLanes[index].setShape(shape);
		}
		
		else if(direction == LaneDirection.BACKWARD) {
			backwardLanes[index].setShape(shape);
		}	
	}
	
	
}
