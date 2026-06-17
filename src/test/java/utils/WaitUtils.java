package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtils.java
 * ==============
 * This utility class provides reusable wait methods.
 * Instead of using Thread.sleep() (which is bad practice),
 * we use explicit waits that wait only until the element is ready.
 *
 * Usage: WaitUtils.waitForElementVisible(driver, By.id("myElement"), 10);
 */
public class WaitUtils {

    /**
     * Wait until an element is visible on the page.
     *
     * @param driver  - the WebDriver instance
     * @param locator - how to find the element (By.id, By.cssSelector, etc.)
     * @param seconds - maximum seconds to wait
     * @return the WebElement once it becomes visible
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait until an element is clickable (visible + enabled).
     *
     * @param driver  - the WebDriver instance
     * @param locator - how to find the element
     * @param seconds - maximum seconds to wait
     * @return the WebElement once it becomes clickable
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait until an element is present in the DOM (may not be visible).
     *
     * @param driver  - the WebDriver instance
     * @param locator - how to find the element
     * @param seconds - maximum seconds to wait
     * @return the WebElement once it is present
     */
    public static WebElement waitForElementPresent(WebDriver driver, By locator, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait until the page URL contains a specific text.
     * Useful after login to confirm navigation happened.
     *
     * @param driver  - the WebDriver instance
     * @param urlPart - part of the URL to check for
     * @param seconds - maximum seconds to wait
     * @return true if URL contains the text within timeout
     */
    public static boolean waitForUrlContains(WebDriver driver, String urlPart, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.urlContains(urlPart));
    }

    /**
     * Check if an element is displayed on the page (with a short wait).
     *
     * @param driver  - the WebDriver instance
     * @param locator - how to find the element
     * @param seconds - maximum seconds to wait
     * @return true if element is visible, false otherwise
     */
    public static boolean isElementDisplayed(WebDriver driver, By locator, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
