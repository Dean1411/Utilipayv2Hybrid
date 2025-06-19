package UtilipayV2Hybrid.testCases;
import org.testng.Assert;
import org.testng.annotations.Test;

import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.DatabaseUtils;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;
import pageObject.TransactPage;

public class UnitsValidationTest extends Base {

    @Test
	public void doLookup() throws InterruptedException {
        HomePage hP = new HomePage(Base.getDriver());
        LoginPage lP = new LoginPage(Base.getDriver());
        NavigationPage nav = new NavigationPage(Base.getDriver());
        TransactPage tranPg = new TransactPage(Base.getDriver());

        hP.click_Btn();

        lP.email(prop.getProperty("myEmail"));
        lP.pssWrd(prop.getProperty("myPassword"));
        lP.loginBtn();

        nav.click_CashierManagement();
        nav.click_Transact();

    
        String meterNumber = prop.getProperty("mtrNum");
        tranPg.insert_MtrNum(meterNumber);
        Thread.sleep(1000);

        tranPg.clickContinue();
        tranPg.enterAmnt("100");
        tranPg.clickBreakDown();

 
        String ppBreakdown = tranPg.getBreakDown();

        if (!ppBreakdown.isEmpty()) {
            tranPg.paymentMethod("Cash");
            tranPg.purchase();

            System.out.println("Transaction Breakdown: " + ppBreakdown);
		    Assert.assertTrue(true);

            DatabaseUtils.validateTransaction(meterNumber);

        }else{
        	Assert.fail();
        }
    }
}
