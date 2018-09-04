package application;

import java.net.URL;
import java.util.ResourceBundle;
import map.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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

		map = new Map(canvas.getWidth(), canvas.getHeight(), 20, 6, 10);
		map.setGC(canvas.getGraphicsContext2D());
		map.draw(canvas.getGraphicsContext2D());

		canvas.widthProperty().addListener(obser -> {
			map.getWidthProperty().set(canvas.getWidth());
		});
		canvas.heightProperty().addListener(obser -> {
			map.getHeightProperty().set(canvas.getHeight());
		});

	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		canvas.setWidth(root.getWidth());
		canvas.setHeight(root.getHeight());
	}
}
