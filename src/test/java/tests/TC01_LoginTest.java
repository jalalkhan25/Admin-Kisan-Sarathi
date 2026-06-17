package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TC01 - Verify login was successful (login already done in BaseTest @BeforeSuite)
 */
public class TC01_LoginTest extends BaseTest {

    @Test(priority = 1)
    public void verifyLoginSuccessful() {
        Assert.assertFalse(driver.getCurrentUrl().endsWith("/login"),
                "Should not be on login page after login");
        System.out.println("[PASS] Login verified. URL: " + driver.getCurrentUrl());
    }
}
