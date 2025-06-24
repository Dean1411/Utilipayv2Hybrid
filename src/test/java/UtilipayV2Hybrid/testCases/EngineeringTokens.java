package UtilipayV2Hybrid.testCases;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.EncryptCreditToken;
import pageObject.EngineeringPage;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;

public class EngineeringTokens extends Base{
	private String validityMsg;
	
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
		
		String[] engineeringOption = {
				
		        "encrypt credit token",
		        "verify encrypted token",
		        "manufacturing token",
		        "manufacturing key change token",
		        "manufacturing mngt function token",
		        "key change token",
		        "management function token"
		};
		
        String expectedMsg = "Your file has been successfully uploaded. You will receive an email notification once the processing is complete.";
        SoftAssert softAssert = new SoftAssert();

        for (String option : engineeringOption) {
            logger.info("Processing: " + option);
            
            String validityMsg = (String) eP.clickOption(option); 
            System.out.println("Validity message for " + option + ": " + validityMsg);

            softAssert.assertNotNull(validityMsg, "Validity message is null for option: " + option);
            softAssert.assertEquals(validityMsg, expectedMsg, "Invalid message for: " + option);

            nav.click_Engineering(); 

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        softAssert.assertAll();
				
	}

}
