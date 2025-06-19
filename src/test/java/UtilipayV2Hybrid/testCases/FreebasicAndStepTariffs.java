package UtilipayV2Hybrid.testCases;

import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.DatabaseUtilsEnd2End;
import pageObject.HomePage;
import pageObject.Import_ExportPage;
import pageObject.LoginPage;
import pageObject.MunicipalMaintenancePage;
import pageObject.NavigationPage;
import pageObject.TransactPage;
import pageObject.UserManagementPage;

public class FreebasicAndStepTariffs  extends Base{
	
    SoftAssert softAssert;
    HomePage hP;
    LoginPage lP;
    NavigationPage nav;
    Import_ExportPage importFile;
    MunicipalMaintenancePage mun;
    TransactPage tranPg;
    UnitsAndStepsValidation unitsAndSteps;
    EndToEnd purchase;
    
    @BeforeClass
    public void loginOnce() throws InterruptedException {
        softAssert = new SoftAssert();
        hP = new HomePage(Base.getDriver());
        lP = new LoginPage(Base.getDriver());
        nav = new NavigationPage(Base.getDriver());
        importFile = new Import_ExportPage(Base.getDriver());
        mun = new MunicipalMaintenancePage(Base.getDriver());
        tranPg = new TransactPage(Base.getDriver());
        unitsAndSteps = new UnitsAndStepsValidation();
        purchase = new EndToEnd();

        try {
            hP.click_Btn();
            lP.email(prop.getProperty("myEmail"));
            lP.pssWrd(prop.getProperty("myPassword"));
            lP.loginBtn();
            System.out.println("1. Login successful");
        } catch (Exception ex) {
            System.out.println("Login failed. Exception: " + ex.getMessage());
        }
    }
    
    @Test
    public void verifyFreebasicAndSteps() throws InterruptedException {
        try {
            String meterNumber = prop.getProperty("wtrMtr");
            boolean meterExists = false;
            int maxRetries = 10;
            int retryDelayMillis = 2000;

            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                if (purchase.checkMeterMeterBeforeVend(meterNumber)) {
                    meterExists = true;
                    break;
                }
                Thread.sleep(retryDelayMillis);
            }

            if (!meterExists) {
                Assert.fail("Meter does not exist after " + maxRetries + " attempts.");
            }

            performPurchasesAndValidate(meterNumber);

        } catch (Exception ex) {
            Assert.fail("End-to-end test failed: " + ex.getMessage());
        }
    }

    
    private void performPurchasesAndValidate(String meterNumber) throws InterruptedException, SQLException {
        SoftAssert softAssert = new SoftAssert();  

        nav.click_CashierManagement();
        nav.click_Transact();
        tranPg.insert_MtrNum(meterNumber);
        Thread.sleep(1000);
        tranPg.clickContinue();
        tranPg.enterAmnt("100");
        tranPg.clickBreakDown();

        String ppBreakdown = tranPg.getBreakDown();

        if (ppBreakdown == null || ppBreakdown.trim().isEmpty()) {
            try {
                String errorMsg = tranPg.errorMessage();

                if (errorMsg == null || errorMsg.trim().isEmpty()) {
                    softAssert.fail("Prepaid Breakdown is empty and no error message was displayed.");
                } else {
                    softAssert.fail("Prepaid Breakdown is empty. Error message displayed: " + errorMsg);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                softAssert.fail("Exception occurred while checking error message: " + ex.getMessage());
            }
        } else {
        	System.out.println("Transaction Breakdown: " + ppBreakdown);
            tranPg.paymentMethod("Cash");
            tranPg.purchase();
        }


        String meterId = DatabaseUtilsEnd2End.getMeterId(meterNumber);
        softAssert.assertNotNull(meterId, "Meter ID not found");

        DatabaseUtilsEnd2End.MeterTransactionInfo transactionInfo = DatabaseUtilsEnd2End.getLastStepAndUnits(meterId);
        softAssert.assertNotNull(transactionInfo, "Failed to fetch transaction info");

        if (transactionInfo != null) {
            String lastStep = String.valueOf(transactionInfo.getSteps());
            double units = transactionInfo.getUnits();

            System.out.println("Meter ID: " + meterId);
            System.out.println("Last Step: " + lastStep);
            System.out.println("Units: " + units);

            softAssert.assertEquals(units, 21.9, "Units received is not equal to 168.6. Found: " + units);

            String expectedStep = "6 kl @ R 1 / kl 4 kl @ R 3 / kl 11.9 kl @ R 4 / kl";

            String normalizedActualLastStep = lastStep.replaceAll("\\s+", " ").trim();
            String normalizedExpectedStep = expectedStep.replaceAll("\\s+", " ").trim();

            softAssert.assertEquals(normalizedActualLastStep, normalizedExpectedStep, 
                "Final step is not as expected. Found: " + normalizedActualLastStep);
        }
        
        softAssert.assertAll(); 
    }
    

}
