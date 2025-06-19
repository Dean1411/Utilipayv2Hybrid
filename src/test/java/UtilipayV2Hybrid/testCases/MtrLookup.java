package UtilipayV2Hybrid.testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;
import pageObject.TransactPage;

public class MtrLookup extends Base {
	
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
		
		tranPg.insert_MtrNum(prop.getProperty("mtrNum"));
		Thread.sleep(1000);
		
		String accDetails = tranPg.getAccountDetails();

		if (!accDetails.isEmpty()) {
		    System.out.println("Account Details: " + accDetails);
		    Assert.assertFalse(accDetails.isEmpty(), "Account details should not be empty");
		} else {
		    Assert.fail("Lookup unsuccessful");
		}	
		
	}

}
