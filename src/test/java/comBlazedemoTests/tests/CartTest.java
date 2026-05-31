package comBlazedemoTests.tests;

import comBlazedemoTests.BaseTest;
import comBlazedemoTests.pages.CartPage;
import comBlazedemoTests.pages.HomePage;
import comBlazedemoTests.pages.NavBarPage;
import comBlazedemoTests.pages.ProductPage;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * CartTest - DemoBlaze
 * Covers: Add to Cart, Cart Items, Delete, Place Order, Purchase Confirmation
 */
public class CartTest extends BaseTest {

    private HomePage   homePage;
    private CartPage   cartPage;
    private NavBarPage navBar;

    // Product used across tests — must exist in DemoBlaze
    private static final String TEST_PRODUCT = "Samsung galaxy s6";
    private static final String BASE_URL      = "https://www.demoblaze.com";

    @BeforeMethod
    public void initPages() {
        homePage = new HomePage(guiDriver);
        cartPage = new CartPage(guiDriver);
        navBar   = new NavBarPage(guiDriver);
        guiDriver.browserAction.navigateTo("https://www.demoblaze.com");
    }

    // ─── Helper: add one product and navigate to cart ─────────────────────────
    private CartPage addProductAndGoToCart(String productName) {
        homePage.clickProduct(productName);
        ProductPage productPage = new ProductPage(guiDriver);
        productPage.addToCartAndAccept();

        navBar.clickCart();
        return new CartPage(guiDriver);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ADD TO CART
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify product is added to cart and appears in cart table", groups = {"smoke", "regression", "functional"})
    @Severity(SeverityLevel.BLOCKER)
    public void testAddProductToCart() {
        CartPage cart = addProductAndGoToCart(TEST_PRODUCT);

        Assert.assertTrue(cart.isProductInCart(TEST_PRODUCT),
                TEST_PRODUCT + " should appear in cart");
    }

    @Test(description = "Verify cart item count increases after adding a product", groups = {"regression", "functional"})
    @Severity(SeverityLevel.CRITICAL)
    public void testCartItemCountIncreases() {
        CartPage cart = addProductAndGoToCart(TEST_PRODUCT);

        Assert.assertTrue(cart.getCartItemCount() >= 1,
                "Cart should have at least 1 item after adding a product");
    }

    @Test(description = "Verify total price is shown in cart", groups = {"regression", "functional"})
    @Severity(SeverityLevel.CRITICAL)
    public void testCartTotalIsDisplayed() {
        CartPage cart = addProductAndGoToCart(TEST_PRODUCT);
        String total = cart.getTotalPrice();

        Assert.assertNotNull(total, "Total price should not be null");
        Assert.assertFalse(total.isEmpty(), "Total price should not be empty");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // DELETE FROM CART
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify deleting item removes it from cart", groups = {"regression", "functional"})
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteItemFromCart() {
        CartPage cart = addProductAndGoToCart(TEST_PRODUCT);
        Assert.assertTrue(cart.isProductInCart(TEST_PRODUCT), "Pre-condition: product should be in cart");

        cart.deleteItemByName(TEST_PRODUCT);

        // Allow time for DOM update
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        Assert.assertFalse(cart.isProductInCart(TEST_PRODUCT),
                "Product should be removed from cart after delete");
    }

    @Test(description = "Verify cart shows empty after deleting only item", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    public void testCartEmptyAfterDeletion() {
        CartPage cart = addProductAndGoToCart(TEST_PRODUCT);
        cart.deleteFirstItem();

        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        Assert.assertTrue(cart.isCartEmpty(), "Cart should be empty after deleting all items");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PLACE ORDER MODAL
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify Place Order modal opens from cart page", groups = {"regression", "functional"})
    @Severity(SeverityLevel.CRITICAL)
    public void testPlaceOrderModalOpens() {
        CartPage cart = addProductAndGoToCart(TEST_PRODUCT);
        cart.clickPlaceOrder();

        Assert.assertTrue(cart.isOrderModalOpen(),
                "Place Order modal should be visible");
    }

    @Test(description = "Verify order form is fillable", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    public void testOrderFormCanBeFilled() {
        CartPage cart = addProductAndGoToCart(TEST_PRODUCT);
        cart.clickPlaceOrder();
        cart.fillOrderForm("John Doe", "Egypt", "Cairo", "1234567890123456", "12", "2025");

        // No assertion needed — if no exception is thrown, form is fillable
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PURCHASE CONFIRMATION
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify successful purchase shows Thank You confirmation", groups = {"functional"})
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulPurchase() {
        CartPage cart = addProductAndGoToCart(TEST_PRODUCT);

        boolean success = cart.completePurchase(
                "John Doe", "Egypt", "Cairo",
                "1234567890123456", "12", "2025"
        );

        Assert.assertTrue(success, "Purchase should complete with Thank You message");
    }

    @Test(description = "Verify confirmation text contains order details", groups = {"functional"})
    @Severity(SeverityLevel.NORMAL)
    public void testPurchaseConfirmationContainsDetails() {
        CartPage cart = addProductAndGoToCart(TEST_PRODUCT);
        cart.completePurchase(
                "Jane Smith", "USA", "New York",
                "9876543210987654", "06", "2026"
        );

        String confirmText = cart.getConfirmationDetails();
        Assert.assertFalse(confirmText.isEmpty(),
                "Confirmation text should not be empty");
    }

    @Test(description = "Verify clicking OK after purchase closes modal and redirects", groups = {"functional"})
    @Severity(SeverityLevel.NORMAL)
    public void testOkButtonAfterPurchase() {
        CartPage cart = addProductAndGoToCart(TEST_PRODUCT);

        cart.clickPlaceOrder();
        cart.fillOrderForm("Ali Hassan", "Egypt", "Alexandria",
                "1111222233334444", "03", "2024");
        cart.clickPurchase();

        // تأكد إن الـ SweetAlert ظهرت الأول
        Assert.assertTrue(cart.isPurchaseSuccessful(), "SweetAlert should appear");

        // دوس OK فوراً بـ JS
        cart.clickOkOnConfirmation();

        // بعد ما الـ SweetAlert تختفي روح الـ home
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector(".sweet-alert")));

        navBar.clickHome();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.getCurrentUrl().contains("index.html")
                        || d.getCurrentUrl().equals(BASE_URL + "/")
                        || d.getCurrentUrl().equals(BASE_URL));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("index.html")
                        || currentUrl.equals(BASE_URL + "/")
                        || currentUrl.equals(BASE_URL),
                "Should be on homepage, but was: " + currentUrl
        );
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // EDGE CASES
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify navigating directly to cart page works", groups = {"regression", "functional"})
    @Severity(SeverityLevel.MINOR)
    public void testDirectCartNavigation() {
        driver.navigate().to(BASE_URL + "/cart.html");
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"),
                "Should be on cart page");
    }

    @Test(description = "Verify adding same product twice increases cart count", groups = {"functional"})
    @Severity(SeverityLevel.MINOR)
    public void testAddSameProductTwice() {
        // Add first time
        homePage.clickProduct(TEST_PRODUCT);
        ProductPage productPage = new ProductPage(guiDriver);
        productPage.addToCartAndAccept();

        // Go back and add second time
        driver.navigate().back();
        productPage.addToCartAndAccept();

        navBar.clickCart();
        CartPage cart = new CartPage(guiDriver);

        Assert.assertTrue(cart.getCartItemCount() >= 2,
                "Cart should contain at least 2 entries for same product added twice");
    }
}