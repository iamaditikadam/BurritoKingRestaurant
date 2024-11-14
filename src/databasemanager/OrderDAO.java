package databasemanager;

import model.Order;
import java.util.List;

public interface OrderDAO {
	void saveOrderInDatabase(Order order);

	List<Order> getAllOrders();

	Order getOrderById(long orderId);

	void updateOrderStatus(long orderId, String status);

	void printAllOrders();
}