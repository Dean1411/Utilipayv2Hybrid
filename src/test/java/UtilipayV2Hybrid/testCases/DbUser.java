package UtilipayV2Hybrid.testCases;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.testng.annotations.Test;

public class DbUser {

    @Test
    public void testUserExistence() {
//        String username = "dean.aschendorf@inzaloems.co.za";
    	String username = "terrah.mthombeni2@inzaloems.co.za";
        boolean userExists = doesUserExist(username);
        System.out.println("User exists: " + userExists);
        System.out.println("User: " + username);
    }

    public static boolean doesUserExist(String username) {
        // Retrieve DB credentials
        Properties dbProperties = getDatabaseProperties();
        if (dbProperties == null) {
            System.err.println("Database properties could not be loaded!");
            return false;
        }

        String jdbcUrl = dbProperties.getProperty("DB_CONNECTION_STRING");
        String dbUsername = dbProperties.getProperty("DB_USERNAME");
        String dbPassword = dbProperties.getProperty("DB_PASSWORD");

        if (jdbcUrl == null || dbUsername == null || dbPassword == null) {
            System.err.println("Database credentials are missing in properties file!");
            return false;
        }

        // Establish Database Connection Using Username and Password
        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            System.out.println("Connected to the database successfully!");

            // Query Execution Using Parameterized Statement
            String query = "SELECT COUNT(*) FROM [User] WHERE UserName = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        return count > 0;
                    }
                }
                System.out.println("User: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Retrieves database properties (connection string, username, password) from `data.properties`
    private static Properties getDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream input = DbUser.class.getClassLoader().getResourceAsStream("data.properties")) {
            if (input == null) {
                System.err.println("data.properties not found in src/test/resources!");
                return null;
            }
            properties.load(input);
            return properties;
        } catch (IOException e) {
            System.err.println("Failed to load database properties: " + e.getMessage());
            return null;
        }
    }
}
