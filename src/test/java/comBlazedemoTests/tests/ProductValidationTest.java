package comBlazedemoTests.tests;

import comBlazedemoTests.BaseTest;
import comBlazedemoTests.pages.HomePage;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;

public class ProductValidationTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod
    public void initPage() {
        homePage = new HomePage(guiDriver);
        guiDriver.browserAction.navigateTo("https://www.demoblaze.com");
    }

    @Test(description = "Verify each product has title, price, and description", groups = {"regression", "functional"})
    @Severity(SeverityLevel.NORMAL)
    public void testProductDetailsInGrid() {
        List<String> titles = homePage.getDisplayedProductNames();
        List<String> prices = homePage.getDisplayedProductPrices();
        List<String> descs = homePage.getDisplayedProductDescriptions();

        int count = homePage.getProductCount();
        
        Assert.assertEquals(titles.size(), count, "Each product card should have a title");
        Assert.assertEquals(prices.size(), count, "Each product card should have a price");
        Assert.assertEquals(descs.size(), count, "Each product card should have a description");

        for (int i = 0; i < count; i++) {
            Assert.assertFalse(titles.get(i).isEmpty(), "Product title at index " + i + " is empty");
            Assert.assertTrue(prices.get(i).contains("$"), "Product price at index " + i + " does not contain $");
            Assert.assertFalse(descs.get(i).isEmpty(), "Product description at index " + i + " is empty");
        }
    }

    @Test(description = "Verify product images are visible and not broken", groups = {"regression", "functional"})
    @Severity(SeverityLevel.TRIVIAL)
    public void testProductImages() {
        Assert.assertTrue(homePage.areAllProductImagesVisible(), "Some product images are missing or broken");
    }
}
