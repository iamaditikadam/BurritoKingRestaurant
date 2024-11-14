package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
	private long orderId;
	private List<OrderItem> foodItems;
	private double totalPrice;
	private String status;
	private LocalDateTime orderPlacedTime;
	private LocalDateTime orderCollectedTime;
	private int userId;
	private int preparationTime;
	private int creditsUsed;
	private int creditsEarned;
	private double actualPricePaid;

	public Order(List<OrderItem> foodItems, double totalPrice, String status, LocalDateTime orderPlacedTime,
			LocalDateTime orderCollectedTime, int userId) {
		this.foodItems = foodItems;
		this.totalPrice = totalPrice;
		this.status = status;
		this.orderCollectedTime = orderCollectedTime;
		this.orderPlacedTime = orderPlacedTime;
		this.userId = userId;

	}

	public Order(List<OrderItem> foodItems, double totalPrice, String status, LocalDateTime orderPlacedTime,
			LocalDateTime orderCollectedTime, int userId, int preparationTime) {
		this.foodItems = foodItems;
		this.totalPrice = totalPrice;
		this.status = status;
		this.orderCollectedTime = orderCollectedTime;
		this.orderPlacedTime = orderPlacedTime;
		this.userId = userId;
		this.preparationTime = preparationTime;

	}

	public Order(long orderId, List<OrderItem> foodItems, double totalPrice, String status,
			LocalDateTime orderPlacedTime, LocalDateTime orderCollectedTime, int userId, int preparationTime) {
		this(foodItems, totalPrice, status, orderPlacedTime, orderCollectedTime, userId, preparationTime);
		this.orderId = orderId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public List<OrderItem> getFoodItems() {
		return foodItems;
	}

	public void setFoodItems(List<OrderItem> foodItems) {
		this.foodItems = foodItems;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getOrderPlacedTime() {
		return orderPlacedTime;
	}

	public void setOrderPlacedTime(LocalDateTime orderPlacedTime) {
		this.orderPlacedTime = orderPlacedTime;
	}

	public LocalDateTime getOrderCollectedTime() {
		return orderCollectedTime;
	}

	public void setOrderCollectedTime(LocalDateTime orderCollectedTime) {
		this.orderCollectedTime = orderCollectedTime;
	}

	public int getUserID() {
		return userId;
	}

	public void setUserID(int userID) {
		this.userId = userID;
	}

	public int getCreditsUsed() {
		return creditsUsed;
	}

	public void setCreditsUsed(int creditsUsed) {
		this.creditsUsed = creditsUsed;
	}

	public int getCreditsEarned() {
		return creditsEarned;
	}

	public void setCreditsEarned(int creditsEarned) {
		this.creditsEarned = creditsEarned;
	}

	public String getFoodItemsAsString() {
		return foodItems.stream().map(OrderItem::toString).collect(Collectors.joining(", "));
	}

	public int getPreparationTime() {
		return preparationTime;
	}

	public double getActualPricePaid() {
		return totalPrice - creditsUsed / 100.0;
	}

	public void setActualPricePaid(double actualPricePaid) {
		this.actualPricePaid = actualPricePaid;
	}

	@Override
	public String toString() {
		return "OrderID: " + orderId + ", Items: " + foodItems.toString() + ", Total: $" + totalPrice + ", Status: "
				+ status + ", Placed: " + orderPlacedTime
				+ (orderCollectedTime != null ? ", Collected: " + orderCollectedTime : "") + ", Credits Used: "
				+ creditsUsed + ", creditsEarned=" + creditsEarned + ", Actual Price Paid: $" + getActualPricePaid();
	}
}
