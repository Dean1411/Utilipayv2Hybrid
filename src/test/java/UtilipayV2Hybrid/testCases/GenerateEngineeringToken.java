package UtilipayV2Hybrid.testCases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.EncryptCreditToken;
import pageObject.EngineeringPage;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;

public class GenerateEngineeringToken extends Base {
	
	@Test (groups= {"Regression"})
	public void generate_Engineering_Token() {
		
		try {
			logger.info("***Starting Generate Engineering token Test***");
			
			HomePage hP = new HomePage(Base.getDriver());
			LoginPage lP = new LoginPage(Base.getDriver());
			NavigationPage nav = new NavigationPage(Base.getDriver());
			EngineeringPage eP = new EngineeringPage(Base.getDriver());
			EncryptCreditToken ect = new EncryptCreditToken(Base.getDriver());
			
			logger.info("***Click Developer/Tester Login***");
			//hP.click_Btn();
			
			logger.info("***Enter login credentials/Click Login***");
			lP.email(prop.getProperty("myEmail"));
			lP.pssWrd(prop.getProperty("myPassword"));		
			lP.loginBtn();
			
			logger.info("***Navigate to Engineering Page***");
			nav.nav_Engineering();
			nav.click_Engineering();
			
			eP.clickOption("encrypt credit token");
			
			logger.info("***Enter parameters and generate token***");
			ect.enterMeterNumber(prop.getProperty("mtrNum"));
			ect.selectSubClass();
			ect.insertSGC(prop.getProperty("sgc"));
			ect.insertKrn(prop.getProperty("krn"));
			ect.selectEncryptionAlgorithm();
			ect.enterTransferAmount(prop.getProperty("tranAmount"));
			ect.disableKrn();
			ect.clickSubmit();
			ect.okConfirmation();
			
			
			WebDriverWait wait = new WebDriverWait(Base.getDriver(), Duration.ofSeconds(8));
	        WebElement crTkn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='engResult']/div/div/div"))); 

	        if (crTkn != null && !crTkn.getText().trim().isEmpty()) {
	            System.out.println("Results: " + crTkn.getText());
	            Assert.assertTrue(true);
	        } else {
	            System.out.println("No Token Generated");
	            Assert.fail("No Token Generated");  
	        }
		}catch(Exception e) {			
			System.out.println(e);
		}		
	}	
}
