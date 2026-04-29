package comBlazedemoTests.drivers;

import org.openqa.selenium.WebDriver;
import java.util.Objects;

/**
 * DriverManager - Thread-safe WebDriver management
 * Provides singleton access to WebDriver instances using ThreadLocal
 * Ensures thread safety for parallel test execution
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Sets the WebDriver for the current thread
     * @param driver WebDriver instance
     */
    public static void setDriver(WebDriver driver) {
        Objects.requireNonNull(driver, "Driver cannot be null");
        driverThreadLocal.set(driver);
    }

    /**
     * Gets the WebDriver for the current thread
     * @return WebDriver instance
     * @throws IllegalStateException if driver is not initialized
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Call setDriver() first.");
        }
        return driver;
    }

    /**
     * Quits the WebDriver and removes it from ThreadLocal
     * Safely handles null checks to prevent memory leaks
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    /**
     * Checks if a driver is currently initialized for this thread
     * @return true if driver is initialized, false otherwise
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }

    /**
     * Safely removes the driver from ThreadLocal without quitting
     * Use this if you want to manually manage driver lifecycle
     */
    public static void removeDriver() {
        driverThreadLocal.remove();
    }
}

