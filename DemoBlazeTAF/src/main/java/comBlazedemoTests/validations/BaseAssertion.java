package comBlazedemoTests.validations;

import comBlazedemoTests.utils.actions.ActionsHelper;
import comBlazedemoTests.utils.actions.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BaseAssertion {

    protected final WebDriver driver;
    protected final WaitUtils waitUtils;
    protected final ActionsHelper actionsHelper;

    public BaseAssertion(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        this.actionsHelper = new ActionsHelper(driver);
    }

    // ─── Core ─────────────────────────────────────────────────
    protected abstract void assertTrue(boolean condition, String message);
    protected abstract void assertEquals(String actual, String expected, String message);
    protected abstract void assertFalse(boolean condition, String message);

    // ─── Page ─────────────────────────────────────────────────
    protected abstract void assertPageTitle(String expectedTitle);
    protected abstract void assertPageTitleContains(String partialTitle);
    protected abstract void assertCurrentUrl(String expectedUrl);
    protected abstract void assertUrlContains(String partialUrl);

    // ─── Element ──────────────────────────────────────────────
    protected abstract void assertElementVisible(By locator, String elementName);
    protected abstract void assertElementNotVisible(By locator, String elementName);
    protected abstract void assertElementText(By locator, String expectedText, String elementName);
    protected abstract void assertElementTextContains(By locator, String partialText, String elementName);
    protected abstract void assertElementEnabled(By locator, String elementName);
}
