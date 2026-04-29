package comBlazedemoTests.drivers;

import comBlazedemoTests.utils.actions.PropertyReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Chrome Driver Factory
 * Creates and configures ChromeDriver instances with optimized settings
 */
public class ChromeDriverFactory implements AbstractDriver {

    private static final int WAIT_TIME_SECONDS = 10;
    private static final int IMPLICIT_WAIT_SECONDS = 5;

    @Override
    public WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(getChromeOptions());
        configureWaits(driver);
        return driver;
    }

    /**
     * Configures Chrome options for automation
     * @return ChromeOptions configured instance
     */
    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // Maximize window
        options.addArguments("--start-maximized");

        // Disable notifications
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        // Improve performance
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images"); // Optional: disable images for faster load

        // Stability improvements
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Optional: Enable headless mode (uncomment for headless execution)
        // options.addArguments("--headless");

        // Set user data directory for clean profile
        // options.addArguments("user-data-dir=/tmp/chrome");

        return options;
    }

    /**
     * Configures implicit and explicit waits
     * @param driver WebDriver instance
     */
    private void configureWaits(WebDriver driver) {
        // Implicit wait - applies to all element searches
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));

        // Explicit wait configuration (available for use in Page Objects)
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(WAIT_TIME_SECONDS));
    }


}
