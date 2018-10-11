package main.java.engine;

import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import main.java.data.Config;
import main.java.data.Recorder;
import main.java.entities.BackwardLane;
import main.java.entities.Car;
import main.java.entities.Vehicle;
import main.java.map.Map;
import main.java.util.Compare;

public class Updator implements ChangeListener<Number>{

	private Map map;
	private Canvas canvas;
	private VehicleManager vehicleManager;
	Config config;
	Recorder recorder;
	double canvasWidth;
	double canvasTranslateY = 0;
	int counter = 0;
	int timer = 10;
	private RoadVehicle roads[];
	
	class RoadVehicle {
		
		int numberOfLanes;
			LinkedList<Vehicle> forwardLanes[];
			LinkedList<Vehicle>	backwardLanes[];
		
		@SuppressWarnings("unchecked")
		public RoadVehicle(int numberOfLanes) {
			
			forwardLanes = new LinkedList[numberOfLanes];
			backwardLanes = new LinkedList[numberOfLanes];
			for(int i = 0; i < numberOfLanes; i++) {
				forwardLanes[i] = new LinkedList<Vehicle>();
				backwardLanes[i] = new LinkedList<Vehicle>();
			}
		}
		
	}
	
	public Updator(Map map, Canvas canvas, VehicleManager vehicleManager, Config config, Recorder recorder)  {
		
		roads = new RoadVehicle[Map.NUMBER_OF_ROADS];
		
		this.config = config;
		this.recorder = recorder;
		this.map = map;
		this.canvas = canvas;
		this.vehicleManager = vehicleManager;
		canvas.widthProperty().addListener(this);
		canvas.heightProperty().addListener(this);
		
		for(int j = 0; j < Map.NUMBER_OF_ROADS; j++) {
			roads[j] = new RoadVehicle(map.getNumberOfLanes());
			}
		
		
	}
		
	
	
	
	public void update() {
	if(counter <= 6 && timer % 8 == 0) {
	generateVehicle();	
	++counter;
	}
	vehicleUpdate();
		 timer++;
	}
	
	private void vehicleUpdate() {
		for(RoadVehicle roadVehicle: roads) {			// for all roads
						
				for(LinkedList<Vehicle> lane: roadVehicle.backwardLanes) {		// for all backwardLanes
					
					ListIterator<Vehicle> iterator = lane.listIterator(lane.size());
					Vehicle nextVehicle = null;
					if(iterator.hasPrevious()) {
						Vehicle vehicle = iterator.next();
						boolean beDestroyed = vehicle.update(nextVehicle, true);
						nextVehicle = vehicle;
						if(beDestroyed) {
							iterator.remove();
							vehicleManager.addToPool(vehicle);
						}
					}
					
					while(iterator.hasPrevious()) {
						 Vehicle vehicle = iterator.previous();	
						 boolean beDestroyed = vehicle.update(nextVehicle, false);
						 nextVehicle = vehicle;
						 if(beDestroyed) {
							 iterator.remove();
							 vehicleManager.addToPool(vehicle);
						 	}
						}
					}
				
				
				for(LinkedList<Vehicle> lane: roadVehicle.forwardLanes){	//for all forwardLanes
					
					ListIterator<Vehicle> iterator = lane.listIterator(lane.size());
					Vehicle nextVehicle = null;
					
					if(iterator.hasPrevious()) {
						Vehicle vehicle = iterator.previous();
						vehicle.update(nextVehicle, true);
						nextVehicle = vehicle;
					}
					
					while(iterator.hasPrevious()) {
						 Vehicle vehicle = iterator.previous();	
						 vehicle.update(nextVehicle, false);
						 nextVehicle = vehicle;
						}
					}
				
				}
			}
		
	
	
	public void draw() {
		canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		map.draw(canvas.getGraphicsContext2D());
		
		for(int i = 0; i < Map.NUMBER_OF_ROADS; i++) {			// for all roads
			for(int j = 0; j < map.getNumberOfLanes(); j++) {		// for all lanes
				
				LinkedList<Vehicle> lane = roads[i].forwardLanes[j];
					for(Vehicle vehicle: lane)
						vehicle.draw(canvas.getGraphicsContext2D());
				
				
				lane = roads[i].backwardLanes[j];
					for(Vehicle vehicle: lane)
						vehicle.draw(canvas.getGraphicsContext2D());		
				}
			}
		
		}
	
	private void generateVehicle() {
		
		for(int i = 0; i < Map.NUMBER_OF_ROADS; i++) {			// for all roads
			for(int j = 0; j < map.getNumberOfLanes(); j++) {		// for all lanes
				
					roads[i].forwardLanes[j].addFirst(new Car(map.getRoad(i), map.getRoad(i).getForwardLane(j), null, null, map.getCrossing(),map.getWidthProperty(), map.getHeightProperty()));
				
			}
		}
	}

	public void computeVehicleRegions() {
		
	}


	@Override
	public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
		map.getWidthProperty().set(canvas.getWidth());
		map.getHeightProperty().set(canvas.getHeight());
		
	}

}