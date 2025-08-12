package pageObject;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OnlinePurchasePage extends BaseComponent {
	
	private WebDriverWait wait;
	private TransactPage transactPage;
	private String originalWindow;
	
	public OnlinePurchasePage(WebDriver driver) {
		super(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		this.transactPage = new TransactPage(driver);
		this.originalWindow = driver.getWindowHandle();
	}
	
	//Buy prepaid tab
	@FindBy(xpath="//a[normalize-space()='Buy Prepaid']")
	WebElement buyPrepaidBtn;
	
	@FindBy(xpath="//a[@id='prepaid-tab']")
	WebElement buyPrepaidTab;
	
	@FindBy(xpath="//input[@id='meterNumber']")
	WebElement meterNum;
	
	@FindBy(xpath="//div[@id='confirmation']//div[@class='modal-body']")
	WebElement accDetails; 
	
	@FindBy(id="BuyPrepaidModel_Email")
	WebElement prepaidEmail; 
	
	@FindBy(xpath="//input[@id='BuyPrepaidModel_Amount']")
	WebElement buyPpAmount;
	
	@FindBy(xpath="//button[@id='btn-purchase']")
	WebElement buyNow;
	
	@FindBy(xpath="//ul[@class='list-unstyled mb-0']")
	WebElement purchaseOptions;
	
	@FindBy(xpath="//*[@id=\"testAlert\"]/div/div/div[3]/button")
	WebElement warningModal;
	
	@FindBy(xpath="//input[@id='ccName']")
	WebElement cardHolder;
	
	@FindBy(xpath="//input[@id='ccNumber']")
	WebElement ccNum;
	
	@FindBy(xpath="//select[@id='ccOpMonth']")
	WebElement cardMonth;
	
	@FindBy(xpath="//select[@id='ccOpYear']")
	WebElement cardYr;
	
	@FindBy(xpath="//input[@id='ccCvv']")
	WebElement cVV;
	
	@FindBy(xpath="//button[@id='nextBtn']")
	WebElement nxtBtn;
	
	@FindBy(xpath="//button[normalize-space()='Submit']")
	WebElement submitBtn;
	
	@FindBy(xpath="//div[@class='modal-body']//table")
	WebElement paymentNoti;
	
	@FindBy(xpath="//a[normalize-space()='Print/Download']")
	WebElement printBtn;
	
	//CapitecPay Elements
	@FindBy(xpath="//input[@id='txtMN']")
	WebElement mobileNum;
	
	@FindBy(xpath="//div[@class='d-flex flex-wrap gap-2']")
	WebElement statusOptions;
	
	@FindBy(xpath="//button[@id='btnPWC']")
	WebElement cappayBtn;
	
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
	
	@FindBy(xpath="//button[@id='closeModalBtn']")
	WebElement closeModal;
	
	
	public void clickBuyPrepaid() {		
		wait.until(ExpectedConditions.elementToBeClickable(buyPrepaidBtn));
		buyPrepaidBtn.click();		
	}
	
	public void insert_MtrNum(String mtrNum) {
				
	    wait.until(ExpectedConditions.elementToBeClickable(meterNum));
	    meterNum.clear();
	    meterNum.sendKeys(mtrNum);
	    meterNum.sendKeys(Keys.TAB);
	    
	    String acDetails = getAccountDetails();
	    System.out.println("Customer Details: " + acDetails);
	    
	    transactPage.clickContinue();
	}
	
	public void enterEmailandAmount(String email,String amount) {	
		
		wait.until(ExpectedConditions.elementToBeClickable(prepaidEmail));
		prepaidEmail.clear();prepaidEmail.sendKeys(email);
		
		wait.until(ExpectedConditions.elementToBeClickable(buyPpAmount));
		buyPpAmount.clear();buyPpAmount.sendKeys(amount);
		
		wait.until(ExpectedConditions.elementToBeClickable(buyNow));
		buyNow.click();
	}
	
	public String getAccountDetails() {
		wait.until(ExpectedConditions.elementToBeClickable(accDetails));
		return accDetails.getText();
	}
	
	public void selectPaymentOption(String option) {
		
	    if (option.equalsIgnoreCase("Capitec Pay")) {
	        WebElement capitecPayBtn = driver.findElement(By.xpath("//img[@src='/Images/PaymentPortal/CapitecPay.png']/parent::button"));
	        capitecPayBtn.click();
	        return; 
	    }
			
		List<WebElement> paymentOptions = driver.findElements(By.cssSelector("ul.list-unstyled button"));

		for (WebElement opt : paymentOptions) {
		    String optionText = opt.getText().trim();

		    if (optionText.equalsIgnoreCase(option)) {
		    	opt.click();
		        break;
		    }		 		    
		}
	}
	
	public void fillCardDetailsForm(String name,String cardNo, String cvv) {
		
		wait.until(ExpectedConditions.elementToBeClickable(warningModal));
		warningModal.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(cardHolder));
		cardHolder.sendKeys(name);
		
		wait.until(ExpectedConditions.elementToBeClickable(ccNum));
		ccNum.sendKeys(cardNo);		
		
		selectExpiryDatesAndCvv(0,"2028","4512");
	}
	
	public void selectExpiryDatesAndCvv(int month, String yr, String cvv) {
		
		Select cMonth = new Select(cardMonth);
		Select cYr = new Select(cardYr);
		
		cMonth.selectByIndex(month);
		cYr.selectByValue(yr);
		
		wait.until(ExpectedConditions.
				elementToBeClickable(cVV));
		cVV.sendKeys(cvv);
		
		wait.until(ExpectedConditions.
				elementToBeClickable(nxtBtn));
		nxtBtn.click();
		
		wait.until(ExpectedConditions.
				elementToBeClickable(submitBtn));
		submitBtn.click();		
	}
	
	public String getNotiMsg() {
		wait.until(ExpectedConditions.elementToBeClickable(paymentNoti));
		return paymentNoti.getText();
	}
	
	public void printReceipt() {

	    wait.until(ExpectedConditions.elementToBeClickable(printBtn));
	    printBtn.click();

	    wait.until(driver -> driver.getWindowHandles().size() > 1);

	    for (String windowHandle : driver.getWindowHandles()) {
	        if (!windowHandle.equals(originalWindow)) {
	            driver.switchTo().window(windowHandle);
	            break;
	        }
	    }
	}
	
	public void closeNotification() {		
		wait.until(ExpectedConditions.
				elementToBeClickable(closeModal));
		closeModal.click();
	}
	
	public void enterCapitecPayDetails(String mobile, String statusOp) {
	    // Enter mobile number
	    wait.until(ExpectedConditions.elementToBeClickable(mobileNum)).sendKeys(mobile);

	    // Click Capitec Pay button
	    wait.until(ExpectedConditions.elementToBeClickable(cappayBtn)).click();

	    List<WebElement> statusOptions = wait.until(
	        ExpectedConditions.visibilityOfAllElementsLocatedBy(
	            By.xpath("//div[@class='d-flex flex-wrap gap-2']//button")
	        )
	    );

	    for (WebElement opt : statusOptions) {
	        if (opt.getText().equalsIgnoreCase(statusOp)) {
	            wait.until(ExpectedConditions.elementToBeClickable(opt)).click();
	            break;
	        }
	    }
	}




	
//	public void enterCapitecPayDetails(String mobile, String statusOp) {
//		
//		wait.until(ExpectedConditions.
//				elementToBeClickable(mobileNum));
//		mobileNum.sendKeys(mobile);
//		
//		wait.until(ExpectedConditions.
//				elementToBeClickable(cappayBtn));
//		cappayBtn.click();
//		
//		List<WebElement> statusOptions = driver.findElements(By.xpath("//div[@class='d-flex flex-wrap gap-2']"));
//		
//		for(WebElement opt: statusOptions) {
//			if(opt.getText().equalsIgnoreCase("APPROVED")) {
//				opt.click();
//			}
//		}
//		
//				
//	}


}
