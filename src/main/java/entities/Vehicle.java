package main.java.entities;

import java.util.ListIterator;

import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.java.map.Crossing;
import main.java.map.Map;
import main.java.util.MathEngine;
import main.java.util.Region;

public abstract class Vehicle {

	public enum Direction {
		TOP_TO_BOTTOM, BOTTOM_TO_TOP, LEFT_TO_RIGHT, RIGHT_TO_LEFT;
	}

	public enum Type {
		CAR, TWO_WHEELER, HEAVY_VEHICLE;
	}

	private double velocityX;
	private double velocityY;
	private double accelerationX;
	private double accelerationY;
	private Type type;
	// Anti clockwise angle with respect to x-axis
	private double angle; // Angle used for rendering the image, not accurate.

	private Road sourceRoad;
	private Road currentRoad;
	private Road destinationRoad;
	private Lane sourceLane;
	private Lane currentLane;
	private BackwardLane destinationLane;
	private final Region region;
	private boolean inCrossing;
	private Crossing crossing;
	private DoubleProperty mapWidth;
	private DoubleProperty mapHeight;
	private double crossingAngle; // crossingAngle for accurate collision detection
	/**
	 * angle mapping is the angle that the car with its current perspective must be
	 * rotated in order to enter the destination road with respect to positive x -
	 * axis. In other words, it is the angle of vectors formed by source road and
	 * destination road vectors (including direction)
	 **/
	private static double angleMapping[][];

	static {
		angleMapping = new double[Map.NUMBER_OF_ROADS][Map.NUMBER_OF_ROADS];

		int topIndex = Road.TYPE.TOP.getIndex();
		int bottomIndex = Road.TYPE.BOTTOM.getIndex();
		int leftIndex = Road.TYPE.LEFT.getIndex();
		int rightIndex = Road.TYPE.RIGHT.getIndex();

		angleMapping[topIndex][bottomIndex] = 0;
		angleMapping[topIndex][leftIndex] = MathEngine.NINETY_ANGLE_CLOCKWISE;
		angleMapping[topIndex][rightIndex] = MathEngine.NINETY_ANGLE_ANTICLOCKWISE;

		angleMapping[bottomIndex][topIndex] = 0;
		angleMapping[bottomIndex][leftIndex] = MathEngine.NINETY_ANGLE_ANTICLOCKWISE;
		angleMapping[bottomIndex][rightIndex] = MathEngine.NINETY_ANGLE_CLOCKWISE;

		angleMapping[leftIndex][topIndex] = MathEngine.NINETY_ANGLE_ANTICLOCKWISE;
		angleMapping[leftIndex][bottomIndex] = MathEngine.NINETY_ANGLE_CLOCKWISE;
		angleMapping[leftIndex][rightIndex] = 0;

		angleMapping[rightIndex][topIndex] = MathEngine.NINETY_ANGLE_CLOCKWISE;
		angleMapping[rightIndex][bottomIndex] = MathEngine.NINETY_ANGLE_ANTICLOCKWISE;
		angleMapping[rightIndex][leftIndex] = 0;

	}

	private Direction direction;

	private static double vehiclePadding = 4;
	private static double trafficLightPadding = 5;

	protected static double maxSpeed = 15;
	protected static double maxCrossingSpeed = 12;
	protected static double stepSpeed = (3);

	public Vehicle(Road sourceRoad, ForwardLane sourceLane, Road destinationRoad, BackwardLane destinationLane,
			Crossing crossing, DoubleProperty mapWidth, DoubleProperty mapHeight) {

		this.sourceRoad = sourceRoad;
		this.sourceLane = sourceLane;
		this.currentRoad = sourceRoad;
		this.currentLane = sourceLane;
		this.destinationRoad = destinationRoad;
		this.destinationLane = destinationLane;
		this.crossing = crossing;
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
		region = new Region();

		// this.mapWidth.addListener((obs,oldVal, newVal) -> {
		// this.region.setX(this.region.getX() - (oldVal.doubleValue() +
		// newVal.doubleValue())/2) ; });
		// this.mapHeight.addListener((obs,oldVal, newVal) -> {
		// this.region.setY(this.region.getY() - (oldVal.doubleValue() +
		// newVal.doubleValue())/2) ; });

		region.setX(sourceLane.getCarSpawnPoint().getX());
		region.setY(sourceLane.getCarSpawnPoint().getY());

		this.inCrossing = false;
		velocityX = 0;
		velocityY = 0;
		accelerationX = 0;
		accelerationY = 0;
		crossingAngle = 0;
		updateDirection();
	}

	public double getMaxSpeed() {
		return Math.min(Math.sqrt(this.getVelocityX() * this.getVelocityX() + this.getVelocityY() * this.getVelocityY())
				+ stepSpeed, maxSpeed);
	}

	public double getMaxCrossingSpeed() {
		return Math.min(Math.sqrt(this.getVelocityX() * this.getVelocityX() + this.getVelocityY() * this.getVelocityY())
				+ stepSpeed, maxCrossingSpeed);
	}

	public static void setMaxSpeed(double speed) {
		maxSpeed = speed;
	}

	public static void setMaxCrossingSpeed(double speed) {
		maxCrossingSpeed = speed;
	}

	public Type getType() {
		return this.type;
	}

	protected void setType(Type type) {
		this.type = type;
	}

	public abstract void computeRegions();

	public static double getVehiclePadding() {
		return Vehicle.vehiclePadding;
	}

	public void reset() {

		velocityX = 0;
		velocityY = 0;
		accelerationX = 0;
		accelerationY = 0;
		angle = 0;
		crossingAngle = 0;
		inCrossing = false;
		this.direction = null;
		sourceRoad = null;
		sourceLane = null;
		currentRoad = null;
		destinationRoad = null;
		currentLane = null;
		destinationLane = null;
		crossing = null; // there is no need but done to avoid error
		region.setX(0);
		region.setY(0);

	}

	public double getBoundingWidth() {
		return this.getHeight() / 2 * Math.sin(this.crossingAngle) + this.getWidth() / 2 * Math.cos(this.crossingAngle);
	}

	public double getBoundingHeight() {
		return this.getHeight() / 2 * Math.cos(this.crossingAngle) + this.getWidth() / 2 * Math.sin(this.crossingAngle);
	}

	public double getAngle() {
		return this.angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	/** Only valid if vehicle is moving on the lane **/
	public void updateDirection() {

		if ((this.currentRoad.getType() == Road.TYPE.TOP && this.currentLane.getClass() == ForwardLane.class)
				|| (this.currentRoad.getType() == Road.TYPE.BOTTOM
						&& this.currentLane.getClass() == BackwardLane.class))
			direction = Direction.TOP_TO_BOTTOM;

		else if ((this.currentRoad.getType() == Road.TYPE.TOP && this.currentLane.getClass() == BackwardLane.class)
				|| (this.currentRoad.getType() == Road.TYPE.BOTTOM && this.currentLane.getClass() == ForwardLane.class))
			direction = Direction.BOTTOM_TO_TOP;

		else if ((this.currentRoad.getType() == Road.TYPE.LEFT && this.currentLane.getClass() == ForwardLane.class)
				|| (this.currentRoad.getType() == Road.TYPE.RIGHT && this.currentLane.getClass() == BackwardLane.class))
			direction = Direction.LEFT_TO_RIGHT;

		else if ((this.currentRoad.getType() == Road.TYPE.LEFT && this.currentLane.getClass() == BackwardLane.class)
				|| (this.currentRoad.getType() == Road.TYPE.RIGHT && this.currentLane.getClass() == ForwardLane.class))
			direction = Direction.RIGHT_TO_LEFT;

		this.setAngle(0);
	}

	public DoubleProperty getMapWidth() {
		return mapWidth;
	}

	public void setMapWidth(DoubleProperty mapWidth) {
		this.mapWidth = mapWidth;
	}

	public DoubleProperty getMapHeight() {
		return mapHeight;
	}

	public void setMapHeight(DoubleProperty mapHeight) {
		this.mapHeight = mapHeight;
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

	public Road getSourceRoad() {
		return sourceRoad;
	}

	public void setSourceRoad(Road sourceRoad) {
		this.sourceRoad = sourceRoad;
	}

	public Lane getSourceLane() {
		return sourceLane;
	}

	public void setSourceLane(Lane sourceLane) {
		this.sourceLane = sourceLane;
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

	private void forwardLaneVelocityUpdate() {
		TrafficLight trafficLight = this.currentRoad.getTrafficLight();
		switch (this.direction) {

		case TOP_TO_BOTTOM:
			this.setVelocityX(0);
			if (trafficLight.isRed()) {
				double yGap = (trafficLight.getY() - trafficLight.getHeight() / 2)
						- (this.getY() + this.getHeight() / 2);
				if (MathEngine.isSmallerEquals(yGap, 0)) {
					this.setVelocityY(this.getMaxSpeed());
				}

				else if (MathEngine.isLarger(yGap, trafficLightPadding)) {
					this.setVelocityY(Math.min(yGap - trafficLightPadding, this.getMaxSpeed()));
				} else
					this.setVelocityY(0);
			} else {
				this.setVelocityY(this.getMaxSpeed());
			}

			break;

		case BOTTOM_TO_TOP:
			this.setVelocityX(0);
			if (trafficLight.isRed()) {
				double yGap = (this.getY() - this.getHeight() / 2)
						- (trafficLight.getY() + trafficLight.getHeight() / 2);
				if (MathEngine.isSmallerEquals(yGap, 0)) {
					this.setVelocityY(-this.getMaxSpeed());
				}

				else if (MathEngine.isLarger(yGap, trafficLightPadding)) {
					this.setVelocityY(-Math.min(yGap - trafficLightPadding, this.getMaxSpeed()));
				} else
					this.setVelocityY(0);
			} else {
				this.setVelocityY(-this.getMaxSpeed());
			}

			break;

		case LEFT_TO_RIGHT:
			this.setVelocityY(0);
			if (trafficLight.isRed()) {
				double xGap = (trafficLight.getX() - trafficLight.getWidth() / 2) - (this.getX() + this.getWidth() / 2);
				if (MathEngine.isSmallerEquals(xGap, 0)) {
					this.setVelocityX(this.getMaxSpeed());
				}

				else if (MathEngine.isLarger(xGap, trafficLightPadding)) {
					this.setVelocityX(Math.min(xGap - trafficLightPadding, this.getMaxSpeed()));
				} else
					this.setVelocityX(0);
			} else {
				this.setVelocityX(this.getMaxSpeed());
			}

			break;

		case RIGHT_TO_LEFT:
			this.setVelocityY(0);
			if (trafficLight.isRed()) {
				double xGap = (this.getX() - this.getWidth() / 2) - (trafficLight.getX() + trafficLight.getWidth() / 2);
				if (MathEngine.isSmallerEquals(xGap, 0)) {
					this.setVelocityX(-this.getMaxSpeed());
				}

				else if (MathEngine.isLarger(xGap, trafficLightPadding)) {
					this.setVelocityX(-Math.min(xGap - trafficLightPadding, this.getMaxSpeed()));
				} else
					this.setVelocityX(0);
			} else {
				this.setVelocityX(-this.getMaxSpeed());
			}

			break;

		default:
			this.setVelocityX(0);
			this.setVelocityY(0);

		}
	}

	private void backwardLaneVelocityUpdate() {

		switch (this.direction) {

		case TOP_TO_BOTTOM:
			this.setVelocityX(0);
			this.setVelocityY(this.getMaxSpeed());
			break;
		case BOTTOM_TO_TOP:
			this.setVelocityX(0);
			this.setVelocityY(-this.getMaxSpeed());
			break;

		case LEFT_TO_RIGHT:
			this.setVelocityY(0);
			this.setVelocityX(this.getMaxSpeed());
			break;
		case RIGHT_TO_LEFT:
			this.setVelocityY(0);
			this.setVelocityX(-this.getMaxSpeed());
			break;

		default:
			this.setVelocityX(0);
			this.setVelocityY(0);
		}
	}

	/**
	 * Update the vehicle position, velocity and other variables Returns whether the
	 * vehicle left the backwardLane, crossing or the forwardLane region depending
	 * upon what was its current region.
	 * 
	 * @returns true if the vehicle left the region of the map which contained it,
	 *          otherwise false
	 */
	public boolean update() {

		boolean leftRegion = false;
		/** updating velocity **/

		if (inCrossing) {

			crossingVelocityUpdate();
		}

		else if (this.currentLane.getClass() == ForwardLane.class) {
			forwardLaneVelocityUpdate();
		} else if (this.currentLane.getClass() == BackwardLane.class) {

			backwardLaneVelocityUpdate();
		}

		collisionUpdate();

		/** update position **/

		this.getRegion().setX(this.getX() + this.velocityX);
		this.getRegion().setY(this.getY() + this.velocityY);

		if (!this.inCrossing
				&& (this.getDirection() == Direction.TOP_TO_BOTTOM || this.getDirection() == Direction.BOTTOM_TO_TOP)) {
			this.getRegion().setX(this.currentLane.getX());
		}

		if (!this.inCrossing
				&& (this.getDirection() == Direction.LEFT_TO_RIGHT || this.getDirection() == Direction.RIGHT_TO_LEFT)) {
			this.getRegion().setY(this.currentLane.getY());
		}

		/** update consequences **/

		leftRegion = updateVariables();

		return leftRegion;
	}

	protected double getCurrentCrossingTurnAngle() {

		double angle = 0;
		switch (this.direction) {

		case LEFT_TO_RIGHT: {

			double yGap = this.getDestinationLane().getY() - this.getY();
			double xGap = (this.destinationLane.getX() - this.destinationLane.getWidth() / 2 + this.getWidth() / 2)
					- this.getX();
			final double maxYGap = this.getDestinationLane().getY() - this.sourceLane.getY();
			final double maxXGap = (this.destinationLane.getX() - this.destinationLane.getWidth() / 2
					+ this.getWidth() / 2)
					- (this.sourceLane.getX() + this.sourceLane.getWidth() / 2 - this.getWidth() / 2);
			if (MathEngine.isEqual(maxYGap, 0))
				angle = 0;

			else {
				angle = Math.atan(Math.abs(maxXGap / maxYGap));
				angle = angle * Math.abs((yGap / maxYGap) * ((maxXGap - xGap) / maxXGap));
				angle = (MathEngine.isLarger(yGap, 0)) ? angle : -angle;

			}
			break;
		}

		case RIGHT_TO_LEFT: {

			double yGap = this.getDestinationLane().getY() - this.getY();
			double xGap = this.getX()
					- (this.destinationLane.getX() + this.destinationLane.getWidth() / 2 - this.getWidth() / 2);
			final double maxYGap = this.getDestinationLane().getY() - this.sourceLane.getY();
			final double maxXGap = (this.sourceLane.getX() - this.sourceLane.getWidth() / 2 + this.getWidth() / 2)
					- (this.destinationLane.getX() + this.destinationLane.getWidth() / 2 - this.getWidth() / 2);
			if (MathEngine.isEqual(maxYGap, 0))
				angle = 0;

			else {
				angle = Math.atan(Math.abs(maxXGap / maxYGap));
				angle = angle * Math.abs((yGap / maxYGap) * ((maxXGap - xGap) / maxXGap));
				angle = (MathEngine.isLarger(yGap, 0)) ? angle : -angle;

			}
			break;
		}

		case TOP_TO_BOTTOM: {

			double xGap = this.getDestinationLane().getX() - this.getX();
			double yGap = (this.destinationLane.getY() - this.destinationLane.getHeight() / 2 + this.getHeight() / 2)
					- this.getY();
			final double maxXGap = this.getDestinationLane().getX() - this.sourceLane.getX();
			final double maxYGap = (this.destinationLane.getY() - this.destinationLane.getHeight() / 2
					+ this.getHeight() / 2)
					- (this.sourceLane.getY() + this.sourceLane.getHeight() / 2 - this.getHeight() / 2);
			if (MathEngine.isEqual(maxXGap, 0))
				angle = 0;

			else {
				angle = Math.atan(Math.abs(maxYGap / maxXGap));
				angle = angle * Math.abs((xGap / maxXGap) * ((maxYGap - yGap) / maxYGap));

				// here angle is respect to positive x axis
				angle = (MathEngine.isLarger(xGap, 0)) ? angle : -angle;

			}
			break;
		}

		case BOTTOM_TO_TOP: {

			double xGap = this.getDestinationLane().getX() - this.getX();
			double yGap = this.getY()
					- (this.destinationLane.getY() + this.destinationLane.getHeight() / 2 - this.getHeight() / 2);
			final double maxXGap = this.getDestinationLane().getX() - this.sourceLane.getX();
			final double maxYGap = (this.sourceLane.getY() - this.sourceLane.getHeight() / 2 + this.getHeight() / 2)
					- (this.destinationLane.getY() + this.destinationLane.getHeight() / 2 - this.getHeight() / 2);
			if (MathEngine.isEqual(maxXGap, 0))
				angle = 0;

			else {
				angle = Math.atan(Math.abs(maxYGap / maxXGap));
				angle = angle * Math.abs((xGap / maxXGap) * ((maxYGap - yGap) / maxYGap));
				angle = (MathEngine.isLarger(xGap, 0)) ? angle : -angle;

			}
			break;
		}

		}
		return angle;

	}

	private boolean updateVariables() {
		boolean leftRegion = false;
		if (inCrossing) {

			double angle = Vehicle.getAngle(this.sourceRoad.getType().getIndex(),
					this.destinationRoad.getType().getIndex());
			boolean isDirectionChange = !MathEngine.isEqual(angle, 0);

			switch (this.direction) {

			case TOP_TO_BOTTOM: {

				boolean turnLeft = (MathEngine.isLarger(angle, 0)) ? true : false; // for X axis velocity direction;
				int directionSign = (turnLeft) ? -1 : 1; // here left is with respect to standard coordinates and not
															// vehicles perspective

				if (isDirectionChange) {
					double xGap = directionSign
							* ((this.destinationLane.getX() - directionSign * this.destinationLane.getWidth() / 2
									+ directionSign * this.getHeight() / 2) - this.getX());

					if (MathEngine.isSmallerEquals(xGap, 0)) {
						this.setToDestination();
						leftRegion = true;
						double tempWidth = this.getWidth();
						this.getRegion().setWidth(this.getHeight());
						this.getRegion().setHeight(tempWidth);
						updateDirection();
					}
				}

				else {
					double yGap = (this.getDestinationLane().getY() - this.getDestinationLane().getHeight() / 2
							+ this.getHeight() / 2) - this.getY();
					if (MathEngine.isSmallerEquals(yGap, 0)) {
						leftRegion = true;
						this.setToDestination();
					}
				}

				break;
			}

			case BOTTOM_TO_TOP: {

				boolean turnLeft = (MathEngine.isLarger(angle, 0)) ? false : true; // for X axis velocity direction;
				int directionSign = (turnLeft) ? -1 : 1; // here left is with respect to standard coordinates and not
															// vehicles perspective

				if (isDirectionChange) {
					double xGap = directionSign
							* ((this.destinationLane.getX() - directionSign * this.destinationLane.getWidth() / 2
									+ directionSign * this.getHeight() / 2) - this.getX());

					if (MathEngine.isSmallerEquals(xGap, 0)) {
						this.setToDestination();
						leftRegion = true;
						double tempWidth = this.getWidth();
						this.getRegion().setWidth(this.getHeight());
						this.getRegion().setHeight(tempWidth);
						updateDirection();
					}
				}

				else {
					double yGap = this.getY() - (this.getDestinationLane().getY()
							+ this.getDestinationLane().getHeight() / 2 - this.getHeight() / 2);
					if (MathEngine.isSmallerEquals(yGap, 0)) {
						this.setToDestination();
						leftRegion = true;
					}
				}

				break;
			}

			case LEFT_TO_RIGHT: {

				boolean turnUp = (MathEngine.isLarger(angle, 0)) ? false : true; // for X axis velocity direction;
				int directionSign = (turnUp) ? -1 : 1; // here left is with respect to standard coordinates and not
														// vehicles perspective

				if (isDirectionChange) {
					double yGap = directionSign
							* ((this.destinationLane.getY() - directionSign * this.destinationLane.getHeight() / 2
									+ directionSign * this.getWidth() / 2) - this.getY());

					if (MathEngine.isSmallerEquals(yGap, 0)) {
						this.setToDestination();
						leftRegion = true;
						double tempWidth = this.getWidth();
						this.getRegion().setWidth(this.getHeight());
						this.getRegion().setHeight(tempWidth);
						updateDirection();
					}
				}

				else {
					double xGap = (this.getDestinationLane().getX() - this.getDestinationLane().getWidth() / 2
							+ this.getWidth() / 2) - this.getX();
					if (MathEngine.isSmallerEquals(xGap, 0)) {
						this.setToDestination();
						leftRegion = true;
					}
				}

				break;
			}

			case RIGHT_TO_LEFT: {

				boolean turnUp = (MathEngine.isLarger(angle, 0)) ? true : false; // for X axis velocity direction;
				int directionSign = (turnUp) ? -1 : 1; // here left is with respect to standard coordinates and not
														// vehicles perspective

				if (isDirectionChange) {
					double yGap = directionSign
							* ((this.destinationLane.getY() - directionSign * this.destinationLane.getHeight() / 2
									+ directionSign * this.getWidth() / 2) - this.getY());

					if (MathEngine.isSmallerEquals(yGap, 0)) {
						this.setToDestination();
						leftRegion = true;
						double tempWidth = this.getWidth();
						this.getRegion().setWidth(this.getHeight());
						this.getRegion().setHeight(tempWidth);
						updateDirection();
					}
				}

				else {
					double xGap = this.getX() - (this.getDestinationLane().getX()
							+ this.getDestinationLane().getWidth() / 2 - this.getWidth() / 2);
					if (MathEngine.isSmallerEquals(xGap, 0)) {
						this.setToDestination();
						leftRegion = true;
					}
				}

				break;
			}
			}
		}

		else if (this.currentRoad.equals(this.sourceRoad)) {

			switch (this.direction) {

			case TOP_TO_BOTTOM: {
				if (MathEngine.isLargerEquals(this.getY(),
						this.sourceLane.getY() + this.sourceLane.getHeight() / 2 - this.getHeight() / 2)) {
					this.intoCrossingVariablesUpdate();
					leftRegion = true;
				}
				break;
			}

			case BOTTOM_TO_TOP: {
				if (MathEngine.isSmallerEquals(this.getY(),
						this.sourceLane.getY() - this.sourceLane.getHeight() / 2 + this.getHeight() / 2)) {
					this.intoCrossingVariablesUpdate();
					leftRegion = true;
				}
				break;
			}

			case LEFT_TO_RIGHT: {
				if (MathEngine.isLargerEquals(this.getX(),
						this.sourceLane.getX() + this.sourceLane.getWidth() / 2 - this.getWidth() / 2)) {
					this.intoCrossingVariablesUpdate();
					leftRegion = true;
				}
				break;
			}

			case RIGHT_TO_LEFT: {
				if (MathEngine.isSmallerEquals(this.getX(),
						this.sourceLane.getX() - this.sourceLane.getWidth() / 2 + this.getWidth() / 2)) {
					this.intoCrossingVariablesUpdate();
					leftRegion = true;
				}
				break;
			}

			}

		}

		else if (this.currentRoad.equals(this.destinationRoad)) {
			switch (this.direction) {

			case TOP_TO_BOTTOM: {
				if (MathEngine.isLargerEquals(this.getY() - this.getHeight() / 2,
						this.currentLane.getY() + this.currentLane.getHeight() / 2)) {
					leftRegion = true;
				}
				break;
			}

			case BOTTOM_TO_TOP: {
				if (MathEngine.isSmallerEquals(this.getY() + this.getHeight() / 2,
						this.currentLane.getY() - this.currentLane.getHeight() / 2)) {
					leftRegion = true;
				}
				break;
			}

			case LEFT_TO_RIGHT: {
				if (MathEngine.isLargerEquals(this.getX() - this.getWidth() / 2,
						this.currentLane.getX() + this.currentLane.getWidth() / 2)) {
					leftRegion = true;
				}
				break;
			}

			case RIGHT_TO_LEFT: {
				if (MathEngine.isSmallerEquals(this.getX() + this.getWidth() / 2,
						this.currentLane.getX() - this.currentLane.getWidth() / 2)) {
					leftRegion = true;
				}
			}

			}
		}

		return leftRegion;
	}

	private void intoCrossingVariablesUpdate() {
		this.inCrossing = true;
		this.currentLane = null;
		this.currentRoad = null;
	}

	private void crossingVelocityUpdate() {

		double angle = Vehicle.getAngle(this.sourceRoad.getType().getIndex(), // get angle to be rotated to reach its
																				// destination
				this.destinationRoad.getType().getIndex());
		boolean isDirectionChange = !MathEngine.isEqual(angle, 0); // vehicle will turn or go straight

		switch (this.direction) {

		case TOP_TO_BOTTOM: {
			boolean turnLeft = (MathEngine.isLarger(angle, 0)) ? true : false; // for X axis velocity direction;
			int directionSign = (turnLeft) ? -1 : 1; // here left is with respect to standard coordinates and not
														// vehicles perspective

			if (isDirectionChange) {

				double yGap = this.getDestinationLane().getY() - this.getY();
				double xGap = directionSign
						* ((this.destinationLane.getX() - directionSign * this.destinationLane.getWidth() / 2
								+ directionSign * this.getHeight() / 2) - this.getX());
				final double maxYGap = this.getDestinationLane().getY()
						- (this.getSourceLane().getY() + this.getSourceLane().getHeight() / 2 - this.getHeight() / 2);
				final double maxXGap = directionSign
						* ((this.destinationLane.getX() - directionSign * this.destinationLane.getWidth() / 2
								+ directionSign * this.getHeight() / 2) - (this.sourceLane.getX()));

				double tempAngle = Math.atan((maxYGap - yGap) * maxXGap / (xGap) / maxYGap);
				this.crossingAngle = tempAngle;
				this.setAngle(-directionSign * Math.toRadians(Math.abs(angle)) // angle to be rotated for rendering
						* MathEngine.getDistance(maxXGap - xGap, maxYGap - yGap)
						/ MathEngine.getDistance(maxXGap, maxYGap));

				double xSpeed = this.getMaxCrossingSpeed() * maxXGap / (MathEngine.getDistance(maxXGap, maxYGap))
						* Math.sin(tempAngle);
				double ySpeed = this.getMaxCrossingSpeed() * maxYGap / (MathEngine.getDistance(maxXGap, maxYGap))
						* Math.cos(tempAngle);

				if (MathEngine.isLarger(yGap, ySpeed))
					this.setVelocityY(ySpeed);

				else
					this.setVelocityY(yGap);

				if (MathEngine.isLarger(xGap, xSpeed))
					this.setVelocityX(directionSign * xSpeed);
				else
					this.setVelocityX(directionSign * xSpeed);

			}

			else {
				double straightAngle = this.getCurrentCrossingTurnAngle();
				this.setAngle(-straightAngle);
				this.setVelocityX(this.getMaxCrossingSpeed() * Math.sin(straightAngle));
				this.setVelocityY(this.getMaxCrossingSpeed() * Math.cos(straightAngle));

			}
			break;
		}

		case BOTTOM_TO_TOP: {
			boolean turnLeft = (MathEngine.isLarger(angle, 0)) ? false : true; // for X axis velocity direction;
			int directionSign = (turnLeft) ? -1 : 1; // here left is with respect to standard coordinates and not
														// vehicles perspective

			if (isDirectionChange) {

				double yGap = this.getY() - this.getDestinationLane().getY();
				double xGap = directionSign
						* ((this.destinationLane.getX() - directionSign * this.destinationLane.getWidth() / 2
								+ directionSign * this.getHeight() / 2) - this.getX());
				final double maxYGap = (this.getSourceLane().getY() - this.getSourceLane().getHeight() / 2
						+ this.getHeight() / 2) - (this.getDestinationLane().getY());
				final double maxXGap = directionSign
						* ((this.destinationLane.getX() - directionSign * this.destinationLane.getWidth() / 2
								+ directionSign * this.getHeight() / 2) - (this.sourceLane.getX()));

				double tempAngle = Math.atan((maxYGap - yGap) * maxXGap / (xGap) / maxYGap);
				this.crossingAngle = tempAngle;
				this.setAngle(directionSign * Math.toRadians(Math.abs(angle))
						* MathEngine.getDistance(maxXGap - xGap, maxYGap - yGap)
						/ MathEngine.getDistance(maxXGap, maxYGap));

				double xSpeed = this.getMaxCrossingSpeed() * maxXGap / (MathEngine.getDistance(maxXGap, maxYGap))
						* Math.sin(tempAngle);
				double ySpeed = this.getMaxCrossingSpeed() * maxYGap / (MathEngine.getDistance(maxXGap, maxYGap))
						* Math.cos(tempAngle);

				if (MathEngine.isLarger(yGap, ySpeed))
					this.setVelocityY(-ySpeed);

				else
					this.setVelocityY(-yGap);

				this.setVelocityX(directionSign * xSpeed);
			}

			else {
				double straightAngle = this.getCurrentCrossingTurnAngle();
				this.setAngle(straightAngle);
				this.setVelocityX(this.getMaxCrossingSpeed() * Math.sin(straightAngle));
				this.setVelocityY(-this.getMaxCrossingSpeed() * Math.cos(straightAngle));
			}
			break;
		}

		case LEFT_TO_RIGHT: {
			boolean turnUp = (MathEngine.isLarger(angle, 0)) ? false : true; // for Y axis velocity direction;
			int directionSign = (turnUp) ? -1 : 1; // here up is with respect to standard coordinates and not vehicles
													// perspective

			if (isDirectionChange) {
				double xGap = this.getDestinationLane().getX() - this.getX();
				double yGap = directionSign
						* ((this.destinationLane.getY() - directionSign * this.destinationLane.getHeight() / 2
								+ directionSign * this.getWidth() / 2) - this.getY());
				final double maxXGap = this.getDestinationLane().getX()
						- (this.getSourceLane().getX() + this.getSourceLane().getWidth() / 2 - this.getWidth() / 2);
				final double maxYGap = directionSign
						* ((this.destinationLane.getY() - directionSign * this.destinationLane.getHeight() / 2
								+ directionSign * this.getWidth() / 2) - (this.sourceLane.getY()));

				double tempAngle = Math.atan((maxXGap - xGap) * maxYGap / yGap / maxXGap);
				this.crossingAngle = tempAngle;
				this.setAngle(directionSign * Math.toRadians(Math.abs(angle))
						* MathEngine.getDistance(maxXGap - xGap, maxYGap - yGap)
						/ MathEngine.getDistance(maxXGap, maxYGap));

				double xSpeed = this.getMaxCrossingSpeed() * maxXGap / (MathEngine.getDistance(maxYGap, maxXGap))
						* Math.cos(tempAngle);
				double ySpeed = this.getMaxCrossingSpeed() * maxYGap / (MathEngine.getDistance(maxYGap, maxXGap))
						* Math.sin(tempAngle);

				if (MathEngine.isLarger(xGap, xSpeed))
					this.setVelocityX(xSpeed);

				else
					this.setVelocityX(xGap);

				if (MathEngine.isLarger(yGap, ySpeed))
					this.setVelocityY(directionSign * ySpeed);
				else
					this.setVelocityY(directionSign * yGap);
			}

			else {
				double straightAngle = this.getCurrentCrossingTurnAngle();
				this.setAngle(straightAngle);
				this.setVelocityX(this.getMaxCrossingSpeed() * Math.cos(straightAngle));
				this.setVelocityY(this.getMaxCrossingSpeed() * Math.sin(straightAngle));
			}
			break;
		}

		case RIGHT_TO_LEFT: {
			boolean turnUp = (MathEngine.isLarger(angle, 0)) ? true : false; // for X axis velocity direction;
			int directionSign = (turnUp) ? -1 : 1; // here left is with respect to standard coordinates and not vehicles
													// perspective

			if (isDirectionChange) {

				double xGap = this.getX() - this.getDestinationLane().getX();
				double yGap = directionSign
						* ((this.destinationLane.getY() - directionSign * this.destinationLane.getHeight() / 2
								+ directionSign * this.getWidth() / 2) - this.getY());
				final double maxXGap = (this.getSourceLane().getX() - this.getSourceLane().getWidth() / 2
						+ this.getWidth() / 2) - (this.getDestinationLane().getX());
				final double maxYGap = directionSign
						* ((this.destinationLane.getY() - directionSign * this.destinationLane.getHeight() / 2
								+ directionSign * this.getWidth() / 2) - (this.sourceLane.getY()));

				double tempAngle = Math.atan((maxXGap - xGap) * maxYGap / yGap / maxXGap);
				this.crossingAngle = tempAngle;
				this.setAngle(-directionSign * Math.toRadians(Math.abs(angle))
						* MathEngine.getDistance(maxXGap - xGap, maxYGap - yGap)
						/ MathEngine.getDistance(maxXGap, maxYGap));

				double xSpeed = this.getMaxCrossingSpeed() * maxXGap / (MathEngine.getDistance(maxYGap, maxXGap))
						* Math.cos(tempAngle);
				double ySpeed = this.getMaxCrossingSpeed() * maxYGap / (MathEngine.getDistance(maxYGap, maxXGap))
						* Math.sin(tempAngle);

				if (MathEngine.isLarger(xGap, xSpeed))
					this.setVelocityX(-xSpeed);

				else
					this.setVelocityX(-xGap);

				if (MathEngine.isLarger(yGap, ySpeed))
					this.setVelocityY(directionSign * ySpeed);
				else
					this.setVelocityY(directionSign * yGap);
			}

			else {
				double straightAngle = this.getCurrentCrossingTurnAngle();
				this.setAngle(-straightAngle);
				this.setVelocityX(-this.getMaxCrossingSpeed() * Math.cos(straightAngle));
				this.setVelocityY(this.getMaxCrossingSpeed() * Math.sin(straightAngle));
			}
			break;
		}

		}
	}

	private void setToDestination() {
		this.inCrossing = false;
		this.currentLane = this.destinationLane;
		this.currentRoad = this.destinationRoad;
	}

	/** Returns the angle between source road vector and destination road vector **/
	private static double getAngle(int sourceRoad, int destinationRoad) {
		return angleMapping[sourceRoad][destinationRoad];

	}

	public abstract void draw(GraphicsContext gc);

	protected static void drawRotated(Image image, GraphicsContext gc, double clockWiseAngle, double x, double y,
			double width, double height) {
		gc.save();
		gc.translate(x, y);
		gc.rotate(clockWiseAngle);
		gc.drawImage(image, -width / 2, -height / 2, width, height);
		gc.restore();
	}

	/**
	 * Detects if there is a chance of collision between the vehicles
	 * 
	 * @param vehicle
	 * @return
	 */
	protected boolean collisionDetect(Vehicle vehicle) {
		boolean xCollision = false, yCollision = false;

		double xGap = Double.MAX_VALUE, yGap = Double.MAX_VALUE;
		if (MathEngine.isLarger(this.getX(), vehicle.getX())) {
			xGap = (this.getX() - this.getBoundingWidth() + this.getVelocityX())
					- (vehicle.getX() + vehicle.getBoundingWidth());
		} else if (MathEngine.isSmaller(this.getX(), vehicle.getX())) {
			xGap = (vehicle.getX() - vehicle.getBoundingWidth())
					- (this.getX() + this.getBoundingWidth() + this.getVelocityX());
		} else {
			xCollision = true;
		}

		if (MathEngine.isLarger(this.getY(), vehicle.getY())) {
			yGap = (this.getY() - this.getBoundingHeight() + this.getVelocityY())
					- (vehicle.getY() + vehicle.getBoundingHeight());
		} else if (MathEngine.isSmaller(this.getY(), vehicle.getY())) {
			yGap = (vehicle.getY() - vehicle.getBoundingHeight())
					- (this.getY() + this.getBoundingHeight() + this.getVelocityY());
		} else {
			yCollision = true;
		}

		if (MathEngine.isSmaller(xGap, vehiclePadding)) {

			xCollision = true;
		}
		if (MathEngine.isSmaller(yGap, vehiclePadding)) {
			yCollision = true;
		}

		return (xCollision && yCollision);
	}

	private void collisionCheckPrevious(ListIterator<Vehicle> iterator) {
		while (iterator.hasPrevious()) {
			Vehicle vehicle = iterator.previous();
			if (this.collisionDetect(vehicle)) {
				this.setVelocityX(0);
				this.setVelocityY(0);
			}
		}
	}

	private void collisionCheckNext(ListIterator<Vehicle> iterator) {
		while (iterator.hasNext()) {
			Vehicle vehicle = iterator.next();
			if (this.collisionDetect(vehicle)) {
				this.setVelocityX(0);
				this.setVelocityY(0);
			}
		}
	}

	private void collisionUpdate() {

		if (!this.isInCrossing() && this.getCurrentLane().equals(this.getDestinationLane())) {
			int index = this.getDestinationLane().getVehicleIndex(this);
			ListIterator<Vehicle> iterator = this.getDestinationLane().listIterator(index);
			collisionCheckPrevious(iterator);
		} else if (this.isInCrossing()) {
			int index = this.getCrossing().getVehicleIndex(this);
			ListIterator<Vehicle> iterator = this.getCrossing().listIterator(index);
			collisionCheckPrevious(iterator); // check with crossing queue
			iterator = this.getDestinationLane().listIterator();
			collisionCheckNext(iterator); // check with backward lane queue

		} else if (this.getSourceLane().equals(this.getCurrentLane())) {
			int index = this.getSourceLane().getVehicleIndex(this);
			ListIterator<Vehicle> iterator = this.getCurrentLane().listIterator(index);
			collisionCheckPrevious(iterator); // check with this lane queue
			iterator = this.getCrossing().listIterator();
			collisionCheckNext(iterator); // check with crossing queue

		}
	}
}
