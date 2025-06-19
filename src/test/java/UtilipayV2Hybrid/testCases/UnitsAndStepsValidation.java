package UtilipayV2Hybrid.testCases;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import UtilipayV2Hybrid.testBase.Base;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;
import pageObject.TransactPage;

@Test
public class UnitsAndStepsValidation extends Base {

    @BeforeClass
    public void loginOnce() throws InterruptedException {
        HomePage hP = new HomePage(Base.getDriver());
        LoginPage lP = new LoginPage(Base.getDriver());
        NavigationPage nav = new NavigationPage(Base.getDriver());

        //hP.click_Btn();
        lP.email(prop.getProperty("myEmail"));
        lP.pssWrd(prop.getProperty("myPassword"));
        lP.loginBtn();

        nav.click_CashierManagement();
        nav.click_Transact();

        System.out.println("Login successful");
    }

    public void unitsValidation() throws InterruptedException {
        String meterId = String.valueOf(getMtrNoFrmDb(prop.getProperty("mtrNum")));
        if (meterId == null) {
            Assert.fail("Failed to retrieve Meter ID from the database");
            return;
        }

        String previousStepStr = getLastStepFromDB(meterId);
        if (previousStepStr == null || previousStepStr.trim().isEmpty()) {
            Assert.fail("Previous step is null or empty.");
            return;
        }

        double previousUnits = getUnitsFromDB(meterId);
        
        performPurchase(prop.getProperty("mtrNum"), false);
        performPurchase(prop.getProperty("mtrNum"), true);  

        String newStepStr = getLastStepFromDB(meterId);
        if (newStepStr == null || newStepStr.trim().isEmpty()) {
            Assert.fail("New step is null or empty.");
            return;
        }

        double newUnits = getUnitsFromDB(meterId);


        String previousStep = previousStepStr.trim();
        String newStep = newStepStr.trim();

        if (previousStep.isEmpty() || newStep.isEmpty()) {
            Assert.fail("Step values must not be empty.");
            return;
        }

        if (newStep.compareTo(previousStep) <= 0) {
            Assert.fail("Step validation failed: " + newStep + " <= " + previousStep);
        }

        Assert.assertTrue(newUnits > previousUnits,
                "Units validation failed: " + newUnits + " <= " + previousUnits);

        System.out.printf("Step: %s → %s | Units: %.2f → %.2f%n",
                previousStep, newStep, previousUnits, newUnits);


    }



    private void performPurchase(String meterId, boolean isSecondPurchase) throws InterruptedException {
        TransactPage tranPg = new TransactPage(Base.getDriver());

        tranPg.insert_MtrNum(meterId);
        Thread.sleep(1000);
        tranPg.clickContinue();
        
        if (isSecondPurchase) {
        	
        	tranPg.enterAmnt2("10");
	        tranPg.clickBreakDown();
	        Thread.sleep(2000);
	        tranPg.paymentMethod("Credit Card");  
	        tranPg.purchase2();
        } else {
        	
          tranPg.enterAmnt("100");
          tranPg.clickBreakDown();
          tranPg.paymentMethod("Cash");
          tranPg.purchase();
//          tranPg.clearFields();
        }

        Thread.sleep(5000);
    }

    private static Properties getDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream input = UnitsAndStepsValidation.class.getClassLoader().getResourceAsStream("data.properties")) {
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

    public static String getMtrNoFrmDb(String meterNumber) {
        Properties dbProperties = getDatabaseProperties();
        if (dbProperties == null) {
            System.err.println("Database properties could not be loaded!");
            return null;
        }

        String jdbcUrl = dbProperties.getProperty("DB_CONNECTION_STRING");
        String dbUsername = dbProperties.getProperty("DB_USERNAME");
        String dbPassword = dbProperties.getProperty("DB_PASSWORD");

        if (jdbcUrl == null || dbUsername == null || dbPassword == null) {
            System.err.println("Database credentials are missing in properties file!");
            return null;
        }

        String meterId = null;
        String query = "SELECT Id FROM Meters WHERE MeterNumber = ?";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            System.out.println("Connected to the database successfully!.");
            stmt.setString(1, meterNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    meterId = String.valueOf(rs.getObject("Id"));
                    System.out.println("Meter ID Retrieved: " + meterId);
                } else {
                    System.err.println("No Meter ID found for Meter Number: " + meterNumber);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Meter ID: " + e.getMessage());
            e.printStackTrace();
        }

        return meterId;
    }

    private String getLastStepFromDB(String meterId) {
        Properties dbProperties = getDatabaseProperties();
        if (dbProperties == null) {
            System.err.println("Database properties could not be loaded!");
            return null;
        }

        String jdbcUrl = dbProperties.getProperty("DB_CONNECTION_STRING");
        String dbUsername = dbProperties.getProperty("DB_USERNAME");
        String dbPassword = dbProperties.getProperty("DB_PASSWORD");

        if (jdbcUrl == null || dbUsername == null || dbPassword == null) {
            System.err.println("Database credentials are missing in properties file!");
            return null;
        }


        if (meterId == null || meterId.trim().isEmpty()) {
            System.err.println("Invalid Meter ID: " + meterId);
            return null;
        }

        String query = "SELECT TOP 1 ISNULL(Steps, 0) AS Steps FROM TransactionDetails "
                     + "WHERE TransactionId = (SELECT TOP 1 Id FROM Transactions "
                     + "WHERE MeterId = ? ORDER BY TransactionDate DESC) "
                     + "ORDER BY Id DESC";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, meterId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String lastStep = rs.getString("Steps");
                    System.out.println("Last Step Retrieved: " + lastStep);
                    return lastStep;
                } else {
                    System.err.println("No steps found for Meter ID: " + meterId);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getLastStepFromDB: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    private double getUnitsFromDB(String meterId) {
        Properties dbProperties = getDatabaseProperties();
        if (dbProperties == null) {
            System.err.println("Database properties could not be loaded!");
            return 0.0;
        }

        String jdbcUrl = dbProperties.getProperty("DB_CONNECTION_STRING");
        String dbUsername = dbProperties.getProperty("DB_USERNAME");
        String dbPassword = dbProperties.getProperty("DB_PASSWORD");

        if (jdbcUrl == null || dbUsername == null || dbPassword == null) {
            System.err.println("Database credentials are missing in properties file!");
            return 0.0;
        }

        String query = "SELECT TOP 1 Units FROM TransactionDetails "
                     + "WHERE TransactionId = (SELECT TOP 1 Id FROM Transactions "
                     + "WHERE MeterId = ? ORDER BY TransactionDate DESC) "
                     + "ORDER BY Id DESC";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, meterId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double units = rs.getDouble("Units");
                    System.out.println("Units Retrieved: " + units);
                    return units;
                } else {
                    System.err.println("No units found for Meter ID: " + meterId);
                    return 0.0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching units: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }
}
