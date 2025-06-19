package UtilipayV2Hybrid.testCases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.MunicipalMaintenancePage;
import pageObject.NavigationPage;

public class AddMunicipality extends Base {

	@Test (groups= {"Regression"})
	public void AddMun() {
		
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
		
		mun.click_addNwMunicipality();
		mun.client_Name(prop.getProperty("clientName"));
		mun.selectCurrencyId();
		mun.vatNum(prop.getProperty("vat"));
		mun.selectDate();
		mun.pssChangeFrequency();
		mun.selectClient();
		mun.rcptLayout();
		mun.slpDisplay(prop.getProperty("clientName"));
		mun.eMail(prop.getProperty("clientName"));
		mun.telNum(prop.getProperty("tel"));
		mun.addressOne(prop.getProperty("addressL1"));
		mun.addressTwo(prop.getProperty("addressL2"));
		mun.addressThree(prop.getProperty("addressL2"));
		mun.slpFooter(prop.getProperty("clientName"));
		mun.wtrIndAllw(prop.getProperty("kl"));
		mun.elecIndAllw(prop.getProperty("kw"));
		mun.wtrNonIndigAllowAmnt("0");
		mun.elecNonIndigAllowAmnt("0");
		mun.apiUrl(prop.getProperty("emsApiUrl"));
		mun.apiUser(prop.getProperty("emsApiUser"));
		mun.apiPsswrd(prop.getProperty("password"));
		mun.clickSave(); 
		
		WebDriverWait wait = new WebDriverWait(Base.getDriver(), Duration.ofSeconds(10));
		WebElement toaster = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container > div > div.toast-message")));
		
		String toastMessage = toaster.getText();
		String expectedMsg = toastMessage;
		
		
		if (toastMessage != null && !toastMessage.isEmpty()) { 
		    Assert.assertTrue(true);
		    System.out.println("Success: " + toastMessage);
		} else { 
		    Assert.fail("Municipal creation unsuccessful.");
		    System.out.println("Municipal creation unsuccessful.");
		}
	}
}
