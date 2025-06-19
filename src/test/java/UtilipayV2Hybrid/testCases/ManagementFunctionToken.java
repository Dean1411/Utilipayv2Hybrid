package UtilipayV2Hybrid.testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.EngineeringPage;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.ManagementFunctionTokenPage;
import pageObject.NavigationPage;


@Test (groups= {"Regression"})
public class ManagementFunctionToken extends Base{
	
	public void generateManagementFunctionToken() {
		
		HomePage hP = new HomePage(Base.getDriver());
		LoginPage lP = new LoginPage(Base.getDriver());
		NavigationPage nav = new NavigationPage(Base.getDriver());
		EngineeringPage eP = new EngineeringPage(Base.getDriver());
		ManagementFunctionTokenPage mFT = new ManagementFunctionTokenPage(Base.getDriver());
		
		
		logger.info("***Click Developer/Tester Login***");
		//hP.click_Btn();
		
		logger.info("***Enter login credentials/Click Login***");
		lP.email(prop.getProperty("myEmail"));
		lP.pssWrd(prop.getProperty("myPassword"));		
		lP.loginBtn();
		
		logger.info("***Navigate to Engineering Page***");
		nav.nav_Engineering();
		nav.click_Engineering();
		
		eP.clickOption("management function token");
		
		mFT.enterMeterNumber(prop.getProperty("meterNumber"));
		mFT.selectSubClass("Clear Tamper Condition");
		mFT.supplyGroupCode(prop.getProperty("sgcNew"));
		mFT.keyRevisionNum(prop.getProperty("newkrn"));
		mFT.encryptAlgorithm(1);
		mFT.clickAllowKrnUpdate();
		mFT.clickSubmit();
		
		String managementToken = mFT.getModalBody();
		
		if(!managementToken.isEmpty()) {
			
		    System.out.println("Transaction Breakdown: " + managementToken);

		    Assert.assertTrue(true);
		}else {
			
			Assert.fail("Unable to generate Management Token");
		}
	}

}
