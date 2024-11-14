package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Order;
import model.OrderItem;
import model.User;
import databasemanager.DatabaseConnector;
import databasemanager.OrderDAOImpl;
import databasemanager.UserDAOImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaymentController {

	@FXML
	private Label totalPriceLabel;

	@FXML
	private Label estimatedWaitingTimeLabel;

	@FXML
	private TextField cardNumberField;

	@FXML
	private TextField expiryDateField;

	@FXML
	private TextField cvvField;

	@FXML
	private TextField creditsField;

	@FXML
	private Label successMessageLabel;

	@FXML
	private Label errorMessage;

	private double totalPrice;
	private int estimatedWaitingTime;
	private User user;
	private List<OrderItem> orderItems;
	private Stage primaryStage;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setOrderDetails(List<OrderItem> orderItems, double totalPrice, int estimatedWaitingTime, User user) {
		this.orderItems = orderItems;
		this.totalPrice = totalPrice;
		this.estimatedWaitingTime = estimatedWaitingTime;
		this.user = user;

		totalPriceLabel.setText(String.format("$%.2f", totalPrice));
		estimatedWaitingTimeLabel.setText(String.format("%d minutes", estimatedWaitingTime));
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

	@FXML
	private void handleConfirmPayment() throws SQLException {
		String cardNumber = cardNumberField.getText();
		String expiryDate = expiryDateField.getText();
		String cvv = cvvField.getText();
		 int creditsToUse = 0;
		 
		 if (!creditsField.getText().isEmpty()) {
	            try {
	                creditsToUse = Integer.parseInt(creditsField.getText());
	            } catch (NumberFormatException e) {
	                errorMessage.setText("Invalid credits value. Please enter a valid number.");
	                successMessageLabel.setVisible(false);
	                return;
	            }
	        }
		 
		if (validateCardNumber(cardNumber) && validateExpiryDate(expiryDate) && validateCVV(cvv)) {
			placeOrder(creditsToUse);
		} else {
			errorMessage.setText("Invalid payment details. Please check and try again.");
			successMessageLabel.setVisible(false);
		}
	}

	private boolean validateCardNumber(String cardNumber) {
		return cardNumber != null && cardNumber.length() == 16 && cardNumber.matches("\\d+");
	}

	private boolean validateExpiryDate(String expiryDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
			YearMonth parsedDate = YearMonth.parse(expiryDate, formatter);
			YearMonth currentDate = YearMonth.now();
			return parsedDate.isAfter(currentDate);
		} catch (Exception e) {
			return false;
		}
	}

	private boolean validateCVV(String cvv) {
		return cvv != null && cvv.length() == 3 && cvv.matches("\\d+");
	}

	private void placeOrder(int creditsToUse) throws SQLException {
		LocalDateTime orderPlacedTime = LocalDateTime.now();
		double actualPricePaid = totalPrice - (creditsToUse / 100.0);
		int creditsEarned = (int) actualPricePaid;

		Order order = new Order(orderItems, totalPrice, "await for collection", orderPlacedTime, null,
				user.getUserID());
		order.setCreditsUsed(creditsToUse);
		order.setCreditsEarned(creditsEarned);
		order.setActualPricePaid(actualPricePaid);

		OrderDAOImpl orderDAO = new OrderDAOImpl();
		orderDAO.saveOrder(order);

		user.setCredits(user.getCredits() - creditsToUse + creditsEarned);
		UserDAOImpl userDAO = UserDAOImpl.getInstance();
		userDAO.updateUser(user);

		successMessageLabel.setText("Payment successful! Your order is placed.");
		successMessageLabel.setVisible(true);
		errorMessage.setText("");
	}
}
