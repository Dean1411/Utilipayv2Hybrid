package pageObject;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BaseComponent {

	public HomePage (WebDriver driver) {
		super(driver);
	}

	// create locators
	@FindBy(xpath = "//a[normalize-space()='Developer/Tester Login']")
	WebElement testerloginBtn;
	
	@FindBy(xpath = "//span[@class='rounded-circle']")
	WebElement profile;
	
	@FindBy(xpath = "//a[@class='dropdown-item mt-0 waves-effect']")
	WebElement loggedIn;
	
	@FindBy(xpath = "//*[@id=\"toast-container\"]/div/div[2]")
	WebElement failLogin;

	// action methods
	public void click_Btn() {
		testerloginBtn.click();
	}
	
	public void click_Profile() {
		profile.click();
	}
	
	public String getSuccessLoggedInText() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(loggedIn)); 
		
		return loggedIn.getText();//*[@id="toast-container"]/div/div[2]
	}
	
	public String getFailLoggedInText() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(failLogin)); 
		
		return failLogin.getText();
	}
}
