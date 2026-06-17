package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/**
 * FarmerManagementPage.java
 * =========================
 * Page Object for the Farmer Management module.
 *
 * Navigation: Click "Farmer Management" in sidebar menu
 * URL: /farmer-management
 */
public class FarmerManagementPage {

    private WebDriver driver;
    private int waitTime = 15;

    // ============================================================
    // LOCATORS
    // ============================================================

    // Sidebar menu item
    private By sidebarMenu = By.xpath("//a[contains(@href,'/farmer-management')]//p[text()='Farmer Management']");

    // Page title
    private By pageTitle = By.xpath("//h1[text()='Farmer Management']");

    // Search input field
    private By searchInput = By.cssSelector("input[placeholder='Search by Name or Mobile Number ']");

    // Filter button
    private By filterButton = By.xpath("//button[contains(.,'Filters')]");

    // Farmer table
    private By farmerTable = By.cssSelector("table");

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public FarmerManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================================
    // PAGE METHODS
    // ============================================================

    /**
     * Open Farmer Management by clicking sidebar menu item.
     */
    public void openModule() {
        if (WaitUtils.isElementDisplayed(driver, sidebarMenu, 10)) {
            driver.findElement(sidebarMenu).click();
        } else {
            driver.get("https://ks-aams-web.digitalindiacorporation.in/farmer-management");
        }
    }

    /**
     * Check if Farmer Management page is displayed.
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
     * Search for a farmer by name or mobile number.
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
     * Click the Filters button.
     */
    public void clickFilterButton() {
        WebElement element = WaitUtils.waitForElementClickable(driver, filterButton, waitTime);
        element.click();
    }

    /**
     * Check if filter button is displayed.
     */
    public boolean isFilterButtonDisplayed() {
        return WaitUtils.isElementDisplayed(driver, filterButton, waitTime);
    }

    /**
     * Check if the farmer table is displayed.
     */
    public boolean isTableDisplayed() {
        return WaitUtils.isElementDisplayed(driver, farmerTable, waitTime);
    }

    /**
     * Click on first farmer row to view details.
     */
    public void clickViewButton() {
        By firstRow = By.cssSelector("table tbody tr:first-child");
        WebElement element = WaitUtils.waitForElementClickable(driver, firstRow, waitTime);
        element.click();
    }
}
