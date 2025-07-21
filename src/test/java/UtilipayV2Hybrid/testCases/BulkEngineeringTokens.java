package UtilipayV2Hybrid.testCases;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.LoginUtil;
import UtilipayV2Hybrid.utilities.Retry;
import pageObject.EngineeringPage;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;

public class BulkEngineeringTokens extends Base {
    private String validityMsg;

    @Test(groups = { "Regression" }, retryAnalyzer = Retry.class)
    public void generateEngineeringTokens() {

        logger.info("***Starting Generate Engineering token Test***");

        LoginUtil loginUtil = new LoginUtil();
        loginUtil.adminLogIn();

        HomePage hP = loginUtil.getHomePage();
        LoginPage lP = loginUtil.getLoginPage();
        NavigationPage nav = new NavigationPage(Base.getDriver());
        EngineeringPage eP = new EngineeringPage(Base.getDriver());

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

        // Expected modal messages
        String expectedFirstMsg = "CSV file is valid. Please click 'Process File' to continue.";
        String expectedSecondMsg = "Your file has been successfully uploaded. You will receive an email notification once the processing is complete.";

        SoftAssert softAssert = new SoftAssert();

        for (String option : engineeringOption) {
            logger.info("Processing: " + option);

            validityMsg = eP.clickOption(option);
            System.out.println("Validity message for " + option + ": " + validityMsg);

            softAssert.assertNotNull(validityMsg, "Validity message is null for option: " + option);

            String[] messages = validityMsg.split("\\|");

            String firstMsg = messages.length > 0 ? messages[0].trim() : "";
            String secondMsg = messages.length > 1 ? messages[1].trim() : "";

            softAssert.assertTrue(firstMsg.contains(expectedFirstMsg),
                    "First modal message invalid for option: " + option + ". Found: " + firstMsg);
            softAssert.assertTrue(secondMsg.contains(expectedSecondMsg),
                    "Second modal message invalid for option: " + option + ". Found: " + secondMsg);

            nav.click_Engineering();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        softAssert.assertAll();
    }
}
