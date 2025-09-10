package pageObject;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransactionHistoryPage extends BaseComponent {
	
	ReportBuilderPage reportPage;
	private WebDriverWait wait;
	
	public TransactionHistoryPage(WebDriver driver) {
		super(driver);	
		this.reportPage = new ReportBuilderPage(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
    @FindBy(xpath = "//select[@id='municipality']")
    WebElement municipality;

    @FindBy(xpath = "//div[@class='head-label']//div[2]//input[1]")
    WebElement mtr_accNo;

    @FindBy(xpath = "//input[@name='Id']")
    WebElement ref;

    @FindBy(xpath = "//input[@id='flatpickr-range']")
    WebElement datePicker;
    
    @FindBy(xpath = "//div[@class='flatpickr-innerContainer']")
    WebElement flatpickerContainer;
    
    @FindBy(xpath = "///select[@aria-label='Month']")
    WebElement monthSelector;
    
    @FindBy(xpath = "//input[@id='amount']")
    WebElement amount;
    
    @FindBy(xpath = "//button[normalize-space()='Clear']")
    WebElement clearBtn;
    
    @FindBy(xpath = "//button[normalize-space()='Submit']")
    WebElement submitBtn;
    
    @FindBy(xpath = "//select[@name='DataTables_Table_0_length']")
    WebElement showEntries;
    
    @FindBy(xpath = "//input[@type='search']")
    WebElement search;
    
    @FindBy(xpath = "//*[@id=\"DataTables_Table_0\"]/tbody")
    WebElement transactionTable;
    
    
    public void selectMunicipality(String mun) {
    	WebElement municipalityDropdown = driver.findElement(By.xpath("//select[@name='MunicipalityId']"));
    	Select selectMun = new Select(municipalityDropdown);
    	selectMun.selectByVisibleText(mun);    	
    }
    
    public void selectDate(String from, String to) {
    	
    	try {
    		reportPage.monthPicker("January");
    		reportPage.toAndFromDateSelector();    					
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	wait.until(ExpectedConditions.elementToBeClickable
    			(submitBtn)).click();
    }
    
    public String getTransactions() {
        try {
            WebElement transactionsList = wait.until(ExpectedConditions.visibilityOf(transactionTable));
            return transactionsList.getText();
        } catch (TimeoutException e) {
            return "Transaction table not visible.";
        }
    }    
}
