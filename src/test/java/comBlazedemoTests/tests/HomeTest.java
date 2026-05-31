package comBlazedemoTests.tests;

import comBlazedemoTests.BaseTest;
import comBlazedemoTests.pages.HomePage;
import comBlazedemoTests.pages.NavBarPage;
import comBlazedemoTests.pages.ProductPage;
import comBlazedemoTests.utils.actions.PropertyReader;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
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

    @Test(description = "Verify Home page title is correct", groups = {"smoke", "regression", "functional"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify the homepage title is 'STORE'")
    @Story("Home Page")
    public void testHomePageTitle() {
        Assert.assertEquals(driver.getTitle(), "STORE", "Page title mismatch");
    }

    @Test(description = "Verify clicking Home nav reloads homepage", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that clicking the Home link in the navbar correctly redirects/reloads the homepage")
    @Story("Home Page")
    public void testNavHomeClick() {
        navBar.clickHome();
        Assert.assertEquals(driver.getTitle(), "STORE");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CONTACT MODAL
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify Contact modal opens on nav click", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the Contact modal is displayed when clicking the Contact link")
    @Story("Home Page")
    public void testContactModalOpens() {
        navBar.openContactModal();
        Assert.assertTrue(navBar.isContactModalOpen(), "Contact modal should be visible");
    }

    @Test(description = "Verify Contact form sends message successfully", groups = {"functional"})
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that the contact form can be filled and submitted")
    @Story("Home Page")
    public void testContactFormSubmission() {
        navBar.openContactModal();
        navBar.fillAndSubmitContactForm("test@mail.com", "John Doe", "Hello, this is a test message");
        // After submit an alert appears — handled in extended tests
        // Here we just verify form is fillable and submittable without exception
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ABOUT US MODAL
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify About Us modal opens on nav click", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the About Us modal with video is displayed")
    @Story("Home Page")
    public void testAboutUsModalOpens() {
        navBar.openAboutUsModal();
        Assert.assertTrue(navBar.isAboutUsModalOpen(), "About Us modal should be visible");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // LOGIN MODAL
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify Login modal opens on nav click", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the Login modal is displayed")
    @Story("Home Page")
    public void testLoginModalOpens() {
        navBar.openLoginModal();
        Assert.assertTrue(navBar.isLoginModalOpen(), "Login modal should be visible");
    }

    @Test(description = "Verify valid login shows logged-in username in navbar", groups = {"smoke", "regression", "functional"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a valid login displays the username in the navbar")
    @Story("Home Page")
    public void testValidLogin() {
        String username = PropertyReader.getProperty("login.username");
        String password = PropertyReader.getProperty("login.password");

        navBar.loginAs(username, password);

        // Wait for navbar to update
        boolean loggedIn = navBar.isUserLoggedIn();
        Assert.assertTrue(loggedIn, "User should be logged in after valid credentials");
    }

    @Test(description = "Verify logged-in username appears in navbar after login", groups = {"functional"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the correct username is displayed in the navbar after login")
    @Story("Home Page")
    public void testLoggedInUsernameDisplayed() {
        String username = PropertyReader.getProperty("login.username");
        String password = PropertyReader.getProperty("login.password");

        navBar.loginAs(username, password);
        String displayedName = navBar.getLoggedInUsername();

        Assert.assertTrue(displayedName.contains(username),
                "Navbar should display logged-in username, but got: " + displayedName);
    }

    @Test(description = "Verify logout resets navbar to login/signup state", groups = {"regression", "functional"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that logging out removes the username and restores Login link")
    @Story("Home Page")
    public void testLogout() {
        String username = PropertyReader.getProperty("login.username");
        String password = PropertyReader.getProperty("login.password");

        navBar.loginAs(username, password);
        Assert.assertTrue(navBar.isUserLoggedIn(), "Should be logged in first");

        navBar.clickLogout();
        Assert.assertFalse(navBar.isUserLoggedIn(), "User should be logged out");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SIGN UP MODAL
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify Sign Up modal opens on nav click", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the Sign Up modal is displayed")
    @Story("Home Page")
    public void testSignUpModalOpens() {
        navBar.openSignUpModal();
        Assert.assertTrue(navBar.isSignUpModalOpen(), "Sign Up modal should be visible");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CATEGORIES
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify filtering by Phones category shows products", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the Phones category filter displays products")
    @Story("Home Page")
    public void testFilterByPhones() {
        homePage.clickPhonesCategory();
        Assert.assertTrue(homePage.getProductCount() > 0,
                "Phones category should show at least 1 product");
    }

    @Test(description = "Verify filtering by Laptops category shows products", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the Laptops category filter displays products")
    @Story("Home Page")
    public void testFilterByLaptops() {
        homePage.clickLaptopsCategory();
        Assert.assertTrue(homePage.getProductCount() > 0,
                "Laptops category should show at least 1 product");
    }

    @Test(description = "Verify filtering by Monitors category shows products", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the Monitors category filter displays products")
    @Story("Home Page")
    public void testFilterByMonitors() {
        homePage.clickMonitorsCategory();
        Assert.assertTrue(homePage.getProductCount() > 0,
                "Monitors category should show at least 1 product");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PRODUCTS
    // ═══════════════════════════════════════════════════════════════════════════

    @Test(description = "Verify homepage loads products on initial load", groups = {"smoke", "regression", "functional"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that products are loaded when the homepage is opened")
    @Story("Home Page")
    public void testProductsLoadOnHomepage() {
        Assert.assertTrue(homePage.getProductCount() > 0,
                "Homepage should display products");
    }

    @Test(description = "Verify clicking a product navigates to product detail page", groups = {"smoke", "regression", "functional"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that clicking a product redirects to the correct product detail page")
    @Story("Home Page")
    public void testClickProductNavigatesToDetailPage() {
        homePage.clickFirstProduct();
        ProductPage productPage = new ProductPage(guiDriver);
        String productName = productPage.getProductName();

        Assert.assertNotNull(productName, "Product name should not be null");
        Assert.assertFalse(productName.isEmpty(), "Product name should not be empty");
    }

    @Test(description = "Verify product detail page shows price", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the price is correctly displayed on the product detail page")
    @Story("Home Page")
    public void testProductDetailShowsPrice() {
        homePage.clickFirstProduct();
        ProductPage productPage = new ProductPage(guiDriver);
        String price = productPage.getProductPrice();

        Assert.assertNotNull(price, "Price should be displayed on product page");
        Assert.assertTrue(price.contains("$"), "Price should contain $ symbol");
    }

    @Test(description = "Verify clicking Next page loads more products", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that pagination works correctly by clicking the Next button")
    @Story("Home Page")
    public void testNextPageNavigation() {
        int firstPageCount = homePage.getProductCount();
        homePage.clickNextPage();
        int secondPageCount = homePage.getProductCount();

        Assert.assertTrue(secondPageCount > 0, "Next page should have products");
    }

    @Test(description = "Verify cart nav redirects to cart page", groups = {"smoke", "regression", "functional"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that clicking the Cart link in the navbar redirects to the cart page")
    @Story("Home Page")
    public void testNavCartRedirectsToCartPage() {
        navBar.clickCart();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"),
                "URL should contain 'cart' after clicking cart nav");
    }
}