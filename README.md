# Kisan Sarathi 2.0 - Admin Panel Automation Framework

A simple, beginner-friendly Selenium automation framework for the Kisan Sarathi 2.0 Admin Panel.

## Tech Stack

| Tool | Purpose |
|------|---------|
| Java 11+ | Programming language |
| Maven | Build & dependency management |
| Selenium WebDriver | Browser automation |
| TestNG | Test framework |
| WebDriverManager | Auto browser driver management |
| Extent Reports | HTML test reports |

## Project Structure

```
ks-admin-automation/
├── pom.xml                          # Maven config with all dependencies
├── testng.xml                       # TestNG suite config (controls test execution)
├── README.md                        # This file
├── src/test/java/
│   ├── base/
│   │   └── BaseTest.java           # Browser setup & teardown
│   ├── pages/
│   │   ├── LoginPage.java          # Login page elements & actions
│   │   ├── UserManagementPage.java
│   │   ├── FarmerManagementPage.java
│   │   ├── AdvisoryManagementPage.java
│   │   ├── QueryManagementPage.java
│   │   └── CallManagementPage.java
│   ├── tests/
│   │   ├── LoginTest.java          # Login test cases
│   │   ├── UserManagementTest.java
│   │   ├── FarmerManagementTest.java
│   │   ├── AdvisoryManagementTest.java
│   │   ├── QueryManagementTest.java
│   │   └── CallManagementTest.java
│   ├── utils/
│   │   ├── ConfigReader.java       # Reads config.properties
│   │   ├── WaitUtils.java          # Reusable wait methods
│   │   ├── ScreenshotUtils.java    # Screenshot on failure
│   │   └── ExtentReportManager.java # HTML report generation
│   └── listeners/
│       └── TestListener.java       # Auto screenshot + reporting
└── src/test/resources/
    └── config.properties            # URLs, credentials, settings
```

---

## Step 1: Prerequisites

Make sure you have installed:

1. **Java JDK 11 or higher** — [Download](https://adoptium.net/)
2. **Maven** — [Download](https://maven.apache.org/download.cgi)
3. **Google Chrome** browser (latest version)

Verify installations:
```bash
java -version
mvn -version
```

---

## Step 2: Update config.properties

Open `src/test/resources/config.properties` and update:

```properties
browser=chrome
url=https://ks-aams-web.digitalindiacorporation.in/login
username=YOUR_MOBILE_NUMBER
password=YOUR_PASSWORD
explicitWait=20
headless=false
```

**Important:** Never commit real credentials to Git!

---

## Step 3: Run Tests

### From Terminal (Command Prompt / PowerShell):

```bash
# Navigate to the project folder
cd ks-admin-automation

# Run all tests
mvn clean test

# Run only login tests
mvn clean test -Dtest=tests.LoginTest

# Run a specific test method
mvn clean test -Dtest=tests.LoginTest#verifyValidAdminLogin
```

### From IDE (IntelliJ / Eclipse):

1. Right-click on `testng.xml` → Run
2. Or right-click any test class → Run as TestNG Test

---

## Step 4: Check Reports

After test execution:

| Item | Location |
|------|----------|
| **HTML Report** | `reports/TestReport.html` |
| **Screenshots** | `screenshots/` folder |
| **TestNG Report** | `test-output/` folder |

Open `reports/TestReport.html` in your browser to see the detailed report.

---

## Step 5: How to Add a New Test Case

1. Open the relevant test file (e.g., `tests/LoginTest.java`)
2. Add a new method with `@Test` annotation:

```java
@Test(priority = 6, description = "My new test case")
public void verifyMyNewFeature() {
    // Your test code here
    LoginPage loginPage = new LoginPage(driver);
    // ... assertions
}
```

3. Run `mvn clean test` to execute

---

## Step 6: How to Add a New Page Class

1. Create a new file in `src/test/java/pages/` (e.g., `NewModulePage.java`)
2. Follow this template:

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class NewModulePage {
    private WebDriver driver;
    private int waitTime = 20;

    // Locators
    private By pageTitle = By.xpath("//h1[text()='Module Name']");

    // Constructor
    public NewModulePage(WebDriver driver) {
        this.driver = driver;
    }

    // Methods
    public boolean isModulePageDisplayed() {
        return WaitUtils.isElementDisplayed(driver, pageTitle, waitTime);
    }
}
```

3. Create the corresponding test class in `src/test/java/tests/`
4. Add the test class to `testng.xml`

---

## Locator Strategy

We use locators in this priority order:

1. `data-testid` (most stable - needs to be added to frontend)
2. `id`
3. `name`
4. `placeholder`
5. `aria-label`
6. Button text
7. CSS selector
8. Relative XPath (never absolute XPath)

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| ChromeDriver version mismatch | WebDriverManager handles this automatically |
| Element not found | Increase `explicitWait` in config.properties |
| Tests fail on slow network | Increase wait times or add explicit waits |
| Screenshots not saving | Check `screenshots/` folder permissions |
| Login fails | Verify credentials in config.properties |
| CAPTCHA appears | Login attempt counter triggers at 3+ attempts. Clear browser data or wait. |

---

## Notes for Kisan Sarathi Frontend

The frontend currently **does NOT have `data-testid` attributes**. The automation uses `placeholder`, CSS selectors, and XPath as fallbacks.

For more stable automation, add these `data-testid` attributes to the frontend components (see the detailed list in the project documentation).

---

## What's NOT Included (Out of Scope for Now)

- ❌ API automation
- ❌ Excel/JSON data-driven testing
- ❌ Docker
- ❌ CI/CD pipeline
- ❌ Parallel execution
- ❌ Database validation

These can be added later once the basic framework is working.
