package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.EventManagementPage;
import utils.ConfigReader;

/**
 * EventManagementTest
 * ===================
 * Covers the Event Management (Online Events) module of the KS Admin panel.
 *
 * Flow reference (ks2.0_web):
 *   List  : /event-management
 *   Create: /event-management/create
 *
 * Login is handled once in BaseTest (@BeforeSuite). Each test navigates
 * to the module directly and uses the EventManagementPage object.
 */
public class EventManagementTest extends BaseTest {

    /** Reusable page object. */
    private EventManagementPage eventPage() {
        return new EventManagementPage(driver);
    }

    // ------------------------------------------------------------
    // TC-EVT-01: Event Management list page opens
    // ------------------------------------------------------------
    @Test(priority = 1, groups = {"smoke"})
    public void verifyEventManagementPageOpens() throws InterruptedException {
        EventManagementPage page = eventPage();
        page.openModule(BASE_URL);
        Thread.sleep(2000);

        Assert.assertTrue(page.isListPageDisplayed(),
                "Event Management list page header should be visible");
        System.out.println("[PASS] Event Management list page opens");
    }

    // ------------------------------------------------------------
    // TC-EVT-02: Core list controls are present
    // ------------------------------------------------------------
    @Test(priority = 2, groups = {"smoke"})
    public void verifyListControlsPresent() throws InterruptedException {
        EventManagementPage page = eventPage();
        driver.get(BASE_URL + EventManagementPage.LIST_PATH);
        Thread.sleep(2000);

        Assert.assertTrue(page.isSearchInputDisplayed(), "Search input should be visible");
        Assert.assertTrue(page.isFiltersButtonDisplayed(), "Filters button should be visible");
        Assert.assertTrue(page.isEventTableDisplayed(), "Event list/table should be visible");
        System.out.println("[PASS] Search, Filters, and event list are present");
    }

    // ------------------------------------------------------------
    // TC-EVT-03: Switch between Upcoming and Completed tabs
    // ------------------------------------------------------------
    @Test(priority = 3)
    public void verifyTabSwitching() throws InterruptedException {
        EventManagementPage page = eventPage();
        driver.get(BASE_URL + EventManagementPage.LIST_PATH);
        Thread.sleep(2000);

        page.clickCompletedTab();
        Thread.sleep(1500);
        page.clickUpcomingTab();
        Thread.sleep(1500);

        Assert.assertTrue(page.isEventTableDisplayed(),
                "Event list should still be visible after switching tabs");
        System.out.println("[PASS] Upcoming/Completed tab switching works");
    }

    // ------------------------------------------------------------
    // TC-EVT-04: Search an event by keyword
    // ------------------------------------------------------------
    @Test(priority = 4)
    public void verifySearchEvent() throws InterruptedException {
        EventManagementPage page = eventPage();
        driver.get(BASE_URL + EventManagementPage.LIST_PATH);
        Thread.sleep(2000);

        page.searchEvent("Test");
        Thread.sleep(2500); // debounce is 400ms + fetch
        Assert.assertTrue(page.isEventTableDisplayed(),
                "Event list should render results after search");
        System.out.println("[PASS] Event search executed");
    }

    // ------------------------------------------------------------
    // TC-EVT-05: Open filter drawer
    // ------------------------------------------------------------
    @Test(priority = 5)
    public void verifyFilterDrawerOpens() throws InterruptedException {
        EventManagementPage page = eventPage();
        driver.get(BASE_URL + EventManagementPage.LIST_PATH);
        Thread.sleep(2000);

        page.clickFiltersButton();
        Thread.sleep(1500);

        // Drawer shows Date/Category sections
        boolean drawerVisible = isVisible(By.xpath("//*[normalize-space()='Date']"))
                || isVisible(By.xpath("//*[normalize-space()='Category']"))
                || isVisible(By.xpath("//label[contains(text(),'From Date')]"));
        Assert.assertTrue(drawerVisible, "Filter drawer sections should be visible");
        System.out.println("[PASS] Filter drawer opens with filter sections");
    }

    // ------------------------------------------------------------
    // TC-EVT-06: Navigate to Create Online Event form
    // ------------------------------------------------------------
    @Test(priority = 6, groups = {"smoke"})
    public void verifyCreateEventPageOpens() throws InterruptedException {
        EventManagementPage page = eventPage();
        page.openCreateEventPage(BASE_URL);
        Thread.sleep(2500);

        Assert.assertTrue(page.isCreateFormDisplayed(),
                "Create Online Event form heading should be visible");
        Assert.assertTrue(page.isSubmitButtonDisplayed(),
                "Submit button should be visible on the create form");
        System.out.println("[PASS] Create Online Event form opens");
    }

    // ------------------------------------------------------------
    // TC-EVT-07: Create a new online event (Webinar) end-to-end
    // Fills every mandatory field so the event is actually created:
    //   Title, Event Type, Language, Date/Time (<= 60 min),
    //   Description, Thumbnail, Target Audience = Farmers,
    //   Location cascade (Zone -> State -> District -> KVK -> Block),
    //   Coordinator (host).
    // ------------------------------------------------------------
    @Test(priority = 7)
    public void createOnlineEvent() throws InterruptedException {
        EventManagementPage page = eventPage();
        page.openCreateEventPage(BASE_URL);
        Thread.sleep(3000);
        Assert.assertTrue(page.isCreateFormDisplayed(), "Create form must be displayed");

        // 1. Title
        page.enterTitle("Automation Webinar " + System.currentTimeMillis());
        Thread.sleep(500);

        // 2. Event Type
        page.selectEventType("Webinar");
        Thread.sleep(1000);

        // 3. Category cascade: Domain -> Subdomain -> Commodity (optional but set)
        page.selectDomain("Agriculture");
        Thread.sleep(1500);
        page.selectSubdomain("Cereal");
        Thread.sleep(1500);
        page.selectCommodity("Adley");
        Thread.sleep(1000);

        // 4. Language
        page.selectLanguage("English");
        Thread.sleep(1000);

        // 5. Date & Time (tomorrow, 30-minute window: within the 15-60 min / max 1 hour rule)
        java.time.LocalDate tomorrow = java.time.LocalDate.now().plusDays(1);
        page.enterDate(tomorrow.toString()); // yyyy-MM-dd
        page.enterStartTime("11:00");
        page.enterEndTime("11:30");
        Thread.sleep(1000);

        // Verify date/time actually persisted in the controlled inputs
        // (guards against the "date/time vacant after submission" bug)
        String dVal = driver.findElement(org.openqa.selenium.By.cssSelector("input[name='date']")).getAttribute("value");
        String sVal = driver.findElement(org.openqa.selenium.By.cssSelector("input[name='startTime']")).getAttribute("value");
        String eVal = driver.findElement(org.openqa.selenium.By.cssSelector("input[name='endTime']")).getAttribute("value");
        System.out.println("[INFO] Date=" + dVal + " Start=" + sVal + " End=" + eVal);
        Assert.assertFalse(dVal == null || dVal.isEmpty(), "Date should not be vacant after entry");
        Assert.assertFalse(sVal == null || sVal.isEmpty(), "Start time should not be vacant after entry");
        Assert.assertFalse(eVal == null || eVal.isEmpty(), "End time should not be vacant after entry");

        // 6. Description
        page.enterDescription("This is an automation test event. Please ignore.");
        Thread.sleep(500);

        // 7. Target Audience -> Farmers (reveals the location cascade)
        page.selectTargetAudience("Farmers");
        Thread.sleep(2000);

        // 8. Location cascade (each selection loads the next dropdown's options)
        page.selectZone("Kanpur");            // Atari zone iii Kanpur
        Thread.sleep(2500);
        page.selectState("Uttar");            // Uttar Pradesh
        Thread.sleep(2500);
        page.selectDistrict("Azamgarh");      // Azamgarh
        Thread.sleep(2500);
        page.selectKVK("DIC");                // DIC test Azamgarh
        Thread.sleep(2500);
        page.selectBlock("Ahiraula");         // Ahiraula
        Thread.sleep(1500);

        // 9. Thumbnail (mandatory)
        String thumb = new java.io.File("src/test/resources/event-thumbnail.png").getAbsolutePath();
        page.uploadThumbnail(thumb);
        Thread.sleep(3000); // allow S3 upload to complete

        // 10. Coordinator (mandatory host) - a valid KVK Expert of DIC test Azamgarh
        String coordinatorMobile = ConfigReader.getProperty("eventCoordinatorMobile");
        if (coordinatorMobile == null || coordinatorMobile.isEmpty()) {
            coordinatorMobile = "8709769741"; // KVK Expert fallback
        }
        boolean coordinatorAdded = page.addCoordinator(coordinatorMobile);
        System.out.println("[INFO] Coordinator added (" + coordinatorMobile + "): " + coordinatorAdded);
        Thread.sleep(1500);

        // 11. Submit
        page.clickSubmit();
        Thread.sleep(5000);

        // Strict success: redirect back to list page OR success toast
        Assert.assertTrue(page.isCreateSuccessful(),
                "Event should be created successfully (redirect to list or success toast). URL: "
                        + driver.getCurrentUrl());
        System.out.println("[PASS] Online event created successfully. URL: " + driver.getCurrentUrl());
    }

    // ------------------------------------------------------------
    // TC-EVT-08: Verify created event appears in the list
    // ------------------------------------------------------------
    @Test(priority = 8)
    public void verifyEventInList() throws InterruptedException {
        EventManagementPage page = eventPage();
        driver.get(BASE_URL + EventManagementPage.LIST_PATH);
        Thread.sleep(2500);

        page.searchEvent("Automation Webinar");
        Thread.sleep(2500);

        Assert.assertTrue(page.isEventTableDisplayed(),
                "Event list should be visible when verifying created event");
        System.out.println("[PASS] Created event lookup executed in list");
    }
}
