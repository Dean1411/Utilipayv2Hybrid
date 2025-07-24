package UtilipayV2Hybrid.testCases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.DatabaseUtilsEnd2End;
import UtilipayV2Hybrid.utilities.Retry;
import pageObject.EngineeringPage;
import pageObject.HomePage;
import pageObject.Import_ExportPage;
import pageObject.LoginPage;
import pageObject.MunicipalMaintenancePage;
import pageObject.NavigationPage;
import pageObject.ReportBuilderPage;
import pageObject.TransactPage;
import pageObject.UserManagementPage;

public class RegressionClean extends Base {

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
    ReportBuilderPage rBuilder;
    EngineeringPage enPage;
    Import_ExportPage im_ex;

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
        rBuilder = new ReportBuilderPage(Base.getDriver());
        enPage = new EngineeringPage(Base.getDriver());
        im_ex = new Import_ExportPage(Base.getDriver());

        try {
            lP.email(prop.getProperty("myEmail"));
            lP.pssWrd(prop.getProperty("myPassword"));
            lP.loginBtn();
            System.out.println("1. Login successful");
        } catch (Exception ex) {
            System.out.println("Login failed. Exception: " + ex.getMessage());
        }
    }

    @Test(groups = {"Regression"}, retryAnalyzer = Retry.class)
    public void regression() throws InterruptedException {
        try {
            municipalityName = prop.getProperty("regressionMun");
            createMunicipalityIfNotExists(municipalityName);
            configureVendingChannelAndCommission(municipalityName);
            Thread.sleep(1000);
            createTariffAndSteps(municipalityName);
            createUser();
            generateReports();
            bulkEngineering();
            importExport();
        } catch (Exception ex) {
            Assert.fail("Regression test failed: " + ex.getMessage());
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

    private void createTariffAndSteps(String municipalityName) throws InterruptedException {
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

    private void createUser() throws InterruptedException {
        nav.click_Admin();
        nav.click_userMngmnt();
        usrMng.click_addNwUsr();
        usrMng.enter_FullName(getRandomString().toUpperCase() + " " + getRandomString().toUpperCase());
        usrMng.enter_Email(getRandomString() + "@test.co.za");
        usrMng.enter_PhnNmbr(getRandomNum());
        usrMng.selectMultiMunicipalities(3);
        usrMng.selectMultipleRoles(4);
        usrMng.click_saveChanges();

        String toastMessage = usrMng.getToastMssg();
        String expectedMsg = "User successfully created!";

        if (toastMessage.equals(expectedMsg) && !toastMessage.isEmpty()) {
            Assert.assertTrue(true);
            System.out.println("7. " + toastMessage);
        } else {
            Assert.fail("User creation unsuccessful. Actual message: " + toastMessage);
            System.out.println("User creation unsuccessful.");
        }
    }

    private void generateReports() {
        nav.click_Reporting();
        String[] reportTypes = {"prepaid sales", "day end", "month end", "low purchase", "free basic", "arrears recovered"};
        String expectedMsg = "Your report is being prepared. you will receive an email ones it is ready!";
        SoftAssert softAssert = new SoftAssert();

        for (String report : reportTypes) {
            logger.info("=== Generating report: " + report + " ===");
            try {
                rBuilder.selectReport(report);
                String statusMsg = rBuilder.statusMessage();
                logger.info("Status message for [" + report + "]: " + statusMsg);
                softAssert.assertNotNull(statusMsg, "Status message is null for report: [" + report + "]");
                softAssert.assertEquals(statusMsg, expectedMsg, "Unexpected status message for report: [" + report + "]");
            } catch (Exception e) {
                logger.error("Exception for report [" + report + "]: " + e.getMessage());
                softAssert.fail("Exception while processing report [" + report + "]: " + e.getMessage());
            }
        }

        softAssert.assertAll();
        logger.info("*** Reporting Test Completed ***");
    }

    private void bulkEngineering() {
        nav.nav_Engineering();
        nav.click_Engineering();
        String[] engineeringOption = {"encrypt credit token", "verify encrypted token", "manufacturing token", "manufacturing key change token", "manufacturing mngt function token", "key change token", "management function token"};
        String expectedFirstMsg = "CSV file is valid. Please click 'Process File' to continue.";
        String expectedSecondMsg = "Your file has been successfully uploaded. You will receive an email notification once the processing is complete.";
        SoftAssert softAssert = new SoftAssert();

        for (String option : engineeringOption) {
            logger.info("Processing: " + option);
            String validityMsg = enPage.clickOption(option);
            System.out.println("Validity message for " + option + ": " + validityMsg);
            softAssert.assertNotNull(validityMsg, "Validity message is null for option: " + option);
            String[] messages = validityMsg.split("\\|");
            String firstMsg = messages.length > 0 ? messages[0].trim() : "";
            String secondMsg = messages.length > 1 ? messages[1].trim() : "";
            softAssert.assertTrue(firstMsg.contains(expectedFirstMsg), "First modal message invalid for option: " + option + ". Found: " + firstMsg);
            softAssert.assertTrue(secondMsg.contains(expectedSecondMsg), "Second modal message invalid for option: " + option + ". Found: " + secondMsg);
            nav.click_Engineering();
        }
    }
    
    private void importExport() throws InterruptedException {
        nav.click_Admin();

        for (String importType : new String[]{"Import", "Export"}) {
            String navTarget = "Import".equals(importType) ? "Prepaid Import" : "Prepaid Export";
            nav.navigateTo(navTarget);
            im_ex.option(importType);
            Thread.sleep(100); 

            String actualMsg = im_ex.getMsg();
            String expectedMsg = "Import".equals(importType) ? "File uploaded successfully" : "File Exported successfully";

            logger.info("*** Verifying toaster message for " + importType + " ***");

            if (actualMsg == null || actualMsg.isEmpty()) {
                softAssert.fail(importType + ": Toaster message is not visible or is empty.");
            } else {
                softAssert.assertTrue(actualMsg.contains(expectedMsg),
                    importType + ": Toaster message does not contain expected text. Actual: " + actualMsg);
                System.out.println(importType + " Toaster Message: " + actualMsg);
            }
            
            softAssert.assertAll();
            // Optional: Navigate back or reset state before next iteration
        }
    }


//    private void importExport() throws InterruptedException {
//        nav.click_Admin();
//        String importType = "Export";
//        String navTarget = "Import".equals(importType) ? "Prepaid Import" : "Prepaid Export";
//        nav.navigateTo(navTarget);
//        im_ex.option(importType);
//        Thread.sleep(100);
//        String actualMsg = im_ex.getMsg();
//        String expectedMsg = "Import".equals(importType) ? "File uploaded successfully" : "File Exported successfully";
//        logger.info("*** Verifying toaster message ***");
//        if (actualMsg == null || actualMsg.isEmpty()) {
//            softAssert.fail("Toaster message is not visible or is empty.");
//        } else {
//            softAssert.assertTrue(actualMsg.contains(expectedMsg), "Toaster message does not contain expected text. Actual: " + actualMsg);
//            System.out.println("Toaster Message: " + actualMsg);
//        }
//    }

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
