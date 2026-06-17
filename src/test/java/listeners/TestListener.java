package listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentReportManager;
import utils.ScreenshotUtils;

import java.lang.reflect.Field;

/**
 * TestListener - Takes screenshot on FAILURE and SKIP.
 * Logs results to Extent Report.
 */
public class TestListener implements ITestListener {

    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        System.out.println("========================================");
        System.out.println("Test Suite Started: " + context.getName());
        System.out.println("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentReportManager.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
        System.out.println("Running: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "PASSED: " + result.getMethod().getMethodName());
        System.out.println("PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().log(Status.FAIL, "FAILED: " + result.getMethod().getMethodName());
        extentTest.get().log(Status.FAIL, "Reason: " + result.getThrowable().getMessage());
        captureScreenshot(result, "FAIL");
        System.out.println("FAILED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "SKIPPED: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.SKIP, "Reason: " + result.getThrowable().getMessage());
        }
        captureScreenshot(result, "SKIP");
        System.out.println("SKIPPED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReport();
        System.out.println("========================================");
        System.out.println("Test Suite Finished: " + context.getName());
        System.out.println("Report saved at: reports/TestReport.html");
        System.out.println("========================================");
    }

    /**
     * Capture screenshot and attach to report for FAIL/SKIP.
     */
    private void captureScreenshot(ITestResult result, String status) {
        try {
            WebDriver driver = getDriverFromResult(result);
            if (driver != null) {
                String path = ScreenshotUtils.takeScreenshot(driver, result.getMethod().getMethodName() + "_" + status);
                if (status.equals("FAIL")) {
                    extentTest.get().fail("Screenshot:",
                            MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                } else {
                    extentTest.get().skip("Screenshot:",
                            MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                }
            }
        } catch (Exception e) {
            System.out.println("Could not capture screenshot: " + e.getMessage());
        }
    }

    /**
     * Get WebDriver from the test instance - checks both BaseTest (parent) and direct field.
     */
    private WebDriver getDriverFromResult(ITestResult result) {
        try {
            Object instance = result.getInstance();
            // Try direct field first (AllModulesTest has its own 'driver')
            Field driverField = null;
            try {
                driverField = instance.getClass().getDeclaredField("driver");
            } catch (NoSuchFieldException e) {
                // Try parent class (BaseTest pattern)
                driverField = instance.getClass().getSuperclass().getDeclaredField("driver");
            }
            driverField.setAccessible(true);
            return (WebDriver) driverField.get(instance);
        } catch (Exception e) {
            return null;
        }
    }
}
