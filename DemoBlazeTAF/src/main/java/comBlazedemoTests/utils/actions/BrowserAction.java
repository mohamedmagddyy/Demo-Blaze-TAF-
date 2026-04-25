package comBlazedemoTests.utils.actions;

import comBlazedemoTests.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;

public class BrowserAction {

    private final WebDriver driver;

    public BrowserAction(WebDriver driver) {
        this.driver = driver;
    }

    public void maximizeWindow() {
        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            LogsManager.error("[maximizeWindow] Failed", e);
        }
    }

    public void minimizeWindow() {
        try {
            driver.manage().window().minimize();
        } catch (Exception e) {
            LogsManager.error("[minimizeWindow] Failed", e);
        }
    }

    public void closeCurrentWindow() {
        try {
            driver.close();
        } catch (Exception e) {
            LogsManager.error("[closeCurrentWindow] Failed", e);
        }
    }

    public void closeAllWindows() {
        try {
            driver.quit();
        } catch (Exception e) {
            LogsManager.error("[closeAllWindows] Failed", e);
        }
    }

    public void navigateTo(String url) {
        try {
            LogsManager.step("Navigating to: " + url);
            driver.get(url);
        } catch (Exception e) {
            LogsManager.error("[navigateTo] Failed: " + url, e);
        }
    }

    public void navigateBack() {
        try {
            driver.navigate().back();
        } catch (Exception e) {
            LogsManager.error("[navigateBack] Failed", e);
        }
    }

    public void navigateForward() {
        try {
            driver.navigate().forward();
        } catch (Exception e) {
            LogsManager.error("[navigateForward] Failed", e);
        }
    }

    public void refresh() {
        try {
            driver.navigate().refresh();
        } catch (Exception e) {
            LogsManager.error("[refresh] Failed", e);
        }
    }

    public String getCurrentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            LogsManager.error("[getCurrentUrl] Failed", e);
            return "";
        }
    }

    public String getPageTitle() {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            LogsManager.error("[getPageTitle] Failed", e);
            return "";
        }
    }

    public void openNewTab() {
        try {
            driver.switchTo().newWindow(org.openqa.selenium.WindowType.TAB);
        } catch (Exception e) {
            LogsManager.error("[openNewTab] Failed", e);
        }
    }

    public void switchToTab(int index) {
        try {
            java.util.List<String> tabs = new java.util.ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(index));
        } catch (IndexOutOfBoundsException e) {
            LogsManager.error("[switchToTab] Tab index not found: " + index, e);
        } catch (Exception e) {
            LogsManager.error("[switchToTab] Failed", e);
        }
    }

    public void closeCurrentTabAndSwitch(int switchToIndex) {
        try {
            driver.close();
            java.util.List<String> tabs = new java.util.ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(switchToIndex));
        } catch (IndexOutOfBoundsException e) {
            LogsManager.error("[closeCurrentTabAndSwitch] Tab index not found: " + switchToIndex, e);
        } catch (Exception e) {
            LogsManager.error("[closeCurrentTabAndSwitch] Failed", e);
        }
    }
}