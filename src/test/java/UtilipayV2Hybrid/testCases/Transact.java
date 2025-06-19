package UtilipayV2Hybrid.testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;
import pageObject.TransactPage;

public class Transact extends Base {
	
	@Test (groups= {"Regression"}, retryAnalyzer=UtilipayV2Hybrid.utilities.Retry.class)
	public void doLookup() throws InterruptedException  {
		
		HomePage hP = new HomePage(Base.getDriver());
		LoginPage lP = new LoginPage(Base.getDriver());
		NavigationPage nav = new NavigationPage(Base.getDriver());
		TransactPage tranPg = new TransactPage(Base.getDriver());
		
		//hP.click_Btn();
		
		lP.email(prop.getProperty("myEmail"));
		lP.pssWrd(prop.getProperty("myPassword"));		
		lP.loginBtn();
		
		nav.click_CashierManagement();
		nav.click_Transact();
		
		tranPg.insert_MtrNum(prop.getProperty("wtrMtr"));
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
		} else {
		    Assert.fail("Unable to retrieve Prepaid Breakdown");
		    System.out.println("Failed");
		}


		
//		String accDetails = tranPg.getAccountDetails();

//		if (!accDetails.isEmpty()) {
//		    System.out.println("Account Details: " + accDetails);
//		    Assert.assertFalse(accDetails.isEmpty(), "Account details should not be empty");
//		} else {
//		    Assert.fail("Lookup unsuccessful");
//		}	
		

		
	}

}
