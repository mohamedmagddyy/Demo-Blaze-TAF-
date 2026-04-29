package comBlazedemoTests.utils.actions;

import comBlazedemoTests.utils.logs.LogsManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class WaitUtils {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final long DEFAULT_WAIT_TIME = 10;
    private static final long POLLING_TIME = 500;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIME));
    }

    public WebElement waitForElementToBeVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            LogsManager.error("[waitForVisible] Timeout: " + locator, e);
            throw e;
        }
    }

    public WebElement waitForElementToBeClickable(By locator) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            LogsManager.error("[waitForClickable] Timeout: " + locator, e);
            throw e;
        }
    }

    public List<WebElement> waitForAllElementsToBeVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            LogsManager.error("[waitForAllVisible] Timeout: " + locator, e);
            throw e;
        }
    }

    public boolean waitForElementToBeInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public WebElement waitForNestedElementToBeVisible(WebElement parent, By childLocator) {
        return wait.until(driver ->
                parent.findElement(childLocator).isDisplayed()
                        ? parent.findElement(childLocator) : null);
    }

    public WebElement waitForNestedElementToBeClickable(WebElement parent, By childLocator) {
        return wait.until(driver -> {
            WebElement el = parent.findElement(childLocator);
            return (el.isDisplayed() && el.isEnabled()) ? el : null;
        });
    }

    public <T> T fluentWait(Function<WebDriver, T> condition, int timeoutSeconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(POLLING_TIME))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(condition);
    }

    public WebElement waitForElementToBeVisible(By locator, long seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeClickable(By locator, long seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }
    public Alert waitForAlertToBePresent() {
        try {
            return wait.until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            LogsManager.error("[waitForAlert] Timeout waiting for alert", e);
            throw e;
        }
    }
}