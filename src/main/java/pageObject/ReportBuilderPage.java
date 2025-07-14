package pageObject;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReportBuilderPage extends BaseComponent {
	
	private WebDriverWait wait;

	public ReportBuilderPage(WebDriver driver) {
		super(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	//Prepaid sales report elements
    @FindBy(xpath = "//span[normalize-space()='Prepaid Sales']")
    WebElement prepaidSales;
    
    @FindBy(xpath = "//button[@id='loadPresets']")
    WebElement loadPreset;
    
    @FindBy(xpath = "//button[@id='backButton']")
    WebElement bckBtn;
    
    @FindBy(xpath = "//select[@id='Municipality']")
    WebElement selectMun;
    
    @FindBy(xpath = "//input[@id='flatpickr-range']")
    WebElement datePicker;
    
    @FindBy(xpath = "//input[@id='SumTotals']")
    WebElement sumTotals;
    
    @FindBy(xpath = "//button[normalize-space()='Column Configuration']")
    WebElement columnConfig;
    
    @FindBy(xpath = "//button[normalize-space()='Generate Preview']")
    WebElement generatePreview;
    
    @FindBy(xpath = "//a[normalize-space()='Reset']")
    WebElement reset;
    
    @FindBy(xpath = "//button[normalize-space()='Generate Report']")
    WebElement generateReport;
    
    @FindBy(xpath = "//div[@class='accordion-body']//div[@class='row']")
    WebElement draggableColumnTable;
    
    
    //Day End Elements
    @FindBy(xpath = "//span[normalize-space()='Day End']")
    WebElement dayEnd;

    @FindBy(xpath = "//span[normalize-space()='Month End']")
    WebElement manufacturingTokenBtn;

    @FindBy(xpath = "//select[@id='Municipality']")
    WebElement municipality;
    
    @FindBy(xpath = "//input[@id='flatpickr-date']")
    WebElement selectDay;

    @FindBy(xpath = "//button[normalize-space()='Generate Preview']")
    WebElement dayEndGeneratePreview;

    @FindBy(xpath = "//a[@id='backButton']")
    WebElement backBtn;
    
    //Month End Elements
    @FindBy(xpath = "//body[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[3]/a[1]/div[1]/label[1]/span[1]")
    WebElement monthEnd;

    @FindBy(xpath = "//input[@id='flatpickr-range']")
    WebElement selectMonth;


    
    
    
    

}
