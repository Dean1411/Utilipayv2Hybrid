package pageObject;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.*;



public class ReportBuilderPage extends BaseComponent {

    private WebDriverWait wait;
    private boolean customeDone = false;
    //private ScrollUtils scroll;

    public ReportBuilderPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //this.scroll = new ScrollUtils(driver);
    }


    // Elements
    @FindBy(xpath = "//select[@id='dpdReportFormat']")
    WebElement reportFormat;

    @FindBy(xpath = "//span[normalize-space()='Prepaid Sales']")
    WebElement prepaidSales;

    @FindBy(xpath = "//select[@id='Municipality']")
    WebElement selectMun;

    @FindBy(xpath = "//input[@id='flatpickr-range']")
    WebElement datePicker;

    @FindBy(xpath = "//select[@aria-label='Month']")
    WebElement mnthSelection;

    @FindBy(xpath = "//input[@id='SumTotals']")
    WebElement sumTotals;

    @FindBy(xpath = "//button[normalize-space()='Generate Preview']")
    WebElement generatePreview;

    @FindBy(xpath = "//tbody")
    WebElement transactionsBody;

    @FindBy(xpath = "//button[normalize-space()='Generate Report']")
    WebElement generateReport;

    @FindBy(xpath = "//span[normalize-space()='Day End']")
    WebElement dayEnd;

    @FindBy(xpath = "//span[normalize-space()='Month End']")
    WebElement monthEnd;

    @FindBy(xpath = "//input[@id='flatpickr-date']")
    WebElement selectDay;

    @FindBy(xpath = "//button[normalize-space()='Generate Preview']")
    WebElement dayEndGeneratePreview;

    @FindBy(xpath = "//input[@id='DateRange']")
    WebElement selectMonth;

    @FindBy(xpath = "//span[normalize-space()='Low Purchase']")
    WebElement lowPurchase;

    @FindBy(xpath = "//input[@id='Amount']")
    WebElement lowPurchaseAmount;

    @FindBy(xpath = "//span[normalize-space()='Free Basic Issued']")
    WebElement freeBasic;

    @FindBy(xpath = "//span[normalize-space()='Arrears Recovered']")
    WebElement arrearsRecovered;

    @FindBy(xpath = "//span[normalize-space()='Create Report']")
    WebElement customReport;

    @FindBy(xpath = "//button[normalize-space()='Submit']")
    WebElement submitBtn;

    @FindBy(xpath = "//div[contains(@class, 'toast-message')]")
    WebElement statusMsg;
    
    //Custome Report
    @FindBy(xpath = "//select[@id='OrderBy']")
    WebElement orderBy;

    @FindBy(xpath = "//select[@id='GroupBy']")
    WebElement groupBy;

    @FindBy(xpath = "//select[@id='dpdFilterBy']")
    WebElement filterBy;

    @FindBy(xpath = "//button[normalize-space()='Column Configuration']")
    WebElement columnConfigBtn;

    @FindBy(xpath = "//button[normalize-space()='Generate Preview']")
    WebElement generatePreviewBtn;
    
    @FindBy(xpath = "//div[@id='ReportColumns']")
    WebElement rightColumns;
    
    @FindBy(xpath = "//div[@id='columnBank']")
    WebElement leftColumns;
    
    @FindBy(xpath = "//button[normalize-space()='Save As Preset']")
    WebElement saveCustomeReportBtn;
    
    @FindBy(xpath = "//input[@id='presetName']")
    WebElement customeReportName;
    
    @FindBy(xpath = "//button[normalize-space()='Submit']")
    WebElement submitCustomReport;//tbody
    

    public void selectReport(String report) {
        boolean isCustomReport = false;
        String actualMsg = "";

        switch (report.toLowerCase()) {
            case "prepaid sales":
                wait.until(ExpectedConditions.elementToBeClickable(prepaidSales)).click();
                prepaidSales();
                break;
            case "day end":
                wait.until(ExpectedConditions.elementToBeClickable(dayEnd)).click();
                dayEnd();
                break;
            case "month end":
                wait.until(ExpectedConditions.elementToBeClickable(monthEnd)).click();
                monthEnd("Jan");
                break;
            case "low purchase":
                wait.until(ExpectedConditions.elementToBeClickable(lowPurchase)).click();
                lowPurchase(100);
                break;
            case "free basic":
                wait.until(ExpectedConditions.elementToBeClickable(freeBasic)).click();
                freeBasicIssued("Jan");
                break;
            case "arrears recovered":
                wait.until(ExpectedConditions.elementToBeClickable(arrearsRecovered)).click();
                arrearsRecovered();
                break;
            case "custom report":
                scrollToElement(customReport);
                wait.until(ExpectedConditions.elementToBeClickable(customReport)).click();

                List<String> selectedColumns = Arrays.asList(
                    "Customer Name",
                    "Account Number",
                    "Credit Token",
                    "Transaction Date"
                );

                try {
                    organizeCustomeReportColumns(selectedColumns);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                isCustomReport = true;

                // ✅ Assert the correct message for custom report
                actualMsg = statusMessage();
                String expectedCustomMsg = "Preset successfully saved!";
                System.out.println("Status for custom report: " + actualMsg);
                org.testng.Assert.assertEquals(actualMsg, expectedCustomMsg,
                    "Status message mismatch for custom report");
                break;

            default:
                System.out.println("Invalid report selection: " + report);
        }

        // Assert the non-custom message only for non-custom reports
        if (!isCustomReport) {
            String expectedMsg = "Your report is being prepared. you will receive an email ones it is ready!";
            actualMsg = statusMessage();
            System.out.println("Status for " + report + ": " + actualMsg);
            org.testng.Assert.assertEquals(actualMsg, expectedMsg,
                "Status message mismatch for: " + report);
        }
    }



    // Prepaid Sales Report
    public void prepaidSales() {
        try {
            selectMunicipality("Karoo Hoogland");
            wait.until(ExpectedConditions.elementToBeClickable(datePicker)).click();
            monthPicker("January");
            toAndFromDateSelector();
            wait.until(ExpectedConditions.elementToBeClickable(generatePreview)).click();

            if (transactionsBody.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", generateReport);
                Thread.sleep(300);
                wait.until(ExpectedConditions.elementToBeClickable(generateReport)).click();
                System.out.println("Transaction list:\n" + transactionsBody.getText());
                reportFormat("CSV");
                System.out.println("Status: " + statusMessage() + "\n");
            } else {
                System.out.println("No Transactions for current range");
            }
        } catch (Exception e) {
            System.out.println("Error in prepaidSales: " + e.getMessage());
        }
    }

    // Low Purchase 
    public void lowPurchase(double amount) {
        try {
            selectMunicipality("Karoo Hoogland");
            wait.until(ExpectedConditions.elementToBeClickable(datePicker)).click();
            monthPicker("January");
            toAndFromDateSelector();

            WebElement lpAmount = wait.until(ExpectedConditions.elementToBeClickable(lowPurchaseAmount));
            lpAmount.clear();
            lpAmount.sendKeys(String.valueOf((int) amount));

            wait.until(ExpectedConditions.elementToBeClickable(generatePreview)).click();

            if (transactionsBody.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", generateReport);
                Thread.sleep(300);
                wait.until(ExpectedConditions.elementToBeClickable(generateReport)).click();
                System.out.println("Transaction list:\n" + transactionsBody.getText());
                reportFormat("CSV");
                System.out.println("Status: " + statusMessage()+ "\n");
            } else {
                System.out.println("No Transactions for current range");
            }
        } catch (Exception e) {
            System.out.println("Error in lowPurchase: " + e.getMessage());
        }
    }

    // Day End Report
    public void dayEnd() {
        try {
            selectMunicipality("Karoo Hoogland");
            wait.until(ExpectedConditions.elementToBeClickable(selectDay)).click();
            singleDayPicker("2");
            wait.until(ExpectedConditions.elementToBeClickable(generatePreview)).click();

            if (transactionsBody.isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(generateReport)).click();
                System.out.println("Transaction list:\n" + transactionsBody.getText());
                reportFormat("CSV");
                System.out.println("Status: " + statusMessage()+ "\n");
            } else {
                System.out.println("No Transactions for current range");
            }
        } catch (Exception e) {
            System.out.println("Error in dayEnd: " + e.getMessage());
        }
    }

    // Month End Report
    public void monthEnd(String month) {
        try {
            selectMunicipality("Karoo Hoogland");
            wait.until(ExpectedConditions.elementToBeClickable(selectMonth)).click();
            selectMonth(month);
            wait.until(ExpectedConditions.elementToBeClickable(generatePreview)).click();

            if (transactionsBody.isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(generateReport)).click();
                System.out.println("Transaction list:\n" + transactionsBody.getText());
                reportFormat("CSV");
                System.out.println("Status: " + statusMessage()+ "\n");
            } else {
                System.out.println("No Transactions for current range");
            }
        } catch (Exception e) {
            System.out.println("Error in monthEnd: " + e.getMessage());
        }
    }

    // Free Basic Issued Report
    public void freeBasicIssued(String month) {
        try {
            selectMunicipality("Karoo Hoogland");
            wait.until(ExpectedConditions.elementToBeClickable(selectMonth)).click();
            selectMonth(month);
            wait.until(ExpectedConditions.elementToBeClickable(generatePreview)).click();

            if (transactionsBody.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", generateReport);
                Thread.sleep(300);
                wait.until(ExpectedConditions.elementToBeClickable(generateReport)).click();
                System.out.println("Transaction list:\n" + transactionsBody.getText());
                reportFormat("CSV");
                System.out.println("Status: " + statusMessage()+ "\n");
            } else {
                System.out.println("No Transactions for current range");
            }
        } catch (Exception e) {
            System.out.println("Error in freeBasicIssued: " + e.getMessage());
        }
    }

    // Arrears Recovered Report
    public void arrearsRecovered() {
        try {
            selectMunicipality("Karoo Hoogland");
            wait.until(ExpectedConditions.elementToBeClickable(datePicker)).click();
            monthPicker("January");
            toAndFromDateSelector();
            wait.until(ExpectedConditions.elementToBeClickable(generatePreview)).click();

            if (transactionsBody.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", generateReport);
                
                wait.until(ExpectedConditions.elementToBeClickable(generateReport)).click();
                System.out.println("Transaction list:\n" + transactionsBody.getText());
                Thread.sleep(100);
                reportFormat("CSV");
                System.out.println("Status: " + statusMessage()+ "\n");
            } else {
                System.out.println("No Transactions for current range");
            }
        } catch (Exception e) {
            System.out.println("Error in arrearsRecovered: " + e.getMessage());
        }
    }

    // Support Methods
    public void monthPicker(String month) {
        Select monthSelection = new Select(mnthSelection);
        wait.until(ExpectedConditions.elementToBeClickable(datePicker)).click();
        for (WebElement mnth : monthSelection.getOptions()) {
            if (mnth.getText().contains(month)) {
                mnth.click();
                break;
            }
        }
    }

    public void toAndFromDateSelector() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(datePicker)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("flatpickr-calendar")));
        String dayXPath = "//span[contains(@class,'flatpickr-day') and " +
                          "not(contains(@class,'prevMonthDay')) and " +
                          "not(contains(@class,'nextMonthDay')) and " +
                          "not(contains(@class,'disabled'))]";
        for (String targetDay : new String[]{"1", "31"}) {
            List<WebElement> days = driver.findElements(By.xpath(dayXPath));
            for (WebElement day : days) {
                if (day.getText().trim().equals(targetDay)) {
                    day.click();
                    break;
                }
            }
            Thread.sleep(300);
        }
    }
    
    public void toAndFromDateSelector2(String fromDay, String toDay) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(datePicker)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("flatpickr-calendar")));

        String dayXPath = "//span[contains(@class,'flatpickr-day') and " +
                          "not(contains(@class,'prevMonthDay')) and " +
                          "not(contains(@class,'nextMonthDay')) and " +
                          "not(contains(@class,'disabled'))]";

        for (String targetDay : new String[]{fromDay, toDay}) {
            List<WebElement> days = driver.findElements(By.xpath(dayXPath));
            for (WebElement day : days) {
                if (day.getText().trim().equals(targetDay)) {
                    day.click();
                    break;
                }
            }
            Thread.sleep(300);
        }
    }


    public void selectMonth(String targetMonth) {
        WebElement monthContainer = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='flatpickr-monthSelect-months']"))
        );
        List<WebElement> months = monthContainer.findElements(By.xpath(".//span[contains(@class,'flatpickr-monthSelect-month')]"));
        for (WebElement month : months) {
            if (month.getText().trim().equalsIgnoreCase(targetMonth)) {
                month.click();
                break;
            }
        }
    }

    public void singleDayPicker(String selectDay) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("flatpickr-calendar")));
        WebElement leftArrow = driver.findElement(By.xpath("//span[@class='flatpickr-prev-month']"));
        String month = driver.findElement(By.xpath("//span[@class='cur-month']")).getText();
        while (!month.equals("January")) {
            leftArrow.click();
            Thread.sleep(300);
            month = driver.findElement(By.xpath("//span[@class='cur-month']")).getText();
        }
        String dayXPath = "//span[contains(@class,'flatpickr-day') and " +
                          "not(contains(@class,'prevMonthDay')) and " +
                          "not(contains(@class,'nextMonthDay')) and " +
                          "not(contains(@class,'disabled'))]";
        List<WebElement> days = driver.findElements(By.xpath(dayXPath));
        for (WebElement day : days) {
            if (day.getText().trim().equals(selectDay)) {
                day.click();
                break;
            }
        }
        Thread.sleep(300);
    }

    public void selectMunicipality(String mun) {
        Select municipality = new Select(selectMun);
        for (WebElement option : municipality.getOptions()) {
            if (option.getText().contains(mun)) {
                option.click();
                break;
            }
        }
    }

    public void reportFormat(String format) {
    	
        Select option = new Select(reportFormat);
        wait.until(ExpectedConditions.elementToBeClickable(reportFormat));
        option.selectByVisibleText(format);
        wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
    }
    
    //status msg
    public String statusMessage() {
        try {
            WebDriverWait toastWait = new WebDriverWait(driver, Duration.ofSeconds(6));

            WebElement toast = toastWait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.cssSelector("div.toast-message")));

            String message = toast.getText().trim();
            System.out.println("Fetched status message: " + message);

            return message;

        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for status message.");
            return "No status message displayed.";
        }
    }
    
    public void customReportParams() throws InterruptedException {
    	selectMunicipality("Karoo Hoogland");
        monthPicker("January");
        toAndFromDateSelector();
    }
    
    public void organizeCustomeReportColumns(List<String> selectedFields) throws InterruptedException {
    	customReportParams();
        wait.until(ExpectedConditions.elementToBeClickable(columnConfigBtn)).click();

        List<WebElement> allLeftFields = leftColumns.findElements(By.xpath(".//li"));
        WebElement rightList = rightColumns.findElement(By.xpath(".//ul")); 
        
        reversedActions();

        Actions actions = new Actions(driver);

        for (String fieldName : selectedFields) {
            boolean moved = false;

            for (WebElement field : allLeftFields) {
                String name = field.findElement(By.tagName("span")).getText().trim();

                if (name.equalsIgnoreCase(fieldName)) {
                    WebElement handle = field.findElement(By.cssSelector("i.drag-handle"));

                    actions.clickAndHold(handle)
                           .pause(Duration.ofMillis(200)) 
                           .moveToElement(rightList, 10, 10)
                           .pause(Duration.ofMillis(200))
                           .release()
                           .build()
                           .perform();

                    System.out.println("Moved field: " + name);
                    Thread.sleep(800);
                    moved = true;
                    break;
                }
            }

            if (!moved) {
                System.out.println("Field not found in left column: " + fieldName);
            }
        }
        
        Thread.sleep(500);
        scrollToBottom();
        
        generateAndSaveReport("Automation Report");
    }
    
    public void generateAndSaveReport(String reportName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(generatePreviewBtn)).click();

            if (transactionsBody.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveCustomeReportBtn);
                Thread.sleep(300);
                System.out.println("Transaction list:\n" + transactionsBody.getText());

                wait.until(ExpectedConditions.elementToBeClickable(saveCustomeReportBtn)).click();

                WebElement name = wait.until(ExpectedConditions.visibilityOf(customeReportName));
                name.sendKeys(reportName);

                wait.until(ExpectedConditions.elementToBeClickable(submitCustomReport)).click();
                System.out.println("Status: " + statusMessage());
                System.out.println();
            } else {
                System.out.println("No Transactions for current range");
            }
        } catch (Exception e) {
            System.out.println("Error in generateAndSaveReport: " + e.getMessage());
        }
    }

    
    public void scrollToElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    
    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            try {
                Thread.sleep(800); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long newHeight = (long) js.executeScript("return document.body.scrollHeight");
            if (newHeight == lastHeight) {
                break; // reached bottom
            }
            lastHeight = newHeight;
        }
    }

    
    public void scrollBy(int pixels) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
        try { Thread.sleep(500); } catch (InterruptedException e) {}
    }

    
    public void reversedActions() throws InterruptedException {
    	Actions actions = new Actions(driver);
    	
    	

    	List<WebElement> allRightFields = rightColumns.findElements(By.xpath(".//li"));
    	WebElement leftList = leftColumns.findElement(By.xpath(".//ul"));

    	for (WebElement field : allRightFields) {
    	    String name = field.findElement(By.tagName("span")).getText().trim();
    	    
            if (name.equalsIgnoreCase("Meter Number")) {
                System.out.println("Skipping field: " + name);
                continue;
            }
            
    	    WebElement handle = field.findElement(By.cssSelector("i.drag-handle"));

    	    actions.clickAndHold(handle)
    	           .pause(Duration.ofMillis(200))
    	           .moveToElement(leftList, 10, 10)
    	           .pause(Duration.ofMillis(200))
    	           .release()
    	           .build()
    	           .perform();

    	    System.out.println("Moved field: " + name);
    	    Thread.sleep(800);
    	}

    }







}
