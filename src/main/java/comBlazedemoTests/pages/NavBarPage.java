package comBlazedemoTests.pages;

import comBlazedemoTests.drivers.GUIDriver;
import comBlazedemoTests.utils.logs.LogsManager;
import org.openqa.selenium.By;

/**
 * NavBarPage - Fixed navbar present on ALL pages
 * Uses GUIDriver for all interactions (no direct Selenium calls)
 *
 * Navbar items:
 *   Home        → id="nava"           (link → index.html)
 *   Contact     → data-target="#exampleModal"  (modal)
 *   About Us    → data-target="#videoModal"    (modal)
 *   Cart        → id="cartur"         (link → cart.html)
 *   Log in      → id="login2"         (modal)
 *   Log out     → id="logout2"        (hidden by default, shown after login)
 *   Username    → id="nameofuser"     (hidden by default, shown after login)
 *   Sign up     → id="signin2"        (modal)
 */
public class NavBarPage extends BasePage {


    // ─── Navbar links ─────────────────────────────────────────────────────────
    private static final By homeLink    = By.id("nava");
    private static final By cartLink    = By.id("cartur");
    private static final By loginLink   = By.id("login2");
    private static final By logoutLink  = By.id("logout2");
    private static final By usernameTag = By.id("nameofuser");
    private static final By signUpLink  = By.id("signin2");

    // Contact & About Us trigger modals — no id, use data-target
    private static final By contactLink = By.cssSelector("a[data-target='#exampleModal']");
    private static final By aboutUsLink = By.cssSelector("a[data-target='#videoModal']");

    // ─── Modal containers ─────────────────────────────────────────────────────
    private static final By contactModal = By.id("exampleModal");
    private static final By aboutUsModal = By.id("videoModal");
    private static final By loginModal   = By.id("logInModal");
    private static final By signUpModal  = By.id("signInModal");

    // ─── Contact modal fields ─────────────────────────────────────────────────
    private static final By contactEmail   = By.id("recipient-email");
    private static final By contactName    = By.id("recipient-name");
    private static final By contactMessage = By.id("message-text");
    private static final By contactSendBtn = By.cssSelector("#exampleModal .btn-primary");

    // ─── Login modal fields ───────────────────────────────────────────────────
    private static final By loginUsername = By.id("loginusername");
    private static final By loginPassword = By.id("loginpassword");
    private static final By loginBtn      = By.cssSelector("#logInModal .btn-primary");

    // ─── Sign Up modal fields ─────────────────────────────────────────────────
    private static final By signUpUsername = By.id("sign-username");
    private static final By signUpPassword = By.id("sign-password");
    private static final By signUpBtn      = By.cssSelector("#signInModal .btn-primary");

    // ─────────────────────────────────────────────────────────────────────────
    public NavBarPage(GUIDriver gui) {
        super(gui);
    }

    // =========================================================================
    // NAVIGATION
    // =========================================================================

    public void clickHome() {
        gui.actionsHelper.click(homeLink);
    }

    public void clickCart() {
        gui.actionsHelper.click(cartLink);
    }

    // =========================================================================
    // CONTACT MODAL
    // =========================================================================

    public void openContactModal() {
        gui.actionsHelper.click(contactLink);
        gui.waitUtils.waitForElementToBeVisible(contactModal);
    }

    public boolean isContactModalOpen() {
        try {
            return gui.get().findElement(contactModal).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Fill + submit contact form.
     * After clicking Send → browser alert appears.
     */
    public void fillAndSubmitContactForm(String email, String name, String message) {
        gui.actionsHelper.type(contactEmail,   email);
        gui.actionsHelper.type(contactName,    name);
        gui.actionsHelper.type(contactMessage, message);
        gui.actionsHelper.click(contactSendBtn);
        gui.alertUtils.waitForAndAcceptAlert();
    }

    // =========================================================================
    // ABOUT US MODAL
    // =========================================================================

    public void openAboutUsModal() {
        gui.actionsHelper.click(aboutUsLink);
        gui.waitUtils.waitForElementToBeVisible(aboutUsModal);
    }

    public boolean isAboutUsModalOpen() {
        try {
            return gui.get().findElement(aboutUsModal).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // =========================================================================
    // LOGIN MODAL
    // =========================================================================

    public void openLoginModal() {
        LogsManager.step("Waiting for Sign Up modal to be invisible before clicking login");
        gui.waitUtils.waitForElementToBeInvisible(signUpModal);
        gui.actionsHelper.click(loginLink);
        gui.waitUtils.waitForElementToBeVisible(loginModal);
    }

    public void waitForLoginButtonVisible() {
        LogsManager.step("Waiting for Login link to be visible in navbar");
        gui.waitUtils.waitForElementToBeVisible(loginLink);
    }

    public boolean isLoginModalOpen() {
        try {
            return gui.get().findElement(loginModal).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void fillLoginForm(String username, String password) {
        gui.actionsHelper.type(loginUsername, username);
        gui.actionsHelper.type(loginPassword, password);
    }

    public void submitLogin() {
        gui.actionsHelper.click(loginBtn);
    }

    /** One-liner: open → fill → submit */
    public void loginAs(String username, String password) {
        openLoginModal();
        fillLoginForm(username, password);
        submitLogin();
        LogsManager.step("Waiting for login to complete...");
        gui.waitUtils.waitForElementToBeVisible(usernameTag);
        gui.waitUtils.waitForElementToBeInvisible(loginModal);
    }

    // ─── Post-login state ─────────────────────────────────────────────────────

    /**
     * #nameofuser is display:none by default.
     * JS switches it to display:block after successful login.
     */
    public boolean isUserLoggedIn() {
        try {
            LogsManager.step("Checking if user is logged in by waiting for username tag visibility");
            gui.waitUtils.waitForElementToBeVisible(usernameTag);
            String display = gui.get().findElement(usernameTag).getCssValue("display");
            return !display.equalsIgnoreCase("none");
        } catch (Exception e) {
            LogsManager.warn("isUserLoggedIn check failed: " + e.getMessage());
            return false;
        }
    }

    public String getLoggedInUsername() {
        return gui.waitUtils.waitForElementToBeVisible(usernameTag).getText();
    }

    public void clickLogout() {
        gui.actionsHelper.click(logoutLink);

        // استنى بـ JS إن الـ nameofuser يبقى display:none
        gui.waitUtils.fluentWait(d -> {
            String display = (String) ((org.openqa.selenium.JavascriptExecutor) d)
                    .executeScript(
                            "var el = document.getElementById('nameofuser');" +
                                    "return el ? el.style.display : 'none';"
                    );
            return "none".equals(display) || "".equals(display) ? true : null;
        }, 15);
    }

    // =========================================================================
    // SIGN UP MODAL
    // =========================================================================

    public void openSignUpModal() {
        gui.actionsHelper.click(signUpLink);
        gui.waitUtils.waitForElementToBeVisible(signUpModal);
    }

    public boolean isSignUpModalOpen() {
        try {
            return gui.get().findElement(signUpModal).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void fillSignUpForm(String username, String password) {
        gui.actionsHelper.type(signUpUsername, username);
        gui.actionsHelper.type(signUpPassword, password);
    }

    /** After clicking Sign up → browser alert appears → caller handles it */
    public void submitSignUp() {
        gui.actionsHelper.click(signUpBtn);
    }

    public void signUpAs(String username, String password) {
        openSignUpModal();
        fillSignUpForm(username, password);
        submitSignUp();
        gui.alertUtils.waitForAndAcceptAlert();
    }
}