package databasemanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

	private static final String DB_URL = "jdbc:sqlite:trial.db";
	private static Connection connection = null;

	public static Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(DB_URL);
			try (Statement stmt = connection.createStatement()) {
				stmt.execute("PRAGMA busy_timeout = 5000");
			}
			System.out.println("Database connected successfully.");
		}
		return connection;
	}

	public static void initializeDatabase() {

		String sqlUser = "CREATE TABLE IF NOT EXISTS user (" + "userID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "firstName TEXT NOT NULL," + "lastName TEXT NOT NULL," + "userName TEXT NOT NULL,"
				+ "password TEXT NOT NULL," + "isVip BOOLEAN NOT NULL)";

		String sqlOrder = "CREATE TABLE IF NOT EXISTS orders (" + "orderID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "foodItems TEXT NOT NULL," + "totalPrice REAL NOT NULL," + "status TEXT NOT NULL,"
				+ "orderPlacedTime TEXT NOT NULL," + "orderCollectedTime TEXT)";

		try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
			stmt.execute(sqlUser);
			stmt.execute(sqlOrder);
			System.out.println("Tables 'user' and 'orders' created or already exist.");
		} catch (SQLException e) {
			System.out.println("Error initializing database: " + e.getMessage());
		}
	}
}
