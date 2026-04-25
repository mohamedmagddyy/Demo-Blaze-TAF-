package comBlazedemoTests;

import comBlazedemoTests.drivers.GUIDriver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest - Base class for all test classes
 * Handles driver initialization and cleanup
 * Provides common functionality for test cases
 */
public class BaseTest {

    protected WebDriver driver;
    protected GUIDriver guiDriver;

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

