package map;

import entities.BackwardLane;
import entities.Car;
import entities.ForwardLane;
import entities.HeavyVehicle;
import entities.Road;
import entities.TwoWheeler;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import util.LinearPath;;

public class Map implements ChangeListener<Number> {

	private DoubleProperty width;
	private DoubleProperty height;
	private DoubleProperty trafficLightWidth; // with respect to horizontal lane
	private DoubleProperty dividerWidth;
	private DoubleProperty laneWidth;
	private double laneSeparatorWidth; // with respect to horizontal lane
	private double laneSeparatorHeight; // with respect to horizontal lane

	public static final int DEFAULT_NUMBER_OF_LANES = 3;

	public static final double LANE_TO_LANE_SEPARATOR_WIDTH_RATIO = 0.05;
	public static final double LANE_WIDTH_TO_LANE_SEPARATOR_HEIGHT_RATIO = 1;

	private int numberOfLanes;
	private Road topRoad;
	private Road bottomRoad;
	private Road leftRoad;
	private Road rightRoad;
	private Crossing crossing;

	private GraphicsContext graphicsContext;

	private boolean areLanesEqual;

	private double laneSeparatorSpacing; // width respect to horizontal lane

	private boolean drawWhole;

	GraphicsContext gc;

	public void zoon(double factor) {
		Scale scale = new Scale(factor, factor);
		Point2D temp = scale.transform(width.get(), height.get());
		width.set(temp.getX());
		height.set(temp.getY());

	}

	public void setGC(GraphicsContext gc) {
		this.gc = gc;
	}

	public Map(double width, double height, double laneWidth, double dividerWidth, double trafficLightWidth) {

		numberOfLanes = DEFAULT_NUMBER_OF_LANES;

		topRoad = new Road(numberOfLanes);
		bottomRoad = new Road(numberOfLanes);
		leftRoad = new Road(numberOfLanes);
		rightRoad = new Road(numberOfLanes);
		crossing = new Crossing();

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

		drawWhole = true;

		computeRegions(width, height, laneWidth, dividerWidth, trafficLightWidth);

	}

	/**
	 * THIS METHOD IS CUSTOM FOR CREATING A MAP
	 * 
	 * 
	 * @param width
	 * @param height
	 * @param laneWidth
	 * @return
	 */

	public void computeRegions(double width, double height, double laneWidth, double dividerWidth, double trafficLightWidth) {

		/** Set Regions for all the lanes for all roads **/

		final double roadWidth = (2*numberOfLanes) * laneWidth + dividerWidth;
		final double verticalRoadHeight = (height - roadWidth) / 2;
		final double horizontalRoadHeight = (width - roadWidth) / 2;
		
		crossing.getRegion().setAll(width / 2, height / 2, roadWidth, roadWidth);

		/**
		 * Set regions for curves explicitly. Here width and height are considered with
		 * respect to canvas and not considering the orientation or direction of roads.
		 **/
		for (int i = 0; i < numberOfLanes; i++) {
			
			ForwardLane tf = topRoad.getForwardLane(i);
			ForwardLane bf = bottomRoad.getForwardLane(i);
			ForwardLane lf = leftRoad.getForwardLane(i);
			ForwardLane rf = rightRoad.getForwardLane(i);
			
			BackwardLane tb = topRoad.getBackwardLane(i);
			BackwardLane bb = bottomRoad.getBackwardLane(i);
			BackwardLane lb = rightRoad.getBackwardLane(i);
			BackwardLane rb = leftRoad.getBackwardLane(i);
			
			
			
			
			
		/** Set Regions **/	
			tf.getRegion().setAll((width + dividerWidth + laneWidth) / 2 + i * laneWidth,
					verticalRoadHeight / 2, laneWidth, verticalRoadHeight);
			tb.getRegion().setAll((width - dividerWidth - laneWidth) / 2 - i * laneWidth,
					verticalRoadHeight / 2, laneWidth, verticalRoadHeight);

			bf.getRegion().setAll((width - dividerWidth - laneWidth) / 2 - i * laneWidth,
					height - verticalRoadHeight / 2, laneWidth, verticalRoadHeight);
			bb.getRegion().setAll((width + dividerWidth + laneWidth) / 2 + i * laneWidth,
					height - verticalRoadHeight / 2, laneWidth, verticalRoadHeight);

			lf.getRegion().setAll(horizontalRoadHeight / 2,
					(height - dividerWidth - laneWidth) / 2 - i * laneWidth, horizontalRoadHeight, laneWidth);
			lb.getRegion().setAll(horizontalRoadHeight / 2,
					(height + dividerWidth + laneWidth) / 2 + i * laneWidth, horizontalRoadHeight, laneWidth);

			rf.getRegion().setAll(width - horizontalRoadHeight / 2,
					(height + dividerWidth + laneWidth) / 2 + i * laneWidth, horizontalRoadHeight, laneWidth);
			rb.getRegion().setAll(width - horizontalRoadHeight / 2,
					(height - dividerWidth - laneWidth) / 2 - i * laneWidth, horizontalRoadHeight, laneWidth);

			
			/**set Lane length **/
			tf.setLaneLength(tf.getRegion().getHeight());
			tb.setLaneLength(tb.getRegion().getHeight());
			bf.setLaneLength(bf.getRegion().getHeight());
			bb.setLaneLength(bb.getRegion().getHeight());
			lf.setLaneLength(lf.getRegion().getHeight());
			lb.setLaneLength(lb.getRegion().getHeight());
			rf.setLaneLength(rf.getRegion().getHeight());
			rb.setLaneLength(rb.getRegion().getHeight());
			
			
		/** Set Spawn Points **/
			tf.setCarSpawnPoint(new Point2D((width + dividerWidth + laneWidth) / 2 + i * laneWidth, - Car.getImageWidth()/2));
			tf.setTwoWheelerSpawnPoint(new Point2D((width + dividerWidth + laneWidth) / 2 + i * laneWidth, - TwoWheeler.getImageWidth()/2));
			tf.setHeavyVehicleSpawnPoint(new Point2D((width + dividerWidth + laneWidth) / 2 + i * laneWidth, - HeavyVehicle.getImageWidth()/2));
			
			bf.setCarSpawnPoint(new Point2D((width - dividerWidth - laneWidth) / 2 + i * laneWidth, height + Car.getImageWidth()/2));
			bf.setTwoWheelerSpawnPoint(new Point2D((width - dividerWidth - laneWidth) / 2 + i * laneWidth, height + TwoWheeler.getImageWidth()/2));
			bf.setHeavyVehicleSpawnPoint(new Point2D((width - dividerWidth - laneWidth) / 2 + i * laneWidth, height + HeavyVehicle.getImageWidth()/2));
			
			lf.setCarSpawnPoint(new Point2D(- Car.getImageWidth()/2, (height - dividerWidth - laneWidth) / 2 - i * laneWidth));
			lf.setTwoWheelerSpawnPoint(new Point2D(- TwoWheeler.getImageWidth()/2, (height - dividerWidth - laneWidth) / 2 - i * laneWidth));
			lf.setHeavyVehicleSpawnPoint(new Point2D(- HeavyVehicle.getImageWidth()/2, (height - dividerWidth - laneWidth) / 2 - i * laneWidth));
			
			rf.setCarSpawnPoint(new Point2D(width + Car.getImageWidth()/2, (height + dividerWidth + laneWidth) / 2 + i * laneWidth));
			rf.setTwoWheelerSpawnPoint(new Point2D(width + Car.getImageWidth()/2, (height + dividerWidth + laneWidth) / 2 + i * laneWidth));
			rf.setHeavyVehicleSpawnPoint(new Point2D(width + Car.getImageWidth()/2, (height + dividerWidth + laneWidth) / 2 + i * laneWidth));
			
			
			
			
			
			
			/** Set Path **/
			/*
			tf.setCarPath(new LinearPath(tf.getCarSpawnPoint().getX(), tf.getCarSpawnPoint().getY(), tf.getCarSpawnPoint().getX(), tf.getCarSpawnPoint().getY() + tf.getLaneLength()));
			bf.setCarPath(new LinearPath(bf.getCarSpawnPoint().getX(), bf.getCarSpawnPoint().getY(), bf.getCarSpawnPoint().getX(), bf.getCarSpawnPoint().getY() - bf.getLaneLength()));
			lf.setCarPath(new LinearPath(lf.getCarSpawnPoint().getX(), lf.getCarSpawnPoint().getY(), lf.getCarSpawnPoint().getX() + lf.getLaneLength(), lf.getCarSpawnPoint().getY()));
			rf.setCarPath(new LinearPath(rf.getCarSpawnPoint().getX(), rf.getCarSpawnPoint().getY(), rf.getCarSpawnPoint().getX() + rf.getLaneLength(), rf.getCarSpawnPoint().getY()));
			
			tb.setCarPath(new LinearPath(tb.getRegion().getX(), verticalRoadHeight - Car.getImageWidth()/2,tb.getRegion().getX(), - Car.getImageWidth()/2));
			bb.setCarPath(new LinearPath(bb.getRegion().getX(),  - Car.getImageWidth(),tb.getRegion().getX(), - Car.getImageWidth()));
			tb.setCarPath(new LinearPath(tb.getRegion().getX(), verticalRoadHeight - Car.getImageWidth(),tb.getRegion().getX(), - Car.getImageWidth()));
			tb.setCarPath(new LinearPath(tb.getRegion().getX(), verticalRoadHeight - Car.getImageWidth(),tb.getRegion().getX(), - Car.getImageWidth()));*/
		}

		/** Set dividers **/
		topRoad.getDivider().setAll(width / 2, verticalRoadHeight / 2, dividerWidth, verticalRoadHeight);
		bottomRoad.getDivider().setAll(width / 2, (height + roadWidth + verticalRoadHeight) / 2, dividerWidth,
				verticalRoadHeight);
		leftRoad.getDivider().setAll(horizontalRoadHeight / 2, height / 2, horizontalRoadHeight, dividerWidth);
		rightRoad.getDivider().setAll((width + roadWidth + horizontalRoadHeight) / 2, height / 2, horizontalRoadHeight,
				dividerWidth);

		/** Set traffic lights **/
		
		double trafficLightHeight = numberOfLanes*laneWidth;

		topRoad.getTrafficLight().getRegion().setAll((width + dividerWidth + roadWidth / 2) / 2,
				verticalRoadHeight - trafficLightWidth / 2, trafficLightHeight, trafficLightWidth);
		bottomRoad.getTrafficLight().getRegion().setAll((width - dividerWidth - roadWidth / 2) / 2,
				height - verticalRoadHeight + trafficLightWidth / 2, trafficLightHeight, trafficLightWidth);
		leftRoad.getTrafficLight().getRegion().setAll(horizontalRoadHeight - trafficLightWidth / 2,
				(height - dividerWidth - roadWidth / 2) / 2, trafficLightWidth, trafficLightHeight);
		rightRoad.getTrafficLight().getRegion().setAll(width - horizontalRoadHeight + trafficLightWidth / 2,
				(height + dividerWidth + roadWidth / 2) / 2, trafficLightWidth, trafficLightHeight);

		
		/** set paths for crossing TODO here **/

		laneSeparatorHeight = laneWidth * LANE_TO_LANE_SEPARATOR_WIDTH_RATIO;
		laneSeparatorWidth = laneWidth * LANE_WIDTH_TO_LANE_SEPARATOR_HEIGHT_RATIO;
		laneSeparatorSpacing = laneSeparatorWidth;

		/** regions of map is set .... draw whole **/
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

		// draw lanes

		for (int i = 0; i < numberOfLanes; i++) {

			topRoad.getForwardLane(i).getRegion().draw(gc, roadColor, laneSeparatorColor, laneSeparatorStrokeWidth,
					stroke);
			topRoad.getBackwardLane(i).getRegion().draw(gc, roadColor, laneSeparatorColor, laneSeparatorStrokeWidth,
					stroke);

			bottomRoad.getForwardLane(i).getRegion().draw(gc, roadColor, laneSeparatorColor, laneSeparatorStrokeWidth,
					stroke);
			bottomRoad.getBackwardLane(i).getRegion().draw(gc, roadColor, laneSeparatorColor, laneSeparatorStrokeWidth,
					stroke);

			leftRoad.getForwardLane(i).getRegion().draw(gc, roadColor, laneSeparatorColor, laneSeparatorStrokeWidth,
					stroke);
			leftRoad.getBackwardLane(i).getRegion().draw(gc, roadColor, laneSeparatorColor, laneSeparatorStrokeWidth,
					stroke);

			rightRoad.getForwardLane(i).getRegion().draw(gc, roadColor, laneSeparatorColor, laneSeparatorStrokeWidth,
					stroke);
			rightRoad.getBackwardLane(i).getRegion().draw(gc, roadColor, laneSeparatorColor, laneSeparatorStrokeWidth,
					stroke);
			// draw lane separator

		}

		// draw divide

		// draw traffic light
		topRoad.getTrafficLight().getRegion().draw(gc, topRoad.getTrafficLight().getColor().getColor(), null, 0, false);
		bottomRoad.getTrafficLight().getRegion().draw(gc, topRoad.getTrafficLight().getColor().getColor(), null, 0,
				false);
		leftRoad.getTrafficLight().getRegion().draw(gc, topRoad.getTrafficLight().getColor().getColor(), null, 0,
				false);
		rightRoad.getTrafficLight().getRegion().draw(gc, topRoad.getTrafficLight().getColor().getColor(), null, 0,
				false);

		// draw divider
		topRoad.getDivider().draw(gc, dividerColor, null, 0, false);
		bottomRoad.getDivider().draw(gc, dividerColor, null, 0, false);
		leftRoad.getDivider().draw(gc, dividerColor, null, 0, false);
		rightRoad.getDivider().draw(gc, dividerColor, null, 0, false);

		// draw crossing region
		crossing.getRegion().draw(gc, roadColor, null, 0, false);

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

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		computeRegions(width.get(), height.get(), laneWidth.get(), dividerWidth.get(), trafficLightWidth.get());
		gc.clearRect(0, 0, width.get(), height.get());

	}

}
