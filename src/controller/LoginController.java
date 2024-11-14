package controller;

import databasemanager.UserDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.io.IOException;
import java.util.HashMap;

public class LoginController {

	@FXML
	private TextField userName;

	@FXML
	private TextField password;

	private Stage primaryStage;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@FXML
	private void handleLogin() throws SQLException, IOException {
		String usernameText = userName.getText();
		String passwordText = password.getText();

		try {
			UserDAOImpl userDao = UserDAOImpl.getInstance();
			HashMap<String, Object> result = userDao.loginUser(usernameText, passwordText);

			if ((boolean) result.get("loginSuccess")) {
				User user = (User) result.get("user");
				System.out.println("Logged in User ID: " + user.getUserID()); // Debugging line to check userID

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboadView.fxml"));
				Parent root = loader.load();

				DashboardController dashboardController = loader.getController();
				dashboardController.setPrimaryStage(primaryStage);
				dashboardController.setUser(user);

				primaryStage.setScene(new Scene(root));
			} else {
				showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Database Error",
					"An error occurred while trying to log in. Please try again.");
		}
	}

	/*
	 * if (usernameText.isEmpty() || passwordText.isEmpty()) {
	 * showAlert(Alert.AlertType.ERROR, "Login Error!",
	 * "Please enter both username and password"); } else { UserDAOImpl userDAO =
	 * UserDAOImpl.getInstance(); HashMap<String, Object> result =
	 * userDAO.loginUser(usernameText, passwordText); boolean loginSuccess =
	 * (boolean) result.get("loginSuccess");
	 * 
	 * if (loginSuccess) { showAlert(Alert.AlertType.INFORMATION,
	 * "Login Successful!", "Welcome " + usernameText);
	 * 
	 * 
	 * FXMLLoader loader = new
	 * FXMLLoader(getClass().getResource("/view/DashboadView.fxml")); // Ensure path
	 * is correct Parent root = loader.load();
	 * 
	 * DashboardController dashboardController = loader.getController();
	 * dashboardController.setPrimaryStage(primaryStage); // Pass primaryStage here
	 * dashboardController.setUser((User) result.get("user"));
	 * 
	 * primaryStage.setScene(new Scene(root)); } else {
	 * showAlert(Alert.AlertType.ERROR, "Login Error!",
	 * "Invalid username or password"); } } }
	 */

	@FXML
	private void goToRegister() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registerView.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);

			RegisterController registerController = loader.getController();
			registerController.setPrimaryStage(primaryStage);

			primaryStage.setScene(scene);
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
