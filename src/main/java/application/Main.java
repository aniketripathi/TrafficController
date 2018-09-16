package main.java.application;

import java.io.IOException;
import main.java.exceptions.InvalidConfigException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	public static final double MIN_WIDTH = 700;
	public static final double MIN_HEIGHT = 500;

	@Override
	public void start(Stage primaryStage) {
		try {

			primaryStage.setMinHeight(MIN_HEIGHT);
			primaryStage.setMinWidth(MIN_WIDTH);

			AnchorPane r = (AnchorPane) FXMLLoader.load(getClass().getResource("Fxml_gui.fxml"));

			BorderPane root = new BorderPane();
			Scene scene = new Scene(r, 1500, 700);
	
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, InvalidConfigException {

		launch(args);

	}
}
