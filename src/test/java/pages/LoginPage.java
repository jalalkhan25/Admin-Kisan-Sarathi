package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * LoginPage - Page Object with PageFactory.
 * Locators and Actions in same class (Hybrid POM pattern).
 */
public class LoginPage extends BasePage {

    // ===== Web Locators =====
    @FindBy(xpath = "//input[@placeholder='Enter your mobile number']")
    private WebElement usernameInput;

    @FindBy(xpath = "//input[@placeholder='Enter password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[text()='Login']")
    private WebElement loginButton;

    @FindBy(xpath = "//h2[text()='Log in']")
    private WebElement loginHeading;

    @FindBy(id = "userRole")
    private WebElement roleDropdown;

    @FindBy(xpath = "//button[text()='Submit']")
    private WebElement roleSubmitButton;

    // ===== Constructor =====
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ===== Action Methods =====
    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameInput));
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean isLoginPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(loginHeading));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRoleSelectionDisplayed() {
        try {
            Thread.sleep(1000);
            return roleDropdown.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectRole(String roleName) {
        wait.until(ExpectedConditions.visibilityOf(roleDropdown));
        new Select(roleDropdown).selectByVisibleText(roleName);
    }

    public void clickRoleSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(roleSubmitButton));
        roleSubmitButton.click();
    }
}
