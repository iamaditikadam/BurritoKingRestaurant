package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.sql.SQLException;

import databasemanager.UserDAOImpl;

public class UpgradeToVipController {
	@FXML
	private CheckBox promotionsCheckBox;

	@FXML
	private TextField emailTextField;

	private Stage primaryStage;
	private User user;
	private UserDAOImpl userDAO;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setUser(User user) throws SQLException {
		this.user = user;
		this.userDAO = new UserDAOImpl();
	}

	@FXML
	private void handleUpgradeToVIP() {
		if (promotionsCheckBox.isSelected() && !emailTextField.getText().isEmpty()) {
			String email = emailTextField.getText();
			user.setVip(true);
			user.setEmail(email);
			userDAO.updateUser(user);

			showAlert(Alert.AlertType.INFORMATION, "VIP Upgrade",
					"You have been upgraded to VIP. Please log out and log in again to access VIP functionalities.");
			primaryStage.close();
		} else {
			showAlert(Alert.AlertType.ERROR, "Error",
					"You must agree to receive promotions and provide a valid email address.");
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
