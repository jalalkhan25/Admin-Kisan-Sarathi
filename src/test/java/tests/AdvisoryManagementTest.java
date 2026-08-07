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

        // 3-6. Define Advisory Details - native <select> dropdowns.
        //      These cascade: selecting Domain reloads Sub Domain, etc.
        selectAdvisoryDropdown("Domain", "Agriculture");
        Thread.sleep(2000); // wait for sub-domain list to load after domain selection

        scrollDown();
        selectAdvisoryDropdown("Sub Domain", "Cereal");
        Thread.sleep(2000); // wait for commodity list

        scrollDown();
        selectAdvisoryDropdown("Commodity", "Adley");
        Thread.sleep(2000); // wait for problem types

        scrollDown();
        selectAdvisoryDropdown("Problem Type", "Field");
        Thread.sleep(2000);

        // 7. Close any open dropdown, then click NEXT button at bottom
        closeAnyOpenAdvisoryDropdown();
        scrollDown();
        Thread.sleep(500);
        By nextBtn = By.xpath("//button[.//span[contains(text(),'Next')] or contains(text(),'Next')]");
        WebElement next = assertVisible(nextBtn, "Next button");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", next);
        Thread.sleep(300);
        try {
            next.click();
        } catch (RuntimeException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", next);
        }
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
     * Select a value in a "Define Advisory Details" dropdown.
     *
     * These are custom (non-native) dropdowns from the app's shared
     * CustomSelect component, rendered as:
     *   <div><h4>{title}{*}</h4>
     *        <button> ...trigger... </button>
     *        {isOpen && <div>[<input placeholder="Search..."/>]<ul><li>option</li>...</ul></div>}
     *   </div>
     * Domain is NOT searchable; Sub Domain / Commodity / Problem Type render a
     * "Search..." box and load their options on open. Matching is a
     * case-insensitive contains, so "Cereal" matches "Cereals" and "Adley"
     * matches "Adley Millet". The panel is always closed afterward so its
     * option list cannot overlay the footer's Next button.
     */
    private void selectAdvisoryDropdown(String label, String optionText) throws InterruptedException {
        scrollDown();
        Thread.sleep(500);

        Assert.assertTrue(openAdvisoryDropdown(label), "Dropdown trigger for '" + label + "' not found");
        // Commodity / Problem Type fetch their options on open.
        Thread.sleep(1500);

        // Searchable dropdowns expose a "Search..." input - filter to the option.
        By searchBox = By.cssSelector("input[placeholder='Search...']");
        if (isVisible(searchBox)) {
            WebElement search = driver.findElement(searchBox);
            search.clear();
            search.sendKeys(optionText);
            Thread.sleep(1200);
        }

        By option = By.xpath("//ul//li[.//span[contains(normalize-space(.),\"" + optionText + "\")]] "
                + "| //ul//li[contains(normalize-space(.),\"" + optionText + "\")]");
        Assert.assertTrue(isVisible(option), "Option '" + optionText + "' not found in " + label);
        WebElement opt = driver.findElement(option);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", opt);
        try {
            opt.click();
        } catch (RuntimeException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
        }
        Thread.sleep(800);
        System.out.println("  Selected " + label + ": " + optionText);

        closeAnyOpenAdvisoryDropdown();
    }

    /**
     * Open a CustomSelect by clicking the <button> trigger that immediately
     * follows the <h4> whose text starts with the given title.
     */
    private boolean openAdvisoryDropdown(String title) throws InterruptedException {
        By trigger = By.xpath("//h4[starts-with(normalize-space(.),'" + title + "')]/following-sibling::button[1]");
        if (!isVisible(trigger)) {
            trigger = By.xpath("//button[.//p[starts-with(normalize-space(.),'" + title + "')]]");
            if (!isVisible(trigger)) {
                return false;
            }
        }
        WebElement el = driver.findElement(trigger);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        Thread.sleep(300);
        try {
            el.click();
        } catch (RuntimeException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
        Thread.sleep(800);
        return true;
    }

    /**
     * If a CustomSelect panel is still open (its "Search..." box or a clickable
     * option list is visible), click a neutral heading (or the body) to fire
     * the component's outside-click handler and close it. This prevents the
     * option list from overlaying and intercepting the footer Next button.
     */
    private void closeAnyOpenAdvisoryDropdown() throws InterruptedException {
        By searchBox = By.cssSelector("input[placeholder='Search...']");
        boolean open = !driver.findElements(searchBox).isEmpty()
                || !driver.findElements(By.xpath("//ul//li[contains(@class,'cursor-pointer') "
                        + "or contains(@class,'hover:bg-gray-100')]")).isEmpty();
        if (!open) {
            return;
        }
        By neutral = By.xpath("//h2[contains(normalize-space(.),'Define Advisory Details')] "
                + "| //h2[contains(normalize-space(.),'Select Farmer')]");
        try {
            if (isVisible(neutral)) {
                driver.findElement(neutral).click();
            } else {
                ((JavascriptExecutor) driver).executeScript("document.body.click();");
            }
        } catch (RuntimeException e) {
            ((JavascriptExecutor) driver).executeScript("document.body.click();");
        }
        Thread.sleep(500);
    }
}
