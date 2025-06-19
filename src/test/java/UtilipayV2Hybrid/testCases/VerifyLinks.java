package UtilipayV2Hybrid.testCases;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.DatabaseUtilsEnd2End;
import UtilipayV2Hybrid.utilities.LoginUtil;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;
import pageObject.TransactPage;


public class VerifyLinks extends Base{
	
    SoftAssert softAssert;
    HomePage hP;
    LoginPage lP;
    LoginUtil logIn;
    TransactPage transact;
    NavigationPage nav;
    DatabaseUtilsEnd2End checkLinks;

    @BeforeClass(alwaysRun = true)
    @Parameters({"Browser"})
    public void login(String browser) throws IOException {
    	
        hP = new HomePage(Base.getDriver());
        lP = new LoginPage(Base.getDriver());
        nav = new NavigationPage(Base.getDriver());
        checkLinks = new DatabaseUtilsEnd2End();

        if (getProp() == null) {
            prop = new Properties();
            FileInputStream fs = new FileInputStream("./src/test/resources/data.properties");
            prop.load(fs);
        }

        setUp(browser);

        logIn = new LoginUtil();
        logIn.adminLogIn();

        nav = new NavigationPage(getDriver());
        transact = new TransactPage(getDriver());
        
    }
    
    @Test
    public void checkUtilipayLinks() {
    	
    	checkLinks.checkForBrokenLinks();
    }

}
