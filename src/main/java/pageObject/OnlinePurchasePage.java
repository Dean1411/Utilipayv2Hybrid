package pageObject;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OnlinePurchasePage extends BaseComponent {
	
	private WebDriverWait wait;
	
	public OnlinePurchasePage(WebDriver driver) {
		super(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	//Buy prepaid tab
	@FindBy(xpath="//a[normalize-space()='Buy Prepaid']")
	WebElement buyPrepaidBtn;
	
	@FindBy(xpath="//a[@id='prepaid-tab']")
	WebElement buyPrepaidTab;
	
	@FindBy(xpath="//input[@id='meterNumber']")
	WebElement meterNum;
	
	@FindBy(id="BuyPrepaidModel_Email")
	WebElement prepaidEmail; 
	
	@FindBy(xpath="//input[@id='BuyPrepaidModel_Amount']")
	WebElement buyPpAmount;
	
	@FindBy(xpath="//button[@id='btn-purchase']")
	WebElement buyPpBtn;
	
	
	//Pay account tab
	@FindBy(xpath="//a[@id='accounts-tab']")
	WebElement payAccTab;
	
	@FindBy(xpath="//input[@id='account']")
	WebElement accNum;
	
	@FindBy(xpath="//input[@id='PayAccountsModel_Email']")
	WebElement accEmail;
	
	@FindBy(xpath="//input[@id='PayAccountsModel_Amount']")
	WebElement accAmount;
	

	
	
	//Token tab
	@FindBy(xpath="//a[@id='tokens-tab']")
	WebElement tokenTab; 
	
	@FindBy(xpath="//button[@id='btn-amountBreakDown']")
	WebElement breakDownBtn;
	
	@FindBy(xpath="//div[@class='card-body']")
	WebElement prepaidBreakDown;
	


}
