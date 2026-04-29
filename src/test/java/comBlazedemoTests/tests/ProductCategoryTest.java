package comBlazedemoTests.tests;

import comBlazedemoTests.BaseTest;
import comBlazedemoTests.pages.CategoryPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;

public class ProductCategoryTest extends BaseTest {

    private CategoryPage categoryPage;

    @BeforeMethod
    public void initPage() {
        categoryPage = new CategoryPage(guiDriver);
        guiDriver.browserAction.navigateTo("https://www.demoblaze.com");
    }

    @Test(description = "Verify Phones category filtering")
    public void testPhonesFiltering() {
        categoryPage.selectPhones();
        List<String> products = categoryPage.getDisplayedProductNames();
        
        Assert.assertFalse(products.isEmpty(), "Phones list should not be empty");
        // Verify no laptops or monitors are in the list (basic check)
        for (String name : products) {
            String lowerName = name.toLowerCase();
            Assert.assertFalse(lowerName.contains("laptop") || lowerName.contains("monitor") || lowerName.contains("macbook"),
                "Found non-phone product in Phones category: " + name);
        }
    }

    @Test(description = "Verify Laptops category filtering")
    public void testLaptopsFiltering() {
        categoryPage.selectLaptops();
        List<String> products = categoryPage.getDisplayedProductNames();
        
        Assert.assertFalse(products.isEmpty(), "Laptops list should not be empty");
        for (String name : products) {
            String lowerName = name.toLowerCase();
            // In DemoBlaze, Laptops are Sony vaio, MacBook, Dell, etc.
            Assert.assertFalse(lowerName.contains("iphone") || lowerName.contains("nokia") || lowerName.contains("lumia"),
                "Found non-laptop product in Laptops category: " + name);
        }
    }

    @Test(description = "Verify Monitors category filtering")
    public void testMonitorsFiltering() {
        categoryPage.selectMonitors();
        List<String> products = categoryPage.getDisplayedProductNames();
        
        Assert.assertFalse(products.isEmpty(), "Monitors list should not be empty");
        for (String name : products) {
            String lowerName = name.toLowerCase();
            Assert.assertTrue(lowerName.contains("monitor") || lowerName.contains("asus") || lowerName.contains("hd"),
                "Found non-monitor product in Monitors category: " + name);
        }
    }
}
