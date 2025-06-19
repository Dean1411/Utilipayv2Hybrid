package pageObject;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class KeyChangeToken extends BaseComponent{

	public KeyChangeToken(WebDriver driver) {
		super(driver);

	}
	
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	
	@FindBy(xpath="//button[normalize-space()='Bulk Vend']")
	WebElement bulkVendBtn;
	
	@FindBy(xpath="//input[@id='MeterNumber']")
	WebElement mtrNo;
	
	@FindBy(xpath="//input[@id='SGC']")
	WebElement sGC;
	
	@FindBy(xpath="//input[@id='SGCNew']")
	WebElement sGCNew;
	
	@FindBy(xpath="//input[@id='KRN']")
	WebElement kRN;
	
	@FindBy(xpath="//input[@id='KRNNew']")
	WebElement kRNNew;
	
	@FindBy(xpath="//input[@id='TI']")
	WebElement tI;
	
	@FindBy(xpath="//input[@id='TINew']")
	WebElement tINew;
	
	@FindBy(xpath="//input[@id='KEN']")
	WebElement keyExpiryNo;
	
	@FindBy(xpath="//select[@id='ea']")
	WebElement encryptAlgorithm;
	
	@FindBy(xpath="//input[@id='TCT']")
	WebElement tokenCarrrierType;
	
	@FindBy(xpath="//input[@id='Allow3Kct']")
	WebElement allowkrnUpdate;
	
	@FindBy(xpath="//button[normalize-space()='Submit']")
	WebElement submitBtn;
	
	@FindBy(xpath="//a[normalize-space()='Cancel']")
	WebElement cancelBtn;
	
	@FindBy(xpath="//div[@class='modal-body']//div[@class='mb-4 col-md-12']")
	WebElement modelBody;
	
	@FindBy(xpath="//a[normalize-space()='Print/Download']")
	WebElement printOrdownload;
	
	
	//Key Change Mehtods
	
	public void performBulkVend() {
				
		wait.until(ExpectedConditions.elementToBeClickable(bulkVendBtn));
		
		bulkVendBtn.click();
	
	}
	
	public void meterNum(String mtrNu) {
		
		mtrNo.sendKeys(mtrNu);
	}
	
	public void supplyGroupCode(String SGC) {
		
		sGC.clear();
		sGC.sendKeys(SGC);
	}
	
	public void supplyGroupCodeNew(String SGC) {
		
		sGCNew.clear();
		sGCNew.sendKeys(SGC);
	}
	
	public void keyRevisionNum(String krn) {
		
		kRN.clear();
		kRN.sendKeys(krn);
	}
	
	public void keyRevisionNumNew(String krn) {
		
		kRNNew.clear();
		kRNNew.sendKeys(krn);
	}
	
	public void tariffIndex(String ti) {
		
		tI.sendKeys(ti);
	}
	
	public void tariffIndexNew(String tiNew) {
		
		tINew.clear();
		tINew.sendKeys(tiNew);
	}
	
	public void keyExpNum(String keyExNum) {
		
		keyExpiryNo.sendKeys(keyExNum);
	}
	
	public void EncryptAlgorithm() {
		
		Select algorithm = new Select(encryptAlgorithm);
//		algorithm.selectByIndex(0);
		algorithm.selectByVisibleText("7");
		
	}
	
	public void clickAllowKrnUpdate() {
		
		allowkrnUpdate.click();
	}
	
	public void clickSubmit() {
		
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    for (int scroll = 0; scroll < 10; scroll++) {
	        js.executeScript("window.scrollBy(0, 250);"); 
	        try {
	            Thread.sleep(500); 
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	    
		submitBtn.click();	
	}
	
	public String getModelBody() {
		
		wait.until(ExpectedConditions.visibilityOf(modelBody));
		return modelBody.getText();
	}
	
	public void printDownload() {
		
		wait.until(ExpectedConditions.elementToBeClickable(printOrdownload));
		printOrdownload.click();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
