package pageObject;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MunicipalMaintenancePage extends BaseComponent {
	
	 private WebDriverWait wait;
	 private Properties prop;
	 private int numberOfTariffsToCreate = 2;

	
	public MunicipalMaintenancePage(WebDriver driver) {
		super(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	public Properties getProp() {
	    return this.prop;
	}
	
	
	@FindBy(xpath="//a[normalize-space()='Add New']")
	WebElement addNwBtn;
	
	@FindBy(xpath="//tbody")
	WebElement tableBody;
	
	@FindBy(xpath="//a[@class='btn btn-sm btn-text-secondary rounded-pill btn-icon dropdown-toggle hide-arrow waves-effect waves-light']")
	WebElement actions;
	
	@FindBy(xpath="//*[@id=\"DataTables_Table_0\"]/tbody/tr[1]/td[5]/div/div")
	WebElement actionsDataTable;//*[@id="DataTables_Table_0"]/tbody/tr[1]/td[5]/div/div
	
	@FindBy(xpath="//a[normalize-space()='Manage Tariffs']")
	WebElement mngTariffs;
	
	@FindBy(partialLinkText="ManageSupplyGroupCode")
	WebElement mngSGC;
	
	@FindBy(xpath="//button[@id='addNew']")
	WebElement addNewSgc;
	
	@FindBy(xpath="//input[@id='sgc']")//input[@id='sgc']
	WebElement sgc;
	
	@FindBy(id="description")//textarea[@id='description']
	WebElement sgcDescription;
	
	@FindBy(xpath="//button[normalize-space()='Save']")
	WebElement sgcSaveBtn;
	
	@FindBy(xpath="//button[normalize-space()='Cancel']")
	WebElement sgcCancelBtn;
	
	@FindBy(xpath="//*[@id=\"toast-container\"]/div/div[2]")
	WebElement sgcToaster;
	
	@FindBy(xpath="//button[@id='addNew']")
	WebElement addNwTariff;
	
	@FindBy(xpath="//button[@id='addNewYear']")
	WebElement addYr;
	
	@FindBy(xpath="//input[@id='code']")
	WebElement tariffCode;
	
	@FindBy(xpath="//select[@id='type']")
	WebElement mtrType;//input[@type='search']
	
	@FindBy(xpath="//input[@type='search']")
	WebElement searchMunicipality;
	
	@FindBy(xpath="//tr[@class='odd']//i[@class='ti ti-pencil ti-md']")
	WebElement editMun;
	
	@FindBy(xpath="//textarea[@id='description']")
	WebElement desc;
	
	@FindBy(xpath="//button[normalize-space()='Save']")
	WebElement saveCode;
	
	@FindBy(xpath="//table[@id='DataTables_Table_0']")
	WebElement tariffTable;
	
	@FindBy(xpath="//input[@type='search']")
	WebElement searchTariff;
	
	@FindBy(xpath="//body[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/div[2]/div[2]/div[1]/table[1]/tbody[1]/tr[1]/td[4]/a[1]")
	WebElement tariffSetting;
	
	@FindBy(xpath="//button[@id='addNewYear']")
	WebElement addNwTariffYear;
	
	@FindBy(xpath="//select[@id='yearStart']")
	WebElement yrStart;
	
	@FindBy(xpath="//select[@id='yearEnd']")
	WebElement yrEnd;
	
	@FindBy(xpath="//*[@id=\"btnSaveYear\"]")
	WebElement saveTariffYrBtn;
	
	@FindBy(xpath="//a[normalize-space()='Back']")
	WebElement backBtn;
	
	@FindBy(xpath="//button[normalize-space()='Add Sub Category']")
	WebElement addSubCatogory;
	
	@FindBy(xpath="//input[@id='tariff']")
	WebElement tabName;
	
	@FindBy(xpath="//input[@id='startDate']")
	WebElement startDate;
	
	@FindBy(xpath="//input[@id='endDate']")
	WebElement endDate;
	
	@FindBy(xpath="//input[@id='basicCharge']")
	WebElement bscChrge;
	
	@FindBy(xpath="//form[@action='/Tariff/AddTariffYearSubCat']//button[@type='submit'][normalize-space()='Save']")
	WebElement saveTariffYr;
	
	@FindBy(xpath="//button[normalize-space()='Add Step']")
	WebElement addStep;
	
	@FindBy(xpath="//input[@id='endUnit']")
	WebElement endUnitOne;
	
	@FindBy(xpath="//input[@id='rate']")
	WebElement rateOne;
	
	@FindBy(xpath="(//input[@id='endUnit'])[2]")
	WebElement endUnitTwo;
	
	@FindBy(xpath="(//input[@id='rate'])[2]")
	WebElement rateTwo;
	
	@FindBy(xpath="//button[normalize-space()='Add Final Step']")
	WebElement addFnlStep;
	
	@FindBy(xpath="(//input[@id='rate'])[3]")
	WebElement finalRate;
	
	@FindBy(xpath="//button[normalize-space()='Save All']")
	WebElement saveAllBtn;
	
	@FindBy(xpath="//*[@id=\"toast-container\"]/div")
	WebElement tarrifStepToaster;
	
	@FindBy(xpath="//input[@id='Name']")
	WebElement nameOfClient;
	
	
	@FindBy(xpath="//select[@name='CurrencyId']")
	WebElement currencyId; 
	
	@FindBy(xpath="//input[@id='VatNumber']")
	WebElement vatNum; 
	
	@FindBy(xpath="//input[@id='ActivationDate']")
	WebElement activationDate;
	
	@FindBy(xpath="//select[@name='ChangePasswordFrequency']")
	WebElement chngPssFrequency;
	
	@FindBy(xpath="//select[@id='clientId']")
	WebElement client; 
	
	@FindBy(xpath="//select[@id='receiptLayout']")
	WebElement receiptLayout;
	
	@FindBy(xpath="//button[normalize-space()='Add Commission']")
	WebElement addCommission;
	
	@FindBy(xpath="//select[@id='vendingChannel']")
	WebElement channelName;
	
	@FindBy(xpath="//input[@id='percentage']")
	WebElement percentage;
	
	@FindBy(xpath="//input[@id='flatRate']")
	WebElement flatRate;
	
	@FindBy(xpath="//input[@id='enabled']")
	WebElement enablBtn;
	
	@FindBy(xpath="//*[@id=\"formComms\"]/div[2]/button[1]")
	WebElement submitBtn;
	
	@FindBy(xpath="//input[@id='IsActive']")
	WebElement statusCheckbx;
	
	@FindBy(xpath="//input[@id='ChangePassword']")
	WebElement chngePss;
	
	@FindBy(xpath="//input[@id='SlipDisplayName']")
	WebElement slipDisplay;
	
	@FindBy(xpath="//input[@id='EmailAddress']")
	WebElement emailAdd;
	
	@FindBy(xpath="//input[@id='Telephone']")
	WebElement telePhone;
	
	@FindBy(xpath="//input[@id='AddressLine1']")
	WebElement addLine1; 
	
	@FindBy(xpath="//input[@placeholder=\"Address Line 2\"]")
	WebElement addLine2;
	
	@FindBy(css="#AddressLine3")
	WebElement addLine3;
	
	@FindBy(xpath="//input[@id='SlipFooter']")
	WebElement slipFooter;
	
	@FindBy(xpath="//input[@id='WaterIndigentAllowance']")
	WebElement wtrIndigAllowCheckbx; 
	
	@FindBy(xpath="//input[@id='WaterNonIndigentAllowanceAmount']")
	WebElement wtrNonIndigAmnt;
	
	@FindBy(xpath="//input[@id='ElectricityNonIndigentAllowanceAmount']")
	WebElement elecNonIndigAmnt;
	
	@FindBy(xpath="//input[@id='WaterIndigentAllowanceAmount']")
	WebElement wtrIndigAmount;

	@FindBy(xpath="//input[@id='ElectricityIndigentAllowance']")
	WebElement elecIndigAllowCheckbx;
	
	@FindBy(xpath="//input[@id='ElectricityIndigentAllowanceAmount']")
	WebElement elecIndigAmount;
	
	@FindBy(xpath="//input[@id='EMSAPIUrl']")
	WebElement emsApiUrl;
	
	@FindBy(xpath="(//input[@id='EMSAPIUsername'])[1]")
	WebElement emsApiUsername;
	
	@FindBy(xpath="//input[@type='password']")
	WebElement emsApiPsswrd;
	
	@FindBy(xpath="//button[normalize-space()='Save']")
	WebElement saveBtn;
	
	@FindBy(xpath="//a[normalize-space()='Cancel']")
	WebElement cnclBtn;
	
	@FindBy(xpath="//div[@class='toast-message']")
	WebElement toaster;
	
	
	
	public void click_addNwMunicipality() {
		
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
	    wait.until(ExpectedConditions.elementToBeClickable(addNwBtn)); 
	    
	    addNwBtn.click();
	}
	

	public void client_Name(String clntNm) {
		
		wait.until(ExpectedConditions.elementToBeClickable(nameOfClient)); 
		nameOfClient.sendKeys(clntNm);
	}
	
	public void selectCurrencyId() {
		
		WebElement dropdown = driver.findElement(By.xpath("//select[@name='CurrencyId']")); 
		Select select = new Select(dropdown);
		select.selectByValue("1");
	}
	
	public void vatNum(String vat) {
		
		wait.until(ExpectedConditions.elementToBeClickable(vatNum));
		vatNum.sendKeys(vat);
	}
	
	public void selectDate() {
		
	    
//	    String[] dateParts = {"2025", "02", "07"};
//	    
//	    WebElement element = driver.findElement(By.xpath("//input[@id='ActivationDate']"));
//	    
//	    if (element.isDisplayed() && element.isEnabled()) {
//	    	
//	        element.sendKeys(dateParts[0]); 
//	        element.sendKeys(Keys.TAB);
//
//	        element.clear(); 
//	        element.sendKeys(dateParts[1]); 
//
//	        element.sendKeys(dateParts[2]); 
//	    } else {
//	        System.out.println("Element is not visible or interactable.");
//	    }
		
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        WebElement dateField = driver.findElement(By.xpath("//input[@id='ActivationDate']"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('value', arguments[1])", dateField, date);
	    	    
	}
	
	public void pssChangeFrequency() {
		
		WebElement dropdown = driver.findElement(By.xpath("//select[@name='ChangePasswordFrequency']")); 
		Select select = new Select(dropdown);
		select.selectByVisibleText("Three Months");
	}
	
	public void selectClient() {
		
		WebElement dropdown = driver.findElement(By.xpath("//select[@id='clientId']")); 
		Select select = new Select(dropdown);
		select.selectByVisibleText("Inzalo EMS");
	}
	
	public void rcptLayout() {
		
		WebElement dropdown = driver.findElement(By.xpath("//select[@id='receiptLayout']")); 
		Select select = new Select(dropdown);
		select.selectByVisibleText("Default");
	}
	
	public void statusChckBx() {
		
		wait.until(ExpectedConditions.elementToBeClickable(statusCheckbx));
		statusCheckbx.click();
	}
	
	public void chngPassword() {
		
		wait.until(ExpectedConditions.elementToBeClickable(chngePss));
		chngePss.click();
	}
	
	public void editMuncipality() {
		
		wait.until(ExpectedConditions.elementToBeClickable(editMun));
		editMun.click();
	}
	
	public void slpDisplay(String name) {
		
		wait.until(ExpectedConditions.elementToBeClickable(slipDisplay));
		slipDisplay.sendKeys(name);
	}
	
	public void addCommision(String comm, String percent, String fRate) throws InterruptedException {
		
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		js.executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0, 5);", addCommission);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,500)");

		
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(addCommission));
		addCommission.click();
		
		Select commission = new Select(channelName);
		wait.until(ExpectedConditions.elementToBeClickable(channelName));
		commission.selectByVisibleText(comm);
		
		wait.until(ExpectedConditions.elementToBeClickable(percentage));
		percentage.sendKeys(percent);
		
		wait.until(ExpectedConditions.elementToBeClickable(flatRate));
		flatRate.sendKeys(fRate);
		
		wait.until(ExpectedConditions.elementToBeClickable(enablBtn));
		enablBtn.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
		submitBtn.click();
	}
	
	public void eMail(String email) {
		
		wait.until(ExpectedConditions.elementToBeClickable(emailAdd));
		emailAdd.sendKeys(email);
	}
	
	public void addressOne(String add1) {
		
		wait.until(ExpectedConditions.elementToBeClickable(addLine1));
		addLine1.sendKeys(add1);
	}
	
	public void telNum(String phone) {
		
		wait.until(ExpectedConditions.elementToBeClickable(telePhone));
		telePhone.sendKeys(phone);
	}
	
	public void addressTwo(String add2) {
		
		wait.until(ExpectedConditions.elementToBeClickable(addLine2));
		addLine2.sendKeys(add2);
	}
	
	public void addressThree(String add3) {
		
		wait.until(ExpectedConditions.elementToBeClickable(addLine3));
		addLine3.sendKeys(add3);
	}
	
	public void slpFooter(String footer) {
		
		wait.until(ExpectedConditions.elementToBeClickable(slipFooter));
		slipFooter.sendKeys(footer);
	}
	
	public void wtrIndAllw(String kl) {
		
		wait.until(ExpectedConditions.elementToBeClickable(wtrIndigAmount));
		wtrIndigAmount.clear();
		wtrIndigAmount.sendKeys(kl);
	}
	
	public void elecIndAllw(String kw) {
		
		try {
			
			wait.until(ExpectedConditions.elementToBeClickable(elecIndigAmount));
			elecIndigAmount.clear();
			elecIndigAmount.sendKeys(kw);
			
		}catch (Exception ex) {
			
			System.out.println("Exception: " + ex);
		}
	}
	
	public void wtrNonIndigAllowAmnt(String amount) {
		
		try {
			
			wait.until(ExpectedConditions.elementToBeClickable(wtrNonIndigAmnt));
			wtrNonIndigAmnt.clear();
			wtrNonIndigAmnt.sendKeys(amount);
			
			
		}catch (Exception ex) {
			
			System.out.println("Exception: " + ex);
		}
			
	}
	
	public void elecNonIndigAllowAmnt(String amount) {
		
		try {
			
			wait.until(ExpectedConditions.elementToBeClickable(elecNonIndigAmnt));
			elecNonIndigAmnt.clear();
			elecNonIndigAmnt.sendKeys(amount);
			
		}catch (Exception ex) {
			
			System.out.println("Exception: " + ex);
		}
		
	}
	
	public void apiUrl(String url) {
		
		wait.until(ExpectedConditions.elementToBeClickable(emsApiUrl));
		emsApiUrl.sendKeys(url);
	}
	
	public void apiUser(String usrName) {
		
		wait.until(ExpectedConditions.elementToBeClickable(emsApiUsername));
		emsApiUsername.sendKeys(usrName);
		emsApiUsername.sendKeys(Keys.TAB);
	}
	
	public void apiPsswrd(String pssword) {
		
		wait.until(ExpectedConditions.elementToBeClickable(emsApiPsswrd));
		emsApiPsswrd.sendKeys(pssword);
	}
	
	public void clickSave() {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    for (int scroll = 0; scroll < 10; scroll++) {
	        js.executeScript("window.scrollBy(0, 200);"); 
	        try {
	            Thread.sleep(500); 
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
	    saveBtn.click();
	}
	
	public void clickActions() {
		
		wait.until(ExpectedConditions.elementToBeClickable(actions));
		actions.click();
	}
	
	public void manageTariffs(WebDriver driver) throws InterruptedException {
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.elementToBeClickable(mngTariffs));
	    mngTariffs.click();		
	}
	
//	public void municipalActions(String actions) {
//	    if (actions == null) {
//	        System.out.println("Action cannot be null.");
//	        return;
//	    }
//
//	    switch (actions.toLowerCase()) {
//	        case "manage sgc":
//	            manageSgc();
//	            break;
//	        case "manage tariff":
//	        	manageTariff("water");	            
//	            break;
//	        default:
//	            System.out.println("Invalid action: " + actions);
//	            break;
//	    }
//	}
	
	public void municipalActions(String actions, String type, int count) throws InterruptedException {
	    if (actions == null) {
	        System.out.println("Action cannot be null.");
	        return;
	    }

	    switch (actions.toLowerCase()) {
	        case "manage sgc":
	            manageSgc(); 
	            break;

	        case "manage tariff":
	            if (type != null && type.equalsIgnoreCase("all")) {
	                manageTariffsForAll(); // call method without parameters, creates one Water and one Electricity tariff
	            } else if (type != null && count > 0) {
	                manageTariffs(type, count); // single type, multiple tariffs
	            } else {
	                System.out.println("Missing type or count for Manage Tariff.");
	            }
	            break;

	        default:
	            System.out.println("Invalid action: " + actions);
	            break;
	    }
	}
	
	public void manageSgc() {
		
		try {
			
			clickActions();
			
			List<WebElement> rows = driver.findElements(By.xpath("//*[@id='DataTables_Table_0']/tbody/tr"));
			String action = "Manage SGC";
			boolean linkClicked = false;
			
			for(WebElement row: rows) {
				
				WebElement desiredAction = row.findElement(By.xpath("td[5]/div/div"));
				
				List<WebElement> actionLinks = desiredAction.findElements(By.tagName("a"));
				
				for(WebElement link: actionLinks) {
					
					if(link.getText().contains(action)) {
						link.click();
						linkClicked = true;
						System.out.println("Manage Sgc link clicked");//*[@id="DataTables_Table_0"]/tbody/tr[1]/td[5]/div/div
						break;
					}
				}
				
				if(linkClicked) {	
					
				    wait.until(ExpectedConditions.elementToBeClickable(addNewSgc));
				    addNewSgc.click();
					
					wait.until(ExpectedConditions.elementToBeClickable(sgc));
					sgc.sendKeys("400184");
					
					wait.until(ExpectedConditions.elementToBeClickable(sgcDescription));
					sgcDescription.sendKeys("Dean Mun Sgc");
					
					wait.until(ExpectedConditions.elementToBeClickable(sgcSaveBtn));
					sgcSaveBtn.click(); 
					break;
				}else {
					System.out.println("Action link not found");
				}
			}
			
		}catch(Exception ex) {
			
			System.out.println("Exception: " + ex);			
		}
	}
	
//	public void manageTariff(String type) {
//		
//		try {
//			
//			clickActions();
//			
//			List<WebElement> rows = driver.findElements(By.xpath("//*[@id='DataTables_Table_0']/tbody/tr"));
//			String action = "Manage Tariff";
//			boolean linkClicked = false;
//			
//			for(WebElement row: rows) {
//				
//				WebElement desiredAction = row.findElement(By.xpath("td[5]/div/div"));
//				
//				List<WebElement> actionLinks = desiredAction.findElements(By.tagName("a"));
//				
//				for(WebElement link: actionLinks) {
//					
//					if(link.getText().contains(action)) {
//						link.click();
//						linkClicked = true;
//						System.out.println("Manage Tariff link clicked");//*[@id="DataTables_Table_0"]/tbody/tr[1]/td[5]/div/div
//						break;
//					}
//				}
//				
//				if(linkClicked) {	
//					
//					addNewTariff();					
//					newTariffCode("RW001");					
//					selectMtrType("Water");
//					tariffDec("Regression Water tariff");
//					saveTariff();
//					break;
//				}else {
//					System.out.println("Action link not found");
//				}
//			}
//			
//		}catch(Exception ex) {
//			
//			System.out.println("Exception: " + ex);			
//		}
//	}
	
	public void manageTariffsForAll() throws InterruptedException {
	    clickActions();

	    // Click Manage Tariff link
	    List<WebElement> rows = driver.findElements(By.xpath("//*[@id='DataTables_Table_0']/tbody/tr"));
	    String action = "Manage Tariff";
	    boolean linkClicked = false;

	    for (WebElement row : rows) {
	        WebElement desiredAction = row.findElement(By.xpath("td[5]/div/div"));
	        List<WebElement> actionLinks = desiredAction.findElements(By.tagName("a"));

	        for (WebElement link : actionLinks) {
	            if (link.getText().contains(action)) {
	                link.click();
	                linkClicked = true;
	                System.out.println("Manage Tariff link clicked");
	                break;
	            }
	        }
	        if (linkClicked) break;
	    }

	    String[] tariffTypes = {"Water", "Electricity"};
	    String[] tariffCodes = {"RW001", "RE001"};
	    String[] tariffDescriptions = {"Regression Water tariff", "Regression Elec tariff"};

	    for (int i = 0; i < tariffTypes.length; i++) {
	        addNewTariff();
	        newTariffCode(tariffCodes[i]);
	        selectMtrType(tariffTypes[i]);
	        tariffDec(tariffDescriptions[i]);
	        saveTariff();
	        
	        Thread.sleep(500);

	        // navBack(); 
	    }

	}


	public void manageTariffs(String type, int numberOfTariffsToCreate) throws InterruptedException {
	    clickActions();
	    clickManageTariffLink();

	    for (int t = 1; t <= numberOfTariffsToCreate; t++) {
	        addNewTariff();
	        newTariffCode((type.equalsIgnoreCase("Water") ? "RW" : "RE") + String.format("%03d", t));
	        selectMtrType(type);
	        tariffDec("Regression " + type + " tariff " + t);
	        saveTariff();
	    }
	}
	
	public void clickManageTariffLink() throws InterruptedException {
	    String action = "Manage Tariff";
	    boolean linkClicked = false;

	    List<WebElement> rows = driver.findElements(By.xpath("//*[@id='DataTables_Table_0']/tbody/tr"));

	    for (WebElement row : rows) {
	        WebElement desiredAction = row.findElement(By.xpath("td[5]/div/div"));
	        List<WebElement> actionLinks = desiredAction.findElements(By.tagName("a"));

	        for (WebElement link : actionLinks) {
	            if (link.getText().contains(action)) {
	                link.click();
	                linkClicked = true;
	                System.out.println("Manage Tariff link clicked");
	                break;
	            }
	        }
	        if (linkClicked) break;
	    }
	    
	    if (!linkClicked) {
	        System.out.println("Manage Tariff link not found");
	    }
	}

	
	public void searchMunicipality(String municipality) {
		
		wait.until(ExpectedConditions.elementToBeClickable(searchMunicipality));
		searchMunicipality.sendKeys(municipality);
	}
	
	public void addNewTariff() {
	    wait.until(ExpectedConditions.elementToBeClickable(addNwTariff));
	    addNwTariff.click();
	}
	
	public void addYear() {
		
		wait.until(ExpectedConditions.elementToBeClickable(addYr));
		addYr.click();
	}
	
	public void newTariffCode(String trfCode) {
	    try {
	        wait.until(ExpectedConditions.elementToBeClickable(tariffCode));
	        tariffCode.clear();                  
	        tariffCode.sendKeys(trfCode);       
	    } catch (Exception ex) {
	        System.out.println("Error: " + ex.getMessage());
	    }
	}

	public void selectMtrType(String mtrType) {
	    try {
	        if (mtrType.equals("Water") || mtrType.equals("Electricity")) {
	            WebElement dropdown = driver.findElement(By.xpath("//select[@name='MeterType']")); 
	            Select type = new Select(dropdown);
	            type.selectByVisibleText(mtrType);  
	        } else {
	            System.out.println("Invalid Selection: " + mtrType);
	        }
	    } catch (Exception ex) {
	        System.out.println("Error: " + ex.getMessage());
	    }
	}

	public void tariffDec(String description) {
	    try {
	        wait.until(ExpectedConditions.elementToBeClickable(desc));
	        desc.clear();                    
	        desc.sendKeys(description);      
	    } catch(Exception ex) {
	        System.out.println("Error: " + ex.getMessage());
	    }
	}


	public void saveTariff() {
		
		wait.until(ExpectedConditions.elementToBeClickable(saveCode));
		saveCode.click();
	}
	
	public String getToaster() {
		
		wait.until(ExpectedConditions.elementToBeClickable(toaster));
		return toaster.getText();
	}
	
	public void addNewYear() {
		
		wait.until(ExpectedConditions.elementToBeClickable(addNwTariffYear));
		addNwTariffYear.click();
	}
	
	public void selectYrStart(String strYr) throws InterruptedException {
		
		Thread.sleep(1000);
        WebElement dropdown = driver.findElement(By.xpath("//select[@id='yearStart']")); 
        Select yrSelect = new Select(dropdown);
        yrSelect.selectByVisibleText(strYr);
		
	}
	
	public void selectYrEnd(String endYr) throws InterruptedException {
		
		Thread.sleep(1000);
        Select yrSelect = new Select(yrEnd);
        yrSelect.selectByVisibleText(endYr);
		
	}
	
	public void saveYr() {
		
		wait.until(ExpectedConditions.elementToBeClickable(saveTariffYrBtn));
		saveTariffYrBtn.click();
		
	}
	
	public void subCat() {
		
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		    wait.until(ExpectedConditions.elementToBeClickable(addSubCatogory));			
			addSubCatogory.click();
			
		}catch (Exception x) {
			
			System.out.println("Exception: " + x.getMessage());
		}
		
	}
	
	public void TariffYr(String tbNm, String strtDte, String endDte, String browser) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	        wait.until(ExpectedConditions.visibilityOf(tabName));
	        tabName.sendKeys(tbNm);

	        String[] startDateParts = strtDte.split("/");
	        String[] endDateParts = endDte.split("/");

	        WebElement dateFieldStart = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='startDate']")));

	        switch (browser.toLowerCase()) {
	            case "chrome":
	                if (dateFieldStart.isDisplayed() && dateFieldStart.isEnabled()) {
	                    dateFieldStart.click();
	                    dateFieldStart.sendKeys(Keys.CONTROL + "a");
	                    dateFieldStart.sendKeys(Keys.BACK_SPACE);
	                    dateFieldStart.sendKeys(startDateParts[0]); // Year
	                    dateFieldStart.sendKeys(Keys.TAB);
	                    dateFieldStart.sendKeys(startDateParts[1]); // Month
	                    dateFieldStart.sendKeys(startDateParts[2]); // Day
	                } else {
	                    System.out.println("Start date field is not visible or interactable.");
	                }
	                break;

	            case "firefox":
	                if (dateFieldStart.isDisplayed() && dateFieldStart.isEnabled()) {
	                    dateFieldStart.click();
	                    dateFieldStart.sendKeys(Keys.CONTROL + "a");
	                    dateFieldStart.sendKeys(Keys.BACK_SPACE);
	                    dateFieldStart.sendKeys(startDateParts[0]); // Year
	                    dateFieldStart.sendKeys(Keys.TAB);
	                    dateFieldStart.sendKeys(startDateParts[1]); // Month
	                    dateFieldStart.sendKeys(startDateParts[2]); // Day
	                } else {
	                    System.out.println("Start date field is not visible or interactable.");
	                }
	                break;
	                    
	            case "edge":
	                if (dateFieldStart.isDisplayed() && dateFieldStart.isEnabled()) {
	                    dateFieldStart.click();
	                    dateFieldStart.sendKeys(startDateParts[0]); // Year
	                    dateFieldStart.sendKeys(Keys.ARROW_RIGHT);
	                    dateFieldStart.clear();
	                    dateFieldStart.sendKeys(startDateParts[1]); // Month
	                    dateFieldStart.sendKeys(startDateParts[2]); // Day
	                } else {
	                    System.out.println("Start date field is not visible or interactable.");
	                }
	                break;

	            default:
	                System.out.println("Browser not supported for this method");
	                return;
	        }

	        WebElement dateFieldEnd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='endDate']")));

	        if (dateFieldEnd.isDisplayed() && dateFieldEnd.isEnabled()) {
	            dateFieldEnd.click();
	            dateFieldEnd.sendKeys(endDateParts[0]); // Year
	            dateFieldEnd.sendKeys(Keys.ARROW_RIGHT);
	            dateFieldEnd.clear();
	            dateFieldEnd.sendKeys(endDateParts[1]); // Month
	            dateFieldEnd.sendKeys(endDateParts[2]); // Day
	        } else {
	            System.out.println("End date field is not visible or interactable.");
	        }
	        
	        wait.until(ExpectedConditions.elementToBeClickable(bscChrge));
	        bscChrge.sendKeys("100");
	        
	        wait.until(ExpectedConditions.elementToBeClickable(saveTariffYr));
	        saveTariffYr.click();

	    } catch (Exception ex) {
	        System.out.println("Exception: " + ex.getMessage());
	    }
	}


	//Below method only works in chrome
//	public void TariffYr(String tbNm, String strtDte, String endDte) {
//	    try {
//	    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//	    	wait.until(ExpectedConditions.visibilityOf(tabName));
//	    	tabName.sendKeys(tbNm);
//
//	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//	        LocalDate startDate = LocalDate.parse(strtDte, formatter);
//	        LocalDate endDate = LocalDate.parse(endDte, formatter);
//
//	        String startYear = String.valueOf(startDate.getYear());
//	        String startMonthDay = startDate.format(DateTimeFormatter.ofPattern("MM/dd"));
//	        String endYear = String.valueOf(endDate.getYear());
//	        String endMonthDay = endDate.format(DateTimeFormatter.ofPattern("MM/dd"));
//
//	        WebElement dateFieldStart = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='startDate']")));
//	        dateFieldStart.click();
//	        dateFieldStart.sendKeys(Keys.CONTROL + "a"); 
//	        dateFieldStart.sendKeys(Keys.BACK_SPACE); 
//	        dateFieldStart.sendKeys(startYear); 
//	        dateFieldStart.sendKeys(Keys.TAB); 
//	        dateFieldStart.sendKeys(startMonthDay); 
//
//	        WebElement dateFieldEnd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='endDate']")));
//	        dateFieldEnd.click();
//	        dateFieldEnd.sendKeys(Keys.CONTROL + "a");
//	        dateFieldEnd.sendKeys(Keys.BACK_SPACE);
//	        dateFieldEnd.sendKeys(endYear);
//	        dateFieldEnd.sendKeys(Keys.TAB);
//	        dateFieldEnd.sendKeys(endMonthDay); 
//
//	        bscChrge.sendKeys("100");
//
//	        saveTariffYr.click();
//
//	    } catch (Exception ex) {
//	        System.out.println("Exception: " + ex.getMessage());
//	    }
//	}
	
	public void addStep() {
		
		wait.until(ExpectedConditions.elementToBeClickable(addStep));
		addStep.click();
		
	}
	
	public void addFirstStep(int endUnit, int rate) throws InterruptedException {
		
		wait.until(ExpectedConditions.elementToBeClickable(endUnitOne));
		endUnitOne.clear();
	    endUnitOne.sendKeys(String.valueOf(endUnit));
	    
	    Thread.sleep(1000);
	    
	    wait.until(ExpectedConditions.elementToBeClickable(rateOne));
	    rateOne.sendKeys(String.valueOf(rate));
	    	    
	}
	
	public void addScndSTep(int endUnit, int rate) throws InterruptedException {
		
		wait.until(ExpectedConditions.elementToBeClickable(endUnitTwo));
		endUnitTwo.clear();
	    endUnitTwo.sendKeys(String.valueOf(endUnit));
	    
	    wait.until(ExpectedConditions.elementToBeClickable(rateTwo));
	    rateTwo.sendKeys(String.valueOf(rate));
	}
	
	public void addFinalStepBtn() {
		
		wait.until(ExpectedConditions.elementToBeClickable(addFnlStep));
		addFnlStep.click();
		
	}
	
	public void addFinalStep(int rate){
	    
		wait.until(ExpectedConditions.elementToBeClickable(finalRate));
		finalRate.sendKeys(String.valueOf(rate));		
	}
	
	public void saveAllStepsBtn() {
		
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    for (int scroll = 0; scroll < 10; scroll++) {
	        js.executeScript("window.scrollBy(0, 150);"); 
	        try {
	            Thread.sleep(500); 
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
		
	    wait.until(ExpectedConditions.elementToBeClickable(saveAllBtn));
		saveAllBtn.click();		
	}
	
	public void navBack() throws InterruptedException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].scrollIntoView({block: 'center'});", backBtn
	    );

	    Thread.sleep(500);

	    wait.until(ExpectedConditions.elementToBeClickable(backBtn));

	    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -100);");

	    backBtn.click();
	}

	
	public String getStepsToaster() throws InterruptedException {
		
		wait.until(ExpectedConditions.elementToBeClickable(tarrifStepToaster));
		return tarrifStepToaster.getText();
		
	}
	
	public void searchTariff(String tariff) {
		
		wait.until(ExpectedConditions.elementToBeClickable(searchTariff));
		searchTariff.sendKeys(tariff);
		
	}
	
	public void tableBody() {
		
		WebElement table = driver.findElement(By.id("DataTables_Table_0"));
		
		List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));
		
		for (WebElement row: rows) {
			
			WebElement setting = row.findElement(By.xpath("./td[4]/a[1]"));
			setting.click();
		}
	}

}
