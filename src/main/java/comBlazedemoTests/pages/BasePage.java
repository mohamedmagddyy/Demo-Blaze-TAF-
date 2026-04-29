package comBlazedemoTests.pages;

import comBlazedemoTests.drivers.GUIDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Parent class for all Page Object Models
 * Every page extends this and gets GUIDriver + common utilities for free.
 *
 * Rule: No test logic here. Only reusable page-level helpers.
 */
public abstract class BasePage {

    protected final GUIDriver gui;

    protected BasePage(GUIDriver gui) {
        this.gui = gui;
    }

    // =========================================================================
    // BROWSER INFO
    // =========================================================================

    public String getPageTitle() {
        return gui.browserAction.getPageTitle();
    }

    public String getCurrentUrl() {
        return gui.browserAction.getCurrentUrl();
    }

    // =========================================================================
    // ELEMENT STATE
    // =========================================================================

    /**
     * Safe visibility check — returns false instead of throwing
     */
    protected boolean isElementVisible(By locator) {
        try {
            return gui.get().findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait explicitly for an element to be visible
     */
    protected WebElement waitForVisible(By locator) {
        return gui.waitUtils.waitForElementToBeVisible(locator);
    }

    /**
     * Wait explicitly for an element to be clickable
     */
    protected WebElement waitForClickable(By locator) {
        return gui.waitUtils.waitForElementToBeClickable(locator);
    }

    /**
     * Get trimmed text of a visible element
     */
    protected String getText(By locator) {
        return gui.waitUtils.waitForElementToBeVisible(locator).getText().trim();
    }

    // =========================================================================
    // PAGE LOAD
    // =========================================================================

    /**
     * Wait until a specific element is present — used to confirm page is loaded.
     * Each subclass can call this in its constructor or first action.
     *
     * Example: waitForPageLoad(productGrid);
     */
    protected void waitForPageLoad(By landmarkLocator) {
        gui.waitUtils.waitForElementToBeVisible(landmarkLocator);
    }

    /**
     * Wait until URL contains a specific fragment
     */
    protected void waitForUrlContains(String urlFragment) {
        new WebDriverWait(gui.get(), Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains(urlFragment));
    }

    // =========================================================================
    // NAVIGATION
    // =========================================================================

    protected void navigateTo(String url) {
        gui.browserAction.navigateTo(url);
    }

    protected void goBack() {
        gui.browserAction.navigateBack();
    }

    // =========================================================================
    // SCROLL
    // =========================================================================

    protected void scrollTo(By locator) {
        gui.actionsHelper.scrollTo(locator);
    }
}