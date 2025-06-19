package UtilipayV2Hybrid.testCases;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.MunicipalMaintenancePage;
import pageObject.NavigationPage;

public class AddTariff extends Base {
	
	@Parameters({"Browser"})
	@Test (groups= {"Regression"})
	public void CreateNewTariffCode(String browser) throws InterruptedException {
		
		HomePage hP = new HomePage(Base.getDriver());
		LoginPage lP = new LoginPage(Base.getDriver());
		NavigationPage nav = new NavigationPage(Base.getDriver());
		MunicipalMaintenancePage mun = new MunicipalMaintenancePage(Base.getDriver());
		
		logger.info("***Click Developer/Tester Login***");
		//hP.click_Btn();
		
		logger.info("***Enter login credentials/Click Login***");
		lP.email(prop.getProperty("myEmail"));
		lP.pssWrd(prop.getProperty("myPassword"));		
		lP.loginBtn();
		
		nav.click_Admin();
		nav.click_MunicipalManagement();
		mun.clickActions();
		mun.manageTariffs(Base.getDriver());
		mun.addNewTariff();
		mun.newTariffCode("W123");
		mun.selectMtrType("Water");
		mun.tariffDec("Water Tariff");
		mun.saveTariff();
		mun.searchTariff("W123");
		mun.tableBody();
		mun.addYear();
		mun.selectYrStart("2025");
		mun.selectYrEnd("2026");
		mun.saveYr();
		mun.subCat();
		mun.TariffYr("Annual", "2025/07/01", "2026/06/30", browser);
		mun.addStep();
		mun.addFirstStep(6, 1);
		mun.addStep();
		mun.addScndSTep(10, 3);
		mun.addFinalStepBtn();
		mun.addFinalStep(4);
		mun.saveAllStepsBtn();
		
		
		String success = mun.getStepsToaster();
		
		if (!success.isEmpty() && !(success == null)) {
			
		    Assert.assertTrue(true);
		    System.out.println(mun.getStepsToaster());
		}else {
			
		    Assert.fail("Unable to create tariff successfully");
		}
		
	}
}
