package listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import base.BaseTest;
import utils.EmailUtil;
import utils.ExtentReportManager;
import utils.ScreenshotUtils;

import java.lang.reflect.Field;

/**
 * TestListener - Takes screenshot on FAILURE and SKIP.
 * Logs results to Extent Report and EmailUtil.
 */
public class TestListener implements ITestListener {

    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private String currentSuite = "";

    @Override
    public void onStart(ITestContext context) {
        currentSuite = context.getName();
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
        BaseTest.totalTests++;
        BaseTest.passedTests++;
        EmailUtil.addResult(result.getMethod().getMethodName(), currentSuite, "PASS", null, null);
        System.out.println("PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().log(Status.FAIL, "FAILED: " + result.getMethod().getMethodName());
        extentTest.get().log(Status.FAIL, "Reason: " + result.getThrowable().getMessage());
        BaseTest.totalTests++;
        BaseTest.failedTests++;
        String ssPath = captureScreenshot(result, "FAIL");
        String reason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown";
        EmailUtil.addResult(result.getMethod().getMethodName(), currentSuite, "FAIL", reason, ssPath);
        System.out.println("FAILED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "SKIPPED: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.SKIP, "Reason: " + result.getThrowable().getMessage());
        }
        BaseTest.totalTests++;
        BaseTest.skippedTests++;
        String ssPath = captureScreenshot(result, "SKIP");
        String reason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Dependency failed / Setup error";
        EmailUtil.addResult(result.getMethod().getMethodName(), currentSuite, "SKIP", reason, ssPath);
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
    private String captureScreenshot(ITestResult result, String status) {
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
                return path;
            }
        } catch (Exception e) {
            System.out.println("Could not capture screenshot: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get WebDriver from the test instance.
     */
    private WebDriver getDriverFromResult(ITestResult result) {
        try {
            Object instance = result.getInstance();
            Field driverField = null;
            try {
                driverField = instance.getClass().getDeclaredField("driver");
            } catch (NoSuchFieldException e) {
                driverField = instance.getClass().getSuperclass().getDeclaredField("driver");
            }
            driverField.setAccessible(true);
            return (WebDriver) driverField.get(instance);
        } catch (Exception e) {
            return null;
        }
    }
}
