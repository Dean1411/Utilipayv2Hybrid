package pageObject;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConsumerRecordsPage extends BaseComponent{
	
	private WebDriverWait wait;
	
	public ConsumerRecordsPage(WebDriver driver) {
		
		super(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	@FindBy(xpath="//div[@id='DataTables_Table_0_wrapper']") 
	WebElement dataTable; 
	
	@FindBy(xpath="//div[@data-i18n='User Management']")
	WebElement userMngmnt;

}
