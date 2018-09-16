package main.java.application;

import java.net.URL;
import java.util.ResourceBundle;

import main.java.util.Scale;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.java.data.Config;
import main.java.data.Recorder;
import main.java.engine.Updator;
import main.java.engine.VehicleManager;
import main.java.entities.Car;
import main.java.map.Map;
import main.java.simulator.Simulator;

public class Controller implements Initializable, ChangeListener<Number> {

	@FXML
	private Canvas canvas;
	@FXML
	private AnchorPane root;
	@FXML
	private FlowPane flowPane;
	@FXML
	private VBox topControlsVBox;
	@FXML
	private MenuBar menuBar;
	@FXML
	private ToolBar toolbar;
	@FXML
	private HBox toolbarHBox;
	@FXML
	private ButtonBar simulatorControlButtonBar;
	@FXML
	private HBox timerHBox;
	@FXML
	private AnchorPane canvasAnchorContainer;
	@FXML
	private Pane canvasContainer;
	@FXML
	private ToolBar statusBar;
	@FXML
	private HBox statusDisplayHbox;
	@FXML
	private HBox generatedHbox;
	@FXML
	private HBox crossedHbox;

	private Map map;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		root.widthProperty().addListener(this);
		root.heightProperty().addListener(this);
		// canvas.setWidth(root.getWidth());
		// canvas.setHeight(root.getHeight());

		double laneWidth = Scale.LANE_WIDTH_METERS * Scale.pixelToMeterRatio;
		double dividerWidth = Scale.DIVIDER_WIDTH_METERS * Scale.pixelToMeterRatio;
		double crossingWidth = (laneWidth * 6 + dividerWidth) * 1.3;
		double trafficLightWidth = dividerWidth;
		map = new Map(canvas.getWidth(), canvas.getHeight(), laneWidth, dividerWidth,  trafficLightWidth, crossingWidth, crossingWidth);
		map.setGC(canvas.getGraphicsContext2D());
		
		VehicleManager vehicleManager = new VehicleManager();
		Config config = new Config(map.getNumberOfLanes());
		Recorder recorder = new Recorder(map.getNumberOfLanes());
		Updator updator = new Updator(map, canvas, vehicleManager, config, recorder);
		Simulator sim = new Simulator(updator);
		sim.play();
		
		
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		canvas.setWidth(root.getWidth());
		canvas.setHeight(root.getHeight());
	}
}
