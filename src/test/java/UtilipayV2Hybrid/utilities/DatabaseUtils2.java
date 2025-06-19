package UtilipayV2Hybrid.utilities;

import java.sql.*;

import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class DatabaseUtils2 {
    private static Connection connection;

    // Establish database connection
    public static void connect() {
        String dbUrl = "jdbc:sqlserver://your-db-url;databaseName=yourDB";
        String dbUser = "yourUsername";
        String dbPassword = "yourPassword";

        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                System.out.println("Database connected successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed.");
        }
    }

    // Method to execute SELECT queries
    public static ResultSet executeQuery(String query) {
        try {
            connect();
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing query: " + query);
        }
    }

    // Method to fetch an integer value from a query
    public static int getIntValue(String query, String columnName) {
        try (ResultSet resultSet = executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Default value if no result found
    }

    // Validate transaction details from the database
    public static void validateTransaction(String meterNumber) {
        connect();

        // Query to fetch latest transaction details
        String query = "SELECT UnitsReceived, StepCompleted FROM MeterTransactions WHERE MeterNumber = '" + meterNumber + "' ORDER BY TransactionDate DESC LIMIT 1";

        int unitsReceived = getIntValue(query, "UnitsReceived");
        int lastStep = getIntValue(query, "StepCompleted");

        System.out.println("Database Validation:");
        System.out.println("Units Received: " + unitsReceived);
        System.out.println("Last Step Completed: " + lastStep);

        // Validate units received
        Assert.assertTrue(unitsReceived > 0, "No units received from database!");
        Assert.assertTrue(lastStep >= 0, "Invalid step recorded!");

        close();
    }

    // Close the database connection
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @AfterClass
    public void tearDown() {
        DatabaseUtils2.close();
    }

    
    
}
