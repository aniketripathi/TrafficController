package main.java.application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.java.data.config.Config;
import main.java.entities.Road.TYPE;

public class ConfigDialog implements Initializable {

	@FXML
	private AnchorPane anchorPane;
	@FXML
	private ComboBox<TYPE> roadSelectComboBox;
	@FXML
	private ComboBox<Integer> laneSelectComboBox;
	@FXML
	private TextField twoWheelerProbability;
	@FXML
	private TextField carProbability;
	@FXML
	private TextField heavyVehicleProbability;
	@FXML
	private TextField rate;
	@FXML
	private Button resetToDefaultButton;
	@FXML
	private Button resetToOldButton;
	@FXML
	private Button oKButton;
	@FXML
	private Button closeButton;

	private Config oldConfig;
	private Config config;
	private int numberOfLanes;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		roadSelectComboBox.getItems().setAll(TYPE.values());

		roadSelectComboBox.setOnAction((evt) -> loadValues());
		laneSelectComboBox.setOnAction((evt) -> loadValues());

		// set limit to textField
		twoWheelerProbability.textProperty().addListener((obs, oldval, newval) -> {
			if (!newval.matches("\\d*([\\.]\\d*)?"))
				twoWheelerProbability.setText(oldval);
			else if (newval.matches("\\d*(\\.\\d+)?")) {
				int road = roadSelectComboBox.getValue().getIndex();
				int lane = laneSelectComboBox.getValue();
				config.getRoadProperty(road).getLaneProperty(lane).setTwoWheelerProbability(Double.parseDouble(newval));
			}

		});
		carProbability.textProperty().addListener((obs, oldval, newval) -> {
			if (!newval.matches("\\d*([\\.]\\d*)?"))
				carProbability.setText(oldval);
			else if (newval.matches("\\d*(\\.\\d+)?")) {
				int road = roadSelectComboBox.getValue().getIndex();
				int lane = laneSelectComboBox.getValue();
				config.getRoadProperty(road).getLaneProperty(lane).setCarProbability(Double.parseDouble(newval));
			}
		});
		heavyVehicleProbability.textProperty().addListener((obs, oldval, newval) -> {
			if (!newval.matches("\\d*([\\.]\\d*)?"))
				heavyVehicleProbability.setText(oldval);
			else if (newval.matches("\\d*(\\.\\d+)?")) {
				int road = roadSelectComboBox.getValue().getIndex();
				int lane = laneSelectComboBox.getValue();
				config.getRoadProperty(road).getLaneProperty(lane)
						.setHeavyVehicleProbability(Double.parseDouble(newval));
			}
		});
		rate.textProperty().addListener((obs, oldval, newval) -> {
			if (!newval.matches("\\d*([\\.]\\d*)?"))
				rate.setText(oldval);
			else if (newval.matches("\\d*(\\.\\d+)?")) {
				int road = roadSelectComboBox.getValue().getIndex();
				int lane = laneSelectComboBox.getValue();
				config.getRoadProperty(road).getLaneProperty(lane).setRate(Double.parseDouble(newval));
			}
		});

		resetToDefaultButton.setOnAction((evt) -> {
			config.reset();
			loadValues();
		});
		resetToOldButton.setOnAction((evt) -> {
			config = new Config(oldConfig);
			loadValues();
		});

	}

	public void setOldConfig(Config config) {
		this.oldConfig = config;
		this.config = new Config(oldConfig);
	}

	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
		Integer lanes[] = new Integer[numberOfLanes];
		for (int i = 0; i < numberOfLanes; i++)
			lanes[i] = Integer.valueOf(i);

		laneSelectComboBox.getItems().setAll(lanes);
	}

	private void loadValues() {
		if (roadSelectComboBox.getValue() != null && laneSelectComboBox.getValue() != null) {
			int road = roadSelectComboBox.getValue().getIndex();
			int lane = laneSelectComboBox.getValue();
			twoWheelerProbability.setText(
					String.valueOf(config.getRoadProperty(road).getLaneProperty(lane).getTwoWheelerProbability()));
			carProbability
					.setText(String.valueOf(config.getRoadProperty(road).getLaneProperty(lane).getCarProbability()));
			heavyVehicleProbability.setText(
					String.valueOf(config.getRoadProperty(road).getLaneProperty(lane).getHeavyVehicleProbability()));
			rate.setText(String.valueOf(config.getRoadProperty(road).getLaneProperty(lane).getRate()));
		}
	}

	public Button getCloseButton() {
		return closeButton;
	}

	public Button getOKButton() {
		return oKButton;
	}

	public Config getConfig() {
		return config;
	}

}
