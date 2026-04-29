package comBlazedemoTests.pages;

import comBlazedemoTests.drivers.GUIDriver;
import org.openqa.selenium.By;

/**
 * ProductPage - prod.html?idp_=X
 * Individual product detail page — navigated to by clicking a product on HomePage.
 * Uses GUIDriver for all interactions.
 */
public class ProductPage extends BasePage {


    // ─── Locators (from prod.html) ────────────────────────────────────────────
    private static final By productName   = By.cssSelector(".name");
    private static final By productPrice  = By.cssSelector(".price-container");
    private static final By productDesc   = By.cssSelector("#more-information p");
    private static final By addToCartBtn  = By.cssSelector(".btn-success");  // "Add to cart"

    // ─────────────────────────────────────────────────────────────────────────
    public ProductPage(GUIDriver gui) {
        super(gui);
    }

    // =========================================================================
    // GETTERS
    // =========================================================================

    public String getProductName() {
        return gui.waitUtils.waitForElementToBeVisible(productName).getText().trim();
    }

    public String getProductPrice() {
        return gui.waitUtils.waitForElementToBeVisible(productPrice).getText().trim();
    }

    public String getProductDescription() {
        return gui.waitUtils.waitForElementToBeVisible(productDesc).getText().trim();
    }

    // =========================================================================
    // ACTIONS
    // =========================================================================

    /**
     * Clicks "Add to cart" button.
     * After clicking → browser alert appears with "Product added" message.
     * Caller must handle the alert: gui.alertUtils.acceptAlertIfPresent(gui.get())
     */
    public void clickAddToCart() {
        gui.actionsHelper.click(addToCartBtn);
    }

    /** One-liner: add to cart + accept the browser alert */
    public void addToCartAndAccept() {
        clickAddToCart();
        gui.alertUtils.waitForAndAcceptAlert();
    }

    public void goBack() {
        gui.browserAction.navigateBack();
    }
}