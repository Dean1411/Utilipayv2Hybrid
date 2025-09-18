package UtilipayV2Hybrid.utilities;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class DatePickerUtils {
    private WebDriver driver;
    private WebDriverWait wait;

    public DatePickerUtils(WebDriver driver, Duration timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeoutSeconds);
    }

    //Combined method: select date range + month
    public void selectDate(WebElement datePicker, WebElement monthSelector, WebElement flatpickerContainer,
                           String fromDay, String toDay, String month) throws InterruptedException {

        // Select the month first
        if (monthSelector != null) {
            // Dropdown month picker
            Select monthDropdown = new Select(monthSelector);
            for (WebElement option : monthDropdown.getOptions()) {
                if (option.getText().trim().equalsIgnoreCase(month)) {
                    option.click();
                    break;
                }
            }
        } else if (flatpickerContainer != null) {
            // Flatpickr month grid
            wait.until(ExpectedConditions.visibilityOf(flatpickerContainer));
            List<WebElement> months = flatpickerContainer.findElements(
                    By.xpath(".//span[contains(@class,'flatpickr-monthSelect-month')]")
            );
            for (WebElement m : months) {
                if (m.getText().trim().equalsIgnoreCase(month)) {
                    m.click();
                    break;
                }
            }
        }

        // Select the day range 
        wait.until(ExpectedConditions.elementToBeClickable(datePicker)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("flatpickr-calendar")));

        String dayXPath = "//span[contains(@class,'flatpickr-day') and " +
                "not(contains(@class,'prevMonthDay')) and " +
                "not(contains(@class,'nextMonthDay')) and " +
                "not(contains(@class,'disabled'))]";

        for (String targetDay : new String[]{fromDay, toDay}) {
            List<WebElement> days = driver.findElements(By.xpath(dayXPath));
            for (WebElement day : days) {
                if (day.getText().trim().equals(targetDay)) {
                    day.click();
                    break;
                }
            }
            Thread.sleep(300);
        }
    }

    //Single day selection (with navigation)
    public void selectSingleDay(WebElement datePicker, String targetDay, String targetMonth) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(datePicker)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("flatpickr-calendar")));

        WebElement leftArrow = driver.findElement(By.xpath("//span[@class='flatpickr-prev-month']"));
        String month = driver.findElement(By.xpath("//span[@class='cur-month']")).getText();

        while (!month.equalsIgnoreCase(targetMonth)) {
            leftArrow.click();
            Thread.sleep(300);
            month = driver.findElement(By.xpath("//span[@class='cur-month']")).getText();
        }

        String dayXPath = "//span[contains(@class,'flatpickr-day') and " +
                          "not(contains(@class,'prevMonthDay')) and " +
                          "not(contains(@class,'nextMonthDay')) and " +
                          "not(contains(@class,'disabled'))]";

        List<WebElement> days = driver.findElements(By.xpath(dayXPath));
        for (WebElement day : days) {
            if (day.getText().trim().equals(targetDay)) {
                day.click();
                break;
            }
        }
        Thread.sleep(300);
    }
}
