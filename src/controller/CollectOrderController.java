package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Order;
import model.User;
import databasemanager.OrderDAOImpl;

import java.time.LocalDateTime;

public class CollectOrderController {

	@FXML
	private Label orderDetailsLabel;
	@FXML
	private DatePicker collectTimePicker;

	private Stage primaryStage;
	private User user;
	private Order order;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setOrder(Order order) {
		this.order = order;
		orderDetailsLabel.setText("Order #" + order.getOrderId() + ": " + order.getFoodItemsAsString() + ", Total: $"
				+ order.getTotalPrice() + ", Status: " + order.getStatus());
	}

	@FXML
	private void handleCollectOrder() {
		if (collectTimePicker.getValue() == null) {
			showAlert(Alert.AlertType.ERROR, "Invalid Collection Time", "Please select a valid collection time.");
			return;
		}

		LocalDateTime collectTime = collectTimePicker.getValue().atStartOfDay();
		if (collectTime.isBefore(order.getOrderPlacedTime().plusMinutes(order.getPreparationTime()))) {
			showAlert(Alert.AlertType.ERROR, "Invalid Collection Time",
					"Collection time must be after order preparation time.");
			return;
		}

		OrderDAOImpl orderDAO = new OrderDAOImpl();
		boolean success = orderDAO.collectOrder((int) order.getOrderId(), collectTime);
		if (success) {
			showAlert(Alert.AlertType.INFORMATION, "Order Collected", "Order has been successfully collected.");
			primaryStage.close();
		} else {
			showAlert(Alert.AlertType.ERROR, "Order Collection Failed",
					"Failed to collect the order. Please try again.");
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
	private void handleCancel() {
		primaryStage.close();
	}
}
