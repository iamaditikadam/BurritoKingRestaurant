package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Order;
import model.User;
import databasemanager.OrderDAOImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ViewAllOrdersController {

	@FXML
	private ListView<Order> ordersListView;

	private Stage primaryStage;
	private User user;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setUser(User user) {
		this.user = user;
		loadAllOrders();

	}

	private void loadAllOrders() {
		if (user == null) {
			throw new IllegalStateException("User must be set before loading orders.");
		}

		OrderDAOImpl orderDAO = new OrderDAOImpl();
		List<Order> allOrders = orderDAO.getAllOrdersForUser(user.getUserID());

		ordersListView.setItems(FXCollections.observableArrayList(allOrders));
		ordersListView.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
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
									+ order.getTotalPrice() + ", Status: " + order.getStatus() + ", Placed: "
									+ order.getOrderPlacedTime());
						}
					}
				};
			}
		});
	}

	@FXML
	private void handleBackToDashboard() throws SQLException {
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
}
