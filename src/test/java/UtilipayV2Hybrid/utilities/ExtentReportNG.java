package UtilipayV2Hybrid.utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import UtilipayV2Hybrid.testBase.Base;

public class ExtentReportNG extends Base implements  ITestListener{
	
	public String reportName;
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent; 
//	public ExtentTest test;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	
	
	@Override  
	public void onStart(ITestContext context) {  
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
//		Date dt=new Date();
//		String currentDateTimeStamp=sdf.format(dt);
	
	String currentTimeStamp=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	
	reportName="Test Summary Report-" +currentTimeStamp+".html";
	
	//Creates UI of the report
	sparkReporter=new ExtentSparkReporter(".\\reports\\"+reportName );  
	// title of the report
	
	sparkReporter.config().setReportName("UtilipayV2 Web Test Summary");
	//sparkReporter.config().setTheme(Theme.DARK); //select UI look or theme type
	sparkReporter.config().setTheme(Theme.DARK);
	
	// generate common info in to the report. Ex: application+module+tester+environment name, 
	extent=new ExtentReports();
	extent.attachReporter(sparkReporter);
	extent.setSystemInfo("Application", "UtilipayV2");
	extent.setSystemInfo("Module", "Admin");
	extent.setSystemInfo("Environment", "Dev");
	extent.setSystemInfo("User Name", System.getProperty("user.name"));
	
	//automatically get OS & Browser name from testNG xml file
	String osName=context.getCurrentXmlTest().getParameter("OS"); //windows
	extent.setSystemInfo("Operating System", osName);
	
	String browserName=context.getCurrentXmlTest().getParameter("Browser");
	extent.setSystemInfo("Browser Name", browserName); //chrome
	
	//add groups name into the report if available in testng.xml file
	List <String>includedGroups=context.getCurrentXmlTest().getIncludedGroups();
	if(!includedGroups.isEmpty()) {
		extent.setSystemInfo("Groups Name", includedGroups.toString());
	}
	
	}  
	 
//	@Override  
//	public void onTestSuccess(ITestResult result) {  
//	 // creating test case entries & update status of the test method
//	// in to the report using ExtentTest class
//		test=extent.createTest(result.getTestClass().getName());
//		test.assignCategory(result.getMethod().getGroups()); //display groups in report
//		test.log(Status.PASS, result.getName()+"_"+"got successfully executed");
//	}  
	@Override
	public void onTestSuccess(ITestResult result) {
	    ExtentTest extentTest = extent.createTest(result.getTestClass().getName() + " :: " + result.getMethod().getMethodName());
	    extentTest.assignCategory(result.getMethod().getGroups());
	    extentTest.log(Status.PASS, result.getName() + " passed successfully");
	    test.set(extentTest);
	}

	  
//	@Override  
//	public void onTestFailure(ITestResult result) {  
//		test=extent.createTest(result.getTestClass().getName());
//		test.assignCategory(result.getMethod().getGroups());
//		test.log(Status.FAIL, result.getName()+"_"+"got failed.The error is_"+result.getThrowable());
//				
//		try {
//			String screenShotPath=getScreenshot(result.getName());
//			test.addScreenCaptureFromPath(screenShotPath);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}  
	
//	@Override
//	public void onTestFailure(ITestResult result) {
//	    String screenshotPath = null;
//	    try {
//	        screenshotPath = getScreenshot(result.getName()); 
//	    } catch (IOException e) {
//	        System.out.println("Exception while taking screenshot: " + e.getMessage());
//	    }
//
//	    if (screenshotPath != null && !screenshotPath.isEmpty()) {
//	        test.get().addScreenCaptureFromPath(screenshotPath);
//	    } else {
//	        System.out.println("Screenshot path is null or empty, skipping adding screenshot.");
//	    }
//
//	    test.get().fail(result.getThrowable());
//	}
	@Override
	public void onTestFailure(ITestResult result) {
	    ExtentTest extentTest = extent.createTest(result.getTestClass().getName() + " :: " + result.getMethod().getMethodName());
	    extentTest.assignCategory(result.getMethod().getGroups());
	    test.set(extentTest);

	    String screenshotPath = null;
	    try {
	        screenshotPath = getScreenshot(result.getMethod().getMethodName());
	    } catch (IOException e) {
	        System.out.println("Exception while taking screenshot: " + e.getMessage());
	    }

	    if (screenshotPath != null && !screenshotPath.isEmpty()) {
	        extentTest.addScreenCaptureFromPath(screenshotPath);
	    } else {
	        System.out.println("Screenshot path is null or empty, skipping adding screenshot.");
	    }

	    extentTest.fail(result.getThrowable());
	}


	  
//	@Override  
//	public void onTestSkipped(ITestResult result) {  
//		test=extent.createTest(result.getTestClass().getName());
//		test.assignCategory(result.getMethod().getGroups());
//		test.log(Status.SKIP, result.getName()+"_"+"got skipped");
//		test.log(Status.INFO, result.getThrowable().getMessage()); 
//	}  
	@Override
	public void onTestSkipped(ITestResult result) {
	    ExtentTest extentTest = extent.createTest(result.getTestClass().getName() + " :: " + result.getMethod().getMethodName());
	    extentTest.assignCategory(result.getMethod().getGroups());
	    extentTest.log(Status.SKIP, result.getName() + " was skipped");
	    extentTest.log(Status.INFO, result.getThrowable().getMessage());
	    test.set(extentTest);
	}

	  
	@Override  
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {  
	  
	}  
	  
	@Override  
	public void onFinish(ITestContext context) {  
		extent.flush();
		
		String reportPath=System.getProperty("user.dir")+"\\reports\\"+reportName;
		File extentReport=new File(reportPath);
		
		if (Desktop.isDesktopSupported()) {
		    try {
		        Desktop.getDesktop().browse(new File(reportPath).toURI());
//		    	Desktop.getDesktop().browse(extentReport.toURI());
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

		
//		//Code automatically opens html report
//		try {
//			Desktop.getDesktop().browse(extentReport.toURI());
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
	} 

}
