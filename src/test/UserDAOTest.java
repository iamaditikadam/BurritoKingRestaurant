package test;

import model.User;
import org.junit.jupiter.api.*;
import databasemanager.UserDAOImpl;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

	private UserDAOImpl userDAO;

	@BeforeEach
	void setUp() throws SQLException {
		userDAO = UserDAOImpl.getInstance();
	}

	@Test
	void testSaveUserInDatabase() throws SQLException {
		User user = new User(0, "John", "Doe", "johndoe", "password", false);
		user.setEmail("john.doe@example.com");
		user.setCredits(0);

		assertDoesNotThrow(() -> userDAO.saveUserInDatabase(user));
	}

	@Test
	void testUpgradeToVIP() throws SQLException {
		User user = new User(0, "Jane", "Doe", "janedoe", "password", false);
		user.setEmail("jane.doe@example.com");
		user.setCredits(0);

		userDAO.saveUserInDatabase(user);
		user = (User) userDAO.loginUser("janedoe", "password").get("user");

		user.setVip(true);
		user.setEmail("vip.jane.doe@example.com");
		userDAO.updateUser(user);

		User retrievedUser = (User) userDAO.loginUser("janedoe", "password").get("user");

		assertNotNull(retrievedUser);
		assertTrue(retrievedUser.isVip());
		assertEquals("vip.jane.doe@example.com", retrievedUser.getEmail());
	}

	@Test
	void testUpdateUserCredits() throws SQLException {
		User user = new User(0, "Alice", "Smith", "alicesmith", "password", false);
		user.setEmail("alice.smith@example.com");
		user.setCredits(0);

		userDAO.saveUserInDatabase(user);
		user = (User) userDAO.loginUser("alicesmith", "password").get("user");

		user.setCredits(100);
		userDAO.updateUser(user);

		User retrievedUser = (User) userDAO.loginUser("alicesmith", "password").get("user");

		assertNotNull(retrievedUser);
		assertEquals(100, retrievedUser.getCredits());
	}
}
