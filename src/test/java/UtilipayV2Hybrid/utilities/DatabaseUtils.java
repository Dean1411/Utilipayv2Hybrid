package UtilipayV2Hybrid.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.testng.Assert;

public class DatabaseUtils {
    private static Connection connection;

//    // Establish passwordless database connection
//    public static void connect() {
//        try {
//            if (connection == null || connection.isClosed()) {
//                connection = getDBConnection();
//                System.out.println("Database connected successfully using Entra MFA.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Database connection failed.");
//        }
//    }
    
    public static void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getDBConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage());
        }
    }


//    private static Connection getDBConnection() {
//        try {
//
////            DefaultAzureCredential credential = new DefaultAzureCredentialBuilder().build();
//        	AzureCliCredential credential = new AzureCliCredentialBuilder().build();
//
//            AccessToken accessToken = credential.getToken(
//                    new TokenRequestContext().setScopes(Collections.singletonList("https://database.windows.net/.default"))
//            ).block(); 
//
//            if (accessToken == null) {
//                throw new RuntimeException("Failed to acquire access token for database authentication.");
//            }
//
//            // Set up SQL Server Data Source
//            SQLServerDataSource dataSource = new SQLServerDataSource();
//            dataSource.setServerName("utilipay-v2.database.windows.net");
//            dataSource.setDatabaseName("utilipay-dev");
//            dataSource.setAuthentication("ActiveDirectoryAccessToken");
//            dataSource.setTrustServerCertificate(false);
//            dataSource.setEncrypt(true);
//
//            // Connect to DB using the Access Token
//            return dataSource.getConnection(accessToken.getToken(), null);
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Database connection failed: " + e.getMessage());
//        }
//    }
    
    
    
//    private static Connection getDBConnection() {
//        try {
//            // Authenticate using Azure CLI with timeout
//            AzureCliCredential credential = new AzureCliCredentialBuilder().build();
//
//            AccessToken accessToken = Mono.fromCallable(() -> {
//                return credential.getToken(new TokenRequestContext().setScopes(Collections.singletonList("https://database.windows.net/.default"))).block();
//            })
//            .timeout(Duration.ofSeconds(60)) 
//            .block();
//
//            if (accessToken == null) {
//                throw new RuntimeException("Failed to acquire access token for database authentication.");
//            }
//
//            // Set up SQL Server Data Source
//            SQLServerDataSource dataSource = new SQLServerDataSource();
//            dataSource.setServerName("utilipay-v2.database.windows.net");
//            dataSource.setDatabaseName("utilipay-dev");
//            dataSource.setAuthentication("ActiveDirectoryDefault");
//            dataSource.setTrustServerCertificate(false);
//            dataSource.setEncrypt(true);
//
//
//            Connection conn = dataSource.getConnection();
//            
//            conn.setNetworkTimeout(null, 30000); 
//
//            if (conn instanceof SQLServerConnection) {
//                SQLServerConnection sqlServerConnection = (SQLServerConnection) conn;
//                sqlServerConnection.setAccessTokenCallbackClass(accessToken.getToken());
//                return sqlServerConnection;
//            } else {
//                throw new RuntimeException("Connection is not an instance of SQLServerConnection.");
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Database connection failed: " + e.getMessage());
//        } catch (Exception e) {
//            throw new RuntimeException("Error during token acquisition or connection: " + e.getMessage());
//        }
//    }
    
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/data.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database credentials: " + e.getMessage());
        }
        return properties;
    }
    
    public static Connection getDBConnection() {
        try {
            // Load credentials from properties file
            Properties properties = loadProperties();
            String connectionString = properties.getProperty("DB_CONNECTION_STRING");
            String username = properties.getProperty("DB_USERNAME");
            String password = properties.getProperty("DB_PASSWORD");

            if (connectionString == null || username == null || password == null) {
                throw new RuntimeException("Database credentials are missing in data.properties.");
            }

            // Establish connection
            Connection conn = DriverManager.getConnection(connectionString, username, password);
            System.out.println("Database connected successfully using username and password.");
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage());
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


    public static int getIntValue(String query, String columnName) {
        try (ResultSet resultSet = executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }

    // Validate transaction details from the database
    public static void validateTransaction(String meterNumber) {
        connect();


        String query = "SELECT UnitsReceived, StepCompleted FROM MeterTransactions WHERE MeterNumber = '" 
                       + meterNumber + "' ORDER BY TransactionDate DESC LIMIT 1";

        int unitsReceived = getIntValue(query, "UnitsReceived");
        int lastStep = getIntValue(query, "StepCompleted");

        System.out.println("Database Validation:");
        System.out.println("Units Received: " + unitsReceived);
        System.out.println("Last Step Completed: " + lastStep);

        Assert.assertTrue(unitsReceived > 0, "No units received from database!");
        Assert.assertTrue(lastStep >= 0, "Invalid step recorded!");

        close();
    }

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
}
