package comBlazedemoTests.tests;

import comBlazedemoTests.BaseTest;
import comBlazedemoTests.pages.HomePage;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PaginationTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void initPage() {
        homePage = new HomePage(guiDriver);
        guiDriver.browserAction.navigateTo("https://www.demoblaze.com");
    }

    @Test(description = "Verify pagination controls visibility and functionality", groups = {"regression", "functional"})
    @Severity(SeverityLevel.MINOR)
    public void testPagination() {
        // Initially, Prev should not be visible or at least not active/clickable in a way that works
        // In DemoBlaze, 'Previous' button might be hidden until we go to next page
        
        Assert.assertTrue(homePage.isNextButtonVisible(), "Next button should be visible on first page");
        
        int firstPageCount = homePage.getProductCount();
        homePage.clickNextPage();
        
        Assert.assertTrue(homePage.isPrevButtonVisible(), "Previous button should be visible on second page");
        
        int secondPageCount = homePage.getProductCount();
        Assert.assertTrue(secondPageCount > 0, "Second page should have products");
        
        homePage.clickPrevPage();
        Assert.assertEquals(homePage.getProductCount(), firstPageCount, "Returning to first page should show same number of products");
    }
}
