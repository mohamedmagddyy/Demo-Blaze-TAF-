package comBlazedemoTests.utils.actions;

import comBlazedemoTests.utils.logs.LogsManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

public class AlertUtils {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    public AlertUtils(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    /**
     * Wait for alert to appear and then accept it.
     */
    public void waitForAndAcceptAlert() {
        try {
            waitUtils.waitForAlertToBePresent().accept();
            LogsManager.pass("Alert accepted after waiting");
        } catch (Exception e) {
            LogsManager.warn("Alert did not appear within timeout: " + e.getMessage());
        }
    }

    /**
     * Wait for alert and dismiss it.
     */
    public void waitForAndDismissAlert() {
        try {
            waitUtils.waitForAlertToBePresent().dismiss();
            LogsManager.pass("Alert dismissed after waiting");
        } catch (Exception e) {
            LogsManager.warn("Alert did not appear within timeout: " + e.getMessage());
        }
    }

    /**
     * Legacy static method for backward compatibility.
     * Use waitForAndAcceptAlert() instead for reliability.
     */
    public static void acceptAlertIfPresent(WebDriver driver) {
        try {
            Alert alert = driver.switchTo().alert();
            LogsManager.info("Alert found: " + alert.getText());
            alert.accept();
            LogsManager.pass("Alert accepted");
        } catch (NoAlertPresentException e) {
            LogsManager.warn("No alert present to accept immediately");
        }
    }

    public static String getAlertText(WebDriver driver) {
        try {
            return driver.switchTo().alert().getText();
        } catch (NoAlertPresentException e) {
            return null;
        }
    }
}