package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils.java
 * =====================
 * This utility captures screenshots when a test fails.
 * Screenshots are saved in the "screenshots" folder with timestamp.
 *
 * Usage: ScreenshotUtils.takeScreenshot(driver, "testName");
 */
public class ScreenshotUtils {

    // Folder where screenshots will be saved
    private static final String SCREENSHOT_FOLDER = "screenshots";

    /**
     * Take a screenshot and save it to the screenshots folder.
     *
     * @param driver   - the WebDriver instance
     * @param testName - name of the test (used in filename)
     * @return the full path of the saved screenshot
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        // Create screenshots folder if it doesn't exist
        try {
            Files.createDirectories(Paths.get(SCREENSHOT_FOLDER));
        } catch (IOException e) {
            System.out.println("Could not create screenshots folder");
        }

        // Generate filename with timestamp to avoid overwriting
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = SCREENSHOT_FOLDER + File.separator + fileName;

        // Take the screenshot
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshotFile.toPath(), Paths.get(filePath));
            System.out.println("Screenshot saved: " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }

        return filePath;
    }
}
