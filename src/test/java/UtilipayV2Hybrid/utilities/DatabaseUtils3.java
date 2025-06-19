package UtilipayV2Hybrid.utilities;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import org.testng.Assert;

public class DatabaseUtils3 {
    private static Connection connection;

    public static void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getDBConnection();
                System.out.println("Database connected successfully using Azure AD authentication.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to connect to database: " + ex.getMessage(), ex);
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/dbCreds.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database credentials: " + e.getMessage(), e);
        }
        return properties;
    }

    @SuppressWarnings("removal")
	private static Connection getDBConnection() {
        String serverName = "";
        try {
            Properties properties = loadProperties();
            

            String serverPrefix = properties.getProperty("DB_SERVER");
            if (serverPrefix == null || serverPrefix.isEmpty()) {
                throw new RuntimeException("DB_SERVER property is missing or empty");
            }
            serverName = serverPrefix.replace(".database.windows.net", "") + ".database.windows.net";

            String databaseName = properties.getProperty("DB_NAME");
            String tenantId = properties.getProperty("TENANT_ID");
            String clientId = properties.getProperty("CLIENT_ID");
            String clientSecret = properties.getProperty("CLIENT_SECRET");

            if (databaseName == null || tenantId == null || clientId == null || clientSecret == null) {
                throw new RuntimeException("Missing required properties in configuration");
            }

            ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .tenantId(tenantId)
                .clientSecret(clientSecret)
                .build();

            TokenRequestContext requestContext = new TokenRequestContext()
                .addScopes("https://database.windows.net/.default");

            AccessToken accessToken = credential.getToken(requestContext).block();

            if (accessToken == null || accessToken.getToken().isEmpty()) {
                throw new RuntimeException("Failed to obtain access token");
            }

            SQLServerDataSource dataSource = new SQLServerDataSource();
            dataSource.setServerName(serverName);
            dataSource.setDatabaseName(databaseName);
            dataSource.setAccessToken(accessToken.getToken());
            dataSource.setEncrypt(true);
            dataSource.setTrustServerCertificate(true); 
            dataSource.setLoginTimeout(30);

            return dataSource.getConnection();

        } catch (RuntimeException | SQLException ex) {
            throw new RuntimeException("Connection failed to " + serverName + ": " + ex.getMessage(), ex);
        }
    }

    public static PreparedStatement getPreparedStatement(String query) {
        try {
            connect();  
            return connection.prepareStatement(query);  
        } catch (SQLException e) {
            throw new RuntimeException("Error preparing statement: " + query, e);
        }
    }

    public static ResultSet executeQuery(String query) {
        try {
            connect();
            return connection.createStatement().executeQuery(query);
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing query: " + query, ex);
        }
    }

    public static int getIntValue(String query, String columnName) {
        try (ResultSet resultSet = executeQuery(query)) {
            return resultSet.next() ? resultSet.getInt(columnName) : -1;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving value for column: " + columnName, e);
        }
    }

    public static void validateTransaction(String meterNumber) {
        try {
            connect();
            String query = "SELECT TOP 1 UnitsReceived, StepCompleted FROM MeterTransactions "
                         + "WHERE MeterNumber = '" + meterNumber + "' "
                         + "ORDER BY TransactionDate DESC";

            try (ResultSet rs = executeQuery(query)) {
                if (rs.next()) {
                    int unitsReceived = rs.getInt("UnitsReceived");
                    int lastStep = rs.getInt("StepCompleted");

                    System.out.println("Database Validation:");
                    System.out.println("Units Received: " + unitsReceived);
                    System.out.println("Last Step Completed: " + lastStep);

                    Assert.assertTrue(unitsReceived > 0, "No units received from database!");
                    Assert.assertTrue(lastStep >= 0, "Invalid step recorded!");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Validation failed", ex);
        } finally {
            close();
        }
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
