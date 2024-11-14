package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.MenuController;
import java.io.IOException;

public class MenuScene {

	private Stage primaryStage;
	private Scene scene;
	private String title;

	public MenuScene(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.title = "Menu";
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuView.fxml"));
			Parent root = loader.load();

			MenuController controller = loader.getController();
			controller.setPrimaryStage(primaryStage);

			this.scene = new Scene(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Scene getScene() {
		return scene;
	}

	public String getTitle() {
		return title;
	}
}
