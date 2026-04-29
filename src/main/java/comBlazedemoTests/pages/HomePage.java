package comBlazedemoTests.pages;

import comBlazedemoTests.drivers.GUIDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HomePage - Products grid + Category filter
 * Uses GUIDriver for all interactions (no direct Selenium calls)
 *
 * Layout:
 *   - Left sidebar  → Categories (Phones / Laptops / Monitors)
 *   - Right grid    → <div id="tbodyid"> loaded via AJAX
 *   - Pagination    → id="prev2" / id="next2"
 */
public class HomePage extends BasePage {


    // ─── Categories ───────────────────────────────────────────────────────────
    // onclick attribute used because all 3 share id="itemc" (duplicate ids in HTML)
    private static final By phonesCategory   = By.xpath("//a[@id='itemc' and @onclick=\"byCat('phone')\"]");
    private static final By laptopsCategory  = By.xpath("//a[@id='itemc' and @onclick=\"byCat('notebook')\"]");
    private static final By monitorsCategory = By.xpath("//a[@id='itemc' and @onclick=\"byCat('monitor')\"]");

    // ─── Products grid ────────────────────────────────────────────────────────
    private static final By productGrid    = By.id("tbodyid");
    private static final By productCards   = By.cssSelector("#tbodyid .card");
    private static final By productLinks   = By.cssSelector("#tbodyid a.hrefch"); // title anchors
    private static final By productPrices  = By.cssSelector(".card-block h5");
    private static final By productDescs   = By.cssSelector(".card-block p");
    private static final By productImages  = By.cssSelector(".card-img-top");

    // ─── Pagination ───────────────────────────────────────────────────────────
    private static final By nextBtn = By.id("next2");
    private static final By prevBtn = By.id("prev2");

    // ─────────────────────────────────────────────────────────────────────────
    public HomePage(GUIDriver gui) {
        super(gui);
    }

    // =========================================================================
    // CATEGORIES
    // =========================================================================

    public void clickPhonesCategory() {
        gui.actionsHelper.click(phonesCategory);
        waitForGridToRefresh();
    }

    public void clickLaptopsCategory() {
        gui.actionsHelper.click(laptopsCategory);
        waitForGridToRefresh();
    }

    public void clickMonitorsCategory() {
        gui.actionsHelper.click(monitorsCategory);
        waitForGridToRefresh();
    }

    // =========================================================================
    // PRODUCTS
    // =========================================================================

    public List<String> getDisplayedProductNames() {
        return gui.get().findElements(productLinks)
                .stream()
                .filter(WebElement::isDisplayed)
                .map(el -> el.getText().trim())
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
    }

    public int getProductCount() {
        return gui.get().findElements(productCards).size();
    }

    public boolean isProductVisible(String productName) {
        try {
            return gui.get().findElement(productByName(productName)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click product by name → navigates to prod.html?idp_=X
     */
    public void clickProduct(String productName) {
        gui.actionsHelper.click(productByName(productName));
    }

    public void clickFirstProduct() {
        List<WebElement> links = gui.get().findElements(productLinks);
        if (!links.isEmpty()) {
            links.get(0).click();
        }
    }

    public List<String> getDisplayedProductPrices() {
        return gui.get().findElements(productPrices)
                .stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());
    }

    public List<String> getDisplayedProductDescriptions() {
        return gui.get().findElements(productDescs)
                .stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());
    }

    /**
     * Checks if all images in the current grid are loaded correctly.
     * Returns true if all images have a non-zero naturalWidth.
     */
    public boolean areAllProductImagesVisible() {
        List<WebElement> images = gui.get().findElements(productImages);
        for (WebElement img : images) {
            // Wait up to 2 seconds for each image to load its naturalWidth
            new WebDriverWait(gui.get(), Duration.ofSeconds(2)).until(d -> {
                Object nw = gui.actionsHelper.executeJS("return arguments[0].naturalWidth", img);
                return nw != null && ((Number) nw).longValue() > 0;
            });

            if (!img.isDisplayed()) return false;
            Object naturalWidth = gui.actionsHelper.executeJS("return arguments[0].naturalWidth", img);
            if (naturalWidth == null || ((Number) naturalWidth).longValue() == 0) {
                return false;
            }
        }
        return !images.isEmpty();
    }

    // ─── Pagination ───────────────────────────────────────────────────────────

    public boolean isNextButtonVisible() {
        return isElementVisible(nextBtn);
    }

    public boolean isPrevButtonVisible() {
        return isElementVisible(prevBtn);
    }

    public void clickNextPage() {
        gui.actionsHelper.click(nextBtn);
        waitForGridToRefresh();
    }

    public void clickPrevPage() {
        gui.actionsHelper.click(prevBtn);
        waitForGridToRefresh();
    }

    // =========================================================================
    // PRIVATE HELPERS
    // =========================================================================

    /**
     * The products grid (#tbodyid) exists in the DOM at all times.
     * After clicking a category, AJAX replaces the CHILDREN inside it.
     *
     * Strategy: wait for stale → wait for fresh cards to appear.
     * We do this by waiting until the grid has at least 1 visible .card child.
     */
    private void waitForGridToRefresh() {
        List<WebElement> currentCards = gui.get().findElements(productCards);
        if (!currentCards.isEmpty()) {
            // Wait for at least the first card to go stale (meaning grid is being cleared)
            try {
                new WebDriverWait(gui.get(), Duration.ofSeconds(5))
                        .until(ExpectedConditions.stalenessOf(currentCards.get(0)));
            } catch (Exception e) {
                // If it doesn't go stale, maybe it refreshed too fast or didn't change
            }
        }
        // Wait for new cards to appear
        new WebDriverWait(gui.get(), Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(productCards, 0));
    }

    private By productByName(String productName) {
        return By.xpath(
            "//a[contains(@class,'hrefch') and normalize-space(text())='" + productName + "']"
        );
    }
}