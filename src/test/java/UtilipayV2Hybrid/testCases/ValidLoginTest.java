package UtilipayV2Hybrid.testCases;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.HomePage;
import pageObject.LoginPage;

public class ValidLoginTest extends Base {
		
	@Test (groups= {"Regression"}, retryAnalyzer = UtilipayV2Hybrid.utilities.Retry.class)
	public void valid_Login() {
		logger.info("***Starting Valid Login Test***");
		
		SoftAssert softAssert = new SoftAssert();
		HomePage hP = new HomePage(Base.getDriver());
		LoginPage lP = new LoginPage(Base.getDriver());
		
		logger.info("***Click Developer/Tester Login***");
		//hP.click_Btn();
		
		logger.info("***Enter login credentials/Click Login***");
		lP.email(prop.getProperty("myEmail"));
		lP.pssWrd(prop.getProperty("myPassword"));
		lP.loginBtn();
		
		hP.click_Profile();
		
		
		String loginSuccess = hP.getSuccessLoggedInText();
		
		if (!loginSuccess.isEmpty()) {
			
			System.out.println("Profile: " + loginSuccess);
			Assert.assertTrue(true); 
		}else {
			
			Assert.fail();
			System.out.println();
		}
		
	
	}		
}
