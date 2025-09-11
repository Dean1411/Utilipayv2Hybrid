package UtilipayV2Hybrid.testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

public class Base {
	
//	public static WebDriver driver;
	public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	public Properties prop;
	public FileInputStream fs;
	public static final Logger logger = LogManager.getLogger(Base.class);
	protected String currentBrowserName;
	
	public static WebDriver getDriver() {
	    return driver.get();
	}
	
	public Properties getProp() {
	    return this.prop;
	}


	
	@BeforeClass(alwaysRun = true)
	@Parameters({"Browser", "baseType"})
	public void setUp(String browserName, @Optional("prepaid") String baseType) throws IOException {
	    prop = new Properties();
	    fs = new FileInputStream("./src/test/resources/data.properties");
	    prop.load(fs);

	    if (getDriver() != null) {
	        return;
	    }

	    switch (browserName.toLowerCase()) {
	        case "chrome":
	            ChromeOptions chromeOptions = new ChromeOptions();
//	            chromeOptions.addArguments("--incognito");
//	            chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
//	            chromeOptions.addArguments("--disable-save-password-bubble");
//	            chromeOptions.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
//	                put("credentials_enable_service", false);
//	                put("profile.password_manager_enabled", false);
//	            }});
	            driver.set(new ChromeDriver(chromeOptions));
	            break;

	        case "chromeheadless":
	            ChromeOptions headlessOptions = new ChromeOptions();
	            headlessOptions.addArguments("headless");
	            driver.set(new ChromeDriver(headlessOptions));
	            break;

	        case "firefox":
	            driver.set(new FirefoxDriver());
	            break;

	        case "edge":
	            driver.set(new EdgeDriver());
	            break;

	        default:
	            System.out.println("Browser does not exist");
	            return;
	    }

	    this.currentBrowserName = browserName.toLowerCase();

	    getDriver().manage().deleteAllCookies();
	    getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

	    String url = null;
	    switch(baseType.toLowerCase()) {
	        case "online":
	            url = prop.getProperty("onlineUrl");
	            break;
	        case "prepaid":
	            url = prop.getProperty("baseUrl");
	            break;
	        default:
	            url = prop.getProperty("baseUrl");
	    }

	    // Navigate to the correct URL
	    getDriver().get(url);
	    getDriver().manage().window().maximize();

	}
	
//	@BeforeClass (alwaysRun=true)
//	@Parameters({"Browser"})
//	public void setUp(String browserName) throws IOException {		
//		
//		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//		prop = new Properties();
//		fs = new FileInputStream("./src//test//resources//data.properties"); 
//		prop.load(fs); //load file
//		
//		
//	    if (driver != null) {
//	        return; 
//	    }
//		
//		if (driver == null) {
//			
//			switch(browserName.toLowerCase()) {
//			
//			case "chrome": 
//				driver = new ChromeDriver(); break;
//			case "chromeheadless":
//				ChromeOptions ops = new ChromeOptions();
//				ops.addArguments("headless");
//				driver = new ChromeDriver(ops); break;
//			case "firefox": driver = new FirefoxDriver(); break;
//			case "edge": driver = new EdgeDriver();break;
//			default : System.out.println("Browser does not exist"); return;
//			
//		}
//		
//		this.currentBrowserName = browserName.toLowerCase();
//
//		driver.manage().deleteAllCookies();		
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//		driver.get(prop.getProperty("baseUrl"));
//		driver.manage().window().maximize();
//		}		
//	}
	
	
	
	public String getRandomString() { //commons.lang3 library RandomStringUtils
		return RandomStringUtils.randomAlphabetic(6);
	}
	
	public String getRandomNum() { //commons.lang3 library RandomStringUtils
		return "0" + RandomStringUtils.randomNumeric(9);
	}
	
	public String getRandomAlphaNumeric() {
		String stringValue= RandomStringUtils.randomAlphabetic(4);
		String numericValue=RandomStringUtils.randomNumeric(3);
		return (stringValue+stringValue+"@");
	}
	
//	public String getScreenshot(String methodName) throws IOException {
//		
//	    if (getDriver() == null) {
//	        System.out.println("Screenshot skipped: WebDriver session is closed.");
//	        return null;
//	    }
//		
//		String currentTimeStamp=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
//		String targetFilePath = System.getProperty("user.dir") + "\\reports\\screenshots\\" + methodName + "_" + currentTimeStamp + ".png";
//
////		String targetFilePath = System.getProperty("user.dir")+"\\screenshots"+methodName+"_"+currentTimeStamp+".png";
//		File targetFile = new File(targetFilePath);
//		FileUtils.copyFile(srcFile, targetFile);
//		return targetFilePath;
//	}
	
	public String getScreenshot(String methodName) throws IOException {
	    if (getDriver() == null) {
	        System.out.println("Screenshot skipped: WebDriver is null.");
	        return null;
	    }
	    
	    // Check if WebDriver session is active (works if you use RemoteWebDriver)
	    if (((RemoteWebDriver) getDriver()).getSessionId() == null) {
	        System.out.println("Screenshot skipped: WebDriver session is closed.");
	        return null;
	    }

	    String currentTimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	    File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
	    String targetFilePath = System.getProperty("user.dir") + "\\reports\\screenshots\\" + methodName + "_" + currentTimeStamp + ".png";

	    File targetFile = new File(targetFilePath);
	    FileUtils.copyFile(srcFile, targetFile);
	    return targetFilePath;
	}

	
	@AfterClass(alwaysRun = true)
	public void tearDown() {
	    if (getDriver() != null) {
	        getDriver().quit();
	        driver.remove(); 
	    }
	}

	
//	@AfterClass (alwaysRun=true)
//	public void tearDown() throws InterruptedException{
//		if (driver != null) {
//			
//			driver.quit();
//			driver = null;
//		}		
//	}
	

}
