package comBlazedemoTests.drivers;

import comBlazedemoTests.utils.actions.PropertyReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.time.Duration;

/**
 * Edge Driver Factory
 * Creates and configures EdgeDriver instances with optimized settings
 */
public class EdgeFactory implements AbstractDriver {

    private static final int WAIT_TIME_SECONDS = 10;
    private static final int IMPLICIT_WAIT_SECONDS = 5;

    @Override
    public WebDriver createDriver() {
        WebDriver driver = new EdgeDriver(getEdgeOptions());
        configureWaits(driver);
        return driver;
    }

    /**
     * Configures Edge options for automation
     * @return EdgeOptions configured instance
     */
    private EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();

        // Maximize window
        options.addArguments("--start-maximized");

        // Disable notifications
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        // Improve performance
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");

        // Stability improvements
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Additional Edge-specific arguments
        options.addArguments("--disable-blink-features=AutomationControlled");

         // Read headless from System property (set by CI) or config file
        String headless = System.getProperty("headless",
                PropertyReader.getProperty("headless", "false"));
        if (headless.equalsIgnoreCase("true")) {
            options.addArguments("--headless=new");            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        return options;
    }

    /**
     * Configures implicit and explicit waits
     * @param driver WebDriver instance
     */
    private void configureWaits(WebDriver driver) {
        // Implicit wait - applies to all element searches
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));

        // Page load timeout
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(WAIT_TIME_SECONDS));
    }
}
