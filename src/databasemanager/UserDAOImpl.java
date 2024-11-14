package databasemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import model.User;

public class UserDAOImpl implements UserDAO {
	private static UserDAOImpl instance;

	public UserDAOImpl() {
		// private constructor to enforce singleton pattern
	}

	public static UserDAOImpl getInstance() {
		if (instance == null) {
			instance = new UserDAOImpl();
		}
		return instance;
	}

	@Override
	public boolean saveUserInDatabase(User user) {
		String sqlQuery = "INSERT INTO user(firstName, lastName, userName, password, isVip, email, credits) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			System.out.println("Inserting user into database");
			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getPassword());
			pstmt.setBoolean(5, user.isVip());
			pstmt.setString(6, user.getEmail());
			pstmt.setInt(7, user.getCredits());

			int rowsInserted = pstmt.executeUpdate();
			System.out.println("Rows inserted: " + rowsInserted);
			printAllUsers();
			return rowsInserted > 0;

		} catch (SQLException e) {
			System.out.println("Error inserting user: " + e.getMessage());
			return false;
		}
	}

	public void updateUser(User user) {
		String query = "UPDATE user SET firstName = ?, lastName = ?, password = ?, isVip = ?, email = ?, credits = ? WHERE userID = ?";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, user.getFirstName());
			preparedStatement.setString(2, user.getLastName());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setBoolean(4, user.isVip());
			preparedStatement.setString(5, user.getEmail());
			preparedStatement.setInt(6, user.getCredits());
			preparedStatement.setInt(7, user.getUserID());

			int rowsUpdated = preparedStatement.executeUpdate();
			System.out.println("Rows updated: " + rowsUpdated);
			System.out
					.println("User VIP status updated to: " + user.isVip() + ", Email updated to: " + user.getEmail());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean checkUserNameExists(String userName) {
		String sql = "SELECT COUNT(*) FROM user WHERE userName = ?";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void updateUserCredits(int userId, int credits) {
		String query = "UPDATE user SET credits = ? WHERE userID = ?";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, credits);
			preparedStatement.setInt(2, userId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getUserCredits(int userId) {
		String query = "SELECT credits FROM user WHERE userID = ?";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("credits");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public HashMap<String, Object> loginUser(String userName, String password) {
		HashMap<String, Object> result = new HashMap<>();
		String sql = "SELECT * FROM user WHERE userName = ? AND password = ?";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, userName);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				User user = new User(rs.getInt("userID"), rs.getString("firstName"), rs.getString("lastName"),
						rs.getString("userName"), rs.getString("password"), rs.getBoolean("isVip"));
				user.setEmail(rs.getString("email"));
				user.setCredits(rs.getInt("credits"));
				result.put("loginSuccess", true);
				result.put("user", user);
			} else {
				result.put("loginSuccess", false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result.put("loginSuccess", false);
		}
		return result;
	}

	@Override
	public void getLatestUserId() {
		// Not implemented
	}

	@Override
	public boolean updateUserInDatabase(User user) {
		// Not implemented
		return false;
	}

	@Override
	public boolean addIsVip(int userID) {
		// Not implemented
		return false;
	}

	public boolean updateUserInfo(User user) {
		String sql = "UPDATE user SET firstName = ?, lastName = ?, password = ? WHERE userID = ?";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getPassword());
			pstmt.setInt(4, user.getUserID());
			int rowsUpdated = pstmt.executeUpdate();
			System.out.println("Rows updated: " + rowsUpdated);
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void printAllUsers() {
		String sql = "SELECT * FROM user";
		try (Connection connection = DatabaseConnector.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			System.out.println("Current users in the database:");
			while (rs.next()) {
				System.out.println("UserID: " + rs.getInt("userID") + ", FirstName: " + rs.getString("firstName")
						+ ", LastName: " + rs.getString("lastName") + ", UserName: " + rs.getString("userName")
						+ ", Password: " + rs.getString("password") + ", isVip: " + rs.getBoolean("isVip") + ", Email: "
						+ rs.getString("email") + ", Credits: " + rs.getInt("credits"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
