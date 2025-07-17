package UtilipayV2Hybrid.testCases;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.Retry;
import pageObject.HomePage;
import pageObject.Import_ExportPage;
import pageObject.LoginPage;
import pageObject.NavigationPage;

public class Import_Export extends Base {

    @Parameters({"Browser"})
    @Test(groups = {"Regression"}, retryAnalyzer = Retry.class)
    public void importExportTest() throws InterruptedException {

        SoftAssert softAssert = new SoftAssert();

        // Page object initialization
        //HomePage homePage = new HomePage(Base.getDriver());
        LoginPage loginPage = new LoginPage(Base.getDriver());
        NavigationPage navigationPage = new NavigationPage(Base.getDriver());
        Import_ExportPage importExportPage = new Import_ExportPage(Base.getDriver());

        logger.info("*** Entering login credentials and logging in ***");
        loginPage.email(prop.getProperty("myEmail"));
        loginPage.pssWrd(prop.getProperty("myPassword"));
        loginPage.loginBtn();

        logger.info("*** Navigating to Admin section ***");
        navigationPage.click_Admin();

        String importType = "Export";
        String navTarget = "Import".equals(importType) ? "Prepaid Import" 
        		: "Prepaid Export";
        navigationPage.navigateTo(navTarget);


        importExportPage.option(importType);
        Thread.sleep(100);

        String actualMsg = importExportPage.getMsg();
        String expectedMsg = "Import".equals(importType)
                ? "File uploaded successfully"
                : "File Exported successfully";

        logger.info("*** Verifying toaster message ***");
        if (actualMsg == null || actualMsg.isEmpty()) {
            softAssert.fail("Toaster message is not visible or is empty.");
        } else {
            softAssert.assertTrue(
                    actualMsg.contains(expectedMsg),
                    "Toaster message does not contain expected text. Actual: " + actualMsg
            );
            System.out.println("Toaster Message: " + actualMsg);
        }

        softAssert.assertAll();
    }
}
