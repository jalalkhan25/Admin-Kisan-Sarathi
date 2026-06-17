package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/**
 * QueryManagementPage.java
 * ========================
 * Page Object for the Query Management module.
 *
 * Navigation: Click "Query Management" in sidebar menu
 * URL: /query-management
 */
public class QueryManagementPage {

    private WebDriver driver;
    private int waitTime = 15;

    // ============================================================
    // LOCATORS
    // ============================================================

    // Sidebar menu item
    private By sidebarMenu = By.xpath("//a[contains(@href,'/query-management')]//p[text()='Query Management']");

    // Page title
    private By pageTitle = By.xpath("//h1[contains(text(),'Query')]");

    // Search input field
    private By searchInput = By.cssSelector("input[placeholder='Query No, Mobile No, or Name']");

    // Filter section
    private By filterSection = By.xpath("//button[contains(.,'Filter')] | //div[contains(@class,'filter')]");

    // Query table
    private By queryTable = By.cssSelector("table");

    // Status column
    private By statusColumn = By.xpath("//th[contains(text(),'Status')] | //td[contains(@class,'status')]");

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public QueryManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================================
    // PAGE METHODS
    // ============================================================

    /**
     * Open Query Management by clicking sidebar menu item.
     */
    public void openModule() {
        if (WaitUtils.isElementDisplayed(driver, sidebarMenu, 10)) {
            driver.findElement(sidebarMenu).click();
        } else {
            driver.get("https://ks-aams-web.digitalindiacorporation.in/query-management");
        }
    }

    /**
     * Check if Query Management page is displayed.
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
     * Search for a query.
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
     * Check if filter section is displayed.
     */
    public boolean isFilterDisplayed() {
        return WaitUtils.isElementDisplayed(driver, filterSection, 10);
    }

    /**
     * Check if the query table is displayed.
     */
    public boolean isTableDisplayed() {
        return WaitUtils.isElementDisplayed(driver, queryTable, waitTime);
    }

    /**
     * Click on first query row to view details.
     */
    public void clickViewButton() {
        By firstRow = By.cssSelector("table tbody tr:first-child");
        WebElement element = WaitUtils.waitForElementClickable(driver, firstRow, waitTime);
        element.click();
    }

    /**
     * Check if status column is displayed.
     */
    public boolean isStatusDisplayed() {
        return WaitUtils.isElementDisplayed(driver, statusColumn, 10);
    }
}
