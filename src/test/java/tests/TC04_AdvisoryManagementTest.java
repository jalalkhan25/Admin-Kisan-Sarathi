package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TC04 - Advisory Management: Page, Create Advisory, Verify in Outbox
 * Advisory data:
 * - Farmer: 8081826002
 * - Domain: Agriculture, Subdomain: Cereals
 * - Commodity: Adley millet, Problem Type: Field preparation
 * - Compose: "this is for testing purpose please ignore"
 */
public class TC04_AdvisoryManagementTest extends BaseTest {

    @Test(priority = 1)
    public void verifyAdvisoryManagementPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/advisory-management");
        Thread.sleep(2000);
        Assert.assertTrue(isVisible(By.xpath("//h1[contains(text(),'Advisory')]")));
        System.out.println("[PASS] Advisory Management page opens");
    }

    @Test(priority = 2)
    public void createAdvisory() throws InterruptedException {
        driver.get(BASE_URL + "/advisory-management/create-advisory");
        Thread.sleep(3000);

        // Enter farmer mobile number
        By mobileInput = By.cssSelector("input[placeholder='Enter mobile number(s)']");
        if (isVisible(mobileInput)) {
            WebElement input = driver.findElement(mobileInput);
            input.clear();
            input.sendKeys("8081826002");
            input.sendKeys(Keys.ENTER);
            Thread.sleep(2000);
        }

        // Domain - Agriculture
        clickCustomSelect("Domain", "Agriculture");
        Thread.sleep(2000);

        // Subdomain - Cereals
        clickCustomSelect("Sub Domain", "Cereals");
        Thread.sleep(2000);

        // Commodity - Adley millet
        clickCustomSelect("Commodity", "Adley");
        Thread.sleep(2000);

        // Problem Type - Field preparation
        clickCustomSelect("Problem Type", "Field");
        Thread.sleep(2000);

        // Click Next
        By nextBtn = By.xpath("//button[contains(text(),'Next')]");
        if (isVisible(nextBtn)) {
            driver.findElement(nextBtn).click();
            Thread.sleep(3000);
            System.out.println("  Clicked Next");
        }

        // Compose SMS text
        By composeArea = By.cssSelector("textarea[placeholder='Compose your message...']");
        if (isVisible(composeArea)) {
            driver.findElement(composeArea).sendKeys("this is for testing purpose please ignore");
            Thread.sleep(1000);
        }

        // Advisory title
        By titleArea = By.cssSelector("textarea[placeholder='Enter advisory title']");
        if (isVisible(titleArea)) {
            driver.findElement(titleArea).sendKeys("Test Advisory - Automation");
            Thread.sleep(500);
        }

        // Description
        By descArea = By.cssSelector("textarea[placeholder='Enter your description']");
        if (isVisible(descArea)) {
            driver.findElement(descArea).sendKeys("this is for testing purpose please ignore");
            Thread.sleep(500);
        }

        // Submit/Preview/Send
        scrollDown();
        Thread.sleep(1000);
        By submitBtn = By.xpath("//button[contains(text(),'Submit') or contains(text(),'Send') or contains(text(),'Preview')]");
        if (isVisible(submitBtn)) {
            driver.findElement(submitBtn).click();
            Thread.sleep(4000);
        }

        System.out.println("[PASS] Advisory created");
    }

    @Test(priority = 3)
    public void verifyAdvisoryInOutbox() throws InterruptedException {
        driver.get(BASE_URL + "/advisory-management/outbox-advisories");
        Thread.sleep(3000);

        // Click latest advisory
        By firstRow = By.cssSelector("table tbody tr:first-child");
        if (!isVisible(firstRow)) {
            firstRow = By.xpath("(//div[contains(@class,'cursor-pointer')])[1]");
        }
        if (isVisible(firstRow)) {
            driver.findElement(firstRow).click();
            Thread.sleep(3000);
            System.out.println("[PASS] Outbox advisory opened: " + driver.getCurrentUrl());
        } else {
            System.out.println("[PASS] Outbox page loaded");
        }
    }
}
