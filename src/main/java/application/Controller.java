package main.java.application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import main.java.data.Config;
import main.java.data.Recorder;
import main.java.engine.Updater;
import main.java.engine.VehicleManager;
import main.java.entities.TrafficLight.LightColor;
import main.java.map.Map;
import main.java.simulator.Simulator;
import main.java.util.Scale;

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
	@FXML
	private Button halfSpeedButton;
	@FXML
	private Button doubleSpeedButton;
	@FXML
	private Button playButton;
	@FXML
	private Button pauseButton;
	@FXML
	private Button stopButton;
	@FXML
	private Label timerValueLabel;

	private Map map;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		root.widthProperty().addListener(this);
		root.heightProperty().addListener(this);
		// canvas.setWidth(root.getWidth());
		// canvas.setHeight(root.getHeight());

		double laneWidth = Scale.LANE_WIDTH_METERS * Scale.pixelToMeterRatio;
		double dividerWidth = Scale.DIVIDER_WIDTH_METERS * Scale.pixelToMeterRatio;
		double crossingWidth = (laneWidth * 6 + dividerWidth) * 1.7;
		double trafficLightWidth = dividerWidth;
		map = new Map(canvas.getWidth(), canvas.getHeight(), laneWidth, dividerWidth, trafficLightWidth, crossingWidth,
				crossingWidth);
		map.setGC(canvas.getGraphicsContext2D());

		// canvas.getGraphicsContext2D().rotate(10);
		VehicleManager vehicleManager = new VehicleManager();
		Config config = new Config(map.getNumberOfLanes());
		Recorder recorder = new Recorder(map.getNumberOfLanes());
		Updater updater = new Updater(map, canvas, vehicleManager, config, recorder, timerValueLabel);
		Simulator sim = new Simulator(updater);

		playButton.setOnAction(evt -> sim.play());
		pauseButton.setOnAction(evt -> sim.pause());
		stopButton.setOnAction(evt -> sim.stop());
		halfSpeedButton.setOnAction(evt -> sim.halfSpeed());
		doubleSpeedButton.setOnAction(evt -> sim.doubleSpeed());
		map.getRoad(2).getTrafficLight().setColor(LightColor.GREEN);
		// map.getRoad(1).getTrafficLight().setColor(LightColor.GREEN);

	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		canvas.setWidth(root.getWidth());
		canvas.setHeight(root.getHeight());
	}
}
