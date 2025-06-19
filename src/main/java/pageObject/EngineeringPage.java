package pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class EngineeringPage extends BaseComponent {

    public EngineeringPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath="//span[normalize-space()='Encrypt Credit Token']")
    WebElement encrCrTkn;

    @FindBy(xpath="//span[normalize-space()='Verify Encrypted Token']")
    WebElement verifyEncryptedToken;

    @FindBy(xpath="//span[normalize-space()='Manufacturing Token']")
    WebElement manufacturingToken;

    @FindBy(xpath="//span[normalize-space()='Manufacturing Key Change Token']")
    WebElement manufacturingKeyChangeToken;

    @FindBy(xpath="//span[normalize-space()='Manufacturing Mngt Function Token']")
    WebElement manufacturingMngtFunctionToken;

    @FindBy(xpath="//span[normalize-space()='Key Change Token']")
    WebElement keyChangeToken;

    @FindBy(xpath="//span[normalize-space()='Management Function Token']")
    WebElement managementFunctionToken;

    @FindBy(xpath="//span[normalize-space()='Issue Meter Test Token']")
    WebElement issueMeterTestToken;

    public void clickOption(String option) {
        switch (option.toLowerCase()) {
            case "encrypt credit token":
                encrCrTkn.click();
                break;
            case "verify encrypted token":
                verifyEncryptedToken.click();
                break;
            case "manufacturing token":
                manufacturingToken.click();
                break;
            case "manufacturing key change token":
                manufacturingKeyChangeToken.click();
                break;
            case "manufacturing mngt function token":
                manufacturingMngtFunctionToken.click();
                break;
            case "key change token":
                keyChangeToken.click();
                break;
            case "management function token":
                managementFunctionToken.click();
                break;
            case "issue meter test token":
                issueMeterTestToken.click();
                break;
            default:
                throw new IllegalArgumentException("Invalid option: " + option);
        }
    }
}
