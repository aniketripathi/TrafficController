package main.java.application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.data.config.Config;
import main.java.data.config.ConfigReader;
import main.java.data.config.ConfigWriter;
import main.java.data.recorder.Recorder;
import main.java.engine.Updater;
import main.java.engine.VehicleManager;
import main.java.entities.TrafficLight.LightColor;
import main.java.exceptions.InvalidConfigException;
import main.java.exceptions.ProbabilityException;
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
	@FXML
	private Label generatedValueLabel;
	@FXML
	private Label crossedValueLabel;
	@FXML
	private MenuItem setConfigMenuItem;

	private Stage parentStage;
	private Map map;
	private VehicleManager vehicleManager;
	private Config config;
	private Updater updater;
	private Simulator sim;
	private Recorder recorder;
	private File lastDirectory;
	private FileChooser fileChooser;

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
		vehicleManager = new VehicleManager();
		config = new Config(map.getNumberOfLanes());
		recorder = new Recorder(map.getNumberOfLanes());
		updater = new Updater(map, canvas, vehicleManager, config, recorder, timerValueLabel, generatedValueLabel,
				crossedValueLabel);
		sim = new Simulator(updater);
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));

		playButton.setOnAction(evt -> sim.play());
		pauseButton.setOnAction(evt -> sim.pause());
		stopButton.setOnAction(evt -> sim.stop());
		halfSpeedButton.setOnAction(evt -> sim.halfSpeed());
		doubleSpeedButton.setOnAction(evt -> sim.doubleSpeed());
		map.getRoad(2).getTrafficLight().setColor(LightColor.GREEN);
		// map.getRoad(1).getTrafficLight().setColor(LightColor.GREEN);

	}

	public void openConfigDialog() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		AnchorPane anchorPane = (AnchorPane) fxmlLoader.load(getClass().getResource("Input_config.fxml").openStream());
		ConfigDialog configController = (ConfigDialog) fxmlLoader.getController();
		configController.setOldConfig(config);
		configController.setNumberOfLanes(map.getNumberOfLanes());

		Stage dialogStage = new Stage();
		Scene dialogScene = new Scene(anchorPane, anchorPane.getPrefWidth(), anchorPane.getPrefHeight());
		dialogStage.setTitle("Set Config");
		dialogStage.setScene(dialogScene);
		dialogStage.setResizable(false);
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		configController.getCloseButton().setOnAction((evt) -> {
			dialogStage.close();
		});
		configController.getOKButton().setOnAction((evt) -> {
			try {
				configController.getConfig().validate();
				config = configController.getConfig();
				dialogStage.close();
				updater.setConfig(config);
			} catch (ProbabilityException e) {
				Alert alert = new Alert(AlertType.ERROR, e.getMessage());
				alert.setHeaderText(null);
				alert.setTitle("Probability Error!!");
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.show();
			}
		});

		dialogStage.showAndWait();

	}

	public void saveConfig() {
		fileChooser.setInitialDirectory(lastDirectory);
		File file = fileChooser.showSaveDialog(parentStage);
		if (file != null) {
			try {
				ConfigWriter.writeToPath(file.toPath(), config);
				lastDirectory = file.getParentFile();
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR, e.getMessage());
				alert.setHeaderText(null);
				alert.setTitle(e.getClass().getName());
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.show();
			}
		}
	}

	public void loadConfig() {

		fileChooser.setInitialDirectory(lastDirectory);
		File file = fileChooser.showOpenDialog(parentStage);
		if (file != null) {
			try {
				ConfigReader.readFromFile(file.toPath(), config);
				lastDirectory = file.getParentFile();
				updater.setConfig(config);
			} catch (IOException | InvalidConfigException e) {
				Alert alert = new Alert(AlertType.ERROR, e.getMessage());
				alert.setHeaderText(null);
				e.getClass().getName();
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.show();
			}
		}
	}

	public void setStage(Stage stage) {
		this.parentStage = stage;
	}

	public void resetConfig() {
		config.reset();
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		canvas.setWidth(root.getWidth());
		canvas.setHeight(root.getHeight());
	}
}
