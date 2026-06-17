package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/** CallManagementTest - Call Management page and table */
public class CallManagementTest extends BaseTest {

    @Test(priority = 1)
    public void verifyCallManagementPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Inbound-calls");
        Thread.sleep(2000);
        assertVisible(By.xpath("//h1[contains(text(),'Call')]"), "Call Management title");
        System.out.println("[PASS] Call Management page opens");
    }

    @Test(priority = 2)
    public void verifyCallTableDisplayed() throws InterruptedException {
        driver.get(BASE_URL + "/call-management/Inbound-calls");
        Thread.sleep(2000);
        assertVisible(By.cssSelector("table"), "Call table");
        System.out.println("[PASS] Call table displayed");
    }
}
