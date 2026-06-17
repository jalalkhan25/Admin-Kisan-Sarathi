package tests;

import base.BaseTest;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * AdvisoryManagementTest - Advisory page, create advisory, verify in outbox
 * Flow: Enter farmer number → Select Domain/SubDomain/Commodity/ProblemType → Next → Compose → Submit
 */
public class AdvisoryManagementTest extends BaseTest {

    @Test(priority = 1)
    public void verifyAdvisoryManagementPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/advisory-management");
        Thread.sleep(2000);
        assertVisible(By.xpath("//h1[contains(text(),'Advisory')]"), "Advisory Management title");
        System.out.println("[PASS] Advisory Management page opens");
    }

    @Test(priority = 2)
    public void createAdvisory() throws InterruptedException {
        driver.get(BASE_URL + "/advisory-management/create-advisory");
        Thread.sleep(3000);

        // 1. Enter farmer mobile number
        By mobileInput = By.cssSelector("input[placeholder='Enter mobile number(s)']");
        WebElement input = assertVisible(mobileInput, "Farmer mobile input");
        input.clear();
        input.sendKeys("8081826002");
        input.sendKeys(Keys.ENTER);
        Thread.sleep(2000);

        // 2. Scroll fully down - dropdowns are at bottom
        scrollDown();
        Thread.sleep(2000);
        // Wait for page to fully load after farmer number entry
        // The dropdowns might take time to render
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(2000);

        // 3. Domain - Agriculture (may be pre-selected for Admin role)
        By domainValue = By.xpath("//*[contains(text(),'Agriculture') and ancestor::div[preceding-sibling::h4[contains(text(),'Domain')]]]");
        By domainH4 = By.xpath("//h4[contains(text(),'Domain')]");

        if (isVisible(domainValue)) {
            System.out.println("  Domain already pre-selected: Agriculture");
        } else if (isVisible(domainH4)) {
            clickAdvisoryDropdown("Domain", "Agriculture");
        } else {
            // Domain h4 not found - take screenshot for debugging and print page source snippet
            System.out.println("  WARNING: Domain h4 not found. Current URL: " + driver.getCurrentUrl());
            System.out.println("  Page title: " + driver.getTitle());
            // Try waiting more
            Thread.sleep(5000);
            scrollDown();
            Thread.sleep(1000);
            if (isVisible(domainH4)) {
                clickAdvisoryDropdown("Domain", "Agriculture");
            } else {
                Assert.fail("Domain dropdown not found on create-advisory page after waiting. URL: " + driver.getCurrentUrl());
            }
        }
        Thread.sleep(2000);

        // 4. Sub Domain - Cereals
        scrollDown();
        Thread.sleep(2000); // wait for sub-domain list to load after domain selection
        clickAdvisoryDropdown("Sub Domain", "Cereal");
        Thread.sleep(2000);

        // 5. Commodity - Adley millet
        scrollDown();
        Thread.sleep(2000); // wait for commodity list
        clickAdvisoryDropdown("Commodity", "Adley");
        Thread.sleep(2000);

        // 6. Problem Type - Field preparation
        scrollDown();
        Thread.sleep(2000); // wait for problem types
        clickAdvisoryDropdown("Problem Type", "Field");
        Thread.sleep(2000);

        // 7. Click NEXT button at bottom
        scrollDown();
        Thread.sleep(500);
        By nextBtn = By.xpath("//button[.//span[contains(text(),'Next')] or contains(text(),'Next')]");
        WebElement next = assertVisible(nextBtn, "Next button");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", next);
        Thread.sleep(300);
        next.click();
        Thread.sleep(3000);
        System.out.println("  Next clicked - moved to compose page");

        // 8. COMPOSE page - enter SMS text
        By composeArea = By.cssSelector("textarea[placeholder='Compose your message...']");
        WebElement compose = assertVisible(composeArea, "Compose SMS textarea");
        compose.clear();
        compose.sendKeys("this is for testing purpose please ignore");
        Assert.assertFalse(compose.getAttribute("value").isEmpty(), "SMS text not entered");
        Thread.sleep(1000);

        // 9. Fill advisory title if visible
        By titleArea = By.cssSelector("textarea[placeholder='Enter advisory title']");
        if (isVisible(titleArea)) {
            driver.findElement(titleArea).sendKeys("Test Advisory Automation");
        }

        // 10. Fill description if visible
        By descArea = By.cssSelector("textarea[placeholder='Enter your description']");
        if (isVisible(descArea)) {
            driver.findElement(descArea).sendKeys("this is for testing purpose please ignore");
        }

        // 11. Click SUBMIT button at bottom
        scrollDown();
        Thread.sleep(1000);
        By submitBtn = By.xpath("//button[.//span[contains(text(),'Submit')] or .//span[contains(text(),'Preview')] or contains(text(),'Submit') or contains(text(),'Preview')]");
        WebElement submit = assertVisible(submitBtn, "Submit/Preview button");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", submit);
        Thread.sleep(300);
        submit.click();
        Thread.sleep(5000);

        System.out.println("[PASS] Advisory composed and submitted");
    }

    @Test(priority = 3)
    public void verifyAdvisoryInOutbox() throws InterruptedException {
        driver.get(BASE_URL + "/advisory-management/outbox-advisories");
        Thread.sleep(3000);

        By firstRow = By.cssSelector("table tbody tr:first-child");
        if (!isVisible(firstRow)) {
            firstRow = By.xpath("(//div[contains(@class,'cursor-pointer')])[1]");
        }
        WebElement row = assertVisible(firstRow, "First advisory in outbox");
        row.click();
        Thread.sleep(3000);

        Assert.assertTrue(driver.getCurrentUrl().contains("advisory-details"),
                "Should open advisory details. URL: " + driver.getCurrentUrl());
        System.out.println("[PASS] Outbox advisory opened: " + driver.getCurrentUrl());
    }

    /**
     * Helper: Click CustomSelect dropdown on advisory page.
     * CustomSelect renders as: <h4>Label</h4> then a <div> with a clickable button-like area.
     * Options appear as <ul><li><div><span>Text</span></div></li></ul>
     */
    private void clickAdvisoryDropdown(String label, String optionText) throws InterruptedException {
        // CustomSelect: h4 label + a sibling div that is the clickable dropdown
        // Try multiple approaches to find the clickable area
        By dropdownTrigger = By.xpath("//h4[contains(text(),'" + label + "')]/following-sibling::div[1]");

        if (!isVisible(dropdownTrigger)) {
            // Try: parent of h4, then find the clickable div inside
            dropdownTrigger = By.xpath("//h4[contains(text(),'" + label + "')]/parent::div//div[contains(@class,'cursor') or contains(@class,'border')]");
        }
        if (!isVisible(dropdownTrigger)) {
            // Try: any element after h4 that looks clickable
            dropdownTrigger = By.xpath("//h4[contains(text(),'" + label + "')]/following::div[contains(@class,'rounded') or contains(@class,'border')][1]");
        }
        if (!isVisible(dropdownTrigger)) {
            // Last resort: just click the h4 itself (sometimes the whole area is clickable)
            dropdownTrigger = By.xpath("//h4[contains(text(),'" + label + "')]");
        }

        Assert.assertTrue(isVisible(dropdownTrigger), "Dropdown trigger for '" + label + "' not found");

        WebElement el = driver.findElement(dropdownTrigger);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        Thread.sleep(300);
        el.click();
        Thread.sleep(1500);

        // Options are <li> elements with text inside <span>
        By option = By.xpath("//span[normalize-space()='" + optionText + "']/ancestor::li");
        if (!isVisible(option)) {
            // Try partial match
            option = By.xpath("//li[.//span[contains(text(),'" + optionText + "')]]");
        }
        if (!isVisible(option)) {
            option = By.xpath("//li[contains(.,'" + optionText + "')]");
        }
        Assert.assertTrue(isVisible(option), "Option '" + optionText + "' not found in " + label);
        driver.findElement(option).click();
        Thread.sleep(500);
        System.out.println("  Selected " + label + ": " + optionText);
    }
}
