package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TC06 - Call Management: Page Opens, Table display
 */
public class TC06_CallManagementTest extends BaseTest {

    @Test(priority = 1)
    public void verifyCallManagementPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Inbound-calls");
        Thread.sleep(2000);
        Assert.assertTrue(isVisible(By.xpath("//h1[contains(text(),'Call')]")));
        System.out.println("[PASS] Call Management page opens");
    }

    @Test(priority = 2)
    public void verifyCallTableDisplayed() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Inbound-calls");
        Thread.sleep(2000);
        Assert.assertTrue(isVisible(By.cssSelector("table")));
        System.out.println("[PASS] Call table displayed");
    }
}
