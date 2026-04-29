package comBlazedemoTests.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import java.time.Duration;

/**
 * Firefox Driver Factory
 * Creates and configures FirefoxDriver instances with optimized settings
 */
public class FireFoxFactory implements AbstractDriver {

    private static final int WAIT_TIME_SECONDS = 10;
    private static final int IMPLICIT_WAIT_SECONDS = 5;

    @Override
    public WebDriver createDriver() {
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver(getFirefoxOptions());
        configureWaits(driver);
        return driver;
    }

    /**
     * Configures Firefox options for automation
     * @return FirefoxOptions configured instance
     */
    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();

        // Set a clean Firefox profile
        FirefoxProfile profile = new FirefoxProfile();

        // Disable notifications
        profile.setPreference("dom.webnotifications.enabled", false);

        // Disable popup blocking warning
        profile.setPreference("privacy.trackingprotection.enabled", false);

        // Improve performance
        profile.setPreference("browser.sessionstore.max_tabs_undo", 0);
        profile.setPreference("browser.startup.homepage_override.mstone", "ignore");

        // Set accept language
        profile.setPreference("intl.accept_languages", "en-US,en");

        // Apply profile to options
        options.setProfile(profile);

        // Additional Firefox arguments
        options.addArguments("--start-maximized");

        // Optional: Enable headless mode (uncomment for headless execution)
        // options.addArguments("--headless");

        // Set binary path (if using non-standard installation)
        // options.setBinary("/path/to/firefox");

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
