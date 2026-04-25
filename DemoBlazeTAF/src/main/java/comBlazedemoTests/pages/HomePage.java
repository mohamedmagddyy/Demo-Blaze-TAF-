// src/main/java/comBlazedemoTests/pages/HomePage.java

package comBlazedemoTests.pages;

import comBlazedemoTests.drivers.GUIDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * HomePage - Page Object Model for the home page of DemoBlaze
 * Handles category navigation and product interactions
 */
public class HomePage {

    private final GUIDriver gui;

    // ===== Category Links =====
    private static final By phonesCategory = By.xpath("//a[@id='itemc' and @onclick=\"byCat('phone')\"]");
    private static final By laptopsCategory = By.xpath("//a[@id='itemc' and @onclick=\"byCat('notebook')\"]");
    private static final By monitorsCategory = By.xpath("//a[@id='itemc' and @onclick=\"byCat('monitor')\"]");

    // ===== Product Grid =====
    private static final By productGrid = By.id("tbodyid");

    /**
     * Constructor for HomePage
     * @param gui GUIDriver instance for accessing framework components
     */
    public HomePage(GUIDriver gui) {
        this.gui = gui;
    }

    /**
     * Clicks the Phones category and waits for the product grid to refresh
     */
    public void clickPhonesCategory() {
        gui.actionsHelper.click(phonesCategory);
        gui.waitUtils.waitForElementToBeVisible(productGrid);
    }

    /**
     * Clicks the Laptops category and waits for the product grid to refresh
     */
    public void clickLaptopsCategory() {
        gui.actionsHelper.click(laptopsCategory);
        gui.waitUtils.waitForElementToBeVisible(productGrid);
    }

    /**
     * Clicks the Monitors category and waits for the product grid to refresh
     */
    public void clickMonitorsCategory() {
        gui.actionsHelper.click(monitorsCategory);
        gui.waitUtils.waitForElementToBeVisible(productGrid);
    }

    /**
     * Gets all displayed product names from the product grid
     * @return List of product names as strings
     */
    public List<String> getDisplayedProductNames() {
        List<WebElement> productElements = gui.get().findElements(
            By.xpath("//div[@id='tbodyid']//a[contains(@class,'hrefch')]")
        );
        return productElements.stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a specific product is visible on the page
     * @param productName The name of the product to check
     * @return true if the product is visible, false otherwise
     */
    public boolean isProductVisible(String productName) {
        try {
            return gui.get().findElement(productByName(productName)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clicks on a specific product by name
     * @param productName The name of the product to click
     */
    public void clickProduct(String productName) {
        gui.actionsHelper.click(productByName(productName));
    }

    /**
     * Creates a dynamic locator for a product by name
     * @param productName The product name to locate
     * @return By locator for the product
     */
    private By productByName(String productName) {
        return By.xpath("//a[contains(@class,'hrefch') and normalize-space(text())='" + productName + "']");
    }
}
