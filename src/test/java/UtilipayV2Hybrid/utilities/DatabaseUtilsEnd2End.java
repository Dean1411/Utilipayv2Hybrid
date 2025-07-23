package UtilipayV2Hybrid.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import UtilipayV2Hybrid.testBase.Base;

public class DatabaseUtilsEnd2End extends Base{

    private static Properties getDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseUtils.class.getClassLoader().getResourceAsStream("data.properties")) {
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

    public static String getMeterId(String meterNumber) {
        Properties props = getDatabaseProperties();
        if (props == null) return null;

        String url = props.getProperty("DB_CONNECTION_STRING");
        String user = props.getProperty("DB_USERNAME");
        String pass = props.getProperty("DB_PASSWORD");

        String query = "SELECT Id FROM Meters WHERE MeterNumber = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, meterNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting Meter ID: " + e.getMessage());
        }
        return null;
    }
    
//    public static boolean municipalityExists(String municipalityName) {
//        Properties props = getDatabaseProperties();
//        if (props == null) return false;
//
//        String url = props.getProperty("DB_CONNECTION_STRING");
//        String user = props.getProperty("DB_USERNAME");
//        String pass = props.getProperty("DB_PASSWORD");
//
//        String query = "SELECT 1 FROM dbo.Municipality WHERE Name = ?";
//        try (Connection conn = DriverManager.getConnection(url, user, pass);
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            stmt.setString(1, municipalityName);
//            ResultSet rs = stmt.executeQuery();
//
//            return rs.next(); 
//        } catch (SQLException e) {
//            System.err.println("Error checking municipality existence: " + e.getMessage());
//        }
//        return false;
//    }
    
    public static boolean deleteMunicipalityIfExists(String municipalityName) {
        Properties props = getDatabaseProperties();
        if (props == null) return false;

        String url = props.getProperty("DB_CONNECTION_STRING");
        String user = props.getProperty("DB_USERNAME");
        String pass = props.getProperty("DB_PASSWORD");

        String checkQuery = "SELECT 1 FROM dbo.Municipality WHERE Name = ?";
        String deleteQuery = "DELETE FROM dbo.Municipality WHERE Name = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            // Check if the municipality exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, municipalityName);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // It exists, so delete it
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                        deleteStmt.setString(1, municipalityName);
                        int rowsAffected = deleteStmt.executeUpdate();
                        System.out.println("Municipality deleted: " + rowsAffected + " row(s) affected.");
                        return true;
                    }
                } else {
                    System.out.println("Municipality does not exist: " + municipalityName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting municipality: " + e.getMessage());
        }
        return false;
    }




    public static String getLastStep(String meterId) {
        Properties props = getDatabaseProperties();
        if (props == null) return null;

        String url = props.getProperty("DB_CONNECTION_STRING");
        String user = props.getProperty("DB_USERNAME");
        String pass = props.getProperty("DB_PASSWORD");

        String query = "SELECT TOP 1 ISNULL(Steps, 0) AS Steps FROM TransactionDetails "
                     + "WHERE TransactionId = (SELECT TOP 1 Id FROM Transactions WHERE MeterId = ? ORDER BY TransactionDate DESC) "
                     + "ORDER BY Id DESC";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, meterId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Steps");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching last step: " + e.getMessage());
        }
        return null;
    }

    public static double getLastUnits(String meterId) {
        Properties props = getDatabaseProperties();
        if (props == null) return 0.0;

        String url = props.getProperty("DB_CONNECTION_STRING");
        String user = props.getProperty("DB_USERNAME");
        String pass = props.getProperty("DB_PASSWORD");

        String query = "SELECT TOP 1 Units FROM TransactionDetails "
                     + "WHERE TransactionId = (SELECT TOP 1 Id FROM Transactions WHERE MeterId = ? ORDER BY TransactionDate DESC) "
                     + "ORDER BY Id DESC";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, meterId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("Units");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching units: " + e.getMessage());
        }
        return 0.0;
    }
    
    public static class MeterTransactionInfo {
    	private String steps;
        private double units;

        public MeterTransactionInfo(String steps, double units) {
            this.steps = steps;
            this.units = units;
        }

        public String getSteps() {
            return steps;
        }

        public double getUnits() {
            return units;
        }
    }

    public static MeterTransactionInfo getLastStepAndUnits(String meterId) {
        Properties props = getDatabaseProperties();
        if (props == null) return null;

        String url = props.getProperty("DB_CONNECTION_STRING");
        String user = props.getProperty("DB_USERNAME");
        String pass = props.getProperty("DB_PASSWORD");

        String query = "SELECT TOP 1 Units, Steps FROM TransactionDetails "
                     + "WHERE TransactionId = (SELECT TOP 1 Id FROM Transactions WHERE MeterId = ? ORDER BY TransactionDate DESC) "
                     + "ORDER BY Id DESC";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, meterId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String steps = rs.getString("Steps");  
                double units = rs.getDouble("Units");

                return new MeterTransactionInfo(steps, units); 
            }
        } catch (SQLException e) {
            System.err.println("Error fetching last step and units: " + e.getMessage());
        }
        return null;
    }
    
    public void checkForBrokenLinks() {
        List<WebElement> allLinks = driver.get().findElements(By.tagName("a"));

        System.out.println("All links on webpage: " + allLinks.size());

        for (WebElement link : allLinks) {
            String url = link.getAttribute("href");
            
            System.out.println("+++++++++++++++++++++++++++");

            // Skip empty or javascript links
            if (url == null || url.isEmpty()) {
                System.out.println("URL is empty, skipping.");
                continue;
            }

            if (url.startsWith("javascript:")) {
                System.out.println("Javascript link found, skipping: " + url);
                continue;
            }

            try {
                HttpURLConnection urlRequest = (HttpURLConnection)(new URL(url).openConnection());
                urlRequest.connect();

                if (urlRequest.getResponseCode() >= 400) {
                    System.out.println(url + " is broken.");
                } else {
                    System.out.println(url + " is valid.");
                }

            } catch (IOException e) {
                System.out.println("Error checking URL: " + url);
                e.printStackTrace();
            }
        }
    }
    
    public static boolean doesMeterExist(String meterNumber) {
        Properties props = getDatabaseProperties();
        if (props == null) return false;

        String url = props.getProperty("DB_CONNECTION_STRING");
        String user = props.getProperty("DB_USERNAME");
        String pass = props.getProperty("DB_PASSWORD");

        String query = "SELECT MeterNumber FROM Meters WHERE MeterNumber = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, meterNumber);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // returns true if a result exists

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void updateTransactionStatusToPending(String meterId) {
        Properties props = getDatabaseProperties();
        if (props == null) return;

        String url = props.getProperty("DB_CONNECTION_STRING");
        String user = props.getProperty("DB_USERNAME");
        String pass = props.getProperty("DB_PASSWORD");

        String query = "UPDATE [dbo].[Transactions] SET Status = 2 WHERE MeterId = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, meterId);
            int rowsUpdated = stmt.executeUpdate();

            System.out.println("Updated " + rowsUpdated + " transaction(s) to Status = 2 for MeterId: " + meterId);
        } catch (SQLException e) {
            System.err.println("Error updating transaction status: " + e.getMessage());
        }
    }

}

