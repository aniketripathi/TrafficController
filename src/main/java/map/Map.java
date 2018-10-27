package main.java.map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.java.entities.BackwardLane;
import main.java.entities.Car;
import main.java.entities.ForwardLane;
import main.java.entities.HeavyVehicle;
import main.java.entities.Road;
import main.java.entities.TwoWheeler;
import main.java.util.LinearPath;
import main.java.util.Scale;;

public class Map implements ChangeListener<Number> {

	private DoubleProperty width;
	private DoubleProperty height;
	private DoubleProperty trafficLightWidth; // with respect to horizontal lane
	private DoubleProperty dividerWidth;
	private DoubleProperty laneWidth;
	private double laneSeparatorWidth; // with respect to horizontal lane
	private double laneSeparatorHeight; // with respect to horizontal lane

	public static final int DEFAULT_NUMBER_OF_LANES = 3;
	public static final int NUMBER_OF_ROADS = 4;
	public static final double LANE_TO_LANE_SEPARATOR_WIDTH_RATIO = 0.05;
	public static final double LANE_WIDTH_TO_LANE_SEPARATOR_HEIGHT_RATIO = 1;

	private int numberOfLanes;
	private Road[] roads;
	private Crossing crossing;

	private boolean areLanesEqual;

	private double laneSeparatorSpacing; // width respect to horizontal lane

	private boolean drawWhole;

	GraphicsContext gc;

	public Road getRoad(int index) {
		return roads[index];
	}

	public void setGC(GraphicsContext gc) {
		this.gc = gc;
	}

	public Map(double width, double height, double laneWidth, double dividerWidth, double trafficLightWidth,
			double crossingWidth, double crossingHeight) {

		numberOfLanes = DEFAULT_NUMBER_OF_LANES;
		roads = new Road[NUMBER_OF_ROADS];

		for (int i = 0; i < NUMBER_OF_ROADS; i++) {
			roads[i] = new Road(numberOfLanes, Road.TYPE.getType(i));
		}

		crossing = new Crossing(crossingWidth, crossingHeight);

		this.width = new SimpleDoubleProperty(width);
		this.height = new SimpleDoubleProperty(height);
		this.laneWidth = new SimpleDoubleProperty(laneWidth);
		this.dividerWidth = new SimpleDoubleProperty(dividerWidth);
		this.trafficLightWidth = new SimpleDoubleProperty(trafficLightWidth);

		this.width.addListener(this);
		this.height.addListener(this);
		this.laneWidth.addListener(this);
		this.dividerWidth.addListener(this);
		this.trafficLightWidth.addListener(this);
		this.crossing.getRegion().getWidthProperty().addListener(this);
		this.crossing.getRegion().getHeightProperty().addListener(this);

		drawWhole = true;

		computeRegions();

	}

	public void computeRegions() {

		/** Set Regions for all the lanes for all roads **/

		crossing.getRegion().setX(width.get() / 2);
		crossing.getRegion().setY(height.get() / 2);

		final double verticalRoadLength = (height.get() - crossing.getRegion().getHeight()) / 2;
		final double horizontalRoadLength = (width.get() - crossing.getRegion().getWidth()) / 2;

		/**
		 * Set regions for curves explicitly. Here width and height are considered with
		 * respect to canvas and not considering the orientation or direction of roads.
		 **/
		for (int i = 0; i < numberOfLanes; i++) {

			ForwardLane tf = roads[0].getForwardLane(i);
			ForwardLane bf = roads[1].getForwardLane(i);
			ForwardLane lf = roads[2].getForwardLane(i);
			ForwardLane rf = roads[3].getForwardLane(i);

			BackwardLane tb = roads[0].getBackwardLane(i);
			BackwardLane bb = roads[1].getBackwardLane(i);
			BackwardLane lb = roads[2].getBackwardLane(i);
			BackwardLane rb = roads[3].getBackwardLane(i);

			/** Set Regions **/
			tf.getRegion().setAll((width.get() + dividerWidth.get() + laneWidth.get()) / 2 + i * laneWidth.get(),
					verticalRoadLength / 2, laneWidth.get(), verticalRoadLength);
			tb.getRegion().setAll((width.get() - dividerWidth.get() - laneWidth.get()) / 2 - i * laneWidth.get(),
					verticalRoadLength / 2, laneWidth.get(), verticalRoadLength);

			bf.getRegion().setAll((width.get() - dividerWidth.get() - laneWidth.get()) / 2 - i * laneWidth.get(),
					height.get() - verticalRoadLength / 2, laneWidth.get(), verticalRoadLength);
			bb.getRegion().setAll((width.get() + dividerWidth.get() + laneWidth.get()) / 2 + i * laneWidth.get(),
					height.get() - verticalRoadLength / 2, laneWidth.get(), verticalRoadLength);

			lf.getRegion().setAll(horizontalRoadLength / 2,
					(height.get() - dividerWidth.get() - laneWidth.get()) / 2 - i * laneWidth.get(),
					horizontalRoadLength, laneWidth.get());
			lb.getRegion().setAll(horizontalRoadLength / 2,
					(height.get() + dividerWidth.get() + laneWidth.get()) / 2 + i * laneWidth.get(),
					horizontalRoadLength, laneWidth.get());

			rf.getRegion().setAll(width.get() - horizontalRoadLength / 2,
					(height.get() + dividerWidth.get() + laneWidth.get()) / 2 + i * laneWidth.get(),
					horizontalRoadLength, laneWidth.get());
			rb.getRegion().setAll(width.get() - horizontalRoadLength / 2,
					(height.get() - dividerWidth.get() - laneWidth.get()) / 2 - i * laneWidth.get(),
					horizontalRoadLength, laneWidth.get());

			/** set Lane length **/
			tf.setLaneLength(tf.getRegion().getHeight());
			tb.setLaneLength(tb.getRegion().getHeight());
			bf.setLaneLength(bf.getRegion().getHeight());
			bb.setLaneLength(bb.getRegion().getHeight());
			lf.setLaneLength(lf.getRegion().getWidth());
			lb.setLaneLength(lb.getRegion().getWidth());
			rf.setLaneLength(rf.getRegion().getWidth());
			rb.setLaneLength(rb.getRegion().getWidth());

			double carImageWidth = Scale.CAR_LENGTH_METERS * Scale.pixelToMeterRatio;

			/** Set Spawn Points **/
			tf.setCarSpawnPoint(
					new Point2D((width.get() + dividerWidth.get() + laneWidth.get()) / 2 + i * laneWidth.get(),
							-carImageWidth / 2));
			tf.setTwoWheelerSpawnPoint(
					new Point2D((width.get() + dividerWidth.get() + laneWidth.get()) / 2 + i * laneWidth.get(),
							-TwoWheeler.getImageWidth() / 2));
			tf.setHeavyVehicleSpawnPoint(
					new Point2D((width.get() + dividerWidth.get() + laneWidth.get()) / 2 + i * laneWidth.get(),
							-HeavyVehicle.getImageWidth() / 2));

			bf.setCarSpawnPoint(
					new Point2D((width.get() - dividerWidth.get() - laneWidth.get()) / 2 - i * laneWidth.get(),
							height.get() + carImageWidth / 2));
			bf.setTwoWheelerSpawnPoint(
					new Point2D((width.get() - dividerWidth.get() - laneWidth.get()) / 2 - i * laneWidth.get(),
							height.get() + TwoWheeler.getImageWidth() / 2));
			bf.setHeavyVehicleSpawnPoint(
					new Point2D((width.get() - dividerWidth.get() - laneWidth.get()) / 2 - i * laneWidth.get(),
							height.get() + HeavyVehicle.getImageWidth() / 2));

			lf.setCarSpawnPoint(new Point2D(-carImageWidth / 2,
					(height.get() - dividerWidth.get() - laneWidth.get()) / 2 - i * laneWidth.get()));
			lf.setTwoWheelerSpawnPoint(new Point2D(-TwoWheeler.getImageWidth() / 2,
					(height.get() - dividerWidth.get() - laneWidth.get()) / 2 - i * laneWidth.get()));
			lf.setHeavyVehicleSpawnPoint(new Point2D(-HeavyVehicle.getImageWidth() / 2,
					(height.get() - dividerWidth.get() - laneWidth.get()) / 2 - i * laneWidth.get()));

			rf.setCarSpawnPoint(new Point2D(width.get() + carImageWidth / 2,
					(height.get() + dividerWidth.get() + laneWidth.get()) / 2 + i * laneWidth.get()));
			rf.setTwoWheelerSpawnPoint(new Point2D(width.get() + carImageWidth / 2,
					(height.get() + dividerWidth.get() + laneWidth.get()) / 2 + i * laneWidth.get()));
			rf.setHeavyVehicleSpawnPoint(new Point2D(width.get() + carImageWidth / 2,
					(height.get() + dividerWidth.get() + laneWidth.get()) / 2 + i * laneWidth.get()));

		}

		/** Set dividers **/
		roads[0].getDivider().setAll(width.get() / 2, verticalRoadLength / 2, dividerWidth.get(), verticalRoadLength);
		roads[1].getDivider().setAll(width.get() / 2,
				(height.get() + crossing.getRegion().getHeight() + verticalRoadLength) / 2, dividerWidth.get(),
				verticalRoadLength);
		roads[2].getDivider().setAll(horizontalRoadLength / 2, height.get() / 2, horizontalRoadLength,
				dividerWidth.get());
		roads[3].getDivider().setAll((width.get() + crossing.getRegion().getWidth() + horizontalRoadLength) / 2,
				height.get() / 2, horizontalRoadLength, dividerWidth.get());

		/** Set traffic lights **/

		double trafficLightHeight = numberOfLanes * laneWidth.get();

		roads[0].getTrafficLight().getRegion().setAll((width.get() + dividerWidth.get() + 6 * laneWidth.get() / 2) / 2,
				verticalRoadLength, trafficLightHeight, trafficLightWidth.get());
		roads[1].getTrafficLight().getRegion().setAll((width.get() - dividerWidth.get() - 6 * laneWidth.get() / 2) / 2,
				height.get() - verticalRoadLength, trafficLightHeight, trafficLightWidth.get());
		roads[2].getTrafficLight().getRegion().setAll(horizontalRoadLength,
				(height.get() - dividerWidth.get() - 6 * laneWidth.get() / 2) / 2, trafficLightWidth.get(),
				trafficLightHeight);
		roads[3].getTrafficLight().getRegion().setAll(width.get() - horizontalRoadLength,
				(height.get() + dividerWidth.get() + 6 * laneWidth.get() / 2) / 2, trafficLightWidth.get(),
				trafficLightHeight);

		/** set paths for crossing TODO here **/

		laneSeparatorHeight = laneWidth.get() * LANE_TO_LANE_SEPARATOR_WIDTH_RATIO;
		laneSeparatorWidth = laneWidth.get() * LANE_WIDTH_TO_LANE_SEPARATOR_HEIGHT_RATIO;
		laneSeparatorSpacing = laneSeparatorWidth;

		/** regions of main.java.map is set .... draw whole **/
		drawWhole = true;

	}

	public void draw(GraphicsContext gc) {

		gc.clearRect(0, 0, width.get(), height.get());

		Color roadColor = Color.BLACK;
		Color laneSeparatorColor = Color.WHITE;
		Color dividerColor = Color.BROWN;
		double laneSeparatorStrokeWidth = laneSeparatorHeight;
		boolean stroke = true;

		// clear original attributes
		gc.setLineWidth(0);

		// set background
		gc.setFill(Color.GREEN);
		gc.fillRect(0, 0, width.get(), height.get());

		// draw crossing region
		crossing.getRegion().draw(gc, roadColor, null, 0, false);

		// draw lanes

		for (int j = 0; j < Map.NUMBER_OF_ROADS; j++) {
			for (int i = 0; i < numberOfLanes; i++) {

				roads[j].getForwardLane(i).getRegion().draw(gc, roadColor, laneSeparatorColor, laneSeparatorStrokeWidth,
						stroke);
				roads[j].getBackwardLane(i).getRegion().draw(gc, roadColor, laneSeparatorColor,
						laneSeparatorStrokeWidth, stroke); // draw lane separator

			}

			// draw divide

			// draw traffic light
			roads[j].getTrafficLight().getRegion().draw(gc, roads[j].getTrafficLight().getColor().getColor(), null, 0,
					false);

			// draw divider
			roads[j].getDivider().draw(gc, dividerColor, null, 0, false);
		}

		drawWhole = false;

	}

	public DoubleProperty getWidthProperty() {
		return width;
	}

	public DoubleProperty getHeightProperty() {
		return height;
	}

	public DoubleProperty getLaneWidthProperty() {
		return laneWidth;
	}

	public DoubleProperty getDividerWidthProperty() {
		return dividerWidth;
	}

	public DoubleProperty getTrafficLightWidthProperty() {
		return trafficLightWidth;
	}

	public boolean isAreLanesEqual() {
		return areLanesEqual;
	}

	public void setAreLanesEqual(boolean areLanesEqual) {
		this.areLanesEqual = areLanesEqual;
	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
	}

	public double getWidth() {
		return width.get();
	}

	public void setWidth(double width) {
		this.width.set(width);
	}

	public double getHeight() {
		return height.get();
	}

	public void setHeight(double height) {
		this.height.set(height);
	}

	public Road[] getRoads() {
		return roads;
	}

	public void setRoads(Road[] roads) {
		this.roads = roads;
	}

	public Crossing getCrossing() {
		return crossing;
	}

	public void setCrossing(Crossing crossing) {
		this.crossing = crossing;
	}

	public boolean isDrawWhole() {
		return drawWhole;
	}

	public void setDrawWhole(boolean drawWhole) {
		this.drawWhole = drawWhole;
	}

	public GraphicsContext getGc() {
		return gc;
	}

	public void setGc(GraphicsContext gc) {
		this.gc = gc;
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		computeRegions();

	}

}
