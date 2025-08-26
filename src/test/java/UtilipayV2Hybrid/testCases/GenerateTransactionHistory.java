package UtilipayV2Hybrid.testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;
import pageObject.ReportBuilderPage;
import pageObject.TransactionHistoryPage;

public class GenerateTransactionHistory extends Base {
	
    SoftAssert softAssert;
    HomePage hP;
    LoginPage lP;
    NavigationPage nav;
    UnitsAndStepsValidation unitsAndSteps;
    TransactionHistoryPage tranHis;
    String municipalityName;
    ReportBuilderPage rBuilder;
       

    @BeforeClass
    public void loginOnce() throws InterruptedException {
        softAssert = new SoftAssert();
        hP = new HomePage(Base.getDriver());
        lP = new LoginPage(Base.getDriver());
        nav = new NavigationPage(Base.getDriver());
        rBuilder = new ReportBuilderPage(Base.getDriver());      
        tranHis = new TransactionHistoryPage(Base.getDriver());
        try {
            lP.email(prop.getProperty("myEmail"));
            lP.pssWrd(prop.getProperty("myPassword"));
            lP.loginBtn();
            System.out.println("1. Login successful");
        } catch (Exception ex) {
            System.out.println("Login failed. Exception: " + ex.getMessage());
        }
    }
    
    @Test
    public void generateTranHistory() {
    	
    	navigateToTranHis();
    	parameterSelection("Karoo Hoogland Municipality1","01","31");
    }
    
    public void navigateToTranHis() {
    	nav.click_Admin();
    	nav.click_TransactHistory();
    }
    
    public void parameterSelection(String Municipality, String frm, String to) {
        tranHis.selectMunicipality(Municipality);
        tranHis.selectDate(frm, to);

        String transactionList = tranHis.getTransactions();
        String[] transactions = transactionList.split("\\r?\\n");

        boolean hasValidTransactions = false;

        for (String transaction : transactions) {
            if (!transaction.isEmpty() && 
                !transaction.equalsIgnoreCase("No data available in table")) {

                hasValidTransactions = true;
                System.out.println("Transaction: " + transaction);
            }
        }

        System.out.println("Has valid transactions? " + hasValidTransactions);

        softAssert.assertTrue(hasValidTransactions, 
            "No valid transactions found for municipality: " + Municipality);

        softAssert.assertAll();
    }


}
