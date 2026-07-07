package tests;

import base.BaseTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

/** UserManagementTest - User page, search, filter, create expert, verify, view, disable */
public class UserManagementTest extends BaseTest {

    private static final String EXPERT_NAME = "Jalal Khan Expert";
    private static final String EXPERT_PHONE = generateIncrementalPhone();
    private static final String EXPERT_EMAIL = "jalal" + EXPERT_PHONE.substring(6) + "@expert.com";

    /**
     * Generates phone with 8th digit incrementing.
     * Base: 7000000999 → 7000001999 → 7000002999 ... 7000009999 → 7000010999
     * Reads/writes counter from file: phone_counter.txt
     */
    private static String generateIncrementalPhone() {
        int counter = 0;
        java.io.File counterFile = new java.io.File("phone_counter.txt");
        try {
            if (counterFile.exists()) {
                String val = new String(java.nio.file.Files.readAllBytes(counterFile.toPath())).trim();
                counter = Integer.parseInt(val);
            }
            counter++;
            java.nio.file.Files.write(counterFile.toPath(), String.valueOf(counter).getBytes());
        } catch (Exception e) {
            counter = (int) (System.currentTimeMillis() % 100);
        }
        // Format: 70000XX999 where XX = counter (8th digit increments)
        String base = "700000";
        String mid = String.format("%02d", counter % 100);
        String suffix = "99";
        return base + mid + suffix;
    }

    @Test(priority = 1)
    public void verifyUserManagementPageOpens() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        assertVisible(By.xpath("//h1[text()='User Management']"), "User Management title");
        System.out.println("[PASS] User Management page opens");
    }

    @Test(priority = 5, dependsOnMethods = {"createExpertUser"})
    public void verifyUserSearchFunctionality() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        WebElement search = assertVisible(By.cssSelector("input[placeholder='Search by Username, Mobile...']"), "User search input");
        search.clear();
        search.sendKeys(EXPERT_NAME);
        search.sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        By row = By.cssSelector("table tbody tr");
        Assert.assertTrue(isVisible(row), "No rows found when searching for registered user: " + EXPERT_NAME);
        String rowText = driver.findElement(row).getText().toLowerCase();
        Assert.assertTrue(rowText.contains(EXPERT_NAME.toLowerCase()),
                "Search results should contain '" + EXPERT_NAME + "'. Got: " + rowText);
        System.out.println("[PASS] User search works - found registered user: " + EXPERT_NAME);
    }

    @Test(priority = 3)
    public void verifyUserFilterButton() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        WebElement filterBtn = assertVisible(By.xpath("//button[contains(.,'Filters')]"), "User Filter button");
        filterBtn.click();
        Thread.sleep(1000);
        System.out.println("[PASS] User filter clicked");
    }

    @Test(priority = 4)
    public void createExpertUser() throws InterruptedException {
        driver.get(BASE_URL + "/user-management/registration");
        Thread.sleep(3000);
        assertVisible(By.xpath("//h1[contains(text(),'User')]"), "User Registration heading");

        assertSelectContaining("select[name='division']", "Extension", "SMD");
        Thread.sleep(2000);
        assertSelectContaining("select[name='designation']", "Expert", "Designation");
        Thread.sleep(2000);

        Assert.assertTrue(isVisible(By.cssSelector("select[name='organization']")), "Organization dropdown not visible");
        assertSelectContaining("select[name='organization']", "Kanpur", "Organization");
        Thread.sleep(2000);

        Assert.assertTrue(isVisible(By.cssSelector("select[name='state']")), "State dropdown not visible");
        assertSelectContaining("select[name='state']", "Uttar Pradesh", "State");
        Thread.sleep(2000);

        Assert.assertTrue(isVisible(By.cssSelector("select[name='district']")), "District dropdown not visible");
        assertSelectContaining("select[name='district']", "Azamgarh", "District");
        Thread.sleep(3000);

        Assert.assertTrue(isVisible(By.cssSelector("select[name='kvk']")), "KVK dropdown not visible");
        Select kvk = new Select(driver.findElement(By.cssSelector("select[name='kvk']")));
        Thread.sleep(1000);
        Assert.assertTrue(kvk.getOptions().size() > 1, "KVK has no options");
        kvk.selectByIndex(1);
        Thread.sleep(1000);

        assertFillInput("input[placeholder='Enter Name']", EXPERT_NAME, "Name");
        assertFillInput("input[placeholder='Enter Mobile Number']", EXPERT_PHONE, "Mobile");
        assertFillInput("input[placeholder='Enter Email']", EXPERT_EMAIL, "Email");

        scrollDown();
        Thread.sleep(1000);
        WebElement submitBtn = assertVisible(By.xpath("//button[text()='Submit']"), "Submit button");
        submitBtn.click();
        Thread.sleep(4000);

        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains("/user-management") && !url.contains("/registration"),
                "Should redirect to user-management. URL: " + url);
        System.out.println("[PASS] Expert created: " + EXPERT_NAME + " | Phone: " + EXPERT_PHONE);
    }

    @Test(priority = 6, dependsOnMethods = {"createExpertUser"})
    public void verifyCreatedExpertInList() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        WebElement search = assertVisible(By.cssSelector("input[placeholder='Search by Username, Mobile...']"), "Search input");
        search.clear();
        search.sendKeys(EXPERT_NAME);
        search.sendKeys(Keys.ENTER);
        Thread.sleep(3000);

        By row = By.cssSelector("table tbody tr");
        Assert.assertTrue(isVisible(row), "No rows found for " + EXPERT_NAME);
        String rowText = driver.findElement(row).getText();
        Assert.assertFalse(rowText.contains("No users found"), "User NOT found");
        Assert.assertTrue(rowText.toLowerCase().contains(EXPERT_NAME.toLowerCase()),
                "Name not in results. Row: " + rowText);
        System.out.println("[PASS] Expert verified in list: " + EXPERT_NAME);
    }

    @Test(priority = 7, dependsOnMethods = {"createExpertUser"})
    public void viewExpertDetails() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        WebElement search = driver.findElement(By.cssSelector("input[placeholder='Search by Username, Mobile...']"));
        search.clear();
        search.sendKeys(EXPERT_NAME);
        search.sendKeys(Keys.ENTER);
        Thread.sleep(3000);

        WebElement firstRow = assertVisible(By.cssSelector("table tbody tr:first-child"), "First row");
        firstRow.click();
        Thread.sleep(3000);
        Assert.assertTrue(driver.getCurrentUrl().contains("user-details"), "Should open user details. URL: " + driver.getCurrentUrl());
        System.out.println("[PASS] Viewed details: " + driver.getCurrentUrl());
    }

    @Test(priority = 8, dependsOnMethods = {"createExpertUser"})
    public void disableExpertUser() throws InterruptedException {
        driver.get(BASE_URL + "/user-management");
        Thread.sleep(2000);
        WebElement search = driver.findElement(By.cssSelector("input[placeholder='Search by Username, Mobile...']"));
        search.clear();
        search.sendKeys(EXPERT_NAME);
        search.sendKeys(Keys.ENTER);
        Thread.sleep(3000);

        By toggle = By.xpath("//table//img[@alt='Active' or @alt='Inactive']");
        WebElement toggleEl = assertVisible(toggle, "Toggle icon");
        String before = toggleEl.getAttribute("alt");
        toggleEl.click();
        Thread.sleep(1000);

        WebElement confirmBtn = assertVisible(By.xpath("//button[text()='Confirm']"), "Confirm button");
        confirmBtn.click();
        Thread.sleep(3000);

        String after = driver.findElement(toggle).getAttribute("alt");
        Assert.assertNotEquals(before, after, "Status should change");
        System.out.println("[PASS] Status: " + before + " -> " + after);
    }
}
