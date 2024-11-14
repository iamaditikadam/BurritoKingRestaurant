Overview

Burrito King Restaurant 

This project is a JavaFX application with database connectivity, implementing a user management and order system with VIP functionality.

Prerequisites

IDE: Eclipse (or any other Java-compatible IDE)
Java Version: JDK 17
JavaFX Version: JavaFX 17
Database: SQLite
Setup Database

The project uses an SQLite database named trial.db.
Ensure the trial.db file is located in the root directory of the project.
Running Unit Tests

The project includes unit tests for the UserDAO and OrderDAO classes.
In Eclipse, right-click on the test package.
Select Run As -> JUnit Test.
Ensure all tests pass successfully.
Class Diagram

Classes and Design

Main: The entry point of the application.
User: Represents a user in the system.
Order: Represents an order placed by a user.
OrderItem: Represents an individual item in an order.
UserDAO: Data Access Object for user-related database operations.
OrderDAO: Data Access Object for order-related database operations.
Controllers:

LoginController: Manages user login functionality.
DashboardController: Manages the user dashboard.
PaymentController: Handles order payment and confirmation.
MenuController: Displays the menu and allows users to place orders