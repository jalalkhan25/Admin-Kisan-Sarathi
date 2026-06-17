package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/**
 * UserManagementPage.java
 * =======================
 * Page Object for the User Management module.
 *
 * Navigation: Click "User Management" in sidebar menu
 * URL: /user-management
 */
public class UserManagementPage {

    private WebDriver driver;
    private int waitTime = 15;

    // ============================================================
    // LOCATORS
    // ============================================================

    // Sidebar menu item - the <p> tag with text "User Management" inside <a> link
    private By sidebarMenu = By.xpath("//a[contains(@href,'/user-management')]//p[text()='User Management']");

    // Page title "User Management"
    private By pageTitle = By.xpath("//h1[text()='User Management']");

    // Search input field
    private By searchInput = By.cssSelector("input[placeholder='Search by Username, Mobile...']");

    // Filter button
    private By filterButton = By.xpath("//button[contains(.,'Filters')]");

    // User table
    private By userTable = By.cssSelector("table");

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public UserManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================================
    // PAGE METHODS
    // ============================================================

    /**
     * Open User Management by clicking sidebar menu item.
     */
    public void openModule() {
        // Click on the sidebar menu link
        if (WaitUtils.isElementDisplayed(driver, sidebarMenu, 10)) {
            driver.findElement(sidebarMenu).click();
        } else {
            // Fallback: navigate directly
            driver.get("https://ks-aams-web.digitalindiacorporation.in/user-management");
        }
    }

    /**
     * Check if User Management page is displayed.
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
     * Search for a user by typing in the search field.
     */
    public void searchRecord(String searchText) {
        WebElement element = WaitUtils.waitForElementVisible(driver, searchInput, waitTime);
        element.clear();
        element.sendKeys(searchText);
    }

    /**
     * Check if the search input is displayed.
     */
    public boolean isSearchInputDisplayed() {
        return WaitUtils.isElementDisplayed(driver, searchInput, waitTime);
    }

    /**
     * Click the Filters button.
     */
    public void clickFilterButton() {
        WebElement element = WaitUtils.waitForElementClickable(driver, filterButton, waitTime);
        element.click();
    }

    /**
     * Check if the filter button is displayed.
     */
    public boolean isFilterButtonDisplayed() {
        return WaitUtils.isElementDisplayed(driver, filterButton, waitTime);
    }

    /**
     * Check if the user table is displayed.
     */
    public boolean isTableDisplayed() {
        return WaitUtils.isElementDisplayed(driver, userTable, waitTime);
    }

    /**
     * Click on first user row to view details.
     */
    public void clickViewButton() {
        By firstRow = By.cssSelector("table tbody tr:first-child");
        WebElement element = WaitUtils.waitForElementClickable(driver, firstRow, waitTime);
        element.click();
    }
}
