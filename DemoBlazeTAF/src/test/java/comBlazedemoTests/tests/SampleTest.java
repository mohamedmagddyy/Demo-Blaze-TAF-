package comBlazedemoTests.tests;

import comBlazedemoTests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * SampleTest - Sample test class demonstrating framework usage
 * Shows how to use the GUIDriver and WebDriver in tests
 */
public class SampleTest extends BaseTest {

    /**
     * Sample test - Verifies browser can navigate to URL
     */
    @Test(description = "Verify browser navigation to base URL")
    public void testBrowserNavigation() {
        String baseURL = "https://www.demoblaze.com";
        driver.navigate().to(baseURL);

        String title = driver.getTitle();
        Assert.assertNotNull(title, "Page title should not be null");
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
    }

    /**
     * Sample test - Verifies page load
     */
    @Test(description = "Verify page title is loaded")
    public void testPageTitle() {
        driver.navigate().to("https://www.demoblaze.com");
        String title = driver.getTitle();

        Assert.assertTrue(title.contains("STORE"), "Title should contain 'STORE'");
    }

    /**
     * Sample test - Verify driver is thread-safe
     */
    @Test(description = "Verify driver initialization and availability")
    public void testDriverInitialization() {
        Assert.assertNotNull(driver, "Driver should be initialized");
        Assert.assertTrue(guiDriver.isDriverInitialized(), "Driver should be initialized in DriverManager");
    }
}

