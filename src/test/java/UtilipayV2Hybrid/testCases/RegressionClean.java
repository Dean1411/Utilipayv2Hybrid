package UtilipayV2Hybrid.testCases;

import static io.restassured.RestAssured.given;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.AuthUtils;
import UtilipayV2Hybrid.utilities.ConfigReader;
import UtilipayV2Hybrid.utilities.ConfirmEMSTranUtil;
import UtilipayV2Hybrid.utilities.DatabaseCleanupHelper;
import UtilipayV2Hybrid.utilities.DatabaseUtilsEnd2End;
import UtilipayV2Hybrid.utilities.Retry;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
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
	private static String transactionId;
	
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
    AuthUtils token;
    static ConfirmEMSTranUtil confirm;
    TransactPage transact;
    
    

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
        confirm = new ConfirmEMSTranUtil();
        token = new AuthUtils();
        AuthUtils.authenticate();
        transact = new TransactPage(Base.getDriver());

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
            importExport("Import");
            createUser();
            generateReports();
            bulkEngineering();            
            purchase();
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

//    private void configureVendingChannelAndCommission(String municipalityName) throws InterruptedException {
//        nav.click_MunicipalManagement();
//        mun.searchMunicipality(municipalityName);
//        mun.editMuncipality();
//        mun.addCommision("WEB Channel", "5", "0");
//        mun.clickSave();
//        System.out.println("4. Vending channel successfully configured");
//    }
    
    private void configureVendingChannelAndCommission(String municipalityName) throws InterruptedException {
        // Define vending channels, commissions, and flat rates: {channelName, commission, flatRate}
        String[][] vendingChannels = {
            {"WEB Channel", "5", "1.50"},
            {"EMS Channel", "10", "2.00"}
        };

        // Only run these steps once before adding channels
        nav.click_MunicipalManagement();
        mun.searchMunicipality(municipalityName);
        mun.editMuncipality();

        for (String[] channel : vendingChannels) {
            String channelName = channel[0];
            String commission = channel[1];
            String flatRate = channel[2];

            mun.addCommision(channelName, commission, flatRate);  // Handles clickAddCommission internally
            System.out.println("Added vending channel: " + channelName + " | Commission: " + commission + " | Flat Rate: " + flatRate);
        }

        mun.clickSave();
        System.out.println("Vending channels successfully configured");
    }

//
//    private void createTariffAndSteps(String municipalityName) throws InterruptedException {
//        nav.click_MunicipalManagement();
//        mun.searchMunicipality(municipalityName);
//        mun.municipalActions("Manage Sgc", null, 0);
//        nav.navigateTo("Municipal Maintenance");
//        mun.searchMunicipality(municipalityName);
//
//        // Create one Water tariff and one Electricity tariff with a single call:
//        mun.municipalActions("Manage Tariff", "All", 1);
//
//        // If you want to create one Water and one Electricity tariff separately, comment out above and uncomment below:
//        // mun.municipalActions("Manage Tariff", "Water", 1);
//        // mun.municipalActions("Manage Tariff", "Electricity", 1);
//        String tariffType = "";
//        
//        if(tariffType.equalsIgnoreCase("water")) {
//        	mun.searchTariff(prop.getProperty("wtrTrf"));
//        }else {
//        	mun.searchTariff(prop.getProperty("elecTrf"));
//        }        
//        mun.tableBody();
//        mun.addYear();
//        mun.selectYrStart("2025");
//        mun.selectYrEnd("2026");
//        mun.saveYr();
//        mun.subCat();
//        mun.TariffYr("Annual", "2025/01/01", "2026/06/30", currentBrowserName);
//        mun.addStep();
//        mun.addFirstStep(6, 1);
//        mun.addStep();
//        mun.addScndSTep(10, 3);
//        mun.addFinalStepBtn();
//        mun.addFinalStep(4);
//        mun.saveAllStepsBtn();
//        System.out.println("6. Tariff and steps successfully created");
//    }
    
    private void createTariffAndSteps(String municipalityName) throws InterruptedException {
        nav.click_MunicipalManagement();
        mun.searchMunicipality(municipalityName);
        mun.municipalActions("Manage Sgc", null, 0);
        nav.navigateTo("Municipal Maintenance");
        mun.searchMunicipality(municipalityName);

        // Create both tariffs 
        mun.municipalActions("Manage Tariff", "All", 1);

        
        createTariffSteps(
            prop.getProperty("wtrTrf"),       
            "Water",                         
            6, 1,                           
            10, 3,                           
            4                               
        );
        
//        int navBack = 3;
//        
//        for(int i = 0; i < navBack;i++) {
//        	mun.navBack();
//        }
        
        mun.navBack();

        // Create tariff steps for Electricity
        createTariffSteps(
            prop.getProperty("elecTrf"),     
            "Electricity",                   
            8, 2,                           
            12, 4,                          
            5                               
        );
    }

    private void createTariffSteps(
        String tariffCode,
        String tariffType,
        int firstStepValue1, int firstStepValue2,
        int secondStepValue1, int secondStepValue2,
        int finalStepValue
    ) throws InterruptedException {
        mun.searchTariff(tariffCode);
        mun.tableBody();
        mun.addYear();
        mun.selectYrStart("2025");
        mun.selectYrEnd("2026");
        mun.saveYr();
        mun.subCat();
        mun.TariffYr("Annual", "2025/01/01", "2026/06/30", currentBrowserName);
        mun.addStep();
        mun.addFirstStep(firstStepValue1, firstStepValue2);
        mun.addStep();
        mun.addScndSTep(secondStepValue1, secondStepValue2);
        mun.addFinalStepBtn();
        mun.addFinalStep(finalStepValue);
        mun.saveAllStepsBtn();
        System.out.println(tariffType + " tariff and steps successfully created");
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
    
//    private void importExport() throws InterruptedException {
//        nav.click_Admin();
//
//        for (String importType : new String[]{"Import", "Export"}) {
//            String navTarget = "Import".equals(importType) ? "Prepaid Import" : "Prepaid Export";
//            nav.navigateTo(navTarget);
//            im_ex.option(importType);
//            Thread.sleep(100); 
//
//            String actualMsg = im_ex.getMsg();
//            String expectedMsg = "Import".equals(importType) ? "File uploaded successfully" : "File Exported successfully";
//
//            logger.info("*** Verifying toaster message for " + importType + " ***");
//
//            if (actualMsg == null || actualMsg.isEmpty()) {
//                softAssert.fail(importType + ": Toaster message is not visible or is empty.");
//            } else {
//                softAssert.assertTrue(actualMsg.contains(expectedMsg),
//                    importType + ": Toaster message does not contain expected text. Actual: " + actualMsg);
//                System.out.println(importType + " Toaster Message: " + actualMsg);
//            }
//            
//            softAssert.assertAll();
//        }
//    }
    
    private void importExport(String type) throws InterruptedException {
    	nav.click_Admin();

        String importType = type;
        String navTarget = "Import".equals(importType) ? "Prepaid Import" 
        		: "Prepaid Export";
        nav.navigateTo(navTarget);


        im_ex.option(importType);
        Thread.sleep(100);

        String actualMsg = im_ex.getMsg();
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
    }
    
    public void purchase() throws InterruptedException, SQLException {
        try {
            String meterNumber = prop.getProperty("regElecMtr");
            SoftAssert softAssert = new SoftAssert();

            // Retrieve meterId early, as in performPurchasesAndValidate()
            String meterId = DatabaseUtilsEnd2End.getMeterId(meterNumber);
            softAssert.assertNotNull(meterId, "Meter ID not found for meter number: " + meterNumber);

            String emsVendEndpoint = ConfigReader.get("emsElec");

            Object[][] purchaseData = {
                {120, 25.1, "50 kwh @ R 1 / kwh 4 kl @ R 3 / kwh / 15.1 kwh @ R 4 / kwh", "UI", 50},
                {100, 15.7, "15.7 kwh @ R 5 / kwh", "API", null},
                {150, 26.1, "26.1 kwh @ R 4 / kl", "UI", null},
                {100, 15.7, "15.7 kwh @ R 5 / kwh", "API", null}
            };

            for (int i = 0; i < purchaseData.length; i++) {
                int purchaseNumber = i + 1;
                int amount = (int) purchaseData[i][0];
                double expectedUnits = (double) purchaseData[i][1];
                String expectedSteps = ((String) purchaseData[i][2]).replaceAll("\\s+", " ").trim();
                String type = (String) purchaseData[i][3];
                Double expectedFreeBasic = (Double) purchaseData[i][4];

                System.out.println("\nPurchase " + purchaseNumber + " (" + type + ") Started");

                if ("UI".equals(type)) {
                    nav.click_CashierManagement();
                    nav.click_Transact();
                    transact.insert_MtrNum(meterNumber);
                    Thread.sleep(1000);
                    transact.clickContinue();
                    transact.enterAmnt(String.valueOf(amount));
                    transact.clickBreakDown();

                    String breakdown = transact.getBreakDown();
                    String cleanedSteps = breakdown != null ? breakdown.replaceAll("\\s+", " ").trim() : "";

                    System.out.println("UI Steps: " + cleanedSteps);
                    softAssert.assertNotNull(breakdown, "Breakdown is null for UI purchase " + purchaseNumber);
                    softAssert.assertFalse(cleanedSteps.isEmpty(), "Steps are empty for UI purchase " + purchaseNumber);
                    softAssert.assertEquals(cleanedSteps, expectedSteps, "Steps mismatch for UI purchase " + purchaseNumber);

                    double uiUnits = transact.getUnits();
                    softAssert.assertEquals(uiUnits, expectedUnits, 0.1, "Units mismatch for UI purchase " + purchaseNumber);

                    if (expectedFreeBasic != null) {
                        double actualFreeBasicUnits = transact.getFreeBasicUnits();
                        softAssert.assertEquals(actualFreeBasicUnits, expectedFreeBasic, 0.1, "Free Basic Units mismatch on UI purchase " + purchaseNumber);
                        System.out.println("Free Basic Units Asserted: " + actualFreeBasicUnits);
                    }

                } else if ("API".equals(type)) {
                    Response response = given()
                        .header("Authorization", "Bearer " + AuthUtils.getBearerToken())
                        .when()
                        .post(AuthUtils.getBaseUrl() + emsVendEndpoint);

                    String responseBody = response.getBody().asString();
                    JsonPath jsonPath = new JsonPath(responseBody);

                    String error = jsonPath.getString("error");
                    double actualUnits = jsonPath.getDouble("units");
                    String steps = jsonPath.getString("steps").replaceAll("\\s+", " ").trim();

                    softAssert.assertEquals(response.getStatusCode(), 200, "EMS Vend failed (bad status) purchase " + purchaseNumber);
                    softAssert.assertTrue(error == null || error.isEmpty(), "EMS Vend error purchase " + purchaseNumber + ": " + error);
                    softAssert.assertEquals(actualUnits, expectedUnits, 0.1, "Units mismatch for API purchase " + purchaseNumber);
                    softAssert.assertEquals(steps, expectedSteps, "Steps mismatch for API purchase " + purchaseNumber);

                    System.out.println("API Steps: " + steps);
                }

                // Use pre-retrieved meterId to check database after each purchase
                DatabaseUtilsEnd2End.MeterTransactionInfo info = DatabaseUtilsEnd2End.getLastStepAndUnits(meterId);
                softAssert.assertNotNull(info, "No ledger entry found for meter " + meterNumber + " after purchase " + purchaseNumber);

                if (info != null) {
                    String dbSteps = info.getSteps() != null ? info.getSteps().replaceAll("\\s+", " ").trim() : "";
                    double dbUnits = info.getUnits();

                    System.out.println("DB Units: " + dbUnits + ", DB Steps: " + dbSteps);

                    softAssert.assertEquals(dbUnits, expectedUnits, 0.1, "DB Units mismatch for purchase " + purchaseNumber);
                    softAssert.assertEquals(dbSteps, expectedSteps, "DB Steps mismatch for purchase " + purchaseNumber);
                }
            }

            DatabaseCleanupHelper.collectAndRunCleanupForMeter(meterNumber);
            softAssert.assertAll();

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    
//    public void purchase() throws InterruptedException, SQLException {
//       try {
//    	   String meterNumber = prop.getProperty("regElecMtr");
//           SoftAssert softAssert = new SoftAssert();
//
//           String emsVendEndpoint = ConfigReader.get("emsElec");
//
//           Object[][] purchaseData = {
//               {120, 25.1, 
//            	   "50 kwh @ R 1 / kwh 4 kl @ R 3 / kwh / 15.1 kwh @ R 4 / kwh", "UI", 
//            	   50},
//               {100, 15.7, 
//            		   "15.7 kwh @ R 5 / kwh", "API", 
//            		   null},
//               {150, 26.1, 
//            			   "26.1 kwh @ R 4 / kl", "UI", 
//            			   null},
//               {100, 15.7, 
//            				   "15.7 kwh @ R 5 / kwh", "API", 
//            				   null}
//           };
//
//           for (int i = 0; i < purchaseData.length; i++) {
//               int purchaseNumber = i + 1;
//               int amount = (int) purchaseData[i][0];
//               double expectedUnits = (double) purchaseData[i][1];
//               String expectedSteps = ((String) purchaseData[i][2]).replaceAll("\\s+", " ").trim();
//               String type = (String) purchaseData[i][3];
//               Double expectedFreeBasic = (Double) purchaseData[i][4];
//
//               System.out.println("\nPurchase " + purchaseNumber + " (" + type + ") Started");
//
//               if ("UI".equals(type)) {
//                   nav.click_CashierManagement();
//                   nav.click_Transact();
//                   transact.insert_MtrNum(meterNumber);
//                   Thread.sleep(1000);
//                   transact.clickContinue();
//                   transact.enterAmnt(String.valueOf(amount));
//                   transact.clickBreakDown();
//
//                   String breakdown = transact.getBreakDown();
//                   String cleanedSteps = breakdown != null ? breakdown.replaceAll("\\s+", " ").trim() : "";
//
//                   System.out.println("UI Steps: " + cleanedSteps);
//                   softAssert.assertNotNull(breakdown, "Breakdown is null for UI purchase " + purchaseNumber);
//                   softAssert.assertFalse(cleanedSteps.isEmpty(), "Steps are empty for UI purchase " + purchaseNumber);
//                   softAssert.assertEquals(cleanedSteps, expectedSteps, "Steps mismatch for UI purchase " + purchaseNumber);
//
//                   double uiUnits = transact.getUnits();
//                   softAssert.assertEquals(uiUnits, expectedUnits, 0.1, "Units mismatch for UI purchase " + purchaseNumber);
//
//                   if (expectedFreeBasic != null) {
//                       double actualFreeBasicUnits = transact.getFreeBasicUnits();
//                       softAssert.assertEquals(actualFreeBasicUnits, expectedFreeBasic, 0.1, "Free Basic Units mismatch on UI purchase " + purchaseNumber);
//                       System.out.println("Free Basic Units Asserted: " + actualFreeBasicUnits);
//                   }
//
//               } else if ("API".equals(type)) {
//                   Response response = given()
//                       .header("Authorization", "Bearer " + AuthUtils.getBearerToken())
//                       .when()
//                       .post(AuthUtils.getBaseUrl() + emsVendEndpoint);
//
//                   String responseBody = response.getBody().asString();
//                   JsonPath jsonPath = new JsonPath(responseBody);
//
//                   String error = jsonPath.getString("error");
//                   double actualUnits = jsonPath.getDouble("units");
//                   String steps = jsonPath.getString("steps").replaceAll("\\s+", " ").trim();
//
//                   softAssert.assertEquals(response.getStatusCode(), 200, "EMS Vend failed (bad status) purchase " + purchaseNumber);
//                   softAssert.assertTrue(error == null || error.isEmpty(), "EMS Vend error purchase " + purchaseNumber + ": " + error);
//                   softAssert.assertEquals(actualUnits, expectedUnits, 0.1, "Units mismatch for API purchase " + purchaseNumber);
//                   softAssert.assertEquals(steps, expectedSteps, "Steps mismatch for API purchase " + purchaseNumber);
//
//                   System.out.println("API Steps: " + steps);
//               }
//
//               String meterId = DatabaseUtilsEnd2End.getMeterId(meterNumber);
//               DatabaseUtilsEnd2End.MeterTransactionInfo info = DatabaseUtilsEnd2End.getLastStepAndUnits(meterId);
//
//               if (info != null) {
//                   String dbSteps = info.getSteps() != null ? info.getSteps().replaceAll("\\s+", " ").trim() : "";
//                   double dbUnits = info.getUnits();
//
//                   System.out.println("DB Units: " + dbUnits + ", DB Steps: " + dbSteps);
//
//                   softAssert.assertEquals(dbUnits, expectedUnits, 0.1, "DB Units mismatch for purchase " + purchaseNumber);
//                   softAssert.assertEquals(dbSteps, expectedSteps, "DB Steps mismatch for purchase " + purchaseNumber);
//               } else {
//                   softAssert.fail("No ledger entry found for meter " + meterNumber + " after purchase " + purchaseNumber);
//               }
//           }
//
//           DatabaseCleanupHelper.collectAndRunCleanupForMeter(meterNumber);
//
//
//           softAssert.assertAll();
//       }catch(Exception ex) {
//    	   System.out.println("Exception: " + ex.getMessage());
//       }
//    }


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
    
    
    public void emsVendTest() {
        int vends = 1;
        int total = 0;
        String filePath = "ems_vend_results.txt";
        double expectedUnits = 15.7;
        String expectedStepPattern = "15.7 kwh @ R 5 / kwh"; 

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < vends; i++) {
                String emsVendEndpoint = ConfigReader.get("emsVend");

                Response response = given()
                        .header("Authorization", "Bearer " + AuthUtils.getBearerToken())
                        .when()
                        .post(AuthUtils.getBaseUrl() + emsVendEndpoint);

                String responseBody = response.getBody().asString();
                System.out.println("EMS Vend Response: " + responseBody);

                JsonPath jsonPath = new JsonPath(responseBody);

                // Extract response fields
                String error = jsonPath.getString("error");
                String crToken = jsonPath.getString("creditToken");
                double actualUnits = jsonPath.getDouble("units");
                String steps = jsonPath.getString("steps");
                transactionId = jsonPath.getString("transactionId");

                // Log details
                System.out.println("Transaction Id: " + transactionId);
                System.out.println("Credit Token: " + crToken);
                System.out.println("Units: " + actualUnits);
                System.out.println("Steps: " + steps);
                System.out.println("Response Status: " + response.getStatusCode());

                // Perform assertions
                Assert.assertEquals(response.getStatusCode(), 200, "EMS Vend failed (bad status)");
                Assert.assertTrue(error == null || error.isEmpty(), "EMS Vend failed with error: " + error);
                Assert.assertNotNull(crToken, "Credit Token is null");
                Assert.assertFalse(crToken.isEmpty(), "Credit Token is empty despite no error");
                Assert.assertEquals(actualUnits, expectedUnits, 0.01, "Incorrect number of units returned");

                Assert.assertTrue(steps.contains(expectedStepPattern),
                        "Steps string does not contain expected pattern. Expected to contain: " + expectedStepPattern + ", but got: " + steps);

                // Confirm transaction
                confirm.confirmTransaction(transactionId);

                total++;
            }

            System.out.println("Total purchases: " + total);

        } catch (IOException e) {
            Assert.fail("IOException occurred during EMS Vend: " + e.getMessage());
        }

        Assert.assertTrue(total > 0, "No EMS vends completed");
        Assert.assertNotNull(transactionId, "Transaction ID not set after EMS vend");
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
