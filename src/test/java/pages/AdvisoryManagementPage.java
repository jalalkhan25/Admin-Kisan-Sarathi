package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/**
 * AdvisoryManagementPage.java
 * ============================
 * Page Object for the Advisory Management module.
 *
 * Navigation: Click "Advisory Management" in sidebar menu
 * URL: /advisory-management
 */
public class AdvisoryManagementPage {

    private WebDriver driver;
    private int waitTime = 15;

    // ============================================================
    // LOCATORS
    // ============================================================

    // Sidebar menu item
    private By sidebarMenu = By.xpath("//a[contains(@href,'/advisory-management')]//p[text()='Advisory Management']");

    // Page title
    private By pageTitle = By.xpath("//h1[contains(text(),'Advisory')]");

    // Search input field
    private By searchInput = By.cssSelector("input[placeholder='Search']");

    // Filter button
    private By filterButton = By.xpath("//button[contains(.,'Filter')]");

    // Create Advisory button
    private By createAdvisoryButton = By.xpath("//button[contains(.,'Create') or contains(.,'Compose')]");

    // Advisory table/list
    private By advisoryTable = By.cssSelector("table");

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public AdvisoryManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================================
    // PAGE METHODS
    // ============================================================

    /**
     * Open Advisory Management by clicking sidebar menu item.
     */
    public void openModule() {
        if (WaitUtils.isElementDisplayed(driver, sidebarMenu, 10)) {
            driver.findElement(sidebarMenu).click();
        } else {
            driver.get("https://ks-aams-web.digitalindiacorporation.in/advisory-management");
        }
    }

    /**
     * Check if Advisory Management page is displayed.
     */
    public boolean isModulePageDisplayed() {
        return WaitUtils.isElementDisplayed(driver, pageTitle, waitTime);
    }

    /**
     * Get the page header text.
     */
    public String getPageHeaderText() {
        WebElement element = WaitUtils.waitForElementVisible(driver, pageTitle, waitTime);
        return element.getText();
    }

    /**
     * Search for an advisory.
     */
    public void searchRecord(String searchText) {
        WebElement element = WaitUtils.waitForElementVisible(driver, searchInput, waitTime);
        element.clear();
        element.sendKeys(searchText);
    }

    /**
     * Check if search input is displayed.
     */
    public boolean isSearchInputDisplayed() {
        return WaitUtils.isElementDisplayed(driver, searchInput, 10);
    }

    /**
     * Click the filter button.
     */
    public void clickFilterButton() {
        WebElement element = WaitUtils.waitForElementClickable(driver, filterButton, waitTime);
        element.click();
    }

    /**
     * Check if filter button is displayed.
     */
    public boolean isFilterButtonDisplayed() {
        return WaitUtils.isElementDisplayed(driver, filterButton, 10);
    }

    /**
     * Check if Create Advisory button is visible.
     */
    public boolean isCreateAdvisoryButtonDisplayed() {
        return WaitUtils.isElementDisplayed(driver, createAdvisoryButton, 10);
    }

    /**
     * Click Create Advisory button.
     */
    public void clickCreateAdvisoryButton() {
        WebElement element = WaitUtils.waitForElementClickable(driver, createAdvisoryButton, waitTime);
        element.click();
    }

    /**
     * Check if advisory table or list is displayed.
     */
    public boolean isTableDisplayed() {
        return WaitUtils.isElementDisplayed(driver, advisoryTable, 10);
    }

    /**
     * Click on first advisory to view details.
     */
    public void clickViewButton() {
        By firstRow = By.cssSelector("table tbody tr:first-child");
        if (WaitUtils.isElementDisplayed(driver, firstRow, 5)) {
            WebElement element = WaitUtils.waitForElementClickable(driver, firstRow, waitTime);
            element.click();
        }
    }
}
