package comBlazedemoTests.customlistners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.ExtentTest;
import comBlazedemoTests.drivers.DriverManager;
import comBlazedemoTests.media.ScreenshotsManager;
import comBlazedemoTests.utils.logs.LogsManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethodListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNGListneres implements IExecutionListener, IInvokedMethodListener, ITestListener {

    private static ExtentReports extent;
    private static final Logger logger = LogManager.getLogger(TestNGListneres.class);
    private final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    // IExecutionListener methods
    @Override
    public void onExecutionStart() {
        // Initialize ExtentReports with ExtentSparkReporter
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent.attachReporter(spark);

        LogsManager.info("================================================");
        LogsManager.info("   SUITE STARTED: " + java.time.LocalDateTime.now());
        LogsManager.info("================================================");
        LogsManager.info("");
    }

    @Override
    public void onExecutionFinish() {
        // Flush and close ExtentReports properly
        if (extent != null) {
            extent.flush();
        }
        // Log total execution finished
        logger.info("All tests completed");
        LogsManager.info("================================================");
        LogsManager.info("   RUN FINISHED: " + java.time.LocalDateTime.now());
        LogsManager.info("================================================");
    }

    // IInvokedMethodListener methods
    @Override
    public void beforeInvocation(org.testng.IInvokedMethod method, ITestResult testResult) {
        // Check if the invoked method is a test method (not config)
        if (method.isTestMethod()) {
            // Record start time using System.currentTimeMillis() and store it in a ThreadLocal<Long>
            startTime.set(System.currentTimeMillis());
            // Log the method name that is about to run
            logger.info("Method about to run: " + method.getTestMethod().getMethodName());
        }
    }

    @Override
    public void afterInvocation(org.testng.IInvokedMethod method, ITestResult testResult) {
        // Check if the invoked method is a test method
        if (method.isTestMethod()) {
            // Clean up ThreadLocal to avoid memory leaks
            startTime.remove();
        }
    }

    // ITestListener methods
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getName();
        String description = result.getMethod().getDescription();

        // Create a new ExtentTest node using the test name
        ExtentTest test = extent.createTest(testName, description);
        // Store it in a ThreadLocal<ExtentTest> for thread safety
        extentTest.set(test);
        // Add Allure step: "Test Started: {testName}"
        Allure.step("Test Started: " + testName);
        
        // Log test start

        // Read @Severity annotation from test method
        String priority = "MEDIUM"; // default
        try {
            java.lang.reflect.Method method = result.getMethod()
                .getConstructorOrMethod().getMethod();
            io.qameta.allure.Severity severityAnnotation = 
                method.getAnnotation(io.qameta.allure.Severity.class);
            if (severityAnnotation != null) {
                switch (severityAnnotation.value()) {
                    case CRITICAL -> priority = "HIGH";
                    case NORMAL   -> priority = "MEDIUM";
                    case MINOR    -> priority = "LOW";
                    default       -> priority = severityAnnotation.value().toString();
                }
            }
        } catch (Exception e) {
            priority = "MEDIUM";
        }

        // Update log block to include priority:
        LogsManager.info("╔══════════════════════════════════════════════════╗");
        LogsManager.info("║ TEST     : " + testName);
        LogsManager.info("║ DESC     : " + (description != null && !description.isEmpty() ? description : "No description"));
        LogsManager.info("║ PRIORITY : " + priority);
        LogsManager.info("╚══════════════════════════════════════════════════╝");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Mark ExtentTest as pass with a success message
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.pass("Test passed successfully");
        }
        // Add Allure step with PASSED status
        Allure.step("PASSED");
        
        // Log test end
        long duration = result.getEndMillis() - result.getStartMillis();
        LogsManager.info("✔ PASSED  : " + result.getName() + " (" + duration + " ms)");
        LogsManager.info("");
        
        // Clean up ThreadLocal
        extentTest.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Mark ExtentTest as fail with the exception message
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.fail("Test failed: " + result.getThrowable().getMessage());
            // Capture a screenshot using WebDriver
            try {
                String base64 = ScreenshotsManager.takeScreenshotAsBase64(DriverManager.getDriver(), result.getName());
                if (base64 != null) {
                    // Attach screenshot to ExtentReports as base64
                    test.addScreenCaptureFromBase64String(base64, "Screenshot on failure");
                    // Attach screenshot to Allure report using @Attachment
                    attachScreenshotToAllure(base64, result.getName());
                }
            } catch (Exception e) {
                logger.error("Failed to capture screenshot on failure", e);
            }
        }
        // Log test end
        long duration = result.getEndMillis() - result.getStartMillis();
        LogsManager.error("✘ FAILED  : " + result.getName() + " (" + duration + " ms)");
        LogsManager.error("  REASON  : " + result.getThrowable().getMessage());
        LogsManager.info("");
        // Clean up ThreadLocal
        extentTest.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Mark ExtentTest as skip
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.skip("Test skipped");
        }
        // Add Allure step with SKIPPED status
        Allure.step("SKIPPED");
        
        // Log test end
        LogsManager.warn("⚠ SKIPPED : " + result.getName());
        LogsManager.info("");
        
        // Clean up ThreadLocal
        extentTest.remove();
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    public static byte[] attachScreenshotToAllure(String base64, String testName) {
        return java.util.Base64.getDecoder().decode(base64);
    }
}
