package pageObject;

import java.nio.file.Paths;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EngineeringPage extends BaseComponent {
    
    
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

    @FindBy(xpath = "//input[@type='file']")
    WebElement fileSelection;

    @FindBy(xpath = "//button[normalize-space()='Process File']")
    WebElement processFileBtn;

    @FindBy(xpath = "//div[@class='toast-message']")
    WebElement fileValidityMsg;

    public String clickOption(String option) {
        try {
            switch (option.toLowerCase()) {
                case "encrypt credit token":
                    encryptCreditTokenBtn.click();
                    return processEncryptCreditToken(Paths.get("src", "test", "resources", "bulkEngineering", "bulkVend_EngineeringTemplate_20250528_072022.csv").toAbsolutePath().toString());

                case "verify encrypted token":
                    verifyEncryptedTokenBtn.click();
                    return processVerifyEncryptedToken(Paths.get("src", "test", "resources", "bulkEngineering", "bulkVerify_EngineeringTemplate_20250404_114845.csv").toAbsolutePath().toString());

                case "manufacturing token":
                    manufacturingTokenBtn.click();
                    return processManufacturingToken(Paths.get("src", "test", "resources", "bulkEngineering", "bulkManufacturing_EngineeringTemplate_20250609_192108.csv").toAbsolutePath().toString());

                case "manufacturing key change token":
                    manufacturingKeyChangeTokenBtn.click();
                    return processManufacturingKeyChangeToken(Paths.get("src", "test", "resources", "bulkEngineering", "bulkManufacturingKeyChange_EngineeringTemplate_20250609_192329.csv").toAbsolutePath().toString());

                case "manufacturing mngt function token":
                    manufacturingMngtFunctionTokenBtn.click();
                    return processManufacturingMngtFunctionToken(Paths.get("src", "test", "resources", "bulkEngineering", "bulkManufacturingMgnt_EngineeringTemplate_20250609_192547.csv").toAbsolutePath().toString());

                case "key change token":
                    keyChangeTokenBtn.click();
                    return processKeyChangeToken(Paths.get("src", "test", "resources", "bulkEngineering", "bulkKeyChange_EngineeringTemplate_20250512_201016_T.csv").toAbsolutePath().toString());

                case "management function token":
                    managementFunctionTokenBtn.click();
                    return processManagementFunctionToken(Paths.get("src", "test", "resources", "bulkEngineering", "bulkManage_EngineeringTemplate_20250609_192949.csv").toAbsolutePath().toString());

                case "issue meter test token":
                    issueMeterTestTokenBtn.click();
                    return processIssueMeterTestToken(Paths.get("src", "test", "resources", "bulkEngineering", "bulkIssueTest_EngineeringTemplate.csv").toAbsolutePath().toString());

                default:
                    throw new IllegalArgumentException("Invalid option: " + option);
            }
        } catch (Exception e) {
            return "Exception occurred during '" + option + "': " + e.getMessage();
        }
    }

    

    // Generate Token Methods
    public String processEncryptCreditToken(String filePath) {
        return processTokenFile(filePath);
    }

    public String processVerifyEncryptedToken(String filePath) {
        return processTokenFile(filePath);
    }

    public String processManufacturingToken(String filePath) {
        return processTokenFile(filePath);
    }

    public String processManufacturingKeyChangeToken(String filePath) {
        return processTokenFile(filePath);
    }

    public String  processManufacturingMngtFunctionToken(String filePath) {
        return processTokenFile(filePath);
    }

    public String processKeyChangeToken(String filePath) {
        return processTokenFile(filePath);
    }

    public String processManagementFunctionToken(String filePath) {
        return processTokenFile(filePath);
    }

    public String processIssueMeterTestToken(String filePath) {
        return processTokenFile(filePath);
    }

    public String processTidUpdateToken(String filePath) {
        return processTokenFile(filePath);
    }

//    private String processTokenFile(String filePath) {
//        try {
//            wait.until(ExpectedConditions.elementToBeClickable(bulkVendBtn)).click();
//
//            WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
//            fileInput.sendKeys(filePath);
//
//            wait.until(ExpectedConditions.elementToBeClickable(processFileBtn)).click();
//
//            String msg = getValidityMsg();
//            System.out.println("Message: " + msg);
//
//            return msg;
//
//        } catch (Exception e) {
//            return "Exception: " + e.getMessage();
//        }
//    }
    
    private String processTokenFile(String filePath) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(bulkVendBtn)).click();

            WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
            fileInput.sendKeys(filePath);

            WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='toast-container']/div/div[2]")));
            String firstMsg = toastElement.getText();
            System.out.println("First message: " + firstMsg);

            wait.until(ExpectedConditions.invisibilityOf(toastElement));

            wait.until(ExpectedConditions.elementToBeClickable(processFileBtn)).click();

            WebElement secondToast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='toast-container']/div/div[2]")));

            wait.until(driver -> !secondToast.getText().equals(firstMsg));
            String secondMsg = secondToast.getText();
            System.out.println("Second message: " + secondMsg);


            wait.until(ExpectedConditions.invisibilityOf(secondToast));

            return firstMsg + " | " + secondMsg;

        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }


    public String getValidityMsg() {
        try {
            WebElement msgElement = wait.until(ExpectedConditions.visibilityOf(fileValidityMsg));
            String msgText = msgElement.getText();

            wait.until(ExpectedConditions.invisibilityOf(msgElement));

            return msgText;
        } catch (TimeoutException e) {
            return "No validity message displayed.";
        }
    }

}
