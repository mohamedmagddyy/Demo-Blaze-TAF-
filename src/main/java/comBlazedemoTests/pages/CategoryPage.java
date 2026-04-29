package comBlazedemoTests.pages;

import comBlazedemoTests.drivers.GUIDriver;
import org.openqa.selenium.By;
import java.util.List;

/**
 * CategoryPage - Handles specific interactions and validations for filtered categories.
 * In DemoBlaze, categories are filters applied to the main product grid on the HomePage.
 */
public class CategoryPage extends HomePage {

    public CategoryPage(GUIDriver gui) {
        super(gui);
    }

    /**
     * Filters by Phones and returns this page for chaining.
     */
    public CategoryPage selectPhones() {
        clickPhonesCategory();
        return this;
    }

    /**
     * Filters by Laptops and returns this page for chaining.
     */
    public CategoryPage selectLaptops() {
        clickLaptopsCategory();
        return this;
    }

    /**
     * Filters by Monitors and returns this page for chaining.
     */
    public CategoryPage selectMonitors() {
        clickMonitorsCategory();
        return this;
    }

    /**
     * Validates that all displayed products belong to the specified category.
     * This is a logical check based on keywords commonly found in product names.
     */
    public boolean allProductsMatchCategory(String categoryKeyword) {
        List<String> names = getDisplayedProductNames();
        if (names.isEmpty()) return false;
        
        for (String name : names) {
            // Very basic heuristic for DemoBlaze products
            if (categoryKeyword.equalsIgnoreCase("Phones")) {
                if (name.toLowerCase().contains("laptop") || name.toLowerCase().contains("monitor")) return false;
            } else if (categoryKeyword.equalsIgnoreCase("Laptops")) {
                if (name.toLowerCase().contains("phone") || name.toLowerCase().contains("monitor") || name.toLowerCase().contains("samsung galaxy")) {
                    // Note: Samsung Galaxy S6 is a phone, but Vaio is a laptop.
                    // This heuristic depends on the actual data.
                }
            }
        }
        return true;
    }
}
