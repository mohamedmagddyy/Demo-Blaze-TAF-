package comBlazedemoTests.validations;

import comBlazedemoTests.utils.logs.LogsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class Verification extends BaseAssertion {

    public Verification(WebDriver driver) {
        super(driver);
    }

    // ─── Core ─────────────────────────────────────────────────

    @Override
    public void assertTrue(boolean condition, String message) {
        LogsManager.info("HardAssert assertTrue: " + message);
        Assert.assertTrue(condition, message);
    }

    @Override
    public void assertEquals(String actual, String expected, String message) {
        LogsManager.info("HardAssert assertEquals: " + message + " | Expected: " + expected + " | Actual: " + actual);
        Assert.assertEquals(actual, expected, message);
    }

    @Override
    public void assertFalse(boolean condition, String message) {
        LogsManager.info("HardAssert assertFalse: " + message);
        Assert.assertFalse(condition, message);
    }

    // ─── Page Title ───────────────────────────────────────────

    @Override
    public void assertPageTitle(String expectedTitle) {
        String actual = driver.getTitle();
        LogsManager.info("HardAssert assertPageTitle | Expected: " + expectedTitle + " | Actual: " + actual);
        Assert.assertEquals(actual, expectedTitle, "Page title mismatch");
    }

    @Override
    public void assertPageTitleContains(String partialTitle) {
        String actual = driver.getTitle();
        LogsManager.info("HardAssert assertPageTitleContains: " + partialTitle + " | Actual: " + actual);
        Assert.assertTrue(actual.contains(partialTitle),
                "Page title does not contain: " + partialTitle + " | Actual: " + actual);
    }

    // ─── Page URL ─────────────────────────────────────────────

    @Override
    public void assertCurrentUrl(String expectedUrl) {
        String actual = driver.getCurrentUrl();
        LogsManager.info("HardAssert assertCurrentUrl | Expected: " + expectedUrl + " | Actual: " + actual);
        Assert.assertEquals(actual, expectedUrl, "URL mismatch");
    }

    @Override
    public void assertUrlContains(String partialUrl) {
        String actual = driver.getCurrentUrl();
        LogsManager.info("HardAssert assertUrlContains: " + partialUrl + " | Actual: " + actual);
        Assert.assertTrue(actual.contains(partialUrl),
                "URL does not contain: " + partialUrl + " | Actual: " + actual);
    }

    // ─── Element Visibility ───────────────────────────────────

    @Override
    public void assertElementVisible(By locator, String elementName) {
        try {
            WebElement element = waitUtils.waitForElementToBeVisible(locator);
            LogsManager.info("HardAssert assertElementVisible: " + elementName);
            Assert.assertTrue(element.isDisplayed(), "Element not visible: " + elementName);
        } catch (Exception e) {
            LogsManager.error("Element not found: " + elementName, e);
            Assert.fail("Element not found in DOM: " + elementName);
        }
    }

    @Override
    public void assertElementNotVisible(By locator, String elementName) {
        try {
            boolean invisible = waitUtils.waitForElementToBeInvisible(locator);
            LogsManager.info("HardAssert assertElementNotVisible: " + elementName);
            Assert.assertTrue(invisible, "Element should not be visible: " + elementName);
        } catch (Exception e) {
            LogsManager.error("Error checking visibility: " + elementName, e);
            Assert.fail("Error checking visibility of: " + elementName);
        }
    }

    // ─── Element Text ─────────────────────────────────────────

    @Override
    public void assertElementText(By locator, String expectedText, String elementName) {
        try {
            String actual = waitUtils.waitForElementToBeVisible(locator).getText().trim();
            LogsManager.info("HardAssert assertElementText [" + elementName + "] | Expected: " + expectedText + " | Actual: " + actual);
            Assert.assertEquals(actual, expectedText, "Text mismatch for: " + elementName);
        } catch (Exception e) {
            LogsManager.error("Cannot get text from: " + elementName, e);
            Assert.fail("Cannot get text from element: " + elementName);
        }
    }

    @Override
    public void assertElementTextContains(By locator, String partialText, String elementName) {
        try {
            String actual = waitUtils.waitForElementToBeVisible(locator).getText().trim();
            LogsManager.info("HardAssert assertElementTextContains [" + elementName + "]: " + partialText + " | Actual: " + actual);
            Assert.assertTrue(actual.contains(partialText),
                    "[" + elementName + "] does not contain: " + partialText + " | Actual: " + actual);
        } catch (Exception e) {
            LogsManager.error("Cannot get text from: " + elementName, e);
            Assert.fail("Cannot get text from element: " + elementName);
        }
    }

    // ─── Element Enabled ──────────────────────────────────────

    @Override
    public void assertElementEnabled(By locator, String elementName) {
        try {
            boolean enabled = waitUtils.waitForElementToBeVisible(locator).isEnabled();
            LogsManager.info("HardAssert assertElementEnabled: " + elementName);
            Assert.assertTrue(enabled, "Element is not enabled: " + elementName);
        } catch (Exception e) {
            LogsManager.error("Cannot check enabled state: " + elementName, e);
            Assert.fail("Cannot check enabled state of: " + elementName);
        }
    }
}