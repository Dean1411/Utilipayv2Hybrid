package UtilipayV2Hybrid.testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;
import pageObject.ReportBuilderPage;

public class Reporting extends Base {

    private ReportBuilderPage rB;

    @BeforeClass
    public void setupReportingModule() {
        logger.info("*** Setting up Reporting Test ***");

        HomePage hP = new HomePage(Base.getDriver());
        LoginPage lP = new LoginPage(Base.getDriver());
        NavigationPage nav = new NavigationPage(Base.getDriver());

        logger.info("*** Logging in ***");
        // hP.click_Btn(); // Uncomment if Developer/Tester login is required
        lP.email(prop.getProperty("myEmail"));
        lP.pssWrd(prop.getProperty("myPassword"));
        lP.loginBtn();

        logger.info("*** Navigating to Reporting Section ***");
        nav.click_Reporting();

        rB = new ReportBuilderPage(Base.getDriver());
    }

    @Test(groups = { "Regression" })
    public void generateAllReportsAndAssertStatusMessages() {
        String[] reportTypes = {
            "custom report",
            "day end",
            "month end",
            "low purchase",
            "free basic",
            "arrears recovered",
            "prepaid sales"
        };

        SoftAssert softAssert = new SoftAssert();

        for (String report : reportTypes) {
            logger.info("=== Generating report: " + report + " ===");

            try {
                rB.selectReport(report);

                String statusMsg = rB.statusMessage();
                logger.info("Status message for [" + report + "]: " + statusMsg);

                String expectedMsg;
                if (report.equalsIgnoreCase("custom report")) {
                    expectedMsg = "Preset successfully saved!";
                } else {
                    expectedMsg = "Your report is being prepared. you will receive an email ones it is ready!";
                }

                softAssert.assertNotNull(
                    statusMsg,
                    "Status message is null for report: [" + report + "]"
                );

                softAssert.assertEquals(
                    statusMsg,
                    expectedMsg,
                    "Unexpected status message for report: [" + report + "]"
                );

            } catch (Exception e) {
                logger.error("Exception for report [" + report + "]: " + e.getMessage());
                softAssert.fail("Exception while processing report [" + report + "]: " + e.getMessage());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        softAssert.assertAll();
        logger.info("*** Reporting Test Completed ***");
    }


}
