package UtilipayV2Hybrid.testCases;

import java.sql.SQLException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import UtilipayV2Hybrid.utilities.DatabaseCleanupHelper;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.NavigationPage;
import pageObject.UserManagementPage;

public class AddUser extends Base {
	
	@Test(groups = {"Regression"})
	public void create_New_User() throws SQLException, InterruptedException {
	    SoftAssert sftAssert = new SoftAssert();
	    try {
	        HomePage hP = new HomePage(Base.getDriver());
	        LoginPage lP = new LoginPage(Base.getDriver());
	        NavigationPage nav = new NavigationPage(Base.getDriver());
	        UserManagementPage ump = new UserManagementPage(Base.getDriver());

	        logger.info("***Enter login credentials/Click Login***");
	        lP.email(prop.getProperty("myEmail"));
	        lP.pssWrd(prop.getProperty("myPassword"));
	        lP.loginBtn();

	        nav.click_Admin();
	        nav.click_userMngmnt();
	        ump.click_addNwUsr();

	        ump.enter_FullName(getRandomString().toUpperCase() + " " + getRandomString().toUpperCase());
	        ump.enter_Email(getRandomString() + "@test.co.za");
	        ump.enter_PhnNmbr(getRandomNum());
	        ump.selectMultiMunicipalities(3);
	        ump.selectMultipleRoles(4);
	        ump.click_saveChanges();

	        WebDriverWait wait = new WebDriverWait(Base.getDriver(), Duration.ofSeconds(10));
	        WebElement toaster = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.cssSelector("#toast-container > div > div.toast-message")));

	        String toastMessage = toaster.getText().trim();
	        String expectedMsg = "User successfully created!";

	        sftAssert.assertEquals(toastMessage, expectedMsg, "User creation toast mismatch!");

	    } catch (NoSuchElementException ex) {
	        Assert.fail("Element not found during user creation: " + ex.getMessage());
	    } finally {
	        DatabaseCleanupHelper.deleteTestUsersCreated();
	        sftAssert.assertAll();
	    }
	}

}
