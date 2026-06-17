package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import pages.LoginPage;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

/**
 * BaseTest.java
 * =============
 * - Login ONCE in @BeforeSuite
 * - All test classes extend this — no login code elsewhere
 * - driver.quit() ONCE in @AfterSuite
 */
public class BaseTest {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static String BASE_URL;
    protected static int suffix;

    @BeforeSuite
    public void suiteSetup() throws InterruptedException {
        BASE_URL = ConfigReader.getProperty("baseUrl");
        String browser = ConfigReader.getProperty("browser");
        String headless = ConfigReader.getProperty("headless");
        String username = ConfigReader.getProperty("username");
        String password = ConfigReader.getProperty("password");

        suffix = (int) (System.currentTimeMillis() % 10);
        System.out.println(">> Run suffix: " + suffix);

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--remote-allow-origins=*");
        if ("true".equalsIgnoreCase(headless)) {
            options.addArguments("--headless");
        }
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // LOGIN ONCE
        driver.get(BASE_URL + "/login");
        Thread.sleep(2000);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
        Thread.sleep(5000);

        if (loginPage.isRoleSelectionDisplayed()) {
            loginPage.selectRole("Admin");
            loginPage.clickRoleSubmit();
            Thread.sleep(3000);
        }
        System.out.println(">> LOGIN DONE. URL: " + driver.getCurrentUrl());
    }

    @AfterSuite
    public void suiteTeardown() {
        if (driver != null) {
            driver.quit();
            System.out.println(">> BROWSER CLOSED.");
        }
    }

    // ===== HELPER METHODS (shared by all test classes) =====

    protected boolean isVisible(By locator) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void fillInput(String css, String value) {
        if (isVisible(By.cssSelector(css))) {
            WebElement el = driver.findElement(By.cssSelector(css));
            el.clear();
            el.sendKeys(value);
        }
    }

    protected void selectOptionContaining(String css, String text) {
        if (isVisible(By.cssSelector(css))) {
            Select select = new Select(driver.findElement(By.cssSelector(css)));
            for (WebElement opt : select.getOptions()) {
                if (opt.getText().contains(text)) {
                    select.selectByVisibleText(opt.getText());
                    return;
                }
            }
            if (select.getOptions().size() > 1) select.selectByIndex(1);
        }
    }

    protected void clickCustomSelect(String label, String optionText) {
        try {
            // Scroll down first to make dropdowns visible
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
            Thread.sleep(500);
            By dropdown = By.xpath("//*[contains(text(),'Select " + label + "') or contains(text(),'" + label + "')]");
            if (isVisible(dropdown)) {
                WebElement el = driver.findElement(dropdown);
                // Scroll element into view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
                Thread.sleep(500);
                el.click();
                Thread.sleep(1000);
                if (optionText != null && !optionText.isEmpty()) {
                    By option = By.xpath("//div[contains(text(),'" + optionText + "')] | //li[contains(text(),'" + optionText + "')]");
                    if (isVisible(option)) {
                        driver.findElement(option).click();
                    }
                } else {
                    // Select first option
                    By firstOpt = By.xpath("(//div[contains(@class,'option')])[1] | (//li[contains(@class,'option')])[1]");
                    if (isVisible(firstOpt)) driver.findElement(firstOpt).click();
                }
            }
        } catch (Exception e) {
            System.out.println("  Could not select " + label + ": " + optionText);
        }
    }

    protected void scrollDown() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    // ===== STRICT ASSERTION HELPERS (used by all test classes) =====

    /** Assert element is visible - FAIL if not */
    protected WebElement assertVisible(By locator, String elementName) {
        Assert.assertTrue(isVisible(locator), elementName + " should be visible but was NOT found");
        return driver.findElement(locator);
    }

    /** Assert and fill input - FAIL if input not found */
    protected void assertFillInput(String css, String value, String fieldName) {
        By loc = By.cssSelector(css);
        Assert.assertTrue(isVisible(loc), fieldName + " input not found: " + css);
        WebElement el = driver.findElement(loc);
        el.clear();
        el.sendKeys(value);
        Assert.assertFalse(el.getAttribute("value").isEmpty(), fieldName + " was not filled");
    }

    /** Assert select option from native dropdown - FAIL if not found */
    protected void assertSelectContaining(String css, String text, String fieldName) throws InterruptedException {
        By loc = By.cssSelector(css);
        Assert.assertTrue(isVisible(loc), fieldName + " dropdown not found: " + css);
        Select sel = new Select(driver.findElement(loc));
        Thread.sleep(500);
        boolean found = false;
        for (WebElement opt : sel.getOptions()) {
            if (opt.getText().contains(text)) {
                sel.selectByVisibleText(opt.getText());
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, fieldName + ": option containing '" + text + "' not found in dropdown");
    }

    /** Assert click custom dropdown and select option - FAIL if not clickable */
    protected void assertClickCustomSelect(String label, String optionText) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(500);

        By dropdown = null;
        By s1 = By.xpath("//*[contains(text(),'Select " + label + "')]");
        By s2 = By.xpath("//h4[contains(text(),'" + label + "')]/following-sibling::div[1]");
        By s3 = By.xpath("//*[contains(text(),'" + label + "') and not(self::h4) and not(self::h1) and not(self::label)]");
        By s4 = By.xpath("//*[contains(text(),'Preferred " + label + "')]");

        if (isVisible(s1)) dropdown = s1;
        else if (isVisible(s2)) dropdown = s2;
        else if (isVisible(s4)) dropdown = s4;
        else if (isVisible(s3)) dropdown = s3;

        Assert.assertNotNull(dropdown, "Custom dropdown for '" + label + "' not found on page");
        Assert.assertTrue(isVisible(dropdown), "Custom dropdown for '" + label + "' not found on page");

        WebElement el = driver.findElement(dropdown);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        Thread.sleep(500);
        el.click();
        Thread.sleep(1500);

        if (optionText != null && !optionText.isEmpty()) {
            By option = By.xpath("//div[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + optionText.toLowerCase() + "')] | //li[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + optionText.toLowerCase() + "')]");
            if (!isVisible(option)) {
                option = By.xpath("//*[contains(text(),'" + optionText + "')]");
            }
            Assert.assertTrue(isVisible(option), "Option '" + optionText + "' not found in " + label + " dropdown");
            driver.findElement(option).click();
        } else {
            By firstOpt = By.xpath("(//div[contains(@class,'option')])[1] | (//li[contains(@class,'option')])[1]");
            if (isVisible(firstOpt)) driver.findElement(firstOpt).click();
        }
        Thread.sleep(500);
    }
}
