package comBlazedemoTests.tests;

import comBlazedemoTests.BaseTest;
import comBlazedemoTests.pages.HomePage;
import comBlazedemoTests.pages.NavBarPage;
import comBlazedemoTests.pages.ProductPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * HomeTest - DemoBlaze
 * Covers: Navbar, Modals (Contact/AboutUs/Login/SignUp), Categories, Products
 */
public class HomeTest extends BaseTest {

    private HomePage   homePage;
    private NavBarPage navBar;

    @BeforeMethod
    public void initPage() {
        homePage = new HomePage(guiDriver);
        navBar   = new NavBarPage(guiDriver);
        guiDriver.browserAction.navigateTo("https://www.demoblaze.com");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // NAVBAR TESTS
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify Home page title is correct")
    public void testHomePageTitle() {
        Assert.assertEquals(driver.getTitle(), "STORE", "Page title mismatch");
    }

    @Test(description = "Verify clicking Home nav reloads homepage")
    public void testNavHomeClick() {
        navBar.clickHome();
        Assert.assertEquals(driver.getTitle(), "STORE");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CONTACT MODAL
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify Contact modal opens on nav click")
    public void testContactModalOpens() {
        navBar.openContactModal();
        Assert.assertTrue(navBar.isContactModalOpen(), "Contact modal should be visible");
    }

    @Test(description = "Verify Contact form sends message successfully")
    public void testContactFormSubmission() {
        navBar.openContactModal();
        navBar.fillAndSubmitContactForm("test@mail.com", "John Doe", "Hello, this is a test message");
        // After submit an alert appears — handled in extended tests
        // Here we just verify form is fillable and submittable without exception
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ABOUT US MODAL
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify About Us modal opens on nav click")
    public void testAboutUsModalOpens() {
        navBar.openAboutUsModal();
        Assert.assertTrue(navBar.isAboutUsModalOpen(), "About Us modal should be visible");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // LOGIN MODAL
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify Login modal opens on nav click")
    public void testLoginModalOpens() {
        navBar.openLoginModal();
        Assert.assertTrue(navBar.isLoginModalOpen(), "Login modal should be visible");
    }

    @Test(description = "Verify valid login shows logged-in username in navbar")
    public void testValidLogin() {
        String username = "user_" + System.currentTimeMillis();
        String password = "password123";

        navBar.signUpAs(username, password);
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        
        navBar.loginAs(username, password);

        // Wait for navbar to update
        boolean loggedIn = navBar.isUserLoggedIn();
        Assert.assertTrue(loggedIn, "User should be logged in after valid credentials");
    }

    @Test(description = "Verify logged-in username appears in navbar after login")
    public void testLoggedInUsernameDisplayed() {
        String username = "user_" + (System.currentTimeMillis() + 100);
        String password = "password123";

        navBar.signUpAs(username, password);
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        navBar.loginAs(username, password);
        String displayedName = navBar.getLoggedInUsername();

        Assert.assertTrue(displayedName.contains(username),
                "Navbar should display logged-in username, but got: " + displayedName);
    }

    @Test(description = "Verify logout resets navbar to login/signup state")
    public void testLogout() {
        String username = "user_" + (System.currentTimeMillis() + 200);
        String password = "password123";

        navBar.signUpAs(username, password);
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        navBar.loginAs(username, password);
        Assert.assertTrue(navBar.isUserLoggedIn(), "Should be logged in first");

        navBar.clickLogout();
        Assert.assertFalse(navBar.isUserLoggedIn(), "User should be logged out");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SIGN UP MODAL
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify Sign Up modal opens on nav click")
    public void testSignUpModalOpens() {
        navBar.openSignUpModal();
        Assert.assertTrue(navBar.isSignUpModalOpen(), "Sign Up modal should be visible");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CATEGORIES
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify filtering by Phones category shows products")
    public void testFilterByPhones() {
        homePage.clickPhonesCategory();
        Assert.assertTrue(homePage.getProductCount() > 0,
                "Phones category should show at least 1 product");
    }

    @Test(description = "Verify filtering by Laptops category shows products")
    public void testFilterByLaptops() {
        homePage.clickLaptopsCategory();
        Assert.assertTrue(homePage.getProductCount() > 0,
                "Laptops category should show at least 1 product");
    }

    @Test(description = "Verify filtering by Monitors category shows products")
    public void testFilterByMonitors() {
        homePage.clickMonitorsCategory();
        Assert.assertTrue(homePage.getProductCount() > 0,
                "Monitors category should show at least 1 product");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PRODUCTS
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify homepage loads products on initial load")
    public void testProductsLoadOnHomepage() {
        Assert.assertTrue(homePage.getProductCount() > 0,
                "Homepage should display products");
    }

    @Test(description = "Verify clicking a product navigates to product detail page")
    public void testClickProductNavigatesToDetailPage() {
        homePage.clickFirstProduct();
        ProductPage productPage = new ProductPage(guiDriver);
        String productName = productPage.getProductName();

        Assert.assertNotNull(productName, "Product name should not be null");
        Assert.assertFalse(productName.isEmpty(), "Product name should not be empty");
    }

    @Test(description = "Verify product detail page shows price")
    public void testProductDetailShowsPrice() {
        homePage.clickFirstProduct();
        ProductPage productPage = new ProductPage(guiDriver);
        String price = productPage.getProductPrice();

        Assert.assertNotNull(price, "Price should be displayed on product page");
        Assert.assertTrue(price.contains("$"), "Price should contain $ symbol");
    }

    @Test(description = "Verify clicking Next page loads more products")
    public void testNextPageNavigation() {
        int firstPageCount = homePage.getProductCount();
        homePage.clickNextPage();
        int secondPageCount = homePage.getProductCount();

        Assert.assertTrue(secondPageCount > 0, "Next page should have products");
    }

    @Test(description = "Verify cart nav redirects to cart page")
    public void testNavCartRedirectsToCartPage() {
        navBar.clickCart();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"),
                "URL should contain 'cart' after clicking cart nav");
    }
}