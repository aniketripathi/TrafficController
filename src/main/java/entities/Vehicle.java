package main.java.entities;

import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import main.java.map.Crossing;
import main.java.util.Region;

public abstract class Vehicle {
	
	
	public enum Direction {
		TOP_TO_BOTTOM, BOTTOM_TO_TOP, LEFT_TO_RIGHT, RIGHT_TO_LEFT;
	}
	
	private double velocityX;
	private double velocityY;
	private double accelerationX;
	private double accelerationY;
	private Road currentRoad;
	private Road destinationRoad;
	private Lane currentLane;
	private BackwardLane destinationLane;
	private final Region region;
	private boolean inCrossing;
	private Crossing crossing;
	private DoubleProperty mapWidth;
	private DoubleProperty mapHeight;
	
	private Direction direction;
	
	private static double vehiclePadding = 8;
	private static double trafficLightPadding = 6;
	private static double maxSpeed = 5;
	
	public Vehicle(Road currentRoad, ForwardLane sourceLane, Road destinationRoad,
			BackwardLane destinationLane, Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {

		this.currentRoad = currentRoad;
		this.currentLane = sourceLane;
		this.destinationRoad = destinationRoad;
		this.destinationLane = destinationLane;
		this.crossing = crossing;
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
		region = new Region();
		
		this.mapWidth.addListener((obs,oldVal, newVal) -> { this.region.setX(this.region.getX() - (oldVal.doubleValue() + newVal.doubleValue())/2) ; });
		this.mapHeight.addListener((obs,oldVal, newVal) -> { this.region.setX(this.region.getY() - (oldVal.doubleValue() + newVal.doubleValue())/2) ; });
		

		this.inCrossing = false;
		velocityX = 0;
		velocityY = 0;
		accelerationX = 0;
		accelerationY = 0;
		
		
		updateDirection();
	}
	
	public abstract void computeRegions();
	
	

 	public void reset() {

		velocityX = 0;
		velocityY = 0;
		accelerationX = 0;
		accelerationY = 0;
		currentRoad = null;
		destinationRoad = null;
		currentLane = null;
		destinationLane = null;
		crossing = null;	// there is no need but done to avoid error
		region.setX(0);
		region.setY(0);
		

	}

	
	/** Only valid if vehicle is moving on the lane **/
	public void updateDirection() {
		
		if( (this.currentRoad.getType() == Road.TYPE.TOP && this.currentLane.getClass() == ForwardLane.class) ||
			(this.currentRoad.getType() == Road.TYPE.BOTTOM && this.currentLane.getClass() == BackwardLane.class))
			direction = Direction.TOP_TO_BOTTOM;
		
		else if( (this.currentRoad.getType() == Road.TYPE.TOP && this.currentLane.getClass() == BackwardLane.class) ||
				(this.currentRoad.getType() == Road.TYPE.BOTTOM && this.currentLane.getClass() == ForwardLane.class))
				direction = Direction.BOTTOM_TO_TOP;
		
		else if( (this.currentRoad.getType() == Road.TYPE.LEFT && this.currentLane.getClass() == ForwardLane.class) ||
				(this.currentRoad.getType() == Road.TYPE.RIGHT && this.currentLane.getClass() == BackwardLane.class))
				direction = Direction.LEFT_TO_RIGHT;
		
		else if( (this.currentRoad.getType() == Road.TYPE.LEFT && this.currentLane.getClass() == BackwardLane.class) ||
				(this.currentRoad.getType() == Road.TYPE.RIGHT && this.currentLane.getClass() == ForwardLane.class))
				direction = Direction.LEFT_TO_RIGHT;
		
	}
	
	
	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public double getAccelerationX() {
		return accelerationX;
	}

	public void setAccelerationX(double accelerationX) {
		this.accelerationX = accelerationX;
	}

	public double getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(double accelerationY) {
		this.accelerationY = accelerationY;
	}

	public Road getCurrentRoad() {
		return currentRoad;
	}

	public void setCurrentRoad(Road currentRoad) {
		this.currentRoad = currentRoad;
	}

	public Road getDestinationRoad() {
		return destinationRoad;
	}

	public void setDestinationRoad(Road destinationRoad) {
		this.destinationRoad = destinationRoad;
	}

	public Lane getCurrentLane() {
		return currentLane;
	}

	public void setCurrentLane(Lane currentLane) {
		this.currentLane = currentLane;
	}

	public Lane getDestinationLane() {
		return destinationLane;
	}

	public void setDestinationLane(BackwardLane destinationLane) {
		this.destinationLane = destinationLane;
	}

	public Region getRegion() {
		return region;
	}

	public boolean isInCrossing() {
		return inCrossing;
	}

	public void setInCrossing(boolean inCrossing) {
		this.inCrossing = inCrossing;
	}

	
	public Crossing getCrossing() {
		return crossing;
	}

	
	public void setCrossing(Crossing crossing) {
		this.crossing = crossing;
	}
	
	
	public double getX() {
		return region.getX();
	}
	
	public double getY() {
		return region.getY();
	}
	
	public double getWidth() {
		return region.getWidth();
	}
	
	public double getHeight() {
		return region.getHeight();
	}
	
	
	public Direction getDirection() {
		return this.direction;
	}
	
	private void forwardLaneVehicleUpdation(Vehicle nextVehicle, boolean isLastVehicle) {
			TrafficLight trafficLight = this.currentRoad.getTrafficLight();
		switch(this.direction) {
		
		case TOP_TO_BOTTOM:
				if(isLastVehicle) {
					if(trafficLight.isRed()) {
						double gap = (trafficLight.getRegion().getY() - trafficLight.getRegion().getHeight()/2) - (this.getY() + this.getHeight()/2);
							if(gap > Vehicle.trafficLightPadding)
								this.setVelocityY(Math.min(Vehicle.maxSpeed, gap - trafficLightPadding));
							else this.setVelocityY(0);
					}
					else this.setVelocityY(Vehicle.maxSpeed);
				}
				else {
					double nextVehicleSpeed = nextVehicle.getVelocityY();
					double gap = (nextVehicle.getY() - nextVehicle.getHeight()/2) - (this.getY() + this.getHeight()/2);
					
					if(nextVehicleSpeed > 0) {	
						if(gap == Vehicle.vehiclePadding)
							this.setVelocityY(nextVehicleSpeed);
						
						else if(gap > Vehicle.vehiclePadding) 
							this.setVelocityY(Vehicle.maxSpeed);
						
						else this.setVelocityY(0);  // Not required as gap >= padding for cases but still used as emergency brake 
						
					}
					else if(nextVehicleSpeed == 0) {
						
							if(gap == Vehicle.vehiclePadding)
								this.setVelocityY(0);
							
							else if(gap > Vehicle.vehiclePadding)
									this.setVelocityY(Math.min(Vehicle.maxSpeed, gap - vehiclePadding));
						}
					}
		
			break;
		
		case BOTTOM_TO_TOP:
			if(isLastVehicle) {
				if(trafficLight.isRed()) {
					double gap =  (this.getY() - this.getHeight()/2)  - (trafficLight.getRegion().getY() + trafficLight.getRegion().getHeight()/2);
						if(gap > Vehicle.trafficLightPadding)
							this.setVelocityY(Math.min(Vehicle.maxSpeed, gap - trafficLightPadding));
						else this.setVelocityY(0);
				}
				else this.setVelocityY(-Vehicle.maxSpeed);
			}
			else {
				double nextVehicleSpeed = nextVehicle.getVelocityY();
				double gap = (this.getY() - this.getHeight()/2) - (nextVehicle.getY() + nextVehicle.getHeight()/2);
				
				if(nextVehicleSpeed > 0) {	
					if(gap == Vehicle.vehiclePadding)
						this.setVelocityY(nextVehicleSpeed);
					
					else if(gap > Vehicle.vehiclePadding) 
						this.setVelocityY(-Vehicle.maxSpeed);
					
					else this.setVelocityY(0);  // Not required as gap >= padding for cases but still used as emergency brake 
					
				}
				else if(nextVehicleSpeed == 0) {
					
						if(gap == Vehicle.vehiclePadding)
							this.setVelocityY(0);
						
						else if(gap > Vehicle.vehiclePadding)
								this.setVelocityY(-Math.min(Vehicle.maxSpeed, gap - vehiclePadding));
					}
				}
	
		break;
		
		
		case LEFT_TO_RIGHT:
			if(isLastVehicle) {
				if(trafficLight.isRed()) {
					double gap = (trafficLight.getRegion().getX() - trafficLight.getRegion().getHeight()/2) - (this.getX() + this.getHeight()/2);
						if(gap > Vehicle.trafficLightPadding)
							this.setVelocityX(Math.min(Vehicle.maxSpeed, gap - trafficLightPadding));
						else this.setVelocityX(0);
				}
				else this.setVelocityX(Vehicle.maxSpeed);
			}
			else {
				double nextVehicleSpeed = nextVehicle.getVelocityX();
				double gap = (nextVehicle.getX() - nextVehicle.getHeight()/2) - (this.getX() + this.getHeight()/2);
				
				if(nextVehicleSpeed > 0) {	
					if(gap == Vehicle.vehiclePadding)
						this.setVelocityX(nextVehicleSpeed);
					
					else if(gap > Vehicle.vehiclePadding) 
						this.setVelocityX(Vehicle.maxSpeed);
					
					else this.setVelocityX(0);  // Not required as gap >= padding for cases but still used as emergency brake 
					
				}
				else if(nextVehicleSpeed == 0) {
					
						if(gap == Vehicle.vehiclePadding)
							this.setVelocityX(0);
						
						else if(gap > Vehicle.vehiclePadding)
								this.setVelocityX(Math.min(Vehicle.maxSpeed, gap - vehiclePadding));
					}
				}
	
		break;
			

		case RIGHT_TO_LEFT:
			if(isLastVehicle) {
				if(trafficLight.isRed()) {
					double gap =  (this.getX() - this.getHeight()/2)  - (trafficLight.getRegion().getX() + trafficLight.getRegion().getHeight()/2);
						if(gap > Vehicle.trafficLightPadding)
							this.setVelocityX(Math.min(Vehicle.maxSpeed, gap - trafficLightPadding));
						else this.setVelocityX(0);
				}
				else this.setVelocityX(-Vehicle.maxSpeed);
			}
			else {
				double nextVehicleSpeed = nextVehicle.getVelocityX();
				double gap = (this.getX() - this.getHeight()/2) - (nextVehicle.getX() + nextVehicle.getHeight()/2);
				
				if(nextVehicleSpeed > 0) {	
					if(gap == Vehicle.vehiclePadding)
						this.setVelocityX(nextVehicleSpeed);
					
					else if(gap > Vehicle.vehiclePadding) 
						this.setVelocityX(-Vehicle.maxSpeed);
					
					else this.setVelocityX(0);  // Not required as gap >= padding for cases but still used as emergency brake 
					
				}
				else if(nextVehicleSpeed == 0) {
					
						if(gap == Vehicle.vehiclePadding)
							this.setVelocityX(0);
						
						else if(gap > Vehicle.vehiclePadding)
								this.setVelocityX(-Math.min(Vehicle.maxSpeed, gap - vehiclePadding));
					}
				}
	
		break;
	
		default : this.setVelocityX(0); this.setVelocityY(0);
					
					
				}
	}
	
	
	private boolean backwardLaneVehicleUpdation(Vehicle nextVehicle, boolean isLastVehicle) {
		
		switch(this.direction) {
		
		case TOP_TO_BOTTOM:
				if(isLastVehicle) {
					
					if(this.getY() - this.getHeight()/2 > this.currentLane.getRegion().getY() + this.currentLane.getRegion().getHeight()/2) {
						return true;
					}
						
					 this.setVelocityY(Vehicle.maxSpeed);
				}
				else {
					double nextVehicleSpeed = nextVehicle.getVelocityY();
					double gap = (nextVehicle.getY() - nextVehicle.getHeight()/2) - (this.getY() + this.getHeight()/2);
					
					if(nextVehicleSpeed > 0) {	
						if(gap == Vehicle.vehiclePadding)
							this.setVelocityY(nextVehicleSpeed);
						
						else if(gap > Vehicle.vehiclePadding) 
							this.setVelocityY(Vehicle.maxSpeed);
						
						else this.setVelocityY(0);  // Not required as gap >= padding for cases but still used as emergency brake 
						
					}
					else if(nextVehicleSpeed == 0) {
						
							if(gap == Vehicle.vehiclePadding)
								this.setVelocityY(0);
							
							else if(gap > Vehicle.vehiclePadding)
									this.setVelocityY(Math.min(Vehicle.maxSpeed, gap - vehiclePadding));
						}
					}
		
			break;
		
		case BOTTOM_TO_TOP:
			if(isLastVehicle) {
				
				if(this.getY() + this.getHeight()/2 > this.currentLane.getRegion().getY() - this.currentLane.getRegion().getHeight()/2) {
					return true;
				}
				 this.setVelocityY(-Vehicle.maxSpeed);
			}
			else {
				double nextVehicleSpeed = nextVehicle.getVelocityY();
				double gap = (this.getY() - this.getHeight()/2) - (nextVehicle.getY() + nextVehicle.getHeight()/2);
				
				if(nextVehicleSpeed > 0) {	
					if(gap == Vehicle.vehiclePadding)
						this.setVelocityY(nextVehicleSpeed);
					
					else if(gap > Vehicle.vehiclePadding) 
						this.setVelocityY(-Vehicle.maxSpeed);
					
					else this.setVelocityY(0);  // Not required as gap >= padding for cases but still used as emergency brake 
					
				}
				else if(nextVehicleSpeed == 0) {
					
						if(gap == Vehicle.vehiclePadding)
							this.setVelocityY(0);
						
						else if(gap > Vehicle.vehiclePadding)
								this.setVelocityY(-Math.min(Vehicle.maxSpeed, gap - vehiclePadding));
					}
				}
	
		break;
		
		
		case LEFT_TO_RIGHT:
			if(isLastVehicle) {
				if(this.getX() - this.getWidth()/2 > this.currentLane.getRegion().getX() + this.currentLane.getRegion().getWidth()/2) {
					return true;
				} 
				
				this.setVelocityX(Vehicle.maxSpeed);
			}
			else {
				double nextVehicleSpeed = nextVehicle.getVelocityX();
				double gap = (nextVehicle.getX() - nextVehicle.getHeight()/2) - (this.getX() + this.getHeight()/2);
				
				if(nextVehicleSpeed > 0) {	
					if(gap == Vehicle.vehiclePadding)
						this.setVelocityX(nextVehicleSpeed);
					
					else if(gap > Vehicle.vehiclePadding) 
						this.setVelocityX(Vehicle.maxSpeed);
					
					else this.setVelocityX(0);  // Not required as gap >= padding for cases but still used as emergency brake 
					
				}
				else if(nextVehicleSpeed == 0) {
					
						if(gap == Vehicle.vehiclePadding)
							this.setVelocityX(0);
						
						else if(gap > Vehicle.vehiclePadding)
								this.setVelocityX(Math.min(Vehicle.maxSpeed, gap - vehiclePadding));
					}
				}
	
		break;
			

		case RIGHT_TO_LEFT:
			if(isLastVehicle) {
					if(this.getX() + this.getWidth()/2 > this.currentLane.getRegion().getX() - this.currentLane.getRegion().getWidth()/2) {
						return true;
					} 
				
				
				this.setVelocityX(-Vehicle.maxSpeed);
			}
			else {
				double nextVehicleSpeed = nextVehicle.getVelocityX();
				double gap = (this.getX() - this.getHeight()/2) - (nextVehicle.getX() + nextVehicle.getHeight()/2);
				
				if(nextVehicleSpeed > 0) {	
					if(gap == Vehicle.vehiclePadding)
						this.setVelocityX(nextVehicleSpeed);
					
					else if(gap > Vehicle.vehiclePadding) 
						this.setVelocityX(-Vehicle.maxSpeed);
					
					else this.setVelocityX(0);  // Not required as gap >= padding for cases but still used as emergency brake 
					
				}
				else if(nextVehicleSpeed == 0) {
					
						if(gap == Vehicle.vehiclePadding)
							this.setVelocityX(0);
						
						else if(gap > Vehicle.vehiclePadding)
								this.setVelocityX(-Math.min(Vehicle.maxSpeed, gap - vehiclePadding));
					}
				}
	
		break;
	
		default : this.setVelocityX(0); this.setVelocityY(0);		
				}
		return false;
	}
	
	
	/**
	 * Updation 
	 * @return
	 */
	public boolean update(Vehicle nextVehicle, boolean isLastVehicle) {
	
		boolean beDestroyed = false;
		
	/** update velocity **/
		
		this.getRegion().setX(this.getX() + this.velocityX);
		this.getRegion().setY(this.getY() + this.velocityY);
		TrafficLight trafficLight = this.getCurrentRoad().getTrafficLight();
		
	/** update consequences **/
		
		
		
	/** updating velocity **/
		
		if(inCrossing) {}
		
		else if(this.currentLane.getClass() == ForwardLane.class) {
			forwardLaneVehicleUpdation(nextVehicle, isLastVehicle);
		}
		else if(this.currentLane.getClass() == BackwardLane.class) {
			
			
			beDestroyed =  backwardLaneVehicleUpdation(nextVehicle, isLastVehicle);
		
		}
		
		
	
		return beDestroyed;
	}
	

	
	
	public abstract void draw(GraphicsContext gc) ;
	
	
	
	
}
