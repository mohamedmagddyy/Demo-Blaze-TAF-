package comBlazedemoTests.utils.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FrameAction {
    private final WebDriver driver;
    private final WaitUtils wait;

    public FrameAction(WebDriver driver ) {
        this.driver = driver;
        this.wait = new  WaitUtils (driver);
    }

    public void switchToFrameByIndex(int index) {
        try {
            driver.switchTo().frame(index);
            System.out.println("[SWITCHED TO FRAME] Index: " + index);
        } catch (Exception e) {
            System.err.println("[ERROR SWITCHING TO FRAME] Index: " + index + " | " + e.getMessage());
        }
    }

    public void switchToFrameByNameOrId(String nameOrId) {
        try {
            driver.switchTo().frame(nameOrId);
            System.out.println("[SWITCHED TO FRAME] Name/ID: " + nameOrId);
        } catch (Exception e) {
            System.err.println("[ERROR SWITCHING TO FRAME] Name/ID: " + nameOrId + " | " + e.getMessage());
        }
    }
    public void switchToFrameByElement(By locator) {
        try {
            WebElement frameElement = wait.waitForElementToBeVisible(locator);
            driver.switchTo().frame(frameElement);
            System.out.println("[SWITCHED TO FRAME] Element: " + locator);
        } catch (Exception e) {
            System.err.println("[ERROR SWITCHING TO FRAME] Element: " + locator + " | " + e.getMessage());
        }
    }

   public void switchToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
            System.out.println("[SWITCHED TO DEFAULT CONTENT]");
        } catch (Exception e) {
            System.err.println("[ERROR SWITCHING TO DEFAULT CONTENT] | " + e.getMessage());
        }
    }
}
