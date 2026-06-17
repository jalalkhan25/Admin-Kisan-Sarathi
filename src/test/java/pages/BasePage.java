package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage.java - Parent class for all Page Objects.
 * Uses PageFactory to initialize @FindBy elements.
 * All page classes extend this.
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // PageFactory initializes all @FindBy annotated WebElements
        PageFactory.initElements(driver, this);
    }
}
