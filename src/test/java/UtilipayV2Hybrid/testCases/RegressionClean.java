package UtilipayV2Hybrid.testCases;

import static io.restassured.RestAssured.given;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
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

    @Test(groups = {"Regression"})
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
 
            System.out.println("Regression Test Successfully Completed.");
            return;
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
        // Vending channels to create
        String[][] vendingChannels = {
            {"WEB Channel", "5", "1.50"},
            {"EMS Channel", "10", "2.00"},
            {"Cigicell Channel","8","1.00"}
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
     
        
        mun.navBack();

        // Create tariff steps for Electricity
        createTariffSteps(
            prop.getProperty("elecTrf"),     
            "Electricity",                   
            8, 2,                           
            12, 4,                          
            5                               
        );
        
        System.out.println("Tariffs created successfully.");
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
    
//    public void purchase() throws InterruptedException, SQLException {
//        try {
//        	
//            String meterNumber = prop.getProperty("regWtrMtr");
//            SoftAssert softAssert = new SoftAssert();
//
//            String meterId = DatabaseUtilsEnd2End.getMeterId(meterNumber);
//            System.out.println("Meter Id: " + meterId);
//            softAssert.assertNotNull(meterId, "Meter ID not found for meter number: " + meterNumber);
//
//            String emsVendEndpoint = ConfigReader.get("emsWater");
//
//            Object[][] purchaseData = {
//                {120, 20.6, "4 kl @ R 3 / kl 16.6 kl @ R 4 / kl", "UI", 6.0},
//                {100, 16.4, "16.4 kl @ R 4 / kl", "API", null},
//                {150, 24.5, "24.5 kl @ R 4 / kl", "UI", null},
//                {100, 16.4, "16.4 kl @ R 4 / kl", "API", null}
//            };
//            
//            boolean firstUIPurchase = false;
//
//            for (int i = 0; i < purchaseData.length; i++) {
//            	
//            	int purchaseNumber = i + 1;
//            	int amount = (int) purchaseData[i][0];
//            	double expectedUnits = ((Number) purchaseData[i][1]).doubleValue();
//            	String expectedSteps = ((String) purchaseData[i][2]).replaceAll("\\s+", " ").trim();
//            	String type = (String) purchaseData[i][3];
//            	Double expectedFreeBasic = purchaseData[i][4] != null 
//            	    ? ((Number) purchaseData[i][4]).doubleValue() 
//            	    : 0.0;
//
//                System.out.println("\nPurchase " + purchaseNumber + " (" + type + ") Started");
//
//                if ("UI".equals(type)) {                	
//                    if (!firstUIPurchase) {
//                        nav.click_CashierManagement();
//                        nav.click_Transact();
//                        firstUIPurchase = true;
//                    }                	
//                    transact.insert_MtrNum(meterNumber);
//                    Thread.sleep(1000);
//                    transact.clickContinue();
//                    transact.enterAmnt(String.valueOf(amount));
//                    transact.clickBreakDown();
//                    transact.paymentMethod("cash");
//                    
//                    String breakdown = transact.getBreakDown();
//                    double uiUnits = transact.getUnits();
//                    double actualFreeBasicUnits = transact.getFreeBasicUnits();
//                    String cleanedSteps = breakdown != null ? breakdown.replaceAll("\\s+", " ").trim() : "";
//                    
//                    transact.purchase();
//
//                    System.out.println("UI Steps: " + cleanedSteps);
//                    softAssert.assertNotNull(breakdown, "Breakdown is null for UI purchase " + purchaseNumber);
//                    softAssert.assertFalse(cleanedSteps.isEmpty(), "Steps are empty for UI purchase " + purchaseNumber);
//                    softAssert.assertEquals(cleanedSteps, expectedSteps, "Steps mismatch for UI purchase " + purchaseNumber);
//
//                    
//                    softAssert.assertEquals(uiUnits, expectedUnits, 0.1, "Units mismatch for UI purchase " + purchaseNumber);
//
//                    if (expectedFreeBasic != null) {
//                        
//                        softAssert.assertEquals(actualFreeBasicUnits, expectedFreeBasic, 0.1, "Free Basic Units mismatch on UI purchase " + purchaseNumber);
//                        System.out.println("Free Basic Units Asserted: " + actualFreeBasicUnits);
//                    }
//
//                } else if ("API".equals(type)) {
//                	try {
//                        Response response = given()
//                                .header("Authorization", "Bearer " + AuthUtils.getBearerToken())
//                                .when()
//                                .post(AuthUtils.getBaseUrl() + emsVendEndpoint);
//
//                            String responseBody = response.getBody().asString();
//                            JsonPath jsonPath = new JsonPath(responseBody);
//
//                            String error = jsonPath.getString("error");
//                            double actualUnits = jsonPath.getDouble("units");
//                            String steps = jsonPath.getString("steps").replaceAll("\\s+", " ").trim();
//
//                            softAssert.assertEquals(response.getStatusCode(), 200, "EMS Vend failed (bad status) purchase " + purchaseNumber);
//                            softAssert.assertTrue(error == null || error.isEmpty(), "EMS Vend error purchase " + purchaseNumber + ": " + error);
//                            softAssert.assertEquals(actualUnits, expectedUnits, 0.1, "Units mismatch for API purchase " + purchaseNumber);
//                            softAssert.assertEquals(steps, expectedSteps, "Steps mismatch for API purchase " + purchaseNumber);
//
//                            System.out.println("API Steps: " + steps);
//                	}catch(Exception ex) {
//                		System.out.println("Exception: " + ex);
//                	}
//                }
//
//                // Use meterId to check database after each purchase
//                DatabaseUtilsEnd2End.MeterTransactionInfo info = DatabaseUtilsEnd2End.getLastStepAndUnits(meterId);
//                softAssert.assertNotNull(info, "No ledger entry found for meter " + meterNumber + " after purchase " + purchaseNumber);
//
//                if (info != null) {
//                    String dbSteps = info.getSteps() != null ? info.getSteps().replaceAll("\\s+", " ").trim() : "";
//                    double dbUnits = info.getUnits();
//
//                    System.out.println("DB Units: " + dbUnits + ", DB Steps: " + dbSteps);
//
//                    softAssert.assertEquals(dbUnits, expectedUnits, 0.1, "DB Units mismatch for purchase " + purchaseNumber);
//                    softAssert.assertEquals(dbSteps, expectedSteps, "DB Steps mismatch for purchase " + purchaseNumber);
//                }
//            }
//
//            DatabaseCleanupHelper.collectAndRunCleanupForMeter(meterNumber);
//            softAssert.assertAll();
//
//        } catch (Exception ex) {
//            System.out.println("Exception: " + ex.getMessage());
//            ex.printStackTrace();
//            Assert.fail("Exception during purchase: " + ex.getMessage());
//        }
//    }
    
    public void purchase() throws InterruptedException, SQLException {
        try {
            String meterNumber = prop.getProperty("regWtrMtr");
            SoftAssert softAssert = new SoftAssert();

            String meterId = DatabaseUtilsEnd2End.getMeterId(meterNumber);
            System.out.println("Meter Id: " + meterId);
            softAssert.assertNotNull(meterId, "Meter ID not found for meter number: " + meterNumber);

            String emsVendEndpoint = ConfigReader.get("emsWater");

            // Fetch last 4 transactions dynamically from DB instead of hardcoded purchaseData
            List<DatabaseUtilsEnd2End.MeterTransactionInfo> lastTransactions = 
                DatabaseUtilsEnd2End.getLastNTransactionDetails(meterId, 4);

            Object[][] purchaseData;

            if (lastTransactions.isEmpty()) {
                System.out.println("No previous transactions found - using default purchase data.");
                purchaseData = new Object[][] {
                    {120, 20.6, "4 kl @ R 3 / kl 16.6 kl @ R 4 / kl", "UI", 6.0},
                    {100, 16.4, "16.4 kl @ R 4 / kl", "API", null},
                    {150, 24.5, "24.5 kl @ R 4 / kl", "UI", null},
                    {100, 16.4, "16.4 kl @ R 4 / kl", "API", null}
                };
            } else {
                purchaseData = new Object[lastTransactions.size()][5];
                for (int i = 0; i < lastTransactions.size(); i++) {
                    DatabaseUtilsEnd2End.MeterTransactionInfo info = lastTransactions.get(i);

                    int amount = 100 + (i * 25); 
                    double expectedUnits = info.getUnits();
                    String expectedSteps = info.getSteps() != null ? info.getSteps().replaceAll("\\s+", " ").trim() : "";
                    String type = (i % 2 == 0) ? "UI" : "API"; 
                    Double expectedFreeBasic = (i == 0) ? 6.0 : null;

                    purchaseData[i][0] = amount;
                    purchaseData[i][1] = expectedUnits;
                    purchaseData[i][2] = expectedSteps;
                    purchaseData[i][3] = type;
                    purchaseData[i][4] = expectedFreeBasic;
                }
            }

            boolean firstUIPurchase = false;

            for (int i = 0; i < purchaseData.length; i++) {
                int purchaseNumber = i + 1;
                int amount = (int) purchaseData[i][0];
                double expectedUnits = ((Number) purchaseData[i][1]).doubleValue();
                String expectedSteps = ((String) purchaseData[i][2]).replaceAll("\\s+", " ").trim();
                String type = (String) purchaseData[i][3];
                Double expectedFreeBasic = purchaseData[i][4] != null
                        ? ((Number) purchaseData[i][4]).doubleValue()
                        : 0.0;

                System.out.println("\nPurchase " + purchaseNumber + " (" + type + ") Started");

                if ("UI".equals(type)) {
                    if (!firstUIPurchase) {
                        nav.click_CashierManagement();
                        nav.click_Transact();
                        firstUIPurchase = true;
                    }
                    transact.insert_MtrNum(meterNumber);
                    Thread.sleep(1000);
                    transact.clickContinue();
                    transact.enterAmnt(String.valueOf(amount));
                    transact.clickBreakDown();
                    transact.paymentMethod("cash");

                    String breakdown = transact.getBreakDown();
                    double uiUnits = transact.getUnits();
                    double actualFreeBasicUnits = transact.getFreeBasicUnits();
                    String cleanedSteps = breakdown != null ? breakdown.replaceAll("\\s+", " ").trim() : "";

                    transact.purchase();
                    
                    System.out.println("Expected Units: " + expectedUnits);
                    System.out.println("UI Actual Units: " + uiUnits);
                    System.out.println("Expected Steps: " + expectedSteps);
                    System.out.println("UI Steps: " + cleanedSteps);
                    softAssert.assertNotNull(breakdown, "Breakdown is null for UI purchase " + purchaseNumber);
                    softAssert.assertFalse(cleanedSteps.isEmpty(), "Steps are empty for UI purchase " + purchaseNumber);
                    softAssert.assertEquals(cleanedSteps, expectedSteps, "Steps mismatch for UI purchase " + purchaseNumber);

                    softAssert.assertEquals(uiUnits, expectedUnits, 0.1, "Units mismatch for UI purchase " + purchaseNumber);

                    if (expectedFreeBasic != null) {
                        softAssert.assertEquals(actualFreeBasicUnits, expectedFreeBasic, 0.1,
                                "Free Basic Units mismatch on UI purchase " + purchaseNumber);
                        System.out.println("Free Basic Units Asserted: " + actualFreeBasicUnits);
                    }

                } else if ("API".equals(type)) {
                    try {
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
                    } catch (Exception ex) {
                        System.out.println("Exception: " + ex);
                    }
                }


                // Use meterId to check database after each purchase
                // Verify DB after each purchase

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
            ex.printStackTrace();
            Assert.fail("Exception during purchase: " + ex.getMessage());
        }
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
