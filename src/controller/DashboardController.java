package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Order;
import model.OrderItem;
import model.User;
import databasemanager.OrderDAOImpl;
import databasemanager.UserDAOImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ButtonType;

public class DashboardController {

	@FXML
	private Label welcomeLabel;

	@FXML
	private ListView<Order> orderListView;

	@FXML
	private TextField cancelOrderTextField;

	private Stage primaryStage;
	private User user;
	private OrderDAOImpl orderDAO;
	private UserDAOImpl userDAO;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setUser(User user) throws SQLException {
		this.user = user;
		this.orderDAO = new OrderDAOImpl();
		this.userDAO = new UserDAOImpl();
		// Update the UI with user information if necessary
		updateWelcomeMessage();
		loadActiveOrders();
	}

	private void updateWelcomeMessage() {
		if (user != null) {
			welcomeLabel.setText("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");
		}
	}

	private void loadActiveOrders() {
		OrderDAOImpl orderDAO = new OrderDAOImpl();
		List<Order> activeOrders = orderDAO.getActiveOrders(user);

		System.out.println("Number of active orders: " + activeOrders.size());

		for (Order order : activeOrders) {
			System.out.println("Active Order: " + order);
		}

		orderListView.setItems(FXCollections.observableArrayList(activeOrders));
		orderListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
			@Override
			public ListCell<Order> call(ListView<Order> listView) {
				return new ListCell<Order>() {
					@Override
					protected void updateItem(Order order, boolean empty) {
						super.updateItem(order, empty);
						if (empty || order == null) {
							setText(null);
						} else {
							setText("Order #" + order.getOrderId() + ": " + order.getFoodItemsAsString() + ", Total: $"
									+ order.getTotalPrice() + ", Status: " + order.getStatus());
						}
					}
				};
			}
		});
	}

	@FXML
	private void handleCollectOrder() {
		Order selectedOrder = orderListView.getSelectionModel().getSelectedItem();
		if (selectedOrder == null) {
			showAlert(Alert.AlertType.ERROR, "No Order Selected", "Please select an order to collect.");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CollectOrderView.fxml"));
			Parent collectOrderRoot = loader.load();
			CollectOrderController collectOrderController = loader.getController();
			collectOrderController.setPrimaryStage(primaryStage);
			collectOrderController.setUser(user);
			collectOrderController.setOrder(selectedOrder);

			primaryStage.setScene(new Scene(collectOrderRoot));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleCancelOrder() {
		try {
			long orderId = Long.parseLong(cancelOrderTextField.getText());
			boolean success = orderDAO.cancelOrder(orderId);

			if (success) {
				showAlert(Alert.AlertType.INFORMATION, "Order Cancelled", "The order has been successfully cancelled.");
				loadActiveOrders();
			} else {
				showAlert(Alert.AlertType.ERROR, "Cancellation Failed",
						"Failed to cancel the order. It might already be collected or does not exist.");
			}
		} catch (NumberFormatException e) {
			showAlert(Alert.AlertType.ERROR, "Invalid Order ID", "Please enter a valid order ID.");
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	private void handlerMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuView.fxml"));
			Parent menuRoot = loader.load();
			MenuController menuController = loader.getController();
			menuController.setPrimaryStage(primaryStage);
			menuController.setUser(user);

			Scene menuScene = new Scene(menuRoot);
			primaryStage.setScene(menuScene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleLogout() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
			Parent loginRoot = loader.load();
			LoginController loginController = loader.getController();
			loginController.setPrimaryStage(primaryStage);

			primaryStage.setScene(new Scene(loginRoot));
			primaryStage.setTitle("Login");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleDashboard() {
		System.out.println("Dashboard button clicked");
	}

	@FXML
	private void handleEditProfile() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditProfileView.fxml"));
			Parent editProfileRoot = loader.load();
			EditProfileController editProfileController = loader.getController();
			editProfileController.setPrimaryStage(primaryStage);
			editProfileController.setUser(user);

			primaryStage.setScene(new Scene(editProfileRoot));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleViewAllOrders() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ViewAllOrdersView.fxml"));
			Parent viewAllOrdersRoot = loader.load();
			ViewAllOrdersController viewAllOrdersController = loader.getController();
			viewAllOrdersController.setPrimaryStage(primaryStage);
			viewAllOrdersController.setUser(user);

			primaryStage.setScene(new Scene(viewAllOrdersRoot));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleExportOrders() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ExportOrdersView.fxml"));
			Parent exportOrdersRoot = loader.load();
			ExportOrdersController exportOrdersController = loader.getController();
			exportOrdersController.setPrimaryStage(primaryStage);
			exportOrdersController.setUser(user);

			primaryStage.setScene(new Scene(exportOrdersRoot));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleVIPMember() throws SQLException {
		if (user.isVip()) {
			showAlert(Alert.AlertType.INFORMATION, "Already VIP", "You are already a VIP member.");
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Upgrade to VIP");
		alert.setHeaderText("Would you like to receive promotion information via email?");
		alert.setContentText("Click OK to proceed.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Email Address");
			dialog.setHeaderText("Please enter your email address:");
			dialog.setContentText("Email:");

			Optional<String> emailResult = dialog.showAndWait();
			if (emailResult.isPresent()) {
				String email = emailResult.get();
				user.setVip(true);
				user.setEmail(email);
				userDAO.updateUser(user);

				System.out.println("User upgraded to VIP: " + user.isVip() + ", Email: " + user.getEmail());

				showAlert(Alert.AlertType.INFORMATION, "Upgrade Successful",
						"You have been upgraded to VIP. Please log out and log in again to access VIP functionalities.");
			}
		}
	}

}
