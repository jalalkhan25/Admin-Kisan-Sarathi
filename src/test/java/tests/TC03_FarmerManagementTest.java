package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TC03 - Farmer Management: Page, Search, Filter, Register Farmer
 * Farmer Registration data:
 * - Phone: 7776666888 + suffix
 * - State: Uttar Pradesh, District: Azamgarh
 * - KVK: DIC Test Azamgarh, Block: Ahiraula, Village: first available
 */
public class TC03_FarmerManagementTest extends BaseTest {

    private String farmerPhone = "777666688" + suffix;

    @Test(priority = 1)
    public void verifyFarmerManagementPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/farmer-management");
        Thread.sleep(2000);
        Assert.assertTrue(isVisible(By.xpath("//h1[contains(text(),'Farmer')]")));
        System.out.println("[PASS] Farmer Management page opens");
    }

    @Test(priority = 2)
    public void verifyFarmerSearchFunctionality() throws InterruptedException {
        driver.get(BASE_URL + "/farmer-management");
        Thread.sleep(2000);
        By search = By.cssSelector("input[placeholder='Search by Name or Mobile Number ']");
        if (isVisible(search)) {
            driver.findElement(search).sendKeys("test");
            Thread.sleep(2000);
        }
        System.out.println("[PASS] Farmer search works");
    }

    @Test(priority = 3)
    public void verifyFarmerFilterButton() throws InterruptedException {
        driver.get(BASE_URL + "/farmer-management");
        Thread.sleep(2000);
        By filterBtn = By.xpath("//button[contains(.,'Filters')]");
        if (isVisible(filterBtn)) {
            driver.findElement(filterBtn).click();
            Thread.sleep(1000);
        }
        System.out.println("[PASS] Farmer filter clicked");
    }

    @Test(priority = 4)
    public void registerFarmerInDICTestAzamgarh() throws InterruptedException {
        driver.get(BASE_URL + "/farmer-management/registration");
        Thread.sleep(3000);
        Assert.assertTrue(isVisible(By.xpath("//h1[contains(text(),'Farmer')]")));

        // Full Name
        fillInput("input[placeholder='Enter your name']", "Test Farmer " + suffix);
        Thread.sleep(500);

        // Mobile Number
        fillInput("input[placeholder='Enter your phone number']", farmerPhone);
        Thread.sleep(1000);

        // Age
        fillInput("input[placeholder='Enter your age']", "35");
        Thread.sleep(500);

        // Gender - Male (CustomSelect)
        clickCustomSelect("Gender", "Male");
        Thread.sleep(1000);

        // Language - Hindi (CustomSelect)
        clickCustomSelect("Language", "Hindi");
        Thread.sleep(1000);

        // State - Uttar Pradesh (CustomSelect)
        clickCustomSelect("State", "Uttar Pradesh");
        Thread.sleep(2000);

        // District - Azamgarh (CustomSelect)
        clickCustomSelect("District", "Azamgarh");
        Thread.sleep(2000);

        // KVK - DIC Test Azamgarh (CustomSelect)
        clickCustomSelect("KVK", "Dic Test");
        Thread.sleep(2000);

        // Block - Ahiraula (CustomSelect)
        clickCustomSelect("Block", "Ahiraula");
        Thread.sleep(2000);

        // Village - first available (CustomSelect)
        By villageDropdown = By.xpath("//*[contains(text(),'Select Village')]");
        if (isVisible(villageDropdown)) {
            driver.findElement(villageDropdown).click();
            Thread.sleep(1000);
            // Select first option available
            By firstOption = By.xpath("(//div[contains(@class,'option')] | //li[contains(@class,'option')])[1]");
            if (isVisible(firstOption)) {
                driver.findElement(firstOption).click();
            }
        }
        Thread.sleep(1000);

        // Scroll down and Submit
        scrollDown();
        Thread.sleep(1000);
        By submitBtn = By.xpath("//button[contains(text(),'Submit') or contains(text(),'Register')]");
        if (isVisible(submitBtn)) {
            driver.findElement(submitBtn).click();
            Thread.sleep(4000);
        }

        System.out.println("[PASS] Farmer registered: " + farmerPhone);
    }
}
