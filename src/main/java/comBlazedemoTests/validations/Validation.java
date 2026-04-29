package comBlazedemoTests.validations;

import comBlazedemoTests.utils.logs.LogsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class Validation extends BaseAssertion {

    private static final ThreadLocal<SoftAssert> softAssert =
            ThreadLocal.withInitial(SoftAssert::new);

    public Validation(WebDriver driver) {
        super(driver);
    }

    public static void reset() {
        softAssert.set(new SoftAssert());
        LogsManager.info("SoftAssert reset for new test");
    }

    // ─── Core ─────────────────────────────────────────────────

    @Override
    public void assertTrue(boolean condition, String message) {
        LogsManager.info("SoftAssert assertTrue: " + message);
        softAssert.get().assertTrue(condition, message);
    }

    @Override
    public void assertEquals(String actual, String expected, String message) {
        LogsManager.info("SoftAssert assertEquals: " + message + " | Expected: " + expected + " | Actual: " + actual);
        softAssert.get().assertEquals(actual, expected, message);
    }

    @Override
    public void assertFalse(boolean condition, String message) {
        LogsManager.info("SoftAssert assertFalse: " + message);
        softAssert.get().assertFalse(condition, message);
    }

    // ─── Page Title ───────────────────────────────────────────

    @Override
    public void assertPageTitle(String expectedTitle) {
        String actual = driver.getTitle();
        LogsManager.info("SoftAssert assertPageTitle | Expected: " + expectedTitle + " | Actual: " + actual);
        softAssert.get().assertEquals(actual, expectedTitle, "Page title mismatch");
    }

    @Override
    public void assertPageTitleContains(String partialTitle) {
        String actual = driver.getTitle();
        LogsManager.info("SoftAssert assertPageTitleContains: " + partialTitle + " | Actual: " + actual);
        softAssert.get().assertTrue(actual.contains(partialTitle),
                "Page title does not contain: " + partialTitle + " | Actual: " + actual);
    }

    // ─── Page URL ─────────────────────────────────────────────

    @Override
    public void assertCurrentUrl(String expectedUrl) {
        String actual = driver.getCurrentUrl();
        LogsManager.info("SoftAssert assertCurrentUrl | Expected: " + expectedUrl + " | Actual: " + actual);
        softAssert.get().assertEquals(actual, expectedUrl, "URL mismatch");
    }

    @Override
    public void assertUrlContains(String partialUrl) {
        String actual = driver.getCurrentUrl();
        LogsManager.info("SoftAssert assertUrlContains: " + partialUrl + " | Actual: " + actual);
        softAssert.get().assertTrue(actual.contains(partialUrl),
                "URL does not contain: " + partialUrl + " | Actual: " + actual);
    }

    // ─── Element Visibility ───────────────────────────────────

    @Override
    public void assertElementVisible(By locator, String elementName) {
        try {
            WebElement element = waitUtils.waitForElementToBeVisible(locator);
            LogsManager.info("SoftAssert assertElementVisible: " + elementName);
            softAssert.get().assertTrue(element.isDisplayed(), "Element not visible: " + elementName);
        } catch (Exception e) {
            LogsManager.error("Element not found: " + elementName, e);
            softAssert.get().fail("Element not found in DOM: " + elementName);
        }
    }

    @Override
    public void assertElementNotVisible(By locator, String elementName) {
        try {
            boolean invisible = waitUtils.waitForElementToBeInvisible(locator);
            LogsManager.info("SoftAssert assertElementNotVisible: " + elementName);
            softAssert.get().assertTrue(invisible, "Element should not be visible: " + elementName);
        } catch (Exception e) {
            LogsManager.error("Error checking visibility: " + elementName, e);
            softAssert.get().fail("Error checking visibility of: " + elementName);
        }
    }

    // ─── Element Text ─────────────────────────────────────────

    @Override
    public void assertElementText(By locator, String expectedText, String elementName) {
        try {
            String actual = waitUtils.waitForElementToBeVisible(locator).getText().trim();
            LogsManager.info("SoftAssert assertElementText [" + elementName + "] | Expected: " + expectedText + " | Actual: " + actual);
            softAssert.get().assertEquals(actual, expectedText, "Text mismatch for: " + elementName);
        } catch (Exception e) {
            LogsManager.error("Cannot get text from: " + elementName, e);
            softAssert.get().fail("Cannot get text from element: " + elementName);
        }
    }

    @Override
    public void assertElementTextContains(By locator, String partialText, String elementName) {
        try {
            String actual = waitUtils.waitForElementToBeVisible(locator).getText().trim();
            LogsManager.info("SoftAssert assertElementTextContains [" + elementName + "]: " + partialText + " | Actual: " + actual);
            softAssert.get().assertTrue(actual.contains(partialText),
                    "[" + elementName + "] does not contain: " + partialText + " | Actual: " + actual);
        } catch (Exception e) {
            LogsManager.error("Cannot get text from: " + elementName, e);
            softAssert.get().fail("Cannot get text from element: " + elementName);
        }
    }

    // ─── Element Enabled ──────────────────────────────────────

    @Override
    public void assertElementEnabled(By locator, String elementName) {
        try {
            boolean enabled = waitUtils.waitForElementToBeVisible(locator).isEnabled();
            LogsManager.info("SoftAssert assertElementEnabled: " + elementName);
            softAssert.get().assertTrue(enabled, "Element is not enabled: " + elementName);
        } catch (Exception e) {
            LogsManager.error("Cannot check enabled state: " + elementName, e);
            softAssert.get().fail("Cannot check enabled state of: " + elementName);
        }
    }

    // ─── assertAll ────────────────────────────────────────────

    public static void assertAll() {
        try {
            LogsManager.step("Running assertAll()");
            softAssert.get().assertAll();
        } catch (AssertionError e) {
            LogsManager.error("One or more assertions failed", e);
            throw e;
        } finally {
            softAssert.set(new SoftAssert());
        }
    }
}