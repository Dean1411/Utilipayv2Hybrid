package UtilipayV2Hybrid.testCases;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import UtilipayV2Hybrid.testBase.Base;
import pageObject.OnlinePurchasePage;

public class OnlinePurchase extends Base {

    private SoftAssert softAssert;
    private OnlinePurchasePage onlinePurchasePage;

    @BeforeClass
    public void setup() {
        softAssert = new SoftAssert();
        onlinePurchasePage = new OnlinePurchasePage(Base.getDriver());
    }

    @Test(retryAnalyzer=UtilipayV2Hybrid.utilities.Retry.class)
    public void doOnlinePurchase() {
    	
        String meterNumber = prop.getProperty("mtrNum");
        String email = prop.getProperty("myEmail");
        String amount = "50";
        String paymentOption = "Credit Card";
        String nameOnCard = prop.getProperty("crdHolder");
        String cardNumber = prop.getProperty("ccNo");
        String cvv = "1234";

        performLookup(meterNumber, email, amount);
        choosePaymentOption(paymentOption, nameOnCard, cardNumber, cvv);
        verifyAndPrintReceipt();

        softAssert.assertAll();
    }

    public void performLookup(String meterNumber, String emailAddress, String amount) {
        onlinePurchasePage.insert_MtrNum(meterNumber);
        onlinePurchasePage.enterEmailandAmount(emailAddress, amount);
    }

    public void choosePaymentOption(String option, String name, String cardNumber, String cvv) {
        onlinePurchasePage.selectPaymentOption(option);
        onlinePurchasePage.fillCardDetailsForm(name, cardNumber, cvv);
    }

    public void verifyAndPrintReceipt() {
        String notification = onlinePurchasePage.getNotiMsg();

        if (notification.contains("Payment Successful")) {
            softAssert.assertTrue(true, "Payment was successful.");
        } else {
            softAssert.fail("Unexpected notification: " + notification);
        }

        onlinePurchasePage.printReceipt();
        System.out.println("Notification Message: " + notification);
    }
}
