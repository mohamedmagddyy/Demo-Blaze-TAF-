package comBlazedemoTests.pages;

import comBlazedemoTests.drivers.GUIDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CartPage - cart.html
 * Shows cart items + Place Order modal (popup inside same page).
 * Uses GUIDriver for all interactions.
 *
 * Layout:
 *   - Cart table  → #tbodyid (rows = items)
 *   - Total       → #totalp
 *   - Place Order → button → opens #orderModal (Bootstrap modal)
 *   - After purchase → SweetAlert popup with confirmation
 */
public class CartPage extends BasePage {


    // ─── Cart table ───────────────────────────────────────────────────────────
    private static final By cartRows      = By.cssSelector("#tbodyid tr");
    private static final By cartItemNames = By.cssSelector("#tbodyid tr td:nth-child(2)");
    private static final By totalPrice    = By.id("totalp");

    // ─── Place Order button ───────────────────────────────────────────────────
    private static final By placeOrderBtn = By.cssSelector("button[data-target='#orderModal']");

    // ─── Order modal fields ───────────────────────────────────────────────────
    private static final By orderModal    = By.id("orderModal");
    private static final By orderName     = By.id("name");
    private static final By orderCountry  = By.id("country");
    private static final By orderCity     = By.id("city");
    private static final By orderCard     = By.id("card");
    private static final By orderMonth    = By.id("month");
    private static final By orderYear     = By.id("year");
    private static final By purchaseBtn   = By.cssSelector("#orderModal .btn-primary"); // "Purchase"

    // ─── Purchase confirmation (SweetAlert) ───────────────────────────────────
    private static final By confirmationTitle = By.cssSelector(".sweet-alert h2");
    private static final By confirmationBody  = By.cssSelector(".sweet-alert p");
    private static final By confirmOkBtn      = By.cssSelector(".sweet-alert button.confirm"); // more specific

    // ─────────────────────────────────────────────────────────────────────────
    public CartPage(GUIDriver gui) {
        super(gui);
    }

    // =========================================================================
    // CART ITEMS
    // =========================================================================

    public int getCartItemCount() {
        return gui.get().findElements(cartRows).size();
    }

    public List<String> getCartItemNames() {
        return gui.get().findElements(cartItemNames)
                .stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());
    }

    public boolean isProductInCart(String productName) {
        return getCartItemNames().stream()
                .anyMatch(name -> name.equalsIgnoreCase(productName));
    }

    public boolean isCartEmpty() {
        return gui.get().findElements(cartRows).isEmpty();
    }

    public String getTotalPrice() {
        return gui.waitUtils.waitForElementToBeVisible(totalPrice).getText().trim();
    }

    /**
     * Delete item by clicking the "Delete" link in its row.
     * The delete link is the only <a> tag inside each row.
     */
    public void deleteItemByName(String productName) {
        List<WebElement> rows = gui.get().findElements(cartRows);
        for (WebElement row : rows) {
            if (row.getText().contains(productName)) {
                row.findElement(By.tagName("a")).click();
                break;
            }
        }
    }

    public void deleteFirstItem() {
        List<WebElement> rows = gui.get().findElements(cartRows);
        if (!rows.isEmpty()) {
            rows.get(0).findElement(By.tagName("a")).click();
        }
    }

    // =========================================================================
    // PLACE ORDER MODAL
    // =========================================================================

    public void clickPlaceOrder() {
        gui.actionsHelper.click(placeOrderBtn);
        gui.waitUtils.waitForElementToBeVisible(orderModal);
    }

    public boolean isOrderModalOpen() {
        try {
            return gui.get().findElement(orderModal).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForOrderModalToClose() {
        gui.waitUtils.waitForElementToBeInvisible(orderModal);
    }

    public void fillOrderForm(String name, String country, String city,
                              String card, String month, String year) {
        gui.actionsHelper.type(orderName,    name);
        gui.actionsHelper.type(orderCountry, country);
        gui.actionsHelper.type(orderCity,    city);
        gui.actionsHelper.type(orderCard,    card);
        gui.actionsHelper.type(orderMonth,   month);
        gui.actionsHelper.type(orderYear,    year);
    }
    public void waitForConfirmationToAppear() {
        gui.waitUtils.waitForElementToBeVisible(confirmationTitle);
    }

    public void waitForConfirmationToDisappear() {
        gui.waitUtils.waitForElementToBeInvisible(confirmOkBtn);
    }

    public void clickPurchase() {
        gui.actionsHelper.click(purchaseBtn);
    }

    // =========================================================================
    // CONFIRMATION (SweetAlert)
    // =========================================================================

    public boolean isPurchaseSuccessful() {
        try {
            WebElement title = gui.waitUtils.waitForElementToBeVisible(confirmationTitle);
            return title.getText().equalsIgnoreCase("Thank you for your purchase!");
        } catch (Exception e) {
            return false;
        }
    }

    public String getConfirmationDetails() {
        return gui.waitUtils.waitForElementToBeVisible(confirmationBody).getText();
    }

    public void clickOkOnConfirmation() {
        // استخدم JavaScript click عشان يتجنب أي intercepted click issues
        WebElement okBtn = gui.waitUtils.waitForElementToBeClickable(confirmOkBtn);
        gui.actionsHelper.executeJS("arguments[0].click();", okBtn);
    }

    // =========================================================================
    // FULL FLOW HELPER
    // =========================================================================

    /**
     * Full purchase flow in one call:
     * Place Order → fill form → Purchase → return success status
     */
    public boolean completePurchase(String name, String country, String city,
                                    String card, String month, String year) {
        clickPlaceOrder();
        fillOrderForm(name, country, city, card, month, year);
        clickPurchase();
        // لا تعمل أي حاجة هنا — السبب إن أي interaction ممكن يغلق الـ SweetAlert
        return isPurchaseSuccessful();
    }
}