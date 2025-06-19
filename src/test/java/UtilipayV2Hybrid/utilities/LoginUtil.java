package UtilipayV2Hybrid.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.HomePage;
import pageObject.LoginPage;

public class LoginUtil extends Base {

    private HomePage hP;
    private LoginPage lP;

    // Constructor to ensure properties are loaded
    public LoginUtil() {
        if (getProp() == null) {
            try {
                prop = new Properties();
                FileInputStream fs = new FileInputStream("./src/test/resources/data.properties");
                prop.load(fs);
                System.out.println("Properties loaded successfully in LoginUtil.");
            } catch (IOException e) {
                System.out.println("Failed to load properties in LoginUtil: " + e.getMessage());
            }
        } else {
            prop = getProp(); // Use the one loaded in Base if available
        }
    }

    public void adminLogIn() {
        initPages();
        login(prop.getProperty("myEmail"), prop.getProperty("myPassword"));
    }

    public void cashierLogIn() {
        initPages();
        login(prop.getProperty("cashierEmail"), prop.getProperty("cashierPassword"));
    }

    private void initPages() {
        hP = new HomePage(Base.getDriver());
        lP = new LoginPage(Base.getDriver());
    }

    private void login(String email, String password) {
        try {
            hP.click_Btn();
            lP.email(email);
            lP.pssWrd(password);
            lP.loginBtn();
            System.out.println("Login successful: " + email);
        } catch (Exception ex) {
            System.out.println("Login failed for " + email + ". Exception: " + ex.getMessage());
        }
    }

    public HomePage getHomePage() {
        return hP;
    }

    public LoginPage getLoginPage() {
        return lP;
    }
}
