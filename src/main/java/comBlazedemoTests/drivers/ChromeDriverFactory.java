package comBlazedemoTests.drivers;

import comBlazedemoTests.utils.actions.PropertyReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // ─── Arguments ───────────────────────────────────────────
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--password-store=basic");
        options.addArguments("--disable-features=PasswordLeakDetection");

        // ─── Prefs ────────────────────────────────────────────────
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("autofill.profile_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("dismissSavePasswordBubble", true);
        options.setExperimentalOption("prefs", prefs);

        // ─── Headless ─────────────────────────────────────────────
        String headless = System.getProperty("headless",
                PropertyReader.getProperty("headless", "false"));
        if (headless.equalsIgnoreCase("true")) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        return options;
    }

    private void configureWaits(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(WAIT_TIME_SECONDS));
    }
}