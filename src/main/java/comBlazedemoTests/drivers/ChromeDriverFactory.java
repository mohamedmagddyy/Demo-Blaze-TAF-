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
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-search-engine-choice-screen");

        // Disable password manager and "Save password" prompts
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("autofill.profile_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-save-password-bubble");

         // Read headless from System property (set by CI) or config file
        String headless = System.getProperty("headless",
                PropertyReader.getProperty("headless", "false"));
        if (headless.equalsIgnoreCase("true")) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

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
