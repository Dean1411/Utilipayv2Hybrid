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

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DeviceCodeCredential;
import com.azure.identity.DeviceCodeCredentialBuilder;

public class DbTest {

    @Test
    public void testUserExistence() {
        String username = "dean.aschendorf@inzaloems.co.za";
        boolean userExists = doesUserExist(username);
        System.out.println("User exists: " + userExists);
        System.out.println("User: " + username);
    }

    public static boolean doesUserExist(String username) {
        String jdbcUrl = getConnectionString(); 

        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            System.err.println("Database connection string not found!");
            return false;
        }

        // Retrieve Azure authentication token dynamically
        String accessToken = getAzureAccessToken();
        if (accessToken == null) {
            System.err.println("Failed to retrieve access token.");
            return false;
        }

        // Establish Database Connection Using Active Directory Token
        Properties properties = new Properties();
        properties.setProperty("accessToken", accessToken); //Pass token as property

        try (Connection connection = DriverManager.getConnection(jdbcUrl, properties)) {
            System.out.println("Connected to the database successfully!");

            //Query Execution Using Parameterized Statement
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


// Retrieves the connection string from `data.properties` in `src/test/resources/`. 
    private static String getConnectionString() {
        Properties properties = new Properties();
        try (InputStream input = DbTest.class.getClassLoader().getResourceAsStream("data.properties")) {
            if (input == null) {
                System.err.println("data.properties not found in src/test/resources!");
                return null;
            }
            properties.load(input);
            return properties.getProperty("DB_CONNECTION_STRING");
        } catch (IOException e) {
            System.err.println("Failed to load database connection string: " + e.getMessage());
        }
        return null;
    }

    //Dynamically retrieves the Microsoft authentication token for database access.  
    private static String getAzureAccessToken() {
        try {
            DeviceCodeCredential credential = new DeviceCodeCredentialBuilder()
                .challengeConsumer(challenge -> 
                    System.out.println("\n🔑 Please authenticate at: " + challenge.getVerificationUrl() +
                                       " and enter the code: " + challenge.getUserCode()))
                .build();

            TokenRequestContext tokenRequestContext = new TokenRequestContext()
                    .addScopes("https://database.windows.net/.default");
            AccessToken token = credential.getToken(tokenRequestContext).block();

            return (token != null) ? token.getToken() : null;
        } catch (Exception e) {
            System.err.println("Error retrieving Azure access token: " + e.getMessage());
            return null;
        }
    }
    
}
