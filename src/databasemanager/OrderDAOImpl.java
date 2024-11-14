package databasemanager;

import model.Order;
import model.OrderItem;
import model.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl {

	public OrderDAOImpl() {

	}

	public List<Order> getActiveOrders(User user) {
		List<Order> orders = new ArrayList<>();
		String query = "SELECT * FROM orders WHERE userID = ? AND status = 'await for collection' ORDER BY orderPlacedTime DESC";

		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, user.getUserID());
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				long orderId = resultSet.getLong("orderID");
				String foodItemsStr = resultSet.getString("foodItems");
				List<OrderItem> foodItems = parseFoodItems(foodItemsStr);
				double totalPrice = resultSet.getDouble("totalPrice");
				String status = resultSet.getString("status");
				LocalDateTime orderPlacedTime = LocalDateTime.parse(resultSet.getString("orderPlacedTime"));
				LocalDateTime orderCollectedTime = resultSet.getString("orderCollectedTime") != null
						? LocalDateTime.parse(resultSet.getString("orderCollectedTime"))
						: null;
				int preparationTime = resultSet.getInt("preparationTime");
				int creditsUsed = resultSet.getInt("creditsUsed");
				int creditsEarned = resultSet.getInt("creditsEarned");

				Order order = new Order(foodItems, totalPrice, status, orderPlacedTime, orderCollectedTime,
						user.getUserID(), preparationTime);
				order.setCreditsUsed(creditsUsed);
				order.setCreditsEarned(creditsEarned);
				order.setOrderId(orderId);
				orders.add(order);
				System.out.println("Retrieved Order: " + order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return orders;
	}

	private List<OrderItem> parseFoodItems(String foodItemsString) {
		List<OrderItem> foodItems = new ArrayList<>();
		if (foodItemsString != null && !foodItemsString.isEmpty()) {
			String[] itemsArray = foodItemsString.split(", ");
			for (String itemString : itemsArray) {
				String[] parts = itemString.split(" x| \\($|\\)");
				if (parts.length == 3) {
					String name = parts[0];
					int quantity = Integer.parseInt(parts[1]);
					double price = Double.parseDouble(parts[2]);
					foodItems.add(new OrderItem(name, quantity, price));
				}
			}
		}
		return foodItems;
	}

	public void saveOrder(Order order) {
		String query = "INSERT INTO orders (foodItems, totalPrice, status, orderPlacedTime, orderCollectedTime, userId, preparationTime, creditsUsed, creditsEarned , actualPricePaid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, order.getFoodItemsAsString());
			preparedStatement.setDouble(2, order.getTotalPrice());
			preparedStatement.setString(3, order.getStatus());
			preparedStatement.setString(4, order.getOrderPlacedTime().toString());
			preparedStatement.setString(5,
					order.getOrderCollectedTime() != null ? order.getOrderCollectedTime().toString() : null);
			preparedStatement.setInt(6, order.getUserID());
			preparedStatement.setInt(7, order.getPreparationTime());
			preparedStatement.setInt(8, order.getCreditsUsed());
			preparedStatement.setInt(9, order.getCreditsEarned());
			preparedStatement.setDouble(10, order.getActualPricePaid());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void printAllOrders() {
		String query = "SELECT * FROM orders";

		try (Connection conn = DatabaseConnector.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				System.out.println("Order ID: " + rs.getInt("orderID"));
				System.out.println("Food Items: " + rs.getString("foodItems"));
				System.out.println("Total Price: " + rs.getDouble("totalPrice"));
				System.out.println("Status: " + rs.getString("status"));
				System.out.println("Order Placed Time: " + rs.getString("orderPlacedTime"));
				System.out.println("Order Collected Time: " + rs.getString("orderCollectedTime"));
				System.out.println("Credits Used: " + rs.getInt("creditsUsed"));
				System.out.println("Credits Earned: " + rs.getInt("creditsEarned"));
				System.out.println("-----------------------------------");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Order> getAllOrdersForUser(int userId) {
		List<Order> orders = new ArrayList<>();
		String sql = "SELECT * FROM orders WHERE userId = ? ORDER BY orderPlacedTime DESC";

		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				long orderId = rs.getLong("orderID");
				List<OrderItem> foodItems = parseFoodItems(rs.getString("foodItems"));
				double totalPrice = rs.getDouble("totalPrice");
				String status = rs.getString("status");
				LocalDateTime orderPlacedTime = LocalDateTime.parse(rs.getString("orderPlacedTime"));
				LocalDateTime orderCollectedTime = rs.getString("orderCollectedTime") != null
						? LocalDateTime.parse(rs.getString("orderCollectedTime"))
						: null;
				int preparationTime = rs.getInt("preparationTime");
				int creditsUsed = rs.getInt("creditsUsed");
				int creditsEarned = rs.getInt("creditsEarned");

				Order order = new Order(orderId, foodItems, totalPrice, status, orderPlacedTime, orderCollectedTime,
						userId, preparationTime);
				order.setCreditsUsed(creditsUsed);
				order.setCreditsEarned(creditsEarned);
				order.setOrderId(orderId);
				orders.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return orders;
	}

	public boolean collectOrder(int orderId, LocalDateTime collectTime) {
		String sql = "UPDATE orders SET status = ?, orderCollectedTime = ? WHERE orderID = ? AND status != 'cancelled'";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, "collected");
			pstmt.setString(2, collectTime.toString());
			pstmt.setInt(3, orderId);

			int rowsUpdated = pstmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean cancelOrder(long orderId) {
		String sql = "UPDATE orders SET status = 'cancelled' WHERE orderID = ? AND status != 'collected'";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setLong(1, orderId);

			int rowsUpdated = pstmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void exportOrdersToCSV(List<Order> orders, List<String> fields, String filePath) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(String.join(",", fields));
			writer.newLine();

			for (Order order : orders) {
				StringBuilder line = new StringBuilder();
				for (String field : fields) {
					switch (field) {
					case "OrderID":
						line.append(order.getOrderId()).append(",");
						break;
					case "Items":
						line.append("\"").append(order.getFoodItemsAsString()).append("\"").append(",");
						break;
					case "TotalPrice":
						line.append(order.getTotalPrice()).append(",");
						break;
					case "Status":
						line.append(order.getStatus()).append(",");
						break;
					case "OrderPlacedTime":
						line.append(order.getOrderPlacedTime()).append(",");
						break;
					case "OrderCollectedTime":
						line.append(order.getOrderCollectedTime() != null ? order.getOrderCollectedTime() : "")
								.append(",");
						break;
					case "CreditsUsed":
						line.append(order.getCreditsUsed()).append(",");
						break;
					case "ActualPricePaid":
						line.append(order.getActualPricePaid()).append(",");
						break;
					}
				}
				writer.write(line.deleteCharAt(line.length() - 1).toString());
				writer.newLine();
			}
		}
	}

	// Helper method to parse OrderItems from a String
	private List<OrderItem> parseOrderItems(String itemsString) {
		// Implement this method to parse the itemsString into a List<OrderItem>
		// This is just a placeholder
		return new ArrayList<>();
	}
}
