package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/**
 * CallManagementPage.java
 * =======================
 * Page Object for the Call Management module.
 *
 * Navigation: Click "Call Management" in sidebar menu
 * URL: /call-management/Inbound-calls
 */
public class CallManagementPage {

    private WebDriver driver;
    private int waitTime = 15;

    // ============================================================
    // LOCATORS
    // ============================================================

    // Sidebar menu item
    private By sidebarMenu = By.xpath("//a[contains(@href,'/call-management')]//p[text()='Call Management']");

    // Page title
    private By pageTitle = By.xpath("//h1[contains(text(),'Call')]");

    // Search input field
    private By searchInput = By.cssSelector("input[placeholder='Enter Farmer Number']");

    // Filter button
    private By filterButton = By.xpath("//button[contains(.,'Filter')]");

    // Call table
    private By callTable = By.cssSelector("table");

    // Status column
    private By statusColumn = By.xpath("//th[contains(text(),'Status')] | //td[contains(@class,'status')]");

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public CallManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================================
    // PAGE METHODS
    // ============================================================

    /**
     * Open Call Management by clicking sidebar menu item.
     */
    public void openModule() {
        if (WaitUtils.isElementDisplayed(driver, sidebarMenu, 10)) {
            driver.findElement(sidebarMenu).click();
        } else {
            driver.get("https://ks-aams-web.digitalindiacorporation.in/call-management/Inbound-calls");
        }
    }

    /**
     * Check if Call Management page is displayed.
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
     * Search for a call by farmer number.
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
        return WaitUtils.isElementDisplayed(driver, searchInput, waitTime);
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
     * Check if the call table is displayed.
     */
    public boolean isTableDisplayed() {
        return WaitUtils.isElementDisplayed(driver, callTable, waitTime);
    }

    /**
     * Click on first call row to view details.
     */
    public void clickViewButton() {
        By firstRow = By.cssSelector("table tbody tr:first-child");
        WebElement element = WaitUtils.waitForElementClickable(driver, firstRow, waitTime);
        element.click();
    }

    /**
     * Check if status info is displayed.
     */
    public boolean isStatusDisplayed() {
        return WaitUtils.isElementDisplayed(driver, statusColumn, 10);
    }
}
