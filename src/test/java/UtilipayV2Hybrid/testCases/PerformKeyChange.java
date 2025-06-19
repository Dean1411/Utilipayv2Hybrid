package UtilipayV2Hybrid.testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.EngineeringPage;
import pageObject.HomePage;
import pageObject.KeyChangeToken;
import pageObject.LoginPage;
import pageObject.NavigationPage;

public class PerformKeyChange extends Base{
	
	@Test (groups= {"Regression"})
	public void perform_Key_Change() {
		
		HomePage hP = new HomePage(Base.getDriver());
		LoginPage lP = new LoginPage(Base.getDriver());
		NavigationPage nav = new NavigationPage(Base.getDriver());
		EngineeringPage eP = new EngineeringPage(Base.getDriver());
		KeyChangeToken kct = new KeyChangeToken(Base.getDriver());
		
		logger.info("***Click Developer/Tester Login***");
		hP.click_Btn();
		
		logger.info("***Enter login credentials/Click Login***");
		lP.email(prop.getProperty("myEmail"));
		lP.pssWrd(prop.getProperty("myPassword"));		
		lP.loginBtn();
		
		logger.info("***Navigate to Engineering Page***");
		nav.nav_Engineering();
		nav.click_Engineering();
		
		eP.clickOption("key change token");
		
		kct.meterNum(prop.getProperty("meterNumber"));
		kct.supplyGroupCode(prop.getProperty("sgc"));
		kct.supplyGroupCodeNew(prop.getProperty("sgcNew"));
		kct.keyRevisionNum(prop.getProperty("newkrn"));
		kct.keyRevisionNumNew(prop.getProperty("newkrn"));
		kct.tariffIndexNew(prop.getProperty("newtariffindex"));
		kct.EncryptAlgorithm();
		kct.clickAllowKrnUpdate();
		kct.clickSubmit();
		kct.getModelBody();
		
		if (!kct.getModelBody().isEmpty()) {
			
		    System.out.println("Transaction Breakdown: " + kct.getModelBody());

		    Assert.assertTrue(true);
		}else {
			
			Assert.fail("Unable to perform key change");
		}
	}

}
