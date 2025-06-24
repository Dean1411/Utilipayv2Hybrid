package pageObject;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.nio.file.Paths;

public class EngineeringPage extends BaseComponent {
    
    private String validityMsg;
    private WebDriverWait wait;

    public EngineeringPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @FindBy(xpath = "//span[normalize-space()='Encrypt Credit Token']")
    WebElement encryptCreditTokenBtn;

    @FindBy(xpath = "//span[normalize-space()='Verify Encrypted Token']")
    WebElement verifyEncryptedTokenBtn;

    @FindBy(xpath = "//span[normalize-space()='Manufacturing Token']")
    WebElement manufacturingTokenBtn;

    @FindBy(xpath = "//span[normalize-space()='Manufacturing Key Change Token']")
    WebElement manufacturingKeyChangeTokenBtn;

    @FindBy(xpath = "//span[normalize-space()='Manufacturing Mngt Function Token']")
    WebElement manufacturingMngtFunctionTokenBtn;//form[@id='dropzone']//input[@id='engFunction']

    @FindBy(xpath = "//span[normalize-space()='Key Change Token']")
    WebElement keyChangeTokenBtn;

    @FindBy(xpath = "//span[normalize-space()='Management Function Token']")
    WebElement managementFunctionTokenBtn;

    @FindBy(xpath = "//span[normalize-space()='Issue Meter Test Token']")
    WebElement issueMeterTestTokenBtn;

    @FindBy(xpath = "//button[normalize-space()='Bulk Vend']")
    WebElement bulkVendBtn;

    @FindBy(xpath = "//div[@class='dz-message needsclick']")
    WebElement fileSelection;

    @FindBy(xpath = "//button[normalize-space()='Process File']")
    WebElement processFileBtn;

    @FindBy(xpath = "//*[@id='toast-container']/div/div[2]")
    WebElement fileValidityMsg;

    public void clickOption(String option) {
    	try {
            switch (option.toLowerCase()) {
            case "encrypt credit token":
                encryptCreditTokenBtn.click();
                processEncryptCreditToken("C:\\Utilipayv2Hybrid\\src\\test\\resources\\bulkEngineering\\bulkVend_EngineeringTemplate_20250528_072022.csv");
                break;
            case "verify encrypted token":
                verifyEncryptedTokenBtn.click();
                processVerifyEncryptedToken("C:\\Utilipayv2Hybrid\\src\\test\\resources\\bulkEngineering\\bulkVerify_EngineeringTemplate_20250404_114845.csv");
                break;
            case "manufacturing token":
                manufacturingTokenBtn.click();
                processManufacturingToken("C:\\Utilipayv2Hybrid\\src\\test\\resources\\bulkEngineering\\bulkManufacturing_EngineeringTemplate_20250609_192108.csv");
                break;
            case "manufacturing key change token":
                manufacturingKeyChangeTokenBtn.click();
                processManufacturingKeyChangeToken("C:\\Utilipayv2Hybrid\\src\\test\\resources\\bulkEngineering\\bulkManufacturingKeyChange_EngineeringTemplate_20250609_192329.csv");
                break;
            case "manufacturing mngt function token":
                manufacturingMngtFunctionTokenBtn.click();
                processManufacturingMngtFunctionToken("C:\\Utilipayv2Hybrid\\src\\test\\resources\\bulkEngineering\\bulkManufacturingMgnt_EngineeringTemplate_20250609_192547.csv");
                break;
            case "key change token":
                keyChangeTokenBtn.click();
                processKeyChangeToken("C:\\Utilipayv2Hybrid\\src\\test\\resources\\bulkEngineering\\bulkKeyChange_EngineeringTemplate_20250512_201016_T.csv");
                //processKeyChangeToken(Paths.get("src", "test", "resources", "bulkEngineering", "bulkKeyChange_EngineeringTemplate_20250624_201016_T.csv").toString());
                break;
            case "management function token":
                managementFunctionTokenBtn.click();
                processManagementFunctionToken("C:\\Utilipayv2Hybrid\\src\\test\\resources\\bulkEngineering\\bulkManage_EngineeringTemplate_20250609_192949.csv");
                break;
            case "issue meter test token":
                issueMeterTestTokenBtn.click();
                processIssueMeterTestToken("");
                break;
            default:
                throw new IllegalArgumentException("Invalid option: " + option);
        }
    	}catch (Exception e) {
    		System.out.println("Exception: " + e);
    	}
    }

    // Token Methods
    public void processEncryptCreditToken(String filePath) {
        processTokenFile(filePath);
    }

    public void processVerifyEncryptedToken(String filePath) {
        processTokenFile(filePath);
    }

    public void processManufacturingToken(String filePath) {
        processTokenFile(filePath);
    }

    public void processManufacturingKeyChangeToken(String filePath) {
        processTokenFile(filePath);
    }

    public void processManufacturingMngtFunctionToken(String filePath) {
        processTokenFile(filePath);
    }

    public void processKeyChangeToken(String filePath) {
        processTokenFile(filePath);
    }

    public void processManagementFunctionToken(String filePath) {
        processTokenFile(filePath);
    }

    public void processIssueMeterTestToken(String filePath) {
        processTokenFile(filePath);
    }

    public void processTidUpdateToken(String filePath) {
        processTokenFile(filePath);
    }

    private void processTokenFile(String filePath) {
    	
    	try {
            wait.until(ExpectedConditions.elementToBeClickable(bulkVendBtn)).click();
            wait.until(ExpectedConditions.visibilityOf(fileSelection)).isEnabled();
            
            wait.until(ExpectedConditions.visibilityOf(fileSelection)).sendKeys(filePath);
            
            wait.until(ExpectedConditions.elementToBeClickable(processFileBtn)).click();
            validityMsg = getValidityMsg();
    	}catch (NoSuchElementException e) {
    		
    		System.out.println("Exception: " + e);
    	}
    }

    public String getValidityMsg() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(fileValidityMsg)).getText();
        } catch (TimeoutException e) {
            return "No validity message displayed.";
        }
    }
}
