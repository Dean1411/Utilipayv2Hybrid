package pageObject;

import java.time.Duration;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;






public class EncryptCreditToken extends BaseComponent {
	
	public EncryptCreditToken(WebDriver driver) {
		
		super(driver);
	}
	
	@FindBy(xpath="//button[normalize-space()='Bulk Vend']")
	WebElement bulkVendBtn;
	
	@FindBy(xpath="//div[@class='dz-message needsclick']")
	WebElement fileSelection;
	
	@FindBy(xpath="//input[@id='MeterNumber']")
	WebElement mtrNmb;
	
	@FindBy(id="currencyId")
	WebElement subClss;
	
	@FindBy(xpath="//input[@id='SGC']")
	WebElement sGC;
	
	@FindBy(xpath="//input[@id='KRN']")
	WebElement kRN;
	
	@FindBy(xpath="//input[@id='TI']")
	WebElement tI;
	
	@FindBy(xpath="//input[@id='KEN']")
	WebElement nwKEN;
	
	@FindBy(xpath="//select[@id='ea']")
	WebElement encrAlg;
	
	@FindBy(xpath="//input[@id='TCT']")
	WebElement tCT;
	
	@FindBy(xpath="//input[@id='TransferUnits']")
	WebElement tranAmount;
	
	@FindBy(xpath="//input[@id='KRNUpdate']")
	WebElement krnUpdate;
	
	@FindBy(xpath="//button[normalize-space()='Submit']")
	WebElement submitBtn;
	
	@FindBy(xpath="//a[normalize-space()='Cancel']")
	WebElement cancelBtn;
	
	public void enterMeterNumber(String mtrNum) {
		mtrNmb.sendKeys(mtrNum);
		
		mtrNmb.sendKeys(Keys.TAB);
	}
	
	public void selectSubClass() {
		
		WebElement dropdown = driver.findElement(By.id("currencyId")); 
		Select select = new Select(dropdown);
		select.selectByVisibleText("Water");
	}
	
	public void insertSGC(String sgc) {
		sGC.sendKeys(sgc);	
	}
	
	public void insertKrn(String krn) {
		kRN.clear();
		kRN.sendKeys(krn);
		
	}
	

	public void selectEncryptionAlgorithm() {		
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    wait.until(ExpectedConditions.elementToBeClickable(encrAlg)).click();

	    WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@id='ea']")));
	    Select select = new Select(dropdown);
	    select.selectByVisibleText("7");
	}
	
	public void enterTransferAmount(String amt) {
		
		tranAmount.clear();
		tranAmount.sendKeys(amt);	
	}
	
	public void disableKrn() {
		krnUpdate.click();	
	}
	
	public void clickSubmit() {
		submitBtn.click();	
	}
	
	public void performBulkVend() {
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		
		wait.until(ExpectedConditions.elementToBeClickable(bulkVendBtn));
		
		bulkVendBtn.click();
		
		
		
		
		
	}

}
