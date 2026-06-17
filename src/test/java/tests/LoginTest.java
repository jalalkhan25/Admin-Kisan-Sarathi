package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/** LoginTest - Verify admin login was successful */
public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void verifyLoginSuccessful() {
        String url = driver.getCurrentUrl();
        Assert.assertFalse(url.endsWith("/login"), "Should NOT be on login page. URL: " + url);
        System.out.println("[PASS] Login verified. URL: " + url);
    }
}
