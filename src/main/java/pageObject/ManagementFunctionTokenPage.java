package pageObject;

import java.time.Duration;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ManagementFunctionTokenPage extends BaseComponent {

    private WebDriverWait wait;

    public ManagementFunctionTokenPage(WebDriver driver) {    
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @FindBy(xpath="//button[normalize-space()='Bulk Vend']")
    WebElement bulkVendBtn;

    @FindBy(xpath="//input[@id='MeterNumber']")
    WebElement mtrNo;

    @FindBy(xpath="//select[@id='currencyId']")
    WebElement subClass;

    @FindBy(xpath="//input[@id='SGC']")
    WebElement sGC;

    @FindBy(xpath="//input[@id='KRN']")
    WebElement kRN;

    @FindBy(xpath="//input[@id='TI']")
    WebElement tI;

    @FindBy(xpath="//input[@id='KEN']")
    WebElement keyExpiryNo;

    @FindBy(xpath="//select[@id='ea']")
    WebElement encryptAlgorithm;

    @FindBy(xpath="//input[@id='TCT']")
    WebElement tokenCarrrierType;

    @FindBy(xpath="//input[@id='TransferUnits']")
    WebElement transferUnits;

    @FindBy(xpath="//input[@id='KRNUpdate']")
    WebElement allowKrnUpdate;

    @FindBy(xpath="//button[normalize-space()='Submit']")
    WebElement submitBtn;

    @FindBy(xpath="//div[@class='modal-body']//div[@class='mb-4 col-md-12']")
    WebElement modalBody;

    public void performBulkVend() {
        wait.until(ExpectedConditions.elementToBeClickable(bulkVendBtn));
        bulkVendBtn.click();
    }

    public void enterMeterNumber(String meterNumber) {
        mtrNo.sendKeys(meterNumber);
        mtrNo.sendKeys(Keys.TAB);
    }

    public void supplyGroupCode(String sgc) {
        sGC.clear();
        sGC.sendKeys(sgc);
    }

    public void keyRevisionNum(String krn) {
        kRN.clear();
        kRN.sendKeys(krn);
    }

    public void encryptAlgorithm(int value) {
        Select algorithm = new Select(encryptAlgorithm);
        algorithm.selectByIndex(value);
    }

    public void clickAllowKrnUpdate() {
        allowKrnUpdate.click();
    }

    public void selectSubClass(String value) {
        Select dropdown = new Select(subClass);
        dropdown.selectByVisibleText(value);
    }

    public void clickSubmit() {
        submitBtn.click();
    }

    public String getModalBody() {
        wait.until(ExpectedConditions.visibilityOf(modalBody));
        return modalBody.getText();
    }
}
