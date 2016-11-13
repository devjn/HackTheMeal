package application.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class DishController extends AnchorPane {
	
	public DishController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Dish.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	@FXML
	public Label dishName;
	@FXML
	public ImageView imageDish;
	@FXML
	public Label lbDiet;
	@FXML
	public Label lbEnergy;
	@FXML
	public Label lblOrigin;

}
