package pageObject;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Select getSelect(WebElement dropdown) {
        return new Select(dropdown);
    }

    public Select getSelect(By locator) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(locator));
        return new Select(dropdown);
    }

    @FindBy(xpath = "//button[normalize-space()='Export Data']")
    WebElement exportData;

    @FindBy(xpath = "//button[normalize-space()='Import Data']")
    WebElement importData;

    // Import
    @FindBy(xpath = "//select[@name='FinacialSystem']")
    WebElement importfinancialSystem;

    @FindBy(xpath = "//select[@id='municipal']")
    WebElement importMunicipality;

    @FindBy(xpath = "//input[@id='fileInput']")
    WebElement fileinput;

    @FindBy(xpath = "//button[normalize-space()='Import']")
    WebElement importBtn;

    @FindBy(xpath = "//button[normalize-space()='Cancel']")
    WebElement cancelBtn;

    // Export
    @FindBy(xpath = "//select[@id='financialSystem']")
    WebElement exportfinancialSystem;
    
    @FindBy(xpath = "//input[@name='PlatformChannels']")
    List<WebElement> platformCheckboxes;

    @FindBy(xpath = "//select[@id='municipal']")
    WebElement exportMunicipality;

    @FindBy(xpath = "//input[@id='flatpickr-range']")
    WebElement dateRange;

    @FindBy(xpath = "//button[@id='exportBtn']")
    WebElement exportBtn;

    @FindBy(xpath = "//*[@id=\"toast-container\"]/div")
    WebElement toasterMsg;

    public void option(String function) {
        try {
            switch (function.toLowerCase()) {
                case "import":
                    importFile("EMS", "Dean Municipality");
                    break;
                case "export":
                    exportFile("UtiliPay", "Test Municipality", "Web");
                    break;
                default:
                    System.out.println("Invalid function selection");
                    break;
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
    }

    public void importFile(String finSys, String mun) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(importData));
            importData.click();

            Select importFinOption = getSelect(importfinancialSystem);
            wait.until(ExpectedConditions.elementToBeClickable(importfinancialSystem));
            importFinOption.selectByValue(finSys);

            Select importMun = getSelect(importMunicipality);
            wait.until(ExpectedConditions.elementToBeClickable(importMunicipality));
            importMun.selectByVisibleText(mun);

            fileinput.sendKeys(getFilePath("Dean Mun/90_EMS_20250226 2_Dean_Fixed.csv"));

            wait.until(ExpectedConditions.elementToBeClickable(importBtn));
            importBtn.click();

            Thread.sleep(1000);
            getMsg();

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    public void exportFile(String finSys, String mun, String option1) {
        wait.until(ExpectedConditions.elementToBeClickable(exportData));
        exportData.click();
        
        Select finSystem = getSelect(exportfinancialSystem);
        wait.until(ExpectedConditions.elementToBeClickable(exportfinancialSystem));
        finSystem.selectByVisibleText(finSys);
        
        selectOnlyPlatformOptions(option1);

        Select exportMun = getSelect(exportMunicipality);
        wait.until(ExpectedConditions.elementToBeClickable(exportMunicipality));
        exportMun.selectByVisibleText(mun);
        
        exportDateRange();

        wait.until(ExpectedConditions.elementToBeClickable(exportBtn));
        exportBtn.click();
    }

    public static String[] getMonthStartAndEndDate(int year, int month) {
        YearMonth selectedMonth = YearMonth.of(year, month);
        LocalDate firstDay = selectedMonth.atDay(1);
        LocalDate lastDay = selectedMonth.atEndOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return new String[]{firstDay.format(formatter), lastDay.format(formatter)};
    }
    
    public void exportDateRange() {
    	
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        String[] dates = getMonthStartAndEndDate(year, month);
        String startDate = dates[0];
        String endDate = dates[1];
        String dateRangeValue = startDate + " to " + endDate;

        dateRange.sendKeys(dateRangeValue);
        dateRange.sendKeys(Keys.TAB);
    }
    
    public void selectOnlyPlatformOptions(String... desired) {
        Set<String> keep = Arrays.stream(desired)
                                 .map(String::toLowerCase)
                                 .collect(Collectors.toSet());

        for (WebElement cb : platformCheckboxes) {
            String value = cb.getAttribute("value").toLowerCase();

            if (keep.contains(value) && !cb.isSelected()) {
                cb.click();                               
            }
            if (!keep.contains(value) && cb.isSelected()) {
                cb.click();                              
            }
        }
    }


    public String getMsg() {
        try {
            WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='toast-message']")));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            return (String) js.executeScript("return arguments[0].innerText;", toastElement);
        } catch (Exception ex) {
            System.err.println("Failed to get toaster message: " + ex.getMessage());
            return null;
        }
    }

    public static String getFilePath(String fileName) {
        File file = new File("src/test/resources/ImportFiles/" + fileName);
        if (file.exists()) {
            return file.getAbsolutePath();
        } else {
            throw new RuntimeException("File not found: " + file.getAbsolutePath());
        }
    }
}
