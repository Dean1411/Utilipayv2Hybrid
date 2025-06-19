package UtilipayV2Hybrid.testCases;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;
import pageObject.UserManagementPage;

public class AddUser extends Base {
	
	@Test (groups= {"Regression"})
	public void create_New_User() throws InterruptedException {
		
		HomePage hP = new HomePage(Base.getDriver());
		LoginPage lP = new LoginPage(Base.getDriver());
		NavigationPage nav = new NavigationPage(Base.getDriver());
		UserManagementPage ump = new UserManagementPage(Base.getDriver());
		SoftAssert sftAssert = new SoftAssert();
		
		logger.info("***Click Developer/Tester Login***");
		hP.click_Btn();
		
		logger.info("***Enter login credentials/Click Login***");
		lP.email(prop.getProperty("myEmail"));
		lP.pssWrd(prop.getProperty("myPassword"));		
		lP.loginBtn();
		
		nav.click_Admin();
		nav.click_userMngmnt();
		
		ump.click_addNwUsr();
		
		Thread.sleep(1000l);
		ump.enter_FullName(getRandomString().toUpperCase() + " " + getRandomString().toUpperCase());
		ump.enter_Email(getRandomString()+"@test.co.za");
		ump.enter_PhnNmbr(getRandomNum());
//		ump.selectMun();
		ump.selectMultiMunicipalities(3);
//		ump.selectRole();
		ump.selectMultipleRoles(4);
		ump.click_saveChanges();
		
		WebDriverWait wait = new WebDriverWait(Base.getDriver(), Duration.ofSeconds(10));
		WebElement toaster = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container > div > div.toast-message")));
		//WebElement toaster = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container > div")));
		
		
		String toastMessage = toaster.getText();
		String expectedMsg = "User successfully created!";
		
		
		if (toastMessage != null && !toastMessage.isEmpty()) { 
		    Assert.assertTrue(true);
		    System.out.println(toastMessage);
		} else { 
		    Assert.fail("User creation unsuccessful.");
		    System.out.println("User creation unsuccessful.");
		}
						
	}
}
