package comBlazedemoTests.drivers;

import comBlazedemoTests.utils.actions.PropertyReader;
import org.openqa.selenium.WebDriver;

/**
 * WebDriverFactory - Alternative Factory Implementation
 * Can be used as a static factory method pattern
 * Alternative to using the Browser enum directly
 */
public class WebDriverFactory {

    private WebDriverFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a WebDriver instance based on configuration
     * @return WebDriver instance
     */
    public static WebDriver createWebDriver() {
        String browserType = PropertyReader.getProperty("browserType", "CHROME");
        return createWebDriver(browserType);
    }

    /**
     * Creates a WebDriver instance for specified browser type
     * @param browserType Browser type (CHROME, EDGE, FIREFOX)
     * @return WebDriver instance
     */
    public static WebDriver createWebDriver(String browserType) {
        try {
            Browser browser = Browser.valueOf(browserType.toUpperCase());
            AbstractDriver driverFactory = browser.getDriverFactory();
            return driverFactory.createDriver();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unsupported browser type: " + browserType, e);
        }
    }

    /**
     * Creates a WebDriver instance and registers it with DriverManager
     * @return WebDriver instance managed by DriverManager
     */
    public static WebDriver createManagedDriver() {
        WebDriver driver = createWebDriver();
        DriverManager.setDriver(driver);
        return driver;
    }

    /**
     * Creates a WebDriver instance and registers it with DriverManager
     * @param browserType Browser type (CHROME, EDGE, FIREFOX)
     * @return WebDriver instance managed by DriverManager
     */
    public static WebDriver createManagedDriver(String browserType) {
        WebDriver driver = createWebDriver(browserType);
        DriverManager.setDriver(driver);
        return driver;
    }
}
