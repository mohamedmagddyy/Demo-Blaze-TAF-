// src/test/java/comBlazedemoTests/tests/HomeTest.java

package comBlazedemoTests.tests;

import comBlazedemoTests.drivers.GUIDriver;
import comBlazedemoTests.pages.HomePage;
import comBlazedemoTests.validations.Validation;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * HomeTest - Tests for home page category navigation and product filtering
 */
public class HomeTest {

    private GUIDriver gui;
    private HomePage homePage;

    @BeforeMethod
    public void setup() {
        gui = new GUIDriver();
        homePage = new HomePage(gui);
        gui.browserAction.navigateTo("https://www.demoblaze.com/");
    }

    @AfterMethod
    public void teardown() {
        gui.quitDriver();
    }

    @Test(description = "Verify Phones category shows only phone products")
    public void testPhonesCategory() {
        Validation.reset();
        homePage.clickPhonesCategory();

        List<String> products = homePage.getDisplayedProductNames();
        // Print all products
        products.forEach(p -> System.out.println("Phone category product: " + p));

        // Assert list is not empty
        gui.validation.assertTrue(!products.isEmpty(), "Product list should not be empty for phones category");

        // Assert no laptop or monitor names appear
        for (String name : products) {
            gui.validation.assertFalse(
                name.toLowerCase().matches(".*(laptop|notebook|book|monitor|dell|vaio).*"),
                "Non-phone product found in phones category: " + name
            );
        }
        Validation.assertAll();
    }

    @Test(description = "Verify Laptops category shows only laptop products")
    public void testLaptopsCategory() {
        Validation.reset();
        homePage.clickLaptopsCategory();

        List<String> products = homePage.getDisplayedProductNames();
        // Print all products
        products.forEach(p -> System.out.println("Laptop category product: " + p));

        // Assert list is not empty
        gui.validation.assertTrue(!products.isEmpty(), "Product list should not be empty for laptops category");

        // Assert no phone or monitor names appear
        for (String name : products) {
            gui.validation.assertFalse(
                name.toLowerCase().matches(".*(samsung|nokia|iphone|monitor).*"),
                "Non-laptop product found in laptops category: " + name
            );
        }
        Validation.assertAll();
    }

    @Test(description = "Verify Monitors category shows only monitor products")
    public void testMonitorsCategory() {
        Validation.reset();
        homePage.clickMonitorsCategory();

        List<String> products = homePage.getDisplayedProductNames();
        // Print all products
        products.forEach(p -> System.out.println("Monitor category product: " + p));

        // Assert list is not empty
        gui.validation.assertTrue(!products.isEmpty(), "Product list should not be empty for monitors category");

        // Assert no phone or laptop names appear
        for (String name : products) {
            gui.validation.assertFalse(
                name.toLowerCase().matches(".*(samsung|iphone|laptop|book).*"),
                "Non-monitor product found in monitors category: " + name
            );
        }
        Validation.assertAll();
    }
}
