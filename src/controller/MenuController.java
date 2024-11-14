package controller;

import databasemanager.OrderDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import model.Cart;
import model.OrderItem;
import model.User;
import model.Cart;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuController {

	@FXML
	private Label welcomeLabel;

	@FXML
	private Spinner<Integer> burritoSpinner;
	@FXML
	private Spinner<Integer> friesSpinner;
	@FXML
	private Spinner<Integer> sodaSpinner;
	@FXML
	private Button vipMealButton;

	private OrderDAOImpl orderDAO;
	private Stage primaryStage;
	private User user;

	private int availableFries = 0;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setUser(User user) {
		this.user = user;
		updateWelcomeMessage();
		if (user.isVip()) {
			vipMealButton.setVisible(true);
		}
	}

	private void updateWelcomeMessage() {
		// Assuming there is a welcomeLabel in the FXML file to display the user's name
		if (user != null) {
			// Update the welcome message with the user's name
			welcomeLabel.setText("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");
		}
	}

	@FXML
	public void initialize() {
		burritoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0));
		friesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0));
		sodaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0));
		orderDAO = new OrderDAOImpl();

		Cart cart = Cart.getInstance();
		List<OrderItem> cartItems = cart.getCartItems();
		for (OrderItem item : cartItems) {
			switch (item.getItemName()) {
			case "Burrito":
				burritoSpinner.getValueFactory().setValue(item.getQuantity());
				break;
			case "Fries":
				friesSpinner.getValueFactory().setValue(item.getQuantity());
				break;
			case "Soda":
				sodaSpinner.getValueFactory().setValue(item.getQuantity());
				break;
			}
		}
	}

	@FXML
	private void handlerMenu(ActionEvent event) {
		// Your code here
	}

	@FXML
	private void handleAddToCart() {
		List<OrderItem> orderItems = new ArrayList<>();
		int burritoCount = burritoSpinner.getValue();
		int friesCount = friesSpinner.getValue();
		int sodaCount = sodaSpinner.getValue();

		double totalPrice = 0;
		int estimatedWaitingTime = 0;

		if (burritoCount > 0) {
			orderItems.add(new OrderItem("Burrito", burritoCount, burritoCount * 7.0));
			totalPrice += burritoCount * 7.0;
			estimatedWaitingTime += ((burritoCount + 1) / 2) * 9;
		}
		if (friesCount > 0) {
			if (friesCount > availableFries) {
				int batchesNeeded = (friesCount - availableFries + 4) / 5;
				availableFries += batchesNeeded * 5 - friesCount;
				estimatedWaitingTime += batchesNeeded * 8;
			} else {
				availableFries -= friesCount;
			}
			orderItems.add(new OrderItem("Fries", friesCount, friesCount * 4.0));
			totalPrice += friesCount * 4.0;
		}
		if (sodaCount > 0) {
			orderItems.add(new OrderItem("Soda", sodaCount, sodaCount * 2.5));
			totalPrice += sodaCount * 2.5;
		}

		if (!orderItems.isEmpty()) {
			try {

				Cart cart = Cart.getInstance();
				cart.setCartItems(orderItems);

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ConfirmOrderView.fxml"));
				Parent confirmOrderRoot = loader.load();
				ConfirmOrderController confirmOrderController = loader.getController();
				confirmOrderController.setOrderDetails(orderItems, totalPrice, estimatedWaitingTime, user);
				confirmOrderController.setPrimaryStage(primaryStage);
				confirmOrderController.setUser(user);
				primaryStage.setScene(new Scene(confirmOrderRoot));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			showAlert(Alert.AlertType.ERROR, "Add to Cart Error",
					"Please select at least one item before adding to cart.");
		}
	}

	@FXML
	private void handleOrderVipMeal() {
		List<OrderItem> orderItems = new ArrayList<>();
		orderItems.add(new OrderItem("Burrito", 1, 7.0));
		orderItems.add(new OrderItem("Fries", 1, 4.0));
		orderItems.add(new OrderItem("Soda", 1, 2.5));

		double totalPrice = 7.0 + 4.0 + 2.5 - 3.0; // Discount of $3
		int estimatedWaitingTime = 9 + 8 + 5; // Assuming some fixed preparation times

		try {
			Cart cart = Cart.getInstance();
			cart.setCartItems(orderItems);

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ConfirmOrderView.fxml"));
			Parent confirmOrderRoot = loader.load();
			ConfirmOrderController confirmOrderController = loader.getController();
			confirmOrderController.setOrderDetails(orderItems, totalPrice, estimatedWaitingTime, user);
			confirmOrderController.setPrimaryStage(primaryStage);
			confirmOrderController.setUser(user);
			primaryStage.setScene(new Scene(confirmOrderRoot));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void goToDashboard() throws SQLException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboadView.fxml"));
			Parent root = loader.load();
			DashboardController controller = loader.getController();
			controller.setUser(user);
			controller.setPrimaryStage(primaryStage);
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("Dashboard");
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
