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
		
	map.draw(canvas.getGraphicsContext2D());
		
	}
	
	public void computeVehicleRegions() {
		
	}


	@Override
	public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
		map.getWidthProperty().set(canvas.getWidth());
		map.getHeightProperty().set(canvas.getHeight());
		
	}

}
