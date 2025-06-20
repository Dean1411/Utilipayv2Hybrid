package pageObject;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserManagementPage extends BaseComponent {
	
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	
	public UserManagementPage(WebDriver driver) {
		super(driver);
	}
	
	
	@FindBy(xpath="//button[@id='addNew']")
	WebElement addNwUsr;
	
	@FindBy(xpath="//input[@id='fullname']")
	WebElement fllName; 
	
	@FindBy(xpath="//input[@id='email']")
	WebElement eMail;
	
	@FindBy(xpath="//input[@id='phoneNumber']")
	WebElement phnNum;
	
	@FindBy(xpath="//span[@class='selection']")
	public
	WebElement selectMunicipality;
	
	@FindBy(css="body > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > form:nth-child(3) > div:nth-child(5)")
	WebElement roles; 
	
	@FindBy(xpath="//button[normalize-space()='Save changes']")
	WebElement saveChngsBtn;
	
	
	//Engineering Role
	@FindBy(id="Engineering")
	WebElement engOption; 
	
	@FindBy(xpath="//*[@id=\"toast-container\"]/div/div[2]")
	WebElement toaster;
	
	@FindBy(xpath="//label[normalize-space()='Search:']")
	WebElement search;
	
	@FindBy(xpath="//*[@id=\"82ea859e-b64c-43be-9aae-2d9d0f345059\"]/i")
	WebElement editUser;
	
	@FindBy(xpath="//button[normalize-space()='Save']")
	public
	WebElement save;
	
	@FindBy(xpath="//*[@id=\"toast-container\"]/div/div[2]")
	WebElement usrMngmntToaster;//*[@id=\\\"toast-container\\\"]/div/div[2]
	
	
	
	
	public void click_addNwUsr() {
		
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    wait.until(ExpectedConditions.elementToBeClickable(addNwUsr));
	    
		addNwUsr.click();
	}
	
	public void enter_FullName(String fName) {
		
		wait.until(ExpectedConditions.elementToBeClickable(fllName));
		fllName.sendKeys(fName);
	}
	
	public void enter_Email(String email) {
		
		wait.until(ExpectedConditions.elementToBeClickable(eMail));
		eMail.sendKeys(email);
	}
	
	public void enter_PhnNmbr(String pNum) {
		
		wait.until(ExpectedConditions.elementToBeClickable(phnNum));
		phnNum.sendKeys(pNum);
	}
	
	public void selectMun(String clientName) throws InterruptedException {
		
		try {
		    JavascriptExecutor js = (JavascriptExecutor) driver;
		    js.executeScript("arguments[0].click();", selectMunicipality);

		    Thread.sleep(1000L); 

		    List<WebElement> municipality = driver.findElements(By.xpath("//*[@id='select2Multiple']/optgroup/option"));

		    if (!municipality.isEmpty()) {
		        if (clientName != null && !clientName.isEmpty()) {
		            for (WebElement option : municipality) {
		                if (option.getText().equalsIgnoreCase(clientName)) {
		                    option.click();
		                    System.out.println("Selected Option: " + option.getText());
		                    break;
		                }
		            }
		        } else {
		            Random random = new Random();
		            int randomIndex = random.nextInt(municipality.size());
		            municipality.get(randomIndex).click();
		            System.out.println("Selected Option (Random): " + municipality.get(randomIndex).getText());
		        }
		    } else {
		        System.out.println("No options available in the dropdown.");
		    }
		    
		    wait.until(ExpectedConditions.elementToBeClickable(selectMunicipality));
		    selectMunicipality.click();
		    		  
		    js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", save);
		    
		    Thread.sleep(1000L);
		    
		    wait.until(ExpectedConditions.elementToBeClickable(save));
		    save.click();
		}catch (Exception ex) {
			
			System.out.println("Exception: " + ex.getMessage());
		}
	}

	
	public void selectMultiMunicipalities(int numberToSelect) throws InterruptedException {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].click();", selectMunicipality);

	    Thread.sleep(1000);

	    List<WebElement> municipalities = driver.findElements(By.xpath("//*[@id='select2Multiple']/optgroup/option"));

	    if (!municipalities.isEmpty()) {
	        Random random = new Random();

	        numberToSelect = Math.min(numberToSelect, municipalities.size());

	        for (int i = 0; i < numberToSelect; i++) {
	            int randomIndex = random.nextInt(municipalities.size());
	            
	            WebElement selectedMunicipality = municipalities.get(randomIndex);
	            
	            if (selectedMunicipality.isDisplayed()) {
	                selectedMunicipality.click();
	                System.out.println("Selected Municipality: " + selectedMunicipality.getText());
	            } else {
	                System.out.println("Skipped invisible option: " + selectedMunicipality.getText());
	            }
	        }
	    } else {
	        System.out.println("No options available in the dropdown.");
	    }

//	    selectMunicipality.sendKeys(Keys.TAB); 
	}

	
	public void selectRole() {
	    List<WebElement> roles = driver.findElements(By.xpath("//*[@id='addNewUser']/div/div/div/form/div[2]//input[@type='checkbox']"));

	    if (!roles.isEmpty()) {
	        Random random = new Random();
	        int randomIndex = random.nextInt(roles.size());

	        WebElement checkbox = roles.get(randomIndex);
	        
	        if (!checkbox.isSelected()) {
	            checkbox.click();
	            
	            String roleName = "";
	            try {
	                roleName = checkbox.findElement(By.xpath("following-sibling::label")).getText();
	            } catch (Exception e) {
	                System.out.println("Role name not found, trying alternative methods.");
	                try {
	                    roleName = checkbox.findElement(By.xpath("parent::div//span")).getText();
	                } catch (Exception ex) {
	                    System.out.println("No text found for selected checkbox.");
	                }
	            }

	            System.out.println("✔Random Checkbox Selected: " + roleName);
	        } else {
	            System.out.println("Checkbox at index " + randomIndex + " was already selected.");
	        }
	    } else {
	        System.out.println("No checkboxes found.");
	    }
	}
	
	public void selectMultipleRoles(int numberToSelect) {
	    List<WebElement> checkboxes = driver.findElements(By.xpath("//*[@id='addNewUser']/div/div/div/form/div[2]//input[@type='checkbox']"));

	    if (checkboxes.size() >= numberToSelect) {
	        Random random = new Random();
	        
	        for (int i = 0; i < numberToSelect; i++) {
	            int randomIndex = random.nextInt(checkboxes.size());

	            WebElement checkbox = checkboxes.get(randomIndex);
	            
	            if (!checkbox.isSelected()) {
	                checkbox.click();
	                System.out.println("✔Selected Checkbox: " + randomIndex);
	            }
	        }
	    } else {
	        System.out.println("Not enough checkboxes to select " + numberToSelect);
	    }
	}

	
	public void click_saveChanges() {
		
		saveChngsBtn.click();
	}
	
	public void searchUser(String userName) {
		
		wait.until(ExpectedConditions.elementToBeClickable(search));
		search.sendKeys(userName);
		
	}
	
	public void editUser() {
		
		wait.until(ExpectedConditions.elementToBeClickable(editUser));
		editUser.click();
	}
	
	public String getToastMssg() {
		
		WebElement toaster = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\\\"toast-container\\\"]/div/div[2]")));		
		return toaster.getText();

	}
	
	

}
