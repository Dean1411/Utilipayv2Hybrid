package UtilipayV2Hybrid.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class CleanupUtility {

    private static Properties getDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream input = CleanupUtility.class.getClassLoader().getResourceAsStream("data.properties")) {
            if (input == null) {
                System.err.println("data.properties not found in src/test/resources!");
                return null;
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load database properties: " + e.getMessage());
        }
        return properties;
    }

    /**
     * Executes direct SQL DELETE statements to clean up test data.
     */
    public static void runDirectCleanup() {
        Properties props = getDatabaseProperties();
        if (props == null) {
            System.err.println("Database properties not loaded. Cannot run cleanup.");
            return;
        }

        String url = props.getProperty("DB_CONNECTION_STRING");
        String user = props.getProperty("DB_USERNAME");
        String pass = props.getProperty("DB_PASSWORD");

        String[] cleanupStatements = new String[] {
            "DELETE FROM [dbo].[Municipality] WHERE Name = 'Regression Municipality'",
            "DELETE FROM [dbo].[Accounts] WHERE StandId IN (111896, 111897)",
            "DELETE FROM [dbo].[MeterInstallationHistory] WHERE StandId IN (111896, 111897)",
            "DELETE FROM [dbo].[Stands] WHERE Id IN (111896, 111897)",
            "DELETE FROM [dbo].[Meters] WHERE StandId IN (111896, 111897)"
        };

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            System.out.println("Starting direct cleanup...");
            for (String sql : cleanupStatements) {
                stmt.executeUpdate(sql);
            }
            System.out.println("Cleanup completed successfully.");

        } catch (SQLException e) {
            System.err.println("Error executing cleanup SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
