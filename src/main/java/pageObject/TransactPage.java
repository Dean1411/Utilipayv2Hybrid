package pageObject;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransactPage extends BaseComponent {
	
	private WebDriverWait wait;
	private String originalWindow;
	
	public TransactPage(WebDriver driver) {
		super(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		this.originalWindow = driver.getWindowHandle();
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
	WebElement purchaseBtn;
	
	@FindBy(xpath="//a[normalize-space()='Cancel']")
	WebElement cancelBtn;
	
	@FindBy(xpath="//b[normalize-space()='Free basic Credit: Electricity:']")
	WebElement freeBasicText;
	
	@FindBy(xpath="//div[@class='mb-4 col-md-12']")
	WebElement freeBasicToken;
	
	@FindBy(xpath="//h6[@id='txtError']")
	WebElement errorModal;
	
	public String errorMessage() {
		wait.until(ExpectedConditions.elementToBeClickable(errorModal));
		return errorModal.getText();
	}
	
	public void insert_MtrNum(String mtrNum) {
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
		amount.sendKeys(Amount);
	}
	
	public void clickBreakDown() {
		wait.until(ExpectedConditions.elementToBeClickable(breakDownBtn));
		breakDownBtn.click();
	}
	
//	public String getBreakDown() {
//		wait.until(ExpectedConditions.elementToBeClickable(prepaidBreakDown));
//		return prepaidBreakDown.getText();
//	}
	
	public String getBreakDown() {
	    wait.until(ExpectedConditions.elementToBeClickable(prepaidBreakDown));
	    String fullText = prepaidBreakDown.getText();
	    return extractStepDetails(fullText);
	}

	private String extractStepDetails(String fullText) {
	    if (fullText == null) return "";
	    StringBuilder steps = new StringBuilder();
	    String[] lines = fullText.split("\\r?\\n|\\r");
	    for (String line : lines) {
	        line = line.trim();
	        // Match lines like "4 kl @ R 3 / kl" or "16.6 kl @ R 4 / kl"
	        if (line.matches(".*\\d+\\s*\\w+\\s*@\\s*R\\s*\\d+(\\.\\d+)?\\s*/\\s*\\w+.*")) {
	            steps.append(line).append(" ");
	        }
	    }
	    return steps.toString().trim().replaceAll("\\s+", " ");
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
	
	public void purchase() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(purchaseBtn));
		purchaseBtn.click();
		Thread.sleep(1000);
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
	
//	public String getBreakDownSteps() {
//	    wait.until(ExpectedConditions.visibilityOf(prepaidBreakDown));
//	    WebElement breakdownDiv = driver.findElement(By.id("div_breakdown"));
//	    String fullText = breakdownDiv.getText();
//
//	    String[] lines = fullText.split("\\r?\\n");
//	    StringBuilder purchaseSteps = new StringBuilder();
//
//	    for (String line : lines) {
//	        if (line.trim().equalsIgnoreCase("Free Basic Breakdown")) {
//	            break;
//	        }
//	        if (line.matches(".*@.*\\/.*")) { // Matches lines like "8.7 kwh @ R 5 / kwh"
//	            purchaseSteps.append(line.trim()).append(" ");
//	        }
//	    }
//
//	    return purchaseSteps.toString().trim();
//	}
	
//	public String getBreakDownSteps() {
//	    wait.until(ExpectedConditions.visibilityOf(prepaidBreakDown));
//	    WebElement breakdownDiv = driver.findElement(By.id("div_breakdown"));
//	    String fullText = breakdownDiv.getText();
//
//	    String[] lines = fullText.split("\\r?\\n");
//	    StringBuilder purchaseSteps = new StringBuilder();
//
//	    for (String line : lines) {
//	        if (line.trim().equalsIgnoreCase("Free Basic Breakdown")) {
//	            break;
//	        }
//	        if (line.matches(".*@.*\\/.*")) { // Matches lines like "8.7 kwh @ R 5 / kwh"
//	            purchaseSteps.append(line.trim()).append(" ");
//	        }
//	    }
//
//	    // Normalize whitespace and trim
//	    return purchaseSteps.toString().replaceAll("\\s+", " ").trim();
//	}
	
	public String getBreakDownSteps() {
	    wait.until(ExpectedConditions.visibilityOf(prepaidBreakDown));
	    WebElement breakdownDiv = driver.findElement(By.id("div_breakdown"));
	    String fullText = breakdownDiv.getText();

	    String[] lines = fullText.split("\\r?\\n");
	    StringBuilder purchaseSteps = new StringBuilder();

	    for (String line : lines) {
	        if (line.trim().equalsIgnoreCase("Free Basic Breakdown")) {
	            break;
	        }
	        if (line.matches(".*@.*\\/.*")) { // Matches lines like "8.7 kwh @ R 5 / kwh"
	            purchaseSteps.append(line.trim()).append(" ");
	        }
	    }

	    // Normalize whitespace and trim
	    return purchaseSteps.toString().replaceAll("\\s+", " ").trim();
	}


//	public String getFreeBasicBreakdownSteps() {
//	    wait.until(ExpectedConditions.visibilityOf(prepaidBreakDown));
//	    WebElement breakdownDiv = driver.findElement(By.id("div_breakdown"));
//	    String fullText = breakdownDiv.getText();
//
//	    String[] lines = fullText.split("\\r?\\n");
//	    boolean inFreeBasic = false;
//	    StringBuilder fbSteps = new StringBuilder();
//
//	    for (String line : lines) {
//	        if (line.trim().equalsIgnoreCase("Free Basic Breakdown")) {
//	            inFreeBasic = true;
//	            continue;
//	        }
//	        if (inFreeBasic) {
//	            if (line.toLowerCase().contains("total units")) break;
//	            if (line.matches(".*@.*\\/.*")) {
//	                fbSteps.append(line.trim()).append(" ");
//	            }
//	        }
//	    }
//
//	    return fbSteps.toString().trim();
//	}
	
	public String getFreeBasicBreakdownSteps() {
	    wait.until(ExpectedConditions.visibilityOf(prepaidBreakDown));
	    WebElement breakdownDiv = driver.findElement(By.id("div_breakdown"));
	    String fullText = breakdownDiv.getText();

	    String[] lines = fullText.split("\\r?\\n");
	    boolean inFreeBasic = false;
	    StringBuilder fbSteps = new StringBuilder();

	    for (String line : lines) {
	        if (line.trim().equalsIgnoreCase("Free Basic Breakdown")) {
	            inFreeBasic = true;
	            continue;
	        }
	        if (inFreeBasic) {
	            if (line.toLowerCase().contains("total units")) break;
	            // Only append lines matching unit @ price pattern, trimmed
	            if (line.matches(".*@.*\\/.*")) {
	                fbSteps.append(line.trim()).append(" ");
	            }
	        }
	    }

	    // Normalize whitespace and trim before returning
	    return fbSteps.toString().replaceAll("\\s+", " ").trim();
	}

	public double getUnits() {
	    // Use getBreakDownSteps() to get the paid units breakdown string, e.g. "4 kl @ R 0 / kl 18.8 kl @ R 4 / kl"
	    String steps = getBreakDownSteps();
	    // Extract the total paid units from the steps string (sum of all units)
	    // For example, parse "4 kl" and "18.8 kl" and sum = 22.8
	    return parseTotalUnitsFromSteps(steps);
	}

	public double getFreeBasicUnits() {
	    // Use getFreeBasicBreakdownSteps() to get free basic breakdown string, e.g. "12 kwh @ R 1 / kwh 8 kwh @ R 2 / kwh ..."
	    String freeBasicSteps = getFreeBasicBreakdownSteps();
	    // Extract the total free basic units from the freeBasicSteps string (sum of all units)
	    return parseTotalUnitsFromSteps(freeBasicSteps);
	}

	// Helper method to parse and sum units from a string like "4 kl @ R 0 / kl 18.8 kl @ R 4 / kl"
	private double parseTotalUnitsFromSteps(String steps) {
	    double totalUnits = 0.0;
	    if (steps == null || steps.isEmpty()) return totalUnits;

	    // Split by spaces, find all occurrences of "<number> <unit>", e.g. "4 kl", "18.8 kl"
	    // A regex to match "<number> <unit>" (number could be decimal)
	    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+\\.?\\d*)\\s*\\w+");
	    java.util.regex.Matcher matcher = pattern.matcher(steps);

	    while (matcher.find()) {
	        String numberStr = matcher.group(1);
	        try {
	            totalUnits += Double.parseDouble(numberStr);
	        } catch (NumberFormatException e) {
	            // ignore parsing errors
	        }
	    }
	    return totalUnits;
	}

	
//	public String getFreeBasicBreakdownSteps() {
//	    wait.until(ExpectedConditions.visibilityOf(prepaidBreakDown));
//	    WebElement breakdownDiv = driver.findElement(By.id("div_breakdown"));
//	    String fullText = breakdownDiv.getText();
//
//	    String[] lines = fullText.split("\\r?\\n");
//	    boolean inFreeBasic = false;
//	    StringBuilder fbSteps = new StringBuilder();
//
//	    for (String line : lines) {
//	        if (line.trim().equalsIgnoreCase("Free Basic Breakdown")) {
//	            inFreeBasic = true;
//	            continue;
//	        }
//	        if (inFreeBasic) {
//	            if (line.toLowerCase().contains("total units")) break;
//	            if (line.matches(".*@.*\\/.*")) {
//	                fbSteps.append(line.trim()).append(" ");
//	            }
//	        }
//	    }
//
//	    // Normalize whitespace and trim
//	    return fbSteps.toString().replaceAll("\\s+", " ").trim();
//	}
//	
//	public double getUnits() {
//	    // Use getBreakDownSteps() to get the paid units breakdown string, e.g. "4 kl @ R 0 / kl 18.8 kl @ R 4 / kl"
//	    String steps = getBreakDownSteps();
//	    // Extract the total paid units from the steps string (sum of all units)
//	    // For example, parse "4 kl" and "18.8 kl" and sum = 22.8
//	    return parseTotalUnitsFromSteps(steps);
//	}
//
//	public double getFreeBasicUnits() {
//	    // Use getFreeBasicBreakdownSteps() to get free basic breakdown string, e.g. "12 kwh @ R 1 / kwh 8 kwh @ R 2 / kwh ..."
//	    String freeBasicSteps = getFreeBasicBreakdownSteps();
//	    // Extract the total free basic units from the freeBasicSteps string (sum of all units)
//	    return parseTotalUnitsFromSteps(freeBasicSteps);
//	}
//
//	// Helper method to parse and sum units from a string like "4 kl @ R 0 / kl 18.8 kl @ R 4 / kl"
//	private double parseTotalUnitsFromSteps(String steps) {
//	    double totalUnits = 0.0;
//	    if (steps == null || steps.isEmpty()) return totalUnits;
//
//	    // Split by spaces, find all occurrences of "<number> <unit>", e.g. "4 kl", "18.8 kl"
//	    // A regex to match "<number> <unit>" (number could be decimal)
//	    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+\\.?\\d*)\\s*\\w+");
//	    java.util.regex.Matcher matcher = pattern.matcher(steps);
//
//	    while (matcher.find()) {
//	        String numberStr = matcher.group(1);
//	        try {
//	            totalUnits += Double.parseDouble(numberStr);
//	        } catch (NumberFormatException e) {
//	            // ignore parsing errors
//	        }
//	    }
//	    return totalUnits;
//	}

}
