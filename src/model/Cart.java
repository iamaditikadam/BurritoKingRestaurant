package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private static Cart instance;
	private List<OrderItem> cartItems;

	private Cart() {
		cartItems = new ArrayList<>();
	}

	public static Cart getInstance() {
		if (instance == null) {
			instance = new Cart();
		}
		return instance;
	}

	public List<OrderItem> getCartItems() {
		return cartItems;
	}

	public void addCartItem(OrderItem item) {
		cartItems.add(item);
	}

	public void clearCart() {
		cartItems.clear();
	}

	public void setCartItems(List<OrderItem> items) {
		cartItems = new ArrayList<>(items);
	}
}
