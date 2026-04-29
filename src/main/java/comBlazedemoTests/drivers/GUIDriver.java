package comBlazedemoTests.drivers;

import comBlazedemoTests.pages.NavBarPage;
import comBlazedemoTests.pages.HomePage;
import comBlazedemoTests.utils.actions.ActionsHelper;
import comBlazedemoTests.utils.actions.AlertUtils;
import comBlazedemoTests.utils.actions.BrowserAction;
import comBlazedemoTests.utils.actions.FrameAction;
import comBlazedemoTests.utils.actions.PropertyReader;
import comBlazedemoTests.utils.actions.WaitUtils;
import comBlazedemoTests.utils.logs.LogsManager;
import comBlazedemoTests.validations.Validation;
import comBlazedemoTests.validations.Verification;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;

public class GUIDriver {

    // ===== Framework Components =====
    public final ActionsHelper actionsHelper;
    public final AlertUtils alertUtils;
    public final BrowserAction browserAction;
    public final FrameAction frameAction;
    public final WaitUtils waitUtils;
    public final Validation validation;
    public final Verification verification;
    public final NavBarPage navBar;
    public final HomePage homePage;

    public GUIDriver() {
        // Step 1: Driver first
        initializeDriver();

        // Step 2: Driver is ready — initialize all components
        WebDriver driver = DriverManager.getDriver();
        this.actionsHelper  = new ActionsHelper(driver);
        this.alertUtils     = new AlertUtils(driver);
        this.browserAction  = new BrowserAction(driver);
        this.frameAction    = new FrameAction(driver);
        this.waitUtils      = new WaitUtils(driver);
        this.validation     = new Validation(driver);
        this.verification   = new Verification(driver);
        this.navBar         = new NavBarPage(this);
        this.homePage       = new HomePage(this);
    }

    private void initializeDriver() {
        String browserType = PropertyReader.getProperty("browserType", "CHROME");
        try {
            Browser browser = Browser.valueOf(browserType.toUpperCase());
            AbstractDriver driverFactory = browser.getDriverFactory();
            WebDriver driver = driverFactory.createDriver();
            WebDriver threadSafeDriver = ThreadGuard.protect(driver);
            DriverManager.setDriver(threadSafeDriver);
        } catch (IllegalArgumentException e) {
            LogsManager.error("Invalid browser type specified: " + browserType, e);
            throw new RuntimeException("Invalid browser type: " + browserType +
                    ". Supported types: CHROME, EDGE, FIREFOX", e);
        }
    }

    public WebDriver get() {
        return DriverManager.getDriver();
    }

    public void quitDriver() {
        DriverManager.quitDriver();
    }

    public boolean isDriverInitialized() {
        return DriverManager.isDriverInitialized();
    }
}