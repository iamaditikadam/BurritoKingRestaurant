# Burrito King Order Management System

## Overview
**Burrito King** is a Java-based desktop application designed to simulate a restaurant order management system. This project allows users to register, log in, view the menu, place orders, track order status, and manage VIP memberships. Admins have additional capabilities, such as viewing all orders and exporting data to CSV files.

This project was developed as part of a coursework assignment for the Master’s program, with a focus on applying Object-Oriented Programming (OOP) principles, MVC design pattern, and database management using SQLite.

---

## Table of Contents
1. [Features](#features)
2. [Architecture](#architecture)
3. [Technologies Used](#technologies-used)
4. [Setup and Installation](#setup-and-installation)
5. [Usage Guide](#usage-guide)
6. [Future Improvements](#future-improvements)

---

## Features

### User Features
- **Registration and Login**: Users can create an account and log in to the system.
- **Menu Browsing**: Users can view available menu items, select quantities, and add items to their cart.
- **Order Management**: Users can place orders, view order details, and track order status.
- **VIP Membership**: Users can opt to become VIP members, receiving additional discounts.
- **Payment Processing**: Users can enter payment details, apply credits, and confirm orders.
- **View All Orders**: Users can access and manage all orders in the system.
- **Order Export**: Users can export selected order data to CSV files for record-keeping and analysis.

---

## Architecture

This application follows the **MVC (Model-View-Controller)** pattern:
- **Model**: Represents data entities like `User`, `Order`, and `OrderItem`, and handles business logic.
- **View**: JavaFX FXML files define the user interface layout, with views for login, dashboard, menu, and more.
- **Controller**: Controllers like `LoginController`, `DashboardController`, and `MenuController` handle user interactions and coordinate between the view and model.

### Key Classes and Components

- **DatabaseConnector**: Manages the SQLite database connection and initializes tables.
- **UserDAO & OrderDAO**: Handle database operations for users and orders, encapsulating SQL logic.
- **Controllers**: Coordinate user actions, business logic, and view updates for each UI screen.

---

## Technologies Used

- **Java**: Core programming language for backend logic and OOP implementation.
- **JavaFX**: Framework for building the graphical user interface (GUI).
- **SQLite**: Lightweight relational database for data storage.
- **JavaFX FXML**: Markup for defining the user interface layout.
- **JDK 17 and JavaFX 17**: Compatible versions for optimal JavaFX support.

---

## Setup and Installation

### Prerequisites
- **Java Development Kit (JDK) 17**: Ensure JDK 17 is installed.
- **JavaFX SDK 17**: Download and configure JavaFX.
- **SQLite**: No installation required, as SQLite operates through Java’s JDBC.

### Installation Steps
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/burrito-king.git
   cd burrito-king
