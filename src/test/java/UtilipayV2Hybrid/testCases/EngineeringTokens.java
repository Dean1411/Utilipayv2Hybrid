package UtilipayV2Hybrid.testCases;

import org.testng.annotations.Test;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.EncryptCreditToken;
import pageObject.EngineeringPage;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;

public class EngineeringTokens extends Base{
	
	
	@Test (groups= {"Regression"})
	public void generateEngineeringTokens() {
		
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
		
		eP.clickOption("key change token");
		
		//eP.processEncryptCreditToken(currentBrowserName);
		
		
	}

}
