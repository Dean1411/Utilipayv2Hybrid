package pageObject;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;




public class Import_ExportPage extends BaseComponent {
	
	private WebDriverWait wait;

	
	public Import_ExportPage(WebDriver driver) {
		
		super(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		
	}
	
	
    @FindBy(xpath="//button[normalize-space()='Export Data']")
    WebElement exportData;
    
    @FindBy(xpath="//button[normalize-space()='Import Data']")
    WebElement importData;
    
    //Import
    @FindBy(xpath="//select[@name='FinacialSystem']")
    WebElement importfinancialSystem;//select[@id='FinacialSystem']
    
    @FindBy(xpath="//select[@id='municipal']")
    WebElement importMunicipality;
    
    @FindBy(xpath="//input[@id='fileInput']")
    WebElement fileinput;
    
    @FindBy(xpath="//button[normalize-space()='Import']")
    WebElement importBtn;
    
    @FindBy(xpath="//button[normalize-space()='Cancel']")
    WebElement cancelBtn;
    
    //Export
    @FindBy(xpath="//select[@id='FinacialSystem']")
    WebElement exportfinancialSystem;
    
    @FindBy(xpath="//select[@id='Municipal']")
    WebElement exportMunicipality;
    
    @FindBy(xpath="//input[@id='flatpickr-range']")
    WebElement dateRange;
    
    @FindBy(xpath="//button[normalize-space()='Import']")
    WebElement exportBtn;
    
    @FindBy(xpath="//*[@id=\"toast-container\"]/div")
    WebElement toasterMsg;
    
    
    
    public void option(String function) {
 	
    	try {
        	switch(function.toLowerCase()) {
           	
        	case "import":
        		importFile("EMS", "Dean Municipality");
        		break;
        	case "export":
        		exportFile("UtiliPay","Test Municipality");
        		default:
        			System.out.println("Invalid function selection");
        			break;
        	}
    	}catch (Exception ex) {
    		
    		System.out.println("Exception: " + ex);
    	}

    }
    
    public void importFile(String finSys, String mun) {
    	
    	try {
    		wait.until(ExpectedConditions.elementToBeClickable(importData));
    		importData.click();
    		
    		Select importFinOption = new Select(importfinancialSystem);
    		wait.until(ExpectedConditions.elementToBeClickable(importfinancialSystem));
    		importFinOption.selectByValue(finSys);
    		
    		Select importMun = new Select(importMunicipality);
    		wait.until(ExpectedConditions.elementToBeClickable(importMunicipality));
    		importMun.selectByVisibleText(mun);
    		
    		//Add file selection
    		fileinput.sendKeys(Import_ExportPage.getFilePath("Dean Mun/90_EMS_20250226 2_Dean_Fixed.csv"));
    		
    		//Below line is to fail test
//    		fileinput.sendKeys(Import_ExportPage.getFilePath("Dean Mun/90_EMS_20250226 2_Dean.csv"));


    				
    		wait.until(ExpectedConditions.elementToBeClickable(importBtn));
    		importBtn.click();
    		
    		Thread.sleep(1000);
    		getMsg();
    		
    	}catch (Exception ex) {
    		
    		System.out.println("Exception: " +ex.getMessage());
    	}
    }
    
    public void exportFile(String finSys, String mun) {
    	
		wait.until(ExpectedConditions.elementToBeClickable(exportData));
		exportData.click();
    	
        int year = java.time.LocalDate.now().getYear();
        int month = java.time.LocalDate.now().getMonthValue();

        
        String[] dates = getMonthStartAndEndDate(year, month);
        String startDate = dates[0];
        String endDate = dates[1];   

        
        String dateRangeValue = startDate + " to " + endDate;
    	
		
		Select finSystem = new Select(exportfinancialSystem);
		finSystem.selectByVisibleText(finSys);
		
		Select exportMun = new Select(exportMunicipality);
		exportMun.selectByVisibleText(mun);
		
		dateRange.sendKeys(dateRangeValue);
		dateRange.sendKeys(Keys.TAB);
		
		wait.until(ExpectedConditions.elementToBeClickable(exportBtn));
		exportBtn.click();
    }
    
    public static String[] getMonthStartAndEndDate(int year, int month) {
        YearMonth selectedMonth = YearMonth.of(year, month);

        LocalDate firstDay = selectedMonth.atDay(1);
        LocalDate lastDay = selectedMonth.atEndOfMonth();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedFirstDay = firstDay.format(formatter);
        String formattedLastDay = lastDay.format(formatter);

        return new String[]{formattedFirstDay, formattedLastDay};
    }
    
    public String getMsg() {
        try {
            WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='toast-message']")));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            String msg = (String) js.executeScript("return arguments[0].innerText;", toastElement);

            return msg;
        } catch (Exception ex) {
            System.err.println("Failed to get toaster message: " + ex.getMessage());
            return null;
        }
    }


    
	public static String getFilePath(String fileName) {
		
		File file = new File("src/test/resources/ImportFiles/" + fileName);
		
		if(file.exists()) {
			
			return file.getAbsolutePath();
		}else {
			
			throw new RuntimeException("File not found: " + file.getAbsolutePath());
		}
	}
    

}
