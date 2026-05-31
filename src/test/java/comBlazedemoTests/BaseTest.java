package comBlazedemoTests;

import comBlazedemoTests.drivers.GUIDriver;
import comBlazedemoTests.utils.actions.PropertyReader;
import comBlazedemoTests.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * BaseTest - Base class for all test classes
 * Handles driver initialization and cleanup
 * Provides common functionality for test cases
 */
public class BaseTest {

    static {
        try {
            java.io.File logFile = new java.io.File("logs/automation.log");
            if (logFile.exists()) {
                logFile.delete();
            }
            logFile.getParentFile().mkdirs();
            logFile.createNewFile();
        } catch (Exception e) {
            System.err.println("Could not clear log file: " + e.getMessage());
        }
    }

    protected WebDriver driver;
    protected GUIDriver guiDriver;

    @BeforeSuite
    public void suiteSetUp() {
        // Clear Allure results
        Path allureResultsPath = Paths.get("target/allure-results");
        if (Files.exists(allureResultsPath)) {
            try {
                Files.walkFileTree(allureResultsPath, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }
                });
                LogsManager.info("Allure results cleared for new run");
            } catch (IOException e) {
                LogsManager.error("Failed to clear Allure results", e);
            }
        }

        // Log run header
        LogsManager.info("================================================");
        LogsManager.info("   RUN STARTED: " + java.time.LocalDateTime.now());
        LogsManager.info("   BROWSER: " + PropertyReader.getProperty("browserType","CHROME"));
        LogsManager.info("   BASE URL: " + PropertyReader.getProperty("baseURL","https://www.demoblaze.com"));
        LogsManager.info("================================================");
    }

    /**
     * Setup method - Runs before each test
     * Initializes the WebDriver
     */
    @BeforeMethod
    public void setUp() {
        guiDriver = new GUIDriver();
        driver = guiDriver.get();
    }

    /**
     * Teardown method - Runs after each test
     * Quits the WebDriver and cleans up resources
     */
    @AfterMethod
    public void tearDown() {
        if (guiDriver != null && guiDriver.isDriverInitialized()) {
            guiDriver.quitDriver();
        }
    }
}

