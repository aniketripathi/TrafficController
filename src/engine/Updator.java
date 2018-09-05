package engine;

import java.util.ListIterator;

import entities.BackwardLane;
import entities.Vehicle;
import javafx.scene.canvas.Canvas;
import map.Map;
import util.Compare;

public class Updator {

	private Map map;
	private Canvas canvas;
	private VehicleManager vehicleManager;
	
	
	public void updateBackwardLanes() {
		
		for(int i = 0; i < map.getNumberOfLanes(); i++) {
			
			BackwardLane tb = map.getTopRoad().getBackwardLane(i);
			
			if(!tb.isLaneEmpty()) {
				ListIterator<Vehicle> iterator = tb.getQueue().listIterator(tb.getQueue().size());
				
				Vehicle vehicle = iterator.previous();
				
				/** Check last vehicle is out of lane **/
				if(Compare.isSmaller(vehicle.getRegion().getX() - vehicle.getRegion().getWidth(), 0)) {
					iterator.remove();
					vehicleManager.addToPool(vehicle);
				}
				else {
				
				}
				
				
				
			}
			
			
			
		}
		
		
	}
	
}
