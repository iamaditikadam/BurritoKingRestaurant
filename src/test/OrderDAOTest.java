package test;

import model.User;
import org.junit.jupiter.api.*;

import databasemanager.UserDAOImpl;
import model.Order;
import model.OrderItem;
import databasemanager.DatabaseConnector;
import databasemanager.OrderDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOTest {

    private OrderDAOImpl orderDAO;
    private UserDAOImpl userDAO;
    private User testUser;

    @BeforeEach
    void setUp() throws SQLException {
        orderDAO = new OrderDAOImpl();
        userDAO = UserDAOImpl.getInstance();

        // Clear the database to ensure test isolation
        clearDatabase();

        // Create a test user
        testUser = new User(0, "Test", "User", "testuser", "password", false);
        testUser.setEmail("test.user@example.com");
        testUser.setCredits(50);
        userDAO.saveUserInDatabase(testUser);
        testUser = (User) userDAO.loginUser("testuser", "password").get("user");
    }

    @Test
    void testPlaceOrder() throws SQLException {
        List<OrderItem> items = Arrays.asList(
                new OrderItem("Burrito", 2, 7.0),
                new OrderItem("Fries", 1, 4.0)
        );

        Order order = new Order(items, 18.0, "await for collection", LocalDateTime.now(), null, testUser.getUserID());
        order.setCreditsUsed(10);
        order.setCreditsEarned(8);
        order.setActualPricePaid(17.0);

        assertDoesNotThrow(() -> orderDAO.saveOrder(order));

        // Verify the order is saved correctly
        List<Order> orders = orderDAO.getActiveOrders(testUser);
        assertTrue(orders.stream().anyMatch(o -> o.getTotalPrice() == 18.0));
    }

    @Test
    void testGetActiveOrders() {
        List<Order> orders = orderDAO.getActiveOrders(testUser);

        // Assuming the database is cleared before each test, there should be no active orders initially
        assertNotNull(orders);
        assertTrue(orders.isEmpty());

        // Now add an order and test again
        List<OrderItem> items = Arrays.asList(
                new OrderItem("Burrito", 2, 7.0),
                new OrderItem("Fries", 1, 4.0)
        );

        Order order = new Order(items, 18.0, "await for collection", LocalDateTime.now(), null, testUser.getUserID());
        order.setCreditsUsed(10);
        order.setCreditsEarned(8);
        order.setActualPricePaid(17.0);

        assertDoesNotThrow(() -> orderDAO.saveOrder(order));

        // Verify the active orders now contain the new order
        orders = orderDAO.getActiveOrders(testUser);
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
    }

    @Test
    void testCollectOrder() {
        List<Order> orders = orderDAO.getActiveOrders(testUser);

        if (!orders.isEmpty()) {
            Order order = orders.get(0);
            boolean result = orderDAO.collectOrder((int) order.getOrderId(), LocalDateTime.now());
            assertTrue(result);
        }
    }

    // Utility method to clear the database
    private void clearDatabase() throws SQLException {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = DatabaseConnector.getConnection();
            stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM orders");
            stmt.executeUpdate("DELETE FROM user");
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
