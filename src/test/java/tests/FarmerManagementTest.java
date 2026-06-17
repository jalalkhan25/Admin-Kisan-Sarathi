package tests;

import base.BaseTest;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/** FarmerManagementTest - Farmer page, search, register farmer with all mandatory fields */
public class FarmerManagementTest extends BaseTest {

    private String farmerPhone() {
        int num = 1 + suffix;
        return String.format("69999999%02d", num);
    }

    @Test(priority = 1)
    public void verifyFarmerManagementPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/farmer-management");
        Thread.sleep(2000);
        assertVisible(By.xpath("//h1[contains(text(),'Farmer')]"), "Farmer Management title");
        System.out.println("[PASS] Farmer Management page opens");
    }

    @Test(priority = 2)
    public void verifyFarmerSearchFunctionality() throws InterruptedException {
        driver.get(BASE_URL + "/farmer-management");
        Thread.sleep(2000);
        WebElement search = assertVisible(By.cssSelector("input[placeholder='Search by Name or Mobile Number ']"), "Farmer search");
        search.sendKeys("test");
        Thread.sleep(2000);
        System.out.println("[PASS] Farmer search works");
    }

    @Test(priority = 3)
    public void registerFarmerInDICTestAzamgarh() throws InterruptedException {
        driver.get(BASE_URL + "/farmer-management/registration");
        Thread.sleep(3000);
        assertVisible(By.xpath("//h1[contains(text(),'Farmer')]"), "Farmer Registration heading");

        // Full Name
        assertFillInput("input[placeholder='Enter your name']", "Test Farmer " + suffix, "Full Name");
        Thread.sleep(500);

        // Mobile Number
        assertFillInput("input[placeholder='Enter your phone number']", farmerPhone(), "Mobile");
        Thread.sleep(1000);

        // Age
        assertFillInput("input[placeholder='Enter your age']", "35", "Age");
        Thread.sleep(500);

        // Gender - Male (CustomSelect dropdown)
        assertClickCustomSelect("Gender", "Male");
        Thread.sleep(500);

        // Language - English
        assertClickCustomSelect("Language", "English");
        Thread.sleep(500);

        // State - Uttar Pradesh
        assertClickCustomSelect("State", "Uttar Pradesh");
        Thread.sleep(2000);

        // District - Azamgarh
        assertClickCustomSelect("District", "Azamgarh");
        Thread.sleep(2000);

        // KVK - DIC Test
        assertClickCustomSelect("KVK", "Dic");
        Thread.sleep(2000);

        // Block - Ahiraula
        assertClickCustomSelect("Block", "Ahiraula");
        Thread.sleep(1500);

        // Village - Abhaipur
        assertClickCustomSelect("Village", "Abhaipur");
        Thread.sleep(1000);

        // Submit
        scrollDown();
        Thread.sleep(1000);
        By submitBtn = By.xpath("//button[.//span[text()='Submit']] | //button[contains(text(),'Submit')]");
        if (!isVisible(submitBtn)) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 99999)");
            Thread.sleep(1000);
        }
        WebElement submit = assertVisible(submitBtn, "Submit button");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", submit);
        Thread.sleep(500);
        submit.click();
        Thread.sleep(2000);

        // Confirmation modal
        By modalSubmit = By.xpath("//div[contains(@class,'fixed')]//button[text()='Submit']");
        if (isVisible(modalSubmit)) {
            driver.findElement(modalSubmit).click();
            Thread.sleep(5000);
        }

        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains("/farmer-management") && !url.contains("/registration"),
                "Should redirect after registration. URL: " + url);
        System.out.println("[PASS] Farmer registered: " + farmerPhone());
    }
}
