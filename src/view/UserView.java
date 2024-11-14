package view;

import java.util.Scanner;
import model.User;

public class UserView {
	private Scanner sc = new Scanner(System.in);

	public User getUserDetails() {
		System.out.println("Enter user details: ");

		System.out.println("User ID: ");
		int userID = Integer.parseInt(sc.nextLine());

		System.out.println("First Name: ");
		String firstName = sc.nextLine();

		System.out.println("Last Name: ");
		String lastName = sc.nextLine();

		System.out.println("User Name: ");
		String userName = sc.nextLine();

		System.out.println("Password: ");
		String password = sc.nextLine();

		System.out.println("Is VIP (true/false): ");
		boolean isVip = Boolean.parseBoolean(sc.nextLine());

		return new User(userID, firstName, lastName, userName, password, isVip);

	}

	public void showUser(User user) {
		System.out.println("User Details: ");
		System.out.println("User ID: " + user.getUserID());
		System.out.println("First Name: " + user.getFirstName());
		System.out.println("Last Name: " + user.getLastName());
		System.out.println("User Name: " + user.getUserName());
		System.out.println("User Password: " + user.getPassword());
		System.out.println("VIP: " + user.isVip());

	}

	public void showInsertSuccess() {
		System.out.println("User inserted successfully!");
	}

	public void showInsertFailure() {
		System.out.println("User insertion failed!");
	}

}
