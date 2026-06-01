package comBlazedemoTests.customlistners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import comBlazedemoTests.drivers.DriverManager;
import comBlazedemoTests.media.ScreenshotsManager;
import comBlazedemoTests.utils.logs.LogsManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.lang.reflect.Method;
import java.util.Base64;

public class TestNGListneres implements ITestListener, IInvokedMethodListener {

    private static ExtentReports extent;
    private static final Logger logger = LogManager.getLogger(TestNGListneres.class);

    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // =========================
    // SUITE START (SAFE INIT)
    // =========================
    @Override
    public void onStart(ITestContext context) {

        if (extent == null) {
            ExtentSparkReporter spark =
                    new ExtentSparkReporter("test-output/ExtentReport.html");

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }

        LogsManager.info("================================================");
        LogsManager.info(" TEST SUITE STARTED");
        LogsManager.info("================================================");
    }

    // =========================
    // TEST START
    // =========================
    @Override
    public void onTestStart(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        if (extent == null) {
            logger.error("Extent is NOT initialized!");
            return;
        }

        ExtentTest test = extent.createTest(testName, description);
        extentTest.set(test);

        Allure.step("Test Started: " + testName);

        LogsManager.info("================================================");
        LogsManager.info(" TEST: " + testName);
        LogsManager.info(" DESC: " + (description == null ? "N/A" : description));
        LogsManager.info("================================================");
    }

    // =========================
    // SUCCESS
    // =========================
    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTest test = extentTest.get();

        if (test != null) {
            test.pass("Test Passed");
        }

        Allure.step("Test Passed");

        LogsManager.info("✔ PASSED: " + result.getName());

        cleanup();
    }

    // =========================
    // FAILURE
    // =========================
    @Override
    public void onTestFailure(ITestResult result) {

        ExtentTest test = extentTest.get();
        Throwable error = result.getThrowable();

        if (test != null) {
            test.fail(error != null ? error.getMessage() : "Unknown error");
        }

        // Screenshot
        try {
            if (DriverManager.isDriverInitialized()) {

                String base64 = ScreenshotsManager.takeScreenshotAsBase64(
                        DriverManager.getDriver(),
                        result.getName()
                );

                if (base64 != null && test != null) {
                    test.addScreenCaptureFromBase64String(base64, "Failure Screenshot");
                    attachScreenshot(base64);
                }
            }
        } catch (Exception e) {
            logger.error("Screenshot failed", e);
        }

        LogsManager.error("✘ FAILED: " + result.getName());

        if (error != null) {
            LogsManager.error("REASON: " + error.getMessage());
        }

        cleanup();
    }

    // =========================
    // SKIPPED
    // =========================
    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentTest test = extentTest.get();

        if (test != null) {
            test.skip("Test Skipped");
        }

        Allure.step("Test Skipped");

        LogsManager.warn("⚠ SKIPPED: " + result.getName());

        cleanup();
    }

    // =========================
    // CLEANUP
    // =========================
    private void cleanup() {
        extentTest.remove();
    }

    // =========================
    // SUITE FINISH
    // =========================
    @Override
    public void onFinish(ITestContext context) {

        if (extent != null) {
            extent.flush();
        }

        LogsManager.info("================================================");
        LogsManager.info(" TEST SUITE FINISHED");
        LogsManager.info("================================================");
    }

    // =========================
    // SCREENSHOT ATTACHMENT
    // =========================
    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] attachScreenshot(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}