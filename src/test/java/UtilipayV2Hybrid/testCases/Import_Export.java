package UtilipayV2Hybrid.testCases;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.Retry;
import pageObject.HomePage;
import pageObject.Import_ExportPage;
import pageObject.LoginPage;
import pageObject.NavigationPage;

public class Import_Export extends Base{
	
	
	@Parameters({"Browser"})
	@Test (groups= {"Regression"}, retryAnalyzer = Retry.class)
	public void importTest() throws InterruptedException {
		
		SoftAssert softAssert = new SoftAssert();
		HomePage hP = new HomePage(Base.getDriver());
		LoginPage lP = new LoginPage(Base.getDriver());
		NavigationPage nav = new NavigationPage(Base.getDriver());
		Import_ExportPage importExport = new Import_ExportPage(Base.getDriver());
		
		logger.info("***Click Developer/Tester Login***");
		hP.click_Btn();
		
		logger.info("***Enter login credentials/Click Login***");
		lP.email(prop.getProperty("myEmail"));
		lP.pssWrd(prop.getProperty("myPassword"));		
		lP.loginBtn();
		
		nav.click_Admin();
		nav.navigateTo("Prepaid Import");
		
		importExport.option("Import");
		
		Thread.sleep(100);
	    String msg = importExport.getMsg(); 

	    logger.info("***Verifying toaster message***");

	    try {
		    if (msg == null || msg.isEmpty()) {
		        softAssert.fail("Toaster message is not visible or is empty.");
		    } else {
		        softAssert.assertTrue(
		            msg.contains("File uploaded successfully"),
		            "Toaster message does not contain expected text. Actual: " + msg
		        );
		        
		        System.out.println("Toaster Message: " + msg);
		    }
	    }catch(Exception ex) {
	    	
	    	System.out.println("Exception: " + ex);
	    }
	    
	    Thread.sleep(3000);
	    softAssert.assertAll();
	}

}
