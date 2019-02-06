package main.java.application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class OptionsDialog implements Initializable {

	@FXML
	private AnchorPane anchorPane;
	@FXML
	private RadioButton staticRadioButton;
	@FXML
	private RadioButton dynamicRadioButton;
	@FXML
	private TextField defaultTimeTextField;
	@FXML
	private TextField maxGreenTimeTextField;
	@FXML
	private TextField minGreenTimeTextField;
	@FXML
	private TextField pauseTimeTextField;
	@FXML
	private Button OKButton;
	@FXML
	private Button CancelButton;

	private float defaultTime, maxGreenTime, minGreenTime, pauseTime;

	private boolean dynamic = false;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Add listener to text fields to restrict only numeric data

		defaultTimeTextField.textProperty().addListener((obs, oldval, newval) -> {
			if (!newval.matches("\\d*([\\.]\\d*)?"))
				defaultTimeTextField.setText(oldval);
		});

		maxGreenTimeTextField.textProperty().addListener((obs, oldval, newval) -> {
			if (!newval.matches("\\d*([\\.]\\d*)?"))
				maxGreenTimeTextField.setText(oldval);
		});

		minGreenTimeTextField.textProperty().addListener((obs, oldval, newval) -> {
			if (!newval.matches("\\d*([\\.]\\d*)?"))
				minGreenTimeTextField.setText(oldval);
		});

		pauseTimeTextField.textProperty().addListener((obs, oldval, newval) -> {
			if (!newval.matches("\\d*([\\.]\\d*)?"))
				pauseTimeTextField.setText(oldval);
		});

		// If radio buttons not selected then disable corresponding text fields
		staticRadioButton.selectedProperty().addListener((obs) -> {
			if (staticRadioButton.isSelected()) {
				defaultTimeTextField.setDisable(false);
				dynamicRadioButton.setSelected(false);
				dynamic = false;
			} else
				defaultTimeTextField.setDisable(true);
		});

		dynamicRadioButton.selectedProperty().addListener((obs) -> {
			if (dynamicRadioButton.isSelected()) {
				maxGreenTimeTextField.setDisable(false);
				minGreenTimeTextField.setDisable(false);
				staticRadioButton.setSelected(false);
				dynamic = true;
			} else {
				maxGreenTimeTextField.setDisable(true);
				minGreenTimeTextField.setDisable(true);

			}
		});

	}

	public void updateFields() {
		// Initialization
		defaultTimeTextField.setText(String.valueOf(defaultTime));
		maxGreenTimeTextField.setText(String.valueOf(maxGreenTime));
		minGreenTimeTextField.setText(String.valueOf(minGreenTime));
		pauseTimeTextField.setText(String.valueOf(pauseTime));
		if (dynamic) {
			dynamicRadioButton.setSelected(true);
			staticRadioButton.setSelected(false);
			defaultTimeTextField.setDisable(true);
		} else {
			dynamicRadioButton.setSelected(false);
			staticRadioButton.setSelected(true);
			minGreenTimeTextField.setDisable(true);
			maxGreenTimeTextField.setDisable(true);
		}
	}

	public float getDefaultTime() {
		defaultTime = Float.parseFloat(defaultTimeTextField.getText());
		return defaultTime;
	}

	public float getMaxGreenTime() {
		maxGreenTime = Float.parseFloat(maxGreenTimeTextField.getText());
		return maxGreenTime;
	}

	public void setMaxGreenTime(float maxGreenTime) {
		this.maxGreenTime = maxGreenTime;
	}

	public float getMinGreenTime() {
		minGreenTime = Float.parseFloat(minGreenTimeTextField.getText());
		return minGreenTime;
	}

	public void setMinGreenTime(float minGreenTime) {
		this.minGreenTime = minGreenTime;
	}

	public float getPauseTime() {
		pauseTime = Float.parseFloat(pauseTimeTextField.getText());
		return pauseTime;
	}

	public void setDynamicParameters(boolean val, float minGreenTime, float maxGreenTime) {
		dynamic = val;
		this.maxGreenTime = maxGreenTime;
		this.minGreenTime = minGreenTime;
	}

	public void setDefaultTime(float defaultTime) {
		this.defaultTime = defaultTime;
	}

	public void setPauseTime(float pauseTime) {
		this.pauseTime = pauseTime;
	}

	public Button getOKButton() {
		return this.OKButton;
	}

	public Button getCancelButton() {
		return this.CancelButton;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public boolean isDynamic() {
		return this.dynamic;
	}
}
