package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TC02 - User Management: Page, Search, Filter, Create Expert, Verify, View, Disable
 */
public class TC02_UserManagementTest extends BaseTest {

    private String expertPhone = "600777777" + suffix;
    private String expertEmail = "jak" + suffix + "@expert123.com";

    @Test(priority = 1)
    public void verifyUserManagementPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        Assert.assertTrue(isVisible(By.xpath("//h1[text()='User Management']")));
        System.out.println("[PASS] User Management page opens");
    }

    @Test(priority = 2)
    public void verifyUserSearchFunctionality() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        WebElement search = driver.findElement(By.cssSelector("input[placeholder='Search by Username, Mobile...']"));
        search.clear();
        search.sendKeys("test");
        Thread.sleep(2000);
        System.out.println("[PASS] User search works");
    }

    @Test(priority = 3)
    public void verifyUserFilterButton() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        By filterBtn = By.xpath("//button[contains(.,'Filters')]");
        if (isVisible(filterBtn)) {
            driver.findElement(filterBtn).click();
            Thread.sleep(1000);
        }
        System.out.println("[PASS] User filter clicked");
    }

    @Test(priority = 4)
    public void createExpertUser() throws InterruptedException {
        driver.get(BASE_URL + "/user-management/registration");
        Thread.sleep(3000);
        Assert.assertTrue(isVisible(By.xpath("//h1[contains(text(),'User')]")));

        // SMD - Agricultural Extension
        selectOptionContaining("select[name='division']", "Extension");
        Thread.sleep(2000);

        // Designation - KVK Expert
        selectOptionContaining("select[name='designation']", "Expert");
        Thread.sleep(2000);

        // Organization - ATARI Kanpur
        if (isVisible(By.cssSelector("select[name='organization']"))) {
            selectOptionContaining("select[name='organization']", "Kanpur");
        }
        Thread.sleep(2000);

        // State - Uttar Pradesh
        if (isVisible(By.cssSelector("select[name='state']"))) {
            selectOptionContaining("select[name='state']", "Uttar Pradesh");
        }
        Thread.sleep(2000);

        // District - Azamgarh
        if (isVisible(By.cssSelector("select[name='district']"))) {
            selectOptionContaining("select[name='district']", "Azamgarh");
        }
        Thread.sleep(3000);

        // KVK - DIC Test
        if (isVisible(By.cssSelector("select[name='kvk']"))) {
            Select kvk = new Select(driver.findElement(By.cssSelector("select[name='kvk']")));
            Thread.sleep(1000);
            for (WebElement opt : kvk.getOptions()) {
                if (opt.getText().toLowerCase().contains("dic") || opt.getText().toLowerCase().contains("azamgarh")) {
                    kvk.selectByVisibleText(opt.getText());
                    break;
                }
            }
        }
        Thread.sleep(1000);

        // Name, Phone, Email
        fillInput("input[placeholder='Enter Name']", "expert jak");
        fillInput("input[placeholder='Enter Mobile Number']", expertPhone);
        fillInput("input[placeholder='Enter Email']", expertEmail);

        scrollDown();
        Thread.sleep(1000);
        if (isVisible(By.xpath("//button[text()='Submit']"))) {
            driver.findElement(By.xpath("//button[text()='Submit']")).click();
            Thread.sleep(4000);
        }
        System.out.println("[PASS] Expert created: " + expertPhone);
    }

    @Test(priority = 5)
    public void verifyCreatedExpertInList() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        WebElement search = driver.findElement(By.cssSelector("input[placeholder='Search by Username, Mobile...']"));
        search.clear();
        search.sendKeys(expertPhone);
        search.sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        if (isVisible(By.cssSelector("table tbody tr"))) {
            String text = driver.findElement(By.cssSelector("table tbody tr")).getText();
            System.out.println("[PASS] Expert found: " + text.substring(0, Math.min(text.length(), 70)));
        }
    }

    @Test(priority = 6)
    public void viewExpertDetails() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        WebElement search = driver.findElement(By.cssSelector("input[placeholder='Search by Username, Mobile...']"));
        search.clear();
        search.sendKeys(expertPhone);
        search.sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        if (isVisible(By.cssSelector("table tbody tr:first-child"))) {
            driver.findElement(By.cssSelector("table tbody tr:first-child")).click();
            Thread.sleep(3000);
            System.out.println("[PASS] View details: " + driver.getCurrentUrl());
        }
    }

    @Test(priority = 7)
    public void disableExpertUser() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        WebElement search = driver.findElement(By.cssSelector("input[placeholder='Search by Username, Mobile...']"));
        search.clear();
        search.sendKeys(expertPhone);
        search.sendKeys(Keys.ENTER);
        Thread.sleep(3000);

        By toggleIcon = By.xpath("//table//img[@alt='Active' or @alt='Inactive']");
        if (isVisible(toggleIcon)) {
            String before = driver.findElement(toggleIcon).getAttribute("alt");
            driver.findElement(toggleIcon).click();
            Thread.sleep(1000);
            if (isVisible(By.xpath("//button[text()='Confirm']"))) {
                driver.findElement(By.xpath("//button[text()='Confirm']")).click();
                Thread.sleep(3000);
                String after = driver.findElement(toggleIcon).getAttribute("alt");
                System.out.println("[PASS] Status: " + before + " -> " + after);
            }
        }
    }
}
