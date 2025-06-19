package pageObject;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BaseComponent {

	// create constructor
	public LoginPage(WebDriver driver) {
		super(driver);
	}

	// create locators
	@FindBy(xpath = "//input[@id='Input_Email']")
	WebElement email;

	@FindBy(id = "Input_Password")
	WebElement pssWord;

	@FindBy(xpath = "//button[@id='login-submit']")
	WebElement loginBtn;

	@FindBy(xpath = "//span[@class='app-brand-text demo menu-text fw-bold']")
	WebElement utiliPayText;

	// action methods
	public void email(String userName) {

		email.sendKeys(userName);
	}

	public void pssWrd(String pssWrd) {

		pssWord.sendKeys(pssWrd);
	}

	public void loginBtn() {

		loginBtn.click();
	}

	public String getText() {

		try {
			return utiliPayText.getText();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public String getToastMessage() {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Wait for toaster to appear
	        WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='toast-message']")));
	        return toast.getText(); // Capture toaster message
	    } catch (Exception e) {
	        return null; // No toaster message appeared
	    }
	}
}
