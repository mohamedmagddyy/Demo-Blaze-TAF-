package comBlazedemoTests.utils.actions;

import comBlazedemoTests.utils.logs.LogsManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.io.File;

public class ActionsHelper {

    private WebDriver driver;
    private WaitUtils waitUtils;
    private Actions actions;

    public ActionsHelper(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        this.actions = new Actions(driver);
    }

    public void click(By locator) {
        try {
            LogsManager.step("Clicking: " + locator);
            waitUtils.waitForElementToBeClickable(locator).click();
        } catch (ElementClickInterceptedException e) {
            LogsManager.error("[click] Intercepted: " + locator, e);
        } catch (Exception e) {
            LogsManager.error("[click] Failed: " + locator, e);
        }
    }

    public void clickNested(By parent, By child) {
        try {
            LogsManager.step("Clicking nested: " + child);
            WebElement parentElement = waitUtils.waitForElementToBeVisible(parent);
            waitUtils.waitForNestedElementToBeClickable(parentElement, child).click();
        } catch (Exception e) {
            LogsManager.error("[clickNested] Failed: " + child, e);
        }
    }

    public void doubleClick(By locator) {
        try {
            LogsManager.step("Double clicking: " + locator);
            actions.doubleClick(waitUtils.waitForElementToBeClickable(locator)).perform();
        } catch (Exception e) {
            LogsManager.error("[doubleClick] Failed: " + locator, e);
        }
    }

    public void rightClick(By locator) {
        try {
            LogsManager.step("Right clicking: " + locator);
            actions.contextClick(waitUtils.waitForElementToBeClickable(locator)).perform();
        } catch (Exception e) {
            LogsManager.error("[rightClick] Failed: " + locator, e);
        }
    }

    public void hover(By locator) {
        try {
            actions.moveToElement(waitUtils.waitForElementToBeVisible(locator)).perform();
        } catch (Exception e) {
            LogsManager.error("[hover] Failed: " + locator, e);
        }
    }

    public void dragAndDrop(By source, By target) {
        try {
            LogsManager.step("Drag and drop: " + source + " → " + target);
            actions.dragAndDrop(
                    waitUtils.waitForElementToBeVisible(source),
                    waitUtils.waitForElementToBeVisible(target)
            ).perform();
        } catch (Exception e) {
            LogsManager.error("[dragAndDrop] Failed", e);
        }
    }

    public void clickAndHold(By locator) {
        try {
            actions.clickAndHold(waitUtils.waitForElementToBeVisible(locator)).perform();
        } catch (Exception e) {
            LogsManager.error("[clickAndHold] Failed: " + locator, e);
        }
    }

    public void release() {
        try {
            actions.release().perform();
        } catch (Exception e) {
            LogsManager.error("[release] Failed", e);
        }
    }

    public void type(By locator, String text) {
        try {
            LogsManager.step("Typing in: " + locator);
            WebElement element = waitUtils.waitForElementToBeVisible(locator);
            element.clear();
            element.sendKeys(text);
        } catch (ElementNotInteractableException e) {
            LogsManager.error("[type] Not interactable: " + locator, e);
        } catch (Exception e) {
            LogsManager.error("[type] Failed: " + locator, e);
        }
    }

    public void sendKeys(By locator, CharSequence... keysToSend) {
        try {
            waitUtils.waitForElementToBeVisible(locator).sendKeys(keysToSend);
        } catch (Exception e) {
            LogsManager.error("[sendKeys] Failed: " + locator, e);
        }
    }

    public void scrollTo(By locator) {
        try {
            WebElement element = waitUtils.waitForElementToBeVisible(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            LogsManager.error("[scrollTo] Failed: " + locator, e);
        }
    }

    public void uploadFile(By locator, String fileName) {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + "src"
                    + File.separator + "test"
                    + File.separator + "resources"
                    + File.separator + "uploads"
                    + File.separator + fileName;

            LogsManager.step("Uploading file: " + fileName);
            waitUtils.waitForElementToBeVisible(locator).sendKeys(filePath);
        } catch (Exception e) {
            LogsManager.error("[uploadFile] Failed: " + fileName, e);
        }
    }
}