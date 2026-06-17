package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TC05 - Query Management: Table display
 */
public class TC05_QueryManagementTest extends BaseTest {

    @Test(priority = 1)
    public void verifyQueryTableDisplayed() throws InterruptedException {
        driver.get(BASE_URL + "/query-management");
        Thread.sleep(2000);
        Assert.assertTrue(isVisible(By.cssSelector("table")));
        System.out.println("[PASS] Query table displayed");
    }
}
