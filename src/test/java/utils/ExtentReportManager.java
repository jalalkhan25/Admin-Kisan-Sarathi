package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * ExtentReportManager.java
 * ========================
 * This utility manages the Extent Report (HTML test report).
 * After test execution, you can find the report at: reports/TestReport.html
 *
 * The report shows:
 * - Which tests passed/failed
 * - Screenshots for failed tests
 * - Execution time
 */
public class ExtentReportManager {

    // Single instance of ExtentReports (Singleton pattern)
    private static ExtentReports extent;

    /**
     * Get the ExtentReports instance. Creates it if not already created.
     *
     * @return ExtentReports instance
     */
    public static ExtentReports getExtentReports() {
        if (extent == null) {
            // Configure the HTML report file
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reports/TestReport.html");

            // Report settings
            sparkReporter.config().setDocumentTitle("KS Admin Panel - Test Report");
            sparkReporter.config().setReportName("Kisan Sarathi 2.0 Automation Results");
            sparkReporter.config().setTheme(Theme.STANDARD);

            // Create ExtentReports and attach the reporter
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Add system info to the report
            extent.setSystemInfo("Application", "Kisan Sarathi 2.0 Admin Panel");
            extent.setSystemInfo("Browser", ConfigReader.getProperty("browser"));
            extent.setSystemInfo("Environment", "Dev");
            extent.setSystemInfo("URL", ConfigReader.getProperty("url"));
        }
        return extent;
    }

    /**
     * Create a new test entry in the report.
     *
     * @param testName - name of the test case
     * @return ExtentTest instance for logging steps
     */
    public static ExtentTest createTest(String testName) {
        return getExtentReports().createTest(testName);
    }

    /**
     * Flush (save) the report to disk.
     * Must be called after all tests finish.
     */
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
