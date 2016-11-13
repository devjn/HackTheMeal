package application.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class StartController extends AnchorPane {
	
	public StartController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Start.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	@FXML
	public Label labelBottom;
	@FXML
	public Label lblWelcome;
	@FXML
	public Label lblDiet;
	@FXML
	public Label lblEnergy;

}
