package UtilipayV2Hybrid.testCases;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.DatabaseUtilsEnd2End;
import UtilipayV2Hybrid.utilities.LoginUtil;
import pageObject.NavigationPage;
import pageObject.TransactPage;

public class Verify_StepThrough_PrepaidTariffUnits_WithFreeBasic extends Base {

    SoftAssert softAssert;
    LoginUtil logIn;
    TransactPage transact;
    NavigationPage nav;

    @BeforeClass(alwaysRun = true)
    @Parameters({"Browser", "baseType"})
    public void login(String browser, @Optional("prepaid") String baseType) throws IOException {
        if (getProp() == null) {
            prop = new Properties();
            FileInputStream fs = new FileInputStream("./src/test/resources/data.properties");
            prop.load(fs);
        }

        setUp(browser, baseType);

        logIn = new LoginUtil();
        logIn.adminLogIn();

        nav = new NavigationPage(getDriver());
        transact = new TransactPage(getDriver());
    }

    @Test(retryAnalyzer = UtilipayV2Hybrid.utilities.Retry.class)
    public void verifyUnitsAndFreeBasic() throws InterruptedException, SQLException {
        String meterNumber = prop.getProperty("wtrMtr");
        SoftAssert softAssert = new SoftAssert();

        double[] expectedUnits = { 22.8, 26.1, 30.5 };
        String[] expectedSteps = {
            "4 kl @ R 3 / kl 18.8 kl @ R 4 / kl",
            "26.1 kl @ R 4 / kl",
            "30.5 kl @ R 4 / kl"
        };

        for (int i = 0; i < 3; i++) {
            int purchaseNumber = i + 1;
            int purchaseAmount = 100 + (i * 20);
            System.out.println("Purchase " + purchaseNumber);

            nav.click_CashierManagement();
            nav.click_Transact();
            transact.insert_MtrNum(meterNumber);
            Thread.sleep(1000);
            transact.clickContinue();
            transact.enterAmnt(String.valueOf(purchaseAmount));
            transact.clickBreakDown();

            String ppBreakdown = transact.getBreakDown();

            if (ppBreakdown == null || ppBreakdown.trim().isEmpty()) {
                try {
                    String errorMsg = transact.errorMessage();

                    if (errorMsg == null || errorMsg.trim().isEmpty()) {
                        softAssert.fail("Purchase " + purchaseNumber + ": Breakdown is empty and no error message displayed.");
                    } else {
                        softAssert.fail("Purchase " + purchaseNumber + ": Breakdown is empty. Error: " + errorMsg);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    softAssert.fail("Purchase " + purchaseNumber + ": Exception while checking error: " + ex.getMessage());
                }
            } else {
                System.out.println("Breakdown for Purchase " + purchaseNumber + ": " + ppBreakdown);
                transact.paymentMethod("Cash");
                transact.purchase();

                //Check free basic token immediately after first purchase
                if (i == 0) {
                    String freeBasicToken = transact.fbToken();
                    if (freeBasicToken == null) {
                        softAssert.fail("First purchase: Free basic token is missing.");
                    } else if (freeBasicToken.contains("Free basic Credit")) {
                        System.out.println("First purchase: Free basic token = " + freeBasicToken);
                    } else {
                        softAssert.fail("First purchase: Free basic token does not contain expected partial text.");
                    }
                }

                //Free basic should NOT appear on purchases 2 and 3
                if (i > 0) {
                    String freeBasicToken = transact.fbToken();
                    if (freeBasicToken != null) {
                        softAssert.fail("Purchase " + purchaseNumber + ": Unexpected free basic token on a non-first purchase.");
                    }
                }

                String meterId = DatabaseUtilsEnd2End.getMeterId(meterNumber);
                softAssert.assertNotNull(meterId, "Purchase " + purchaseNumber + ": Meter ID not found");

                DatabaseUtilsEnd2End.MeterTransactionInfo transactionInfo = DatabaseUtilsEnd2End.getLastStepAndUnits(meterId);
                softAssert.assertNotNull(transactionInfo, "Purchase " + purchaseNumber + ": Failed to get transaction info");

                if (transactionInfo != null) {
                    String actualStep = String.valueOf(transactionInfo.getSteps()).replaceAll("\\s+", " ").trim();
                    double actualUnits = transactionInfo.getUnits();

                    System.out.println("Purchase " + purchaseNumber + ": Units = " + actualUnits + ", Step = " + actualStep);

                    String expectedStep = expectedSteps[i].replaceAll("\\s+", " ").trim();
                    double expectedUnit = expectedUnits[i];

                    softAssert.assertEquals(actualUnits, expectedUnit,
                        "Purchase " + purchaseNumber + ": Units mismatch. Expected: " + expectedUnit + ", Found: " + actualUnits);

                    softAssert.assertEquals(actualStep, expectedStep,
                        "Purchase " + purchaseNumber + ": Step mismatch. Expected: " + expectedStep + ", Found: " + actualStep);
                }
            }

            Thread.sleep(500);
        }

        String meterId = DatabaseUtilsEnd2End.getMeterId(meterNumber);
        DatabaseUtilsEnd2End.updateTransactionStatusToPending(meterId);

        softAssert.assertAll();
    }
}
