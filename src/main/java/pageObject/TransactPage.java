package pageObject;

import java.time.Duration;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransactPage extends BaseComponent {
	
	private WebDriverWait wait;
	
	public TransactPage(WebDriver driver) {
		super(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	@FindBy(xpath="//input[@id='meterNumber']")
	WebElement mtrNumber;
	
	@FindBy(xpath="//div[@class='modal-body']//table")
	WebElement lookupDetails;
	
	@FindBy(xpath="//button[normalize-space()='Continue']")
	WebElement continueBtn;
	
	@FindBy(xpath="//input[@id='amount']")
	WebElement amount; 
	
	@FindBy(xpath="//button[@id='btn-amountBreakDown']")
	WebElement breakDownBtn;
	
	@FindBy(xpath="//div[@class='card-body']")
	WebElement prepaidBreakDown;
	
	@FindBy(xpath="//input[@id='payment-method-cash']")
	WebElement cashRadioBtn;
	
	@FindBy(xpath="//input[@id='payment-method-credit-card']")
	WebElement ccRadioBtn;
	
	@FindBy(xpath="//button[normalize-space()='Clear']")
	WebElement clearBtn; 
	
	@FindBy(xpath="//button[@id='btn-purchase']")
	WebElement purchaseBtn;//a[normalize-space()='Cancel']
	
	@FindBy(xpath="//a[normalize-space()='Cancel']")
	WebElement cancelBtn;
	
	@FindBy(xpath="//b[normalize-space()='Free basic Credit: Electricity:']")
	WebElement freeBasicText;
	
//	@FindBy(xpath="//div[@class='mb-4 col-md-12']")
//	WebElement freeBasicToken;
	
	@FindBy(xpath="//div[@class='mb-4 col-md-12']")
	WebElement freeBasicToken;
	
	@FindBy(xpath="//h6[@id='txtError']")
	WebElement errorModal;
	
	public String errorMessage() {
		wait.until(ExpectedConditions.elementToBeClickable(errorModal));
		
		return errorModal.getText();
	}
	
	
	public void insert_MtrNum(String mtrNum) {
		
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    wait.until(ExpectedConditions.elementToBeClickable(mtrNumber));
		
	    mtrNumber.clear();
	    mtrNumber.sendKeys(mtrNum);
	    mtrNumber.sendKeys(Keys.TAB);
	}
	
	public String getAccountDetails() {
	    
		wait.until(ExpectedConditions.elementToBeClickable(lookupDetails));
		return lookupDetails.getText();
	}
	
	public void clickContinue() {
		wait.until(ExpectedConditions.elementToBeClickable(continueBtn));
		continueBtn.click();
	}
	
	
	public void enterAmnt(String Amount) {
    	
		wait.until(ExpectedConditions.elementToBeClickable(amount));
		amount.clear();
//		amount.sendKeys(Keys.ARROW_LEFT);
		amount.sendKeys(Amount);
	}
	
	public void enterAmnt2(String Amount) {
    	
		wait.until(ExpectedConditions.elementToBeClickable(amount));
		amount.clear();
		amount.sendKeys(Amount);
	}
	
	public void clickBreakDown() {
		wait.until(ExpectedConditions.elementToBeClickable(breakDownBtn));
		breakDownBtn.click();
	}
	
	public String getBreakDown() {
    	
		wait.until(ExpectedConditions.elementToBeClickable(prepaidBreakDown));
		return prepaidBreakDown.getText();
	}
	
	public void paymethod() {
    	
		wait.until(ExpectedConditions.elementToBeClickable(cashRadioBtn));
		cashRadioBtn.click();
	}
	
	public void paymentMethod(String payMethod) {
    	
		switch(payMethod.toLowerCase()){
			
		case "cash":
			wait.until(ExpectedConditions.elementToBeClickable(cashRadioBtn));
			cashRadioBtn.click();
			break;
		case "credit card":
			wait.until(ExpectedConditions.elementToBeClickable(ccRadioBtn));
			ccRadioBtn.click();
			break;
			default:
				System.out.println("Invalid Payment Method");
				break;
		}
		

	}
	
	public void purchase2() {
    	
		wait.until(ExpectedConditions.elementToBeClickable(purchaseBtn));
		purchaseBtn.click();
	}
	
	public void purchase() {
    	
		wait.until(ExpectedConditions.elementToBeClickable(purchaseBtn));
		purchaseBtn.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(cancelBtn));
		cancelBtn.click();	
	}
	
	public void clearFields() {	
		wait.until(ExpectedConditions.elementToBeClickable(clearBtn));
		clearBtn.click();
	}
	
	
	public String fbText() {
		
		wait.until(ExpectedConditions.elementToBeClickable(freeBasicText));
		return freeBasicText.getText();		
	}
	
	public String fbToken() {
		
		wait.until(ExpectedConditions.elementToBeClickable(freeBasicToken));
		return freeBasicToken.getText();		
	}
	
	

}
