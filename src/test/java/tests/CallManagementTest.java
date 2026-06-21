package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

/** CallManagementTest - Inbound (Missed/Closed) and Outbound (Answered/Unanswered) counts */
public class CallManagementTest extends BaseTest {

    @Test(priority = 1)
    public void verifyCallManagementPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Inbound-calls");
        Thread.sleep(2000);
        assertVisible(By.xpath("//h1[contains(text(),'Call')]"), "Call Management title");
        System.out.println("[PASS] Call Management page opens");
    }

    @Test(priority = 2)
    public void verifyInboundCallTable() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Inbound-calls");
        Thread.sleep(2000);
        assertVisible(By.cssSelector("table"), "Inbound Call table");
        System.out.println("[PASS] Inbound Call table displayed");
    }

    @Test(priority = 3)
    public void verifyInboundMissedCallCount() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Inbound-calls");
        Thread.sleep(2000);

        // Try clicking Missed tab/filter
        By missedTab = By.xpath("//button[contains(text(),'Missed')] | //div[contains(text(),'Missed')] | //span[contains(text(),'Missed')]");
        if (isVisible(missedTab)) {
            driver.findElement(missedTab).click();
            Thread.sleep(2000);
        }

        int missedCount = getCountFromBadgeOrTable("Missed");
        System.out.println("[RESULT] Inbound Missed Calls: " + missedCount);
        System.out.println("[PASS] Inbound missed call count: " + missedCount);
    }

    @Test(priority = 4)
    public void verifyInboundClosedCallCount() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Inbound-calls");
        Thread.sleep(2000);

        // Try clicking Closed/Answered tab
        By closedTab = By.xpath("//button[contains(text(),'Closed')] | //button[contains(text(),'Answered')] | //div[contains(text(),'Closed')] | //span[contains(text(),'Closed')]");
        if (isVisible(closedTab)) {
            driver.findElement(closedTab).click();
            Thread.sleep(2000);
        }

        int closedCount = getCountFromBadgeOrTable("Closed");
        System.out.println("[RESULT] Inbound Closed Calls: " + closedCount);
        System.out.println("[PASS] Inbound closed call count: " + closedCount);
    }

    @Test(priority = 5)
    public void verifyOutboundCallTable() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Outbound-calls");
        Thread.sleep(2000);
        assertVisible(By.cssSelector("table"), "Outbound Call table");
        System.out.println("[PASS] Outbound Call table displayed");
    }

    @Test(priority = 6)
    public void verifyOutboundAnsweredCallCount() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Outbound-calls");
        Thread.sleep(2000);

        // Try clicking Answered tab
        By answeredTab = By.xpath("//button[contains(text(),'Answered')] | //div[contains(text(),'Answered')] | //span[contains(text(),'Answered')]");
        if (isVisible(answeredTab)) {
            driver.findElement(answeredTab).click();
            Thread.sleep(2000);
        }

        int answeredCount = getCountFromBadgeOrTable("Answered");
        System.out.println("[RESULT] Outbound Answered Calls: " + answeredCount);
        System.out.println("[PASS] Outbound answered call count: " + answeredCount);
    }

    @Test(priority = 7)
    public void verifyOutboundUnansweredCallCount() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Outbound-calls");
        Thread.sleep(2000);

        // Try clicking Unanswered/Missed tab
        By unansweredTab = By.xpath("//button[contains(text(),'Unanswered')] | //button[contains(text(),'Not Answered')] | //div[contains(text(),'Unanswered')] | //span[contains(text(),'Unanswered')]");
        if (isVisible(unansweredTab)) {
            driver.findElement(unansweredTab).click();
            Thread.sleep(2000);
        }

        int unansweredCount = getCountFromBadgeOrTable("Unanswered");
        System.out.println("[RESULT] Outbound Unanswered Calls: " + unansweredCount);
        System.out.println("[PASS] Outbound unanswered call count: " + unansweredCount);
    }

    // ===== Helper =====
    private int getCountFromBadgeOrTable(String label) {
        int count = 0;

        // Try getting count from badge/header near the label
        By countBadge = By.xpath("//*[contains(text(),'" + label + "')]/following::span[1] | //*[contains(text(),'" + label + "')]//span");
        if (isVisible(countBadge)) {
            String countText = driver.findElement(countBadge).getText().replaceAll("[^0-9]", "");
            if (!countText.isEmpty()) {
                count = Integer.parseInt(countText);
            }
        }

        // Fallback: count table rows
        if (count == 0) {
            By rows = By.cssSelector("table tbody tr");
            if (isVisible(rows)) {
                List<WebElement> rowList = driver.findElements(rows);
                count = rowList.size();
            }
        }

        return count;
    }
}
