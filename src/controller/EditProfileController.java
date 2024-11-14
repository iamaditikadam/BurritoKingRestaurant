package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

import databasemanager.UserDAOImpl; // Ensure this import is correct based on your project structure

public class EditProfileController {
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField passwordField;

	private Stage primaryStage;
	private User user;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setUser(User user) {
		this.user = user;
		loadUserData();
	}

	private void loadUserData() {
		if (user != null) {
			firstNameField.setText(user.getFirstName());
			lastNameField.setText(user.getLastName());
			passwordField.setText(user.getPassword());
		}
	}

	@FXML
	private void handleSave() throws SQLException {
		// Update user object with new data
		user.setFirstName(firstNameField.getText());
		user.setLastName(lastNameField.getText());
		user.setPassword(passwordField.getText());

		System.out.println("Updating User ID: " + user.getUserID());
		System.out.println("First Name: " + user.getFirstName());
		System.out.println("Last Name: " + user.getLastName());
		System.out.println("Password: " + user.getPassword());

		UserDAOImpl userDao = UserDAOImpl.getInstance();
		boolean success = userDao.updateUserInfo(user);

		// Show alert based on success or failure
		if (success) {
			showAlert(Alert.AlertType.INFORMATION, "Update Successful", "User details have been updated successfully.");
		} else {
			showAlert(Alert.AlertType.ERROR, "Update Failed",
					"There was an error updating the user details. Please try again.");
		}

	}

	@FXML
	private void handleCancel() throws SQLException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboadView.fxml"));
			Parent dashboardRoot = loader.load();
			DashboardController dashboardController = loader.getController();
			dashboardController.setPrimaryStage(primaryStage);
			dashboardController.setUser(user); // Ensure the user is passed back

			primaryStage.setScene(new Scene(dashboardRoot));
		} catch (IOException e) {
			e.printStackTrace();
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
