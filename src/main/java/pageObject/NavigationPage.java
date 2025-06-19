package pageObject;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NavigationPage extends BaseComponent {
	
	private WebDriverWait wait;

	public NavigationPage(WebDriver driver) {
		super(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	//Admin Elements
	@FindBy(xpath="//div[@data-i18n='Admin']") 
	WebElement admin; 
	
	@FindBy(xpath="//div[@data-i18n='User Management']")
	WebElement userMngmnt;	
	
	@FindBy(partialLinkText="Municipal Maintenance")
	WebElement municipalMaintenance;
	
	@FindBy(partialLinkText="Prepaid Import")
	WebElement prepaidImport;
	
	@FindBy(partialLinkText="Prepaid Export")
	WebElement prepaidExport;
	
	@FindBy(partialLinkText="Consumer Records")
	WebElement consumerRecords;
	
	//Engineering Elements
	@FindBy(xpath="//div[@data-i18n='Engineering']")
	WebElement engineering;
	@FindBy(xpath="//div[@data-i18n='Engineering Token']")
	WebElement engineeringTkn; 
	
	@FindBy(xpath="//div[@data-i18n='Reports']")
	WebElement reports;
	
	
	//Cashier Management
	@FindBy(xpath="//div[@data-i18n='Cashier Management']")
	WebElement cashierManagement;
	
	@FindBy(xpath="//div[@data-i18n='Transact']")
	WebElement transact;
	
	
	
	//Admin methods	
	public void click_Admin() {
		
		admin.click();				
	}
	
	
	public void click_userMngmnt() {
		
		userMngmnt.click();
	}
	
	public void click_MunicipalManagement() {
		
		municipalMaintenance.click();
	}
	
	
	
	//Engineering methods
	public void nav_Engineering() {
		
		engineering.click();
	}
	
	public void click_Engineering() {
		
		engineeringTkn.click();
	}
	
	//Cashier Management
	public void click_CashierManagement() throws InterruptedException {
		
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    wait.until(ExpectedConditions.elementToBeClickable(cashierManagement));
		cashierManagement.click();
		
//		Thread.sleep(1000);
//		
//		Select select = new Select(cashierManagement);
//		select.selectByVisibleText("Transact");
	}
	
	public void click_Transact() {
		
	    wait.until(ExpectedConditions.elementToBeClickable(transact));
	    transact.click();
	}
	
	 public void navigateTo(String section) {

	        switch (section.toLowerCase()) {
	            case "admin":
	                wait.until(ExpectedConditions.elementToBeClickable(admin));
	                admin.click();
	                break;

	            case "user management":
	                wait.until(ExpectedConditions.elementToBeClickable(userMngmnt));
	                userMngmnt.click();
	                break;

	            case "municipal maintenance":
	                wait.until(ExpectedConditions.elementToBeClickable(municipalMaintenance));
	                municipalMaintenance.click();
	                break;

	            case "prepaid import":
	                wait.until(ExpectedConditions.elementToBeClickable(prepaidImport));
	                prepaidImport.click();
	                break;

	            case "prepaid export":
	                wait.until(ExpectedConditions.elementToBeClickable(prepaidExport));
	                prepaidExport.click();
	                break;
	                
	            case "Consumer Records":
	                wait.until(ExpectedConditions.elementToBeClickable(consumerRecords));
	                consumerRecords.click();
	                break;

	            case "engineering":
	                wait.until(ExpectedConditions.elementToBeClickable(engineering));
	                engineering.click();
	                break;

	            case "engineering token":
	                wait.until(ExpectedConditions.elementToBeClickable(engineeringTkn));
	                engineeringTkn.click();
	                break;

	            case "reports":
	                wait.until(ExpectedConditions.elementToBeClickable(reports));
	                reports.click();
	                break;

	            case "cashier management":
	                wait.until(ExpectedConditions.elementToBeClickable(cashierManagement));
	                cashierManagement.click();
	                break;

	            case "transact":
	                wait.until(ExpectedConditions.elementToBeClickable(transact));
	                transact.click();
	                break;

	            default:
	                throw new IllegalArgumentException("Invalid section: " + section);
	        }
	 }
	
	

}
