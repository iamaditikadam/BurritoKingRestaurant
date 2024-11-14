package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.OrderItem;
import model.User;
import model.Cart;

import java.io.IOException;
import java.util.List;

public class ConfirmOrderController {

	@FXML
	private TableView<OrderItem> orderTable;
	@FXML
	private TableColumn<OrderItem, String> itemNameColumn;
	@FXML
	private TableColumn<OrderItem, Integer> quantityColumn;
	@FXML
	private TableColumn<OrderItem, Double> priceColumn;
	@FXML
	private Label totalPriceLabel;
	@FXML
	private Label estimatedWaitingTimeLabel;

	@FXML
	private Button goBackButton;

	@FXML
	private Button proceedToPayButton;

	private Stage primaryStage;
	private User user;
	private List<OrderItem> orderItems;
	private double totalPrice;
	private int estimatedWaitingTime;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@FXML
	public void initialize() {
		itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
	}

	public void setOrderDetails(List<OrderItem> orderItems, double totalPrice, int estimatedWaitingTime, User user) {
		this.orderItems = orderItems;
		this.totalPrice = totalPrice;
		this.estimatedWaitingTime = estimatedWaitingTime;
		this.user = user;

		orderTable.getItems().setAll(orderItems);
		totalPriceLabel.setText(String.format("$%.2f", totalPrice));
		estimatedWaitingTimeLabel.setText(String.format("%d minutes", estimatedWaitingTime));
	}

	@FXML
	private void handleConfirmOrder() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Order Confirmed");
		alert.setHeaderText(null);
		alert.setContentText("Your order has been confirmed!");
		alert.showAndWait();

		// Clear the cart after confirming the order
		Cart.getInstance().clearCart();
	}

	@FXML
	private void handleGoBack() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuView.fxml"));
			Parent menuRoot = loader.load();
			MenuController menuController = loader.getController();
			menuController.setPrimaryStage(primaryStage);
			menuController.setUser(user); // Ensure the user is passed back

			primaryStage.setScene(new Scene(menuRoot));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleProceedToPay() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PaymentView.fxml"));
			Parent paymentRoot = loader.load();
			PaymentController paymentController = loader.getController();
			paymentController.setPrimaryStage(primaryStage); // Set the primary stage
			paymentController.setOrderDetails(orderItems, totalPrice, estimatedWaitingTime, user);
			primaryStage.setScene(new Scene(paymentRoot));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
