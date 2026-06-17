package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

/** QueryManagementTest - Query table verification */
public class QueryManagementTest extends BaseTest {

    @Test(priority = 1)
    public void verifyQueryTableDisplayed() throws InterruptedException {
        driver.get(BASE_URL + "/query-management");
        Thread.sleep(2000);
        assertVisible(By.cssSelector("table"), "Query table");
        System.out.println("[PASS] Query table displayed");
    }
}
