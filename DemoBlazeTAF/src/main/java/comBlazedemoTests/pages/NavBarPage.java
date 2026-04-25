// src/main/java/comBlazedemoTests/pages/NavBarPage.java

package comBlazedemoTests.pages;

import comBlazedemoTests.drivers.GUIDriver;
import org.openqa.selenium.By;

/**
 * NavBarPage - Page Object Model for the navigation bar
 * The navbar is always visible and fixed on all pages of the application.
 * Provides methods to interact with navbar items and check panel/modal states.
 */
public class NavBarPage {

    private final GUIDriver gui;

    // ===== Navbar Links =====

    private static final By homeLink = By.id("PLACEHOLDER_home_link");

    private static final By contactLink = By.linkText("Contact");

    private static final By aboutUsLink = By.id("PLACEHOLDER_about_us_link");

    private static final By cartLink = By.id("PLACEHOLDER_cart_link");

    private static final By loginLink = By.id("PLACEHOLDER_login_link");

    private static final By signUpLink = By.id("PLACEHOLDER_sign_up_link");

    // ===== Panels/Modals =====

    private static final By contactPanel = By.id("exampleModal");

    private static final By aboutUsPanel = By.id("PLACEHOLDER_about_us_panel");

    private static final By loginPanel = By.id("PLACEHOLDER_login_panel");

    private static final By signUpPanel = By.id("PLACEHOLDER_sign_up_panel");


    public NavBarPage(GUIDriver gui) {
        this.gui = gui;
    }

    public void clickHome() {
        gui.actionsHelper.click(homeLink);
    }

    public void clickContact() {
        gui.actionsHelper.click(contactLink);
        gui.waitUtils.waitForElementToBeVisible(contactPanel);
    }


    public void clickAboutUs() {
        gui.actionsHelper.click(aboutUsLink);
        gui.waitUtils.waitForElementToBeVisible(aboutUsPanel);
    }


    public void clickCart() {
        gui.actionsHelper.click(cartLink);
    }


    public void clickLogin() {
        gui.actionsHelper.click(loginLink);
        gui.waitUtils.waitForElementToBeVisible(loginPanel);
    }


    public void clickSignUp() {
        gui.actionsHelper.click(signUpLink);
        gui.waitUtils.waitForElementToBeVisible(signUpPanel);
    }

    private boolean isPanelVisible(By locator) {
        try {
            return gui.get().findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isContactPanelOpen() {
        return isPanelVisible(contactPanel);
    }


    public boolean isAboutUsPanelOpen() {
        return isPanelVisible(aboutUsPanel);
    }


    public boolean isLoginPanelOpen() {
        return isPanelVisible(loginPanel);
    }


    public boolean isSignUpPanelOpen() {
        return isPanelVisible(signUpPanel);
    }
}
