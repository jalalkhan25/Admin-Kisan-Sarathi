package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/**
 * EventManagementPage.java
 * ========================
 * Page Object for the Event Management module (Online Events).
 *
 * Frontend reference (ks2.0_web):
 *   - List page  : /event-management        (EventManagementList/MainPage.tsx)
 *   - Create page: /event-management/create  (CreateEvent/CreateEvent.tsx)
 *   - Details/Edit: /event-management/create/details/{eventID}
 *
 * The create form uses a shared CustomSelect component that renders as:
 *   <h4>Label *</h4>
 *   <button type="button"><p>placeholder / selected value</p> ...</button>
 *   (when open) <ul><li><span>Option text</span></li> ...</ul>
 *   with a search box: <input placeholder="Search...">
 */
public class EventManagementPage {

    private final WebDriver driver;
    private final int waitTime = 15;

    // ============================================================
    // ROUTES
    // ============================================================
    public static final String LIST_PATH = "/event-management";
    public static final String CREATE_PATH = "/event-management/create";

    // ============================================================
    // LOCATORS - LIST PAGE
    // ============================================================

    // Sidebar menu item (Event Management)
    private final By sidebarMenu =
            By.xpath("//a[contains(@href,'/event-management')]//p[contains(text(),'Event')]");

    // Page header rendered by the shared Header component
    private final By pageHeader =
            By.xpath("//*[normalize-space()='Event Management']");

    // Section heading on the list page
    private final By onlineEventListHeading =
            By.xpath("//*[contains(normalize-space(),'Online Event List')]");

    // "+ New Event" button (visible only for privileged roles like Admin)
    private final By newEventButton =
            By.xpath("//button[contains(normalize-space(),'New Event')]");

    // Search input on the list page (note: placeholder uses an ellipsis char)
    private final By searchInput =
            By.xpath("//input[starts-with(@placeholder,'Search event')]");

    // Filters button that opens the filter drawer
    private final By filtersButton =
            By.xpath("//button[contains(normalize-space(),'Filters')]");

    // Tabs
    private final By upcomingTab = By.xpath("//button[normalize-space()='Upcoming']");
    private final By completedTab = By.xpath("//button[normalize-space()='Completed']");

    // List-type radio buttons (name='listType')
    // roles: Creator, Coordinator, Co-Coordinator, Participants
    private By listTypeRadio(String role) {
        return By.xpath("//input[@name='listType' and @value='" + role + "']");
    }

    // Event rows / table
    private final By eventTable = By.cssSelector("table");
    private final By firstEventRow = By.cssSelector("table tbody tr:first-child");

    // ============================================================
    // LOCATORS - CREATE FORM
    // ============================================================

    // Create form heading
    private final By createFormHeading =
            By.xpath("//*[contains(normalize-space(),'Create Online Event') or contains(normalize-space(),'Edit Online Event')]");

    // Title input
    private final By titleInput = By.cssSelector("input[name='title']");

    // Date & time inputs (SimpleDateTimeSection)
    private final By dateInput = By.cssSelector("input[name='date']");
    private final By startTimeInput = By.cssSelector("input[name='startTime']");
    private final By endTimeInput = By.cssSelector("input[name='endTime']");

    // Description rich text field (FormRichTextField -> textarea or contenteditable)
    private final By descriptionField =
            By.xpath("//textarea[@name='description'] "
                    + "| //*[@name='description']//textarea "
                    + "| //*[contains(@class,'ql-editor')]");

    // Submit button
    private final By submitButton =
            By.xpath("//button[@type='submit' and (normalize-space()='Submit' or normalize-space()='Update')]");

    // Toast / success message
    private final By successToast =
            By.xpath("//*[contains(text(),'success') or contains(text(),'Success') or contains(text(),'created')]");

    // ============================================================
    // CONSTRUCTOR
    // ============================================================
    public EventManagementPage(WebDriver driver) {
        this.driver = driver;
    }

    // ============================================================
    // NAVIGATION
    // ============================================================

    /** Open the Event Management list via sidebar, or fall back to URL. */
    public void openModule(String baseUrl) {
        if (WaitUtils.isElementDisplayed(driver, sidebarMenu, 8)) {
            driver.findElement(sidebarMenu).click();
        } else {
            driver.get(baseUrl + LIST_PATH);
        }
    }

    /** Navigate directly to the create-event page. */
    public void openCreateEventPage(String baseUrl) {
        driver.get(baseUrl + CREATE_PATH);
    }

    // ============================================================
    // LIST PAGE ACTIONS
    // ============================================================

    public boolean isListPageDisplayed() {
        return WaitUtils.isElementDisplayed(driver, pageHeader, waitTime)
                || WaitUtils.isElementDisplayed(driver, onlineEventListHeading, waitTime);
    }

    public boolean isNewEventButtonDisplayed() {
        return WaitUtils.isElementDisplayed(driver, newEventButton, 8);
    }

    public void clickNewEventButton() {
        WaitUtils.waitForElementClickable(driver, newEventButton, waitTime).click();
    }

    public boolean isSearchInputDisplayed() {
        return WaitUtils.isElementDisplayed(driver, searchInput, 8);
    }

    public void searchEvent(String text) {
        WebElement el = WaitUtils.waitForElementVisible(driver, searchInput, waitTime);
        el.clear();
        el.sendKeys(text);
    }

    public boolean isFiltersButtonDisplayed() {
        return WaitUtils.isElementDisplayed(driver, filtersButton, 8);
    }

    public void clickFiltersButton() {
        WaitUtils.waitForElementClickable(driver, filtersButton, waitTime).click();
    }

    public void clickUpcomingTab() {
        WaitUtils.waitForElementClickable(driver, upcomingTab, waitTime).click();
    }

    public void clickCompletedTab() {
        WaitUtils.waitForElementClickable(driver, completedTab, waitTime).click();
    }

    /** Select a list-type radio button (Creator / Coordinator / Co-Coordinator / Participants). */
    public boolean selectListType(String role) {
        By radio = listTypeRadio(role);
        if (WaitUtils.isElementDisplayed(driver, radio, 8)) {
            WebElement el = driver.findElement(radio);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            return true;
        }
        return false;
    }

    public boolean isEventTableDisplayed() {
        return WaitUtils.isElementDisplayed(driver, eventTable, 8);
    }

    public boolean hasEventRows() {
        return WaitUtils.isElementDisplayed(driver, firstEventRow, 8);
    }

    public void openFirstEvent() {
        WaitUtils.waitForElementClickable(driver, firstEventRow, waitTime).click();
    }

    // ============================================================
    // CREATE FORM ACTIONS
    // ============================================================

    public boolean isCreateFormDisplayed() {
        return WaitUtils.isElementDisplayed(driver, createFormHeading, waitTime);
    }

    public void enterTitle(String title) {
        WebElement el = WaitUtils.waitForElementVisible(driver, titleInput, waitTime);
        el.clear();
        el.sendKeys(title);
    }

    /** Select the Event Type CustomSelect (e.g. "Webinar"). */
    public void selectEventType(String optionText) {
        selectCustomSelect("Event Type", optionText);
    }

    /** Select the Language CustomSelect (label rendered as "Select language"). */
    public void selectLanguage(String optionText) {
        selectCustomSelect("Select language", optionText);
    }

    /** Select the Target Audience CustomSelect ("Farmers" or "Officials"). */
    public void selectTargetAudience(String optionText) {
        selectCustomSelect("Target Audience", optionText);
    }

    /**
     * Select the Domain / Subdomain / Commodity dropdowns.
     * Note: their <h4> label is hidden via CSS but still present in the DOM,
     * so an h4-based XPath still resolves.
     */
    public void selectDomain(String optionText) {
        selectCustomSelect("Domain", optionText);
    }

    public void selectSubdomain(String optionText) {
        selectCustomSelect("Subdomain", optionText);
    }

    public void selectCommodity(String optionText) {
        selectCustomSelect("Commodity", optionText);
    }

    public void enterDate(String yyyyMmDd) {
        WebElement el = WaitUtils.waitForElementVisible(driver, dateInput, waitTime);
        setNativeValue(el, yyyyMmDd);
    }

    public void enterStartTime(String hhmm) {
        WebElement el = WaitUtils.waitForElementVisible(driver, startTimeInput, waitTime);
        setNativeValue(el, hhmm);
    }

    public void enterEndTime(String hhmm) {
        WebElement el = WaitUtils.waitForElementVisible(driver, endTimeInput, waitTime);
        setNativeValue(el, hhmm);
    }

    public void enterDescription(String text) {
        if (WaitUtils.isElementDisplayed(driver, descriptionField, 8)) {
            WebElement el = driver.findElement(descriptionField);
            el.click();
            el.sendKeys(text);
        }
    }

    public boolean isSubmitButtonDisplayed() {
        return WaitUtils.isElementDisplayed(driver, submitButton, 8);
    }

    public void clickSubmit() {
        WebElement el = WaitUtils.waitForElementClickable(driver, submitButton, waitTime);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        el.click();
    }

    public boolean isSuccessMessageDisplayed() {
        return WaitUtils.isElementDisplayed(driver, successToast, 10);
    }

    // ============================================================
    // HELPERS
    // ============================================================

    /**
     * Generic helper for the shared CustomSelect component.
     * Clicks the trigger button next to the given label, optionally types into
     * the search box, then clicks the matching option.
     */
    public void selectCustomSelect(String label, String optionText) {
        // Trigger button: the <button> that follows the <h4> label
        By trigger = By.xpath("//h4[contains(normalize-space(),'" + label + "')]/following-sibling::button[1]");

        // Fallback: a button whose <p> shows the placeholder text (used when h4 is hidden)
        if (!WaitUtils.isElementDisplayed(driver, trigger, 5)) {
            trigger = By.xpath("//button[.//p[normalize-space()='" + label + "']]");
        }

        WebElement triggerEl = WaitUtils.waitForElementClickable(driver, trigger, waitTime);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", triggerEl);
        triggerEl.click();

        // If a search box appears inside the open dropdown, type to filter.
        By searchBox = By.xpath("//input[@placeholder='Search...']");
        if (WaitUtils.isElementDisplayed(driver, searchBox, 3)) {
            WebElement search = driver.findElement(searchBox);
            search.clear();
            search.sendKeys(optionText);
        }

        // Option row: <li> containing a <span> with the option text
        By option = By.xpath("//ul//li[.//span[contains(normalize-space(),'" + optionText + "')]]");
        if (!WaitUtils.isElementDisplayed(driver, option, 5)) {
            option = By.xpath("//li[contains(normalize-space(),'" + optionText + "')]");
        }
        WaitUtils.waitForElementClickable(driver, option, waitTime).click();
    }

    /**
     * Set value on native date/time inputs in a React-compatible way.
     *
     * React overrides the input's `value` setter and tracks the value
     * internally, so a plain `el.value = x` is ignored by the component's
     * onChange handler (the field then reads back as empty after submit).
     * Using the prototype's native setter + a bubbling 'input' event makes
     * React pick up the change and update its controlled state.
     */
    private void setNativeValue(WebElement el, String value) {
        ((JavascriptExecutor) driver).executeScript(
                "var el = arguments[0], value = arguments[1];"
                        + "var proto = window.HTMLInputElement.prototype;"
                        + "var setter = Object.getOwnPropertyDescriptor(proto, 'value').set;"
                        + "setter.call(el, value);"
                        + "el.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "el.dispatchEvent(new Event('change', { bubbles: true }));",
                el, value);
    }

    // ============================================================
    // LOCATION CASCADE (Farmers audience)
    // ============================================================
    // The location dropdowns are CustomSelect components whose visible <h4>
    // label is the selectTitle:
    //   "Select Zone" / "Select State" / "Select District" /
    //   "Select KVK" / "Select Block"
    // Each has a searchable list; option labels come from the API
    // (Atari_Zone, stateName, districtName, kvk name, blockName).

    public void selectZone(String searchText)     { selectLocation("Select Zone", searchText); }
    public void selectState(String searchText)    { selectLocation("Select State", searchText); }
    public void selectDistrict(String searchText) { selectLocation("Select District", searchText); }
    public void selectKVK(String searchText)      { selectLocation("Select KVK", searchText); }
    public void selectBlock(String searchText)    { selectLocation("Select Block", searchText); }

    /**
     * Open a CustomSelect identified by its <h4> title, type into the search
     * box (case-insensitive filter in the component), and click the first
     * matching option. Robust against option label casing differences.
     */
    public void selectLocation(String title, String searchText) {
        // Trigger button immediately after the <h4> title
        By trigger = By.xpath("//h4[normalize-space()='" + title + "' or normalize-space()='" + title + "*']"
                + "/following-sibling::button[1]");
        if (!WaitUtils.isElementDisplayed(driver, trigger, 5)) {
            trigger = By.xpath("//h4[contains(normalize-space(),'" + title + "')]/following-sibling::button[1]");
        }
        WebElement triggerEl = WaitUtils.waitForElementClickable(driver, trigger, waitTime);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", triggerEl);
        triggerEl.click();

        // Type into the open dropdown's search box to filter options
        By searchBox = By.xpath("//input[@placeholder='Search...']");
        if (WaitUtils.isElementDisplayed(driver, searchBox, 4)) {
            WebElement search = driver.findElement(searchBox);
            search.clear();
            search.sendKeys(searchText);
        }

        // Wait a moment for the async option list to populate/filter
        try { Thread.sleep(1500); } catch (InterruptedException ignored) { }

        // Real option rows have a <span>; the "No options"/"Please select..."
        // placeholder row has no <span>.
        By optionRows = By.xpath("//ul[contains(@class,'customScrollbar')]/li[.//span]");
        java.util.List<WebElement> options = driver.findElements(optionRows);

        // Diagnostics: print what the dropdown is offering
        StringBuilder sb = new StringBuilder();
        for (WebElement li : driver.findElements(By.xpath("//ul[contains(@class,'customScrollbar')]/li"))) {
            sb.append('[').append(li.getText().trim()).append(']');
        }
        System.out.println("  >> [" + title + "] search='" + searchText + "' options=" + sb);

        if (options.isEmpty()) {
            throw new org.openqa.selenium.NoSuchElementException(
                    "No selectable options for '" + title + "' (search='" + searchText + "'). Dropdown showed: " + sb);
        }

        WebElement opt = options.get(0);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", opt);
        try {
            opt.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
        }
    }

    // ============================================================
    // THUMBNAIL UPLOAD
    // ============================================================
    /** Upload a thumbnail image by sending its absolute path to the hidden file input. */
    public void uploadThumbnail(String absolutePath) {
        By fileInput = By.cssSelector("input[type='file']");
        WebElement input = WaitUtils.waitForElementPresent(driver, fileInput, waitTime);
        input.sendKeys(absolutePath);
    }

    // ============================================================
    // COORDINATOR (host) - required
    // ============================================================
    /**
     * Enter a coordinator mobile number and click Add. The app looks the user
     * up via an API and adds a chip on success. Returns true if a chip appears.
     */
    public boolean addCoordinator(String mobile) {
        By coordinatorInput = By.cssSelector("input[name='hostUserID']");
        WebElement input = WaitUtils.waitForElementVisible(driver, coordinatorInput, waitTime);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", input);
        input.clear();
        input.sendKeys(mobile);

        // The "Add" button sits next to the coordinator input
        By addButton = By.xpath("//input[@name='hostUserID']/following-sibling::button[normalize-space()='Add']");
        if (WaitUtils.isElementDisplayed(driver, addButton, 5)) {
            driver.findElement(addButton).click();
        } else {
            input.sendKeys(org.openqa.selenium.Keys.ENTER);
        }

        // A chip (with a remove "×" button) confirms the user was added
        By chip = By.xpath("//label[contains(normalize-space(),'Coordinator')]"
                + "/ancestor::div[1]//button[@title='Remove user']");
        return WaitUtils.isElementDisplayed(driver, chip, 12);
    }

    // ============================================================
    // SUCCESS DETECTION (strict)
    // ============================================================
    /**
     * A create is only truly successful when the app redirects back to the
     * list page (/event-management, NOT /create) or shows a success toast.
     */
    public boolean isCreateSuccessful() {
        boolean toast = WaitUtils.isElementDisplayed(driver, successToast, 8);
        String url = driver.getCurrentUrl();
        boolean redirected = url.contains("/event-management") && !url.contains("/create");
        return toast || redirected;
    }
}
