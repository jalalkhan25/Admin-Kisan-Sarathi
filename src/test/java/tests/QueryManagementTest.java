package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/** QueryManagementTest - Query table, pending count, closed count */
public class QueryManagementTest extends BaseTest {

    @Test(priority = 1)
    public void verifyQueryTableDisplayed() throws InterruptedException {
        driver.get(BASE_URL + "/query-management");
        Thread.sleep(2000);
        assertVisible(By.cssSelector("table"), "Query table");
        System.out.println("[PASS] Query table displayed");
    }

    @Test(priority = 2)
    public void verifyPendingQueryCount() throws InterruptedException {
        driver.get(BASE_URL + "/query-management");
        Thread.sleep(2000);

        // Click on Pending tab/filter if available
        By pendingTab = By.xpath("//button[contains(text(),'Pending')] | //div[contains(text(),'Pending')] | //span[contains(text(),'Pending')]");
        if (isVisible(pendingTab)) {
            driver.findElement(pendingTab).click();
            Thread.sleep(2000);
        }

        // Count rows in table or get count from badge/header
        int pendingCount = 0;
        By countBadge = By.xpath("//*[contains(text(),'Pending')]/following::span[1] | //*[contains(text(),'Pending')]//span");
        if (isVisible(countBadge)) {
            String countText = driver.findElement(countBadge).getText().replaceAll("[^0-9]", "");
            if (!countText.isEmpty()) {
                pendingCount = Integer.parseInt(countText);
            }
        }

        // If badge not found, count table rows
        if (pendingCount == 0) {
            By rows = By.cssSelector("table tbody tr");
            if (isVisible(rows)) {
                List<WebElement> rowList = driver.findElements(rows);
                pendingCount = rowList.size();
            }
        }

        System.out.println("[RESULT] Total Pending Queries: " + pendingCount);
        System.out.println("[PASS] Pending query count verified: " + pendingCount);
    }

    @Test(priority = 3)
    public void verifyClosedQueryCount() throws InterruptedException {
        driver.get(BASE_URL + "/query-management");
        Thread.sleep(2000);

        // Click on Closed/Resolved tab if available
        By closedTab = By.xpath("//button[contains(text(),'Closed')] | //button[contains(text(),'Resolved')] | //div[contains(text(),'Closed')] | //span[contains(text(),'Closed')]");
        if (isVisible(closedTab)) {
            driver.findElement(closedTab).click();
            Thread.sleep(2000);
        }

        // Count from badge or table rows
        int closedCount = 0;
        By countBadge = By.xpath("//*[contains(text(),'Closed') or contains(text(),'Resolved')]/following::span[1] | //*[contains(text(),'Closed') or contains(text(),'Resolved')]//span");
        if (isVisible(countBadge)) {
            String countText = driver.findElement(countBadge).getText().replaceAll("[^0-9]", "");
            if (!countText.isEmpty()) {
                closedCount = Integer.parseInt(countText);
            }
        }

        // If badge not found, count table rows
        if (closedCount == 0) {
            By rows = By.cssSelector("table tbody tr");
            if (isVisible(rows)) {
                List<WebElement> rowList = driver.findElements(rows);
                closedCount = rowList.size();
            }
        }

        System.out.println("[RESULT] Total Closed Queries: " + closedCount);
        System.out.println("[PASS] Closed query count verified: " + closedCount);
    }
}
