package UtilipayV2Hybrid.testCases;

import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.DatabaseUtilsEnd2End;
import UtilipayV2Hybrid.utilities.Retry;
import pageObject.HomePage;
import pageObject.Import_ExportPage;
import pageObject.LoginPage;
import pageObject.MunicipalMaintenancePage;
import pageObject.NavigationPage;
import pageObject.TransactPage;
import pageObject.UserManagementPage;


public class RegressionTesting extends Base {

    SoftAssert softAssert;
    HomePage hP;
    LoginPage lP;
    NavigationPage nav;
    Import_ExportPage importFile;
    MunicipalMaintenancePage mun;
    TransactPage tranPg;
    UserManagementPage usrMng;
    UnitsAndStepsValidation unitsAndSteps;
    boolean fileImported = false;
    String municipalityName;


    @BeforeClass
    public void loginOnce() throws InterruptedException {
        softAssert = new SoftAssert();
        hP = new HomePage(Base.getDriver());
        lP = new LoginPage(Base.getDriver());
        nav = new NavigationPage(Base.getDriver());
        importFile = new Import_ExportPage(Base.getDriver());
        mun = new MunicipalMaintenancePage(Base.getDriver());
        tranPg = new TransactPage(Base.getDriver());
        usrMng = new UserManagementPage(Base.getDriver());
        unitsAndSteps = new UnitsAndStepsValidation();

        try {
        	//login
            lP.email(prop.getProperty("myEmail"));
            lP.pssWrd(prop.getProperty("myPassword"));
            lP.loginBtn();
            System.out.println("1. Login successful");
        } catch (Exception ex) {
            System.out.println("Login failed. Exception: " + ex.getMessage());
        }
    }
    
    @Test(groups= {"Regression"}, retryAnalyzer= Retry.class)
    public void regression() throws InterruptedException {
        try {
            municipalityName = prop.getProperty("regressionMun");
            createMunicipalityIfNotExists(municipalityName);
            configureVendingChannelAndCommission(municipalityName);
            
            Thread.sleep(1000);

            //importFile();
            createTariffAndSteps(municipalityName);
            
            
            //Perform purchase below on meter after import.
            
//            String meterNumber = prop.getProperty("wtrMtr");
//            boolean meterExists = false;
//            int maxRetries = 10;
//            int retryDelayMillis = 2000; 
//
//            for (int attempt = 1; attempt <= maxRetries; attempt++) {
//                if (checkMeterMeterBeforeVend(meterNumber)) {
//                    meterExists = true;
//                    break;
//                }
//
//                System.out.println("Attempt " + attempt + ": Meter not found. Retrying...");
//                try {
//                    Thread.sleep(retryDelayMillis);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt(); 
//                    throw new RuntimeException("Thread was interrupted while waiting between retries.", e);
//                }
//            }
//            
//            if (!meterExists) {
//                Assert.fail("Meter number: " + meterNumber + " does not exist in the database after " + maxRetries + " attempts.");
//            }
            
            //performPurchaseAndValidate(prop.getProperty("wtrTrf"));

        } catch (Exception ex) {
            Assert.fail("End-to-end test failed: " + ex.getMessage());
        }
    }
    
    private void createMunicipalityIfNotExists(String municipalityName) throws InterruptedException {
        if (!DatabaseUtilsEnd2End.deleteMunicipalityIfExists(municipalityName)) {
            nav.click_Admin();
            nav.click_MunicipalManagement();
            mun.click_addNwMunicipality();
            mun.client_Name(municipalityName);
            mun.selectCurrencyId();
            mun.vatNum(prop.getProperty("vat"));
            mun.selectDate();
            mun.pssChangeFrequency();
            mun.selectClient();
            mun.rcptLayout();
            mun.slpDisplay(municipalityName);
            mun.eMail(prop.getProperty("myEmail"));
            mun.telNum(prop.getProperty("tel"));
            mun.addressOne(prop.getProperty("addressL1"));
            mun.addressTwo(prop.getProperty("addressL2"));
            mun.addressThree(prop.getProperty("addressL2"));
            mun.slpFooter(municipalityName);
            mun.wtrIndAllw(prop.getProperty("kl"));
            mun.elecIndAllw(prop.getProperty("kw"));
            mun.wtrNonIndigAllowAmnt("0");
            mun.elecNonIndigAllowAmnt("0");
            mun.apiUrl(prop.getProperty("emsApiUrl"));
            mun.apiUser(prop.getProperty("emsApiUser"));
            mun.apiPsswrd(prop.getProperty("password"));
            mun.clickSave();            
            Thread.sleep(1000);
            System.out.println("2. Municipality Successfully created");
            Thread.sleep(1000);
            
            assignMunicipalityToUser(municipalityName);
            
            

        } else {
            System.out.println("2. Municipality already exists in the database. Removing and readding.");
        }
    }

    private String assignMunicipalityToUser(String municipalityName) throws InterruptedException {
    	
    	Thread.sleep(1000);
        nav.click_userMngmnt();
        usrMng.searchUser(prop.getProperty("user"));
        usrMng.editUser();
        usrMng.selectMun(municipalityName);
        
        Thread.sleep(1000);
        String successMsg = usrMng.getToastMssg();
        System.out.println("3. Municipality successfully linked to user profile: " + successMsg);
        return successMsg;
    }
    
    private void configureVendingChannelAndCommission(String municipalityName) throws InterruptedException {
        nav.click_MunicipalManagement();
        mun.searchMunicipality(municipalityName);
        mun.editMuncipality();
        mun.addCommision("WEB Channel", "5", "0");
        mun.clickSave();
        System.out.println("4. Vending channel successfully configured");
    }
    
    private void importFile() {
        nav.navigateTo("Prepaid Import");
        importFile.option("Import");
        String importMsg = importFile.getMsg();
        Assert.assertTrue(importMsg.contains("File uploaded successfully"),
                "File import failed. Message: " + importMsg);
        System.out.println("5. File upload successful");
    }

    private void createTariffAndSteps(String municipalityName) throws InterruptedException {
        //nav.click_Admin();
        nav.click_MunicipalManagement();
        mun.searchMunicipality(municipalityName);
        mun.municipalActions("Manage Sgc");
        nav.navigateTo("Municipal Maintenance");
        mun.searchMunicipality(municipalityName);
        mun.municipalActions("Manage Tariff");
        mun.searchTariff(prop.getProperty("wtrTrf"));
        mun.tableBody();
        mun.addYear();
        mun.selectYrStart("2025");
        mun.selectYrEnd("2026");
        mun.saveYr();
        mun.subCat();
        mun.TariffYr("Annual", "2025/01/01", "2026/06/30", currentBrowserName);
        mun.addStep();
        mun.addFirstStep(6, 1);
        mun.addStep();
        mun.addScndSTep(10, 3);
        mun.addFinalStepBtn();
        mun.addFinalStep(4);
        mun.saveAllStepsBtn();
        System.out.println("6. Tariff and steps successfully created");
    }


    private void performPurchaseAndValidate(String meterNumber) throws InterruptedException, SQLException {
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

//        System.out.println("Transaction Breakdown: " + ppBreakdown);

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
    
    public boolean checkMeterMeterBeforeVend(String meterNumber) {
        boolean exists = DatabaseUtilsEnd2End.doesMeterExist(meterNumber);
        if (exists) {
            System.out.println("Proceeding with action on meter: " + meterNumber);
        } else {
            System.out.println("Meter  " + meterNumber + " not found.");
        }
        return exists;
    }
}

