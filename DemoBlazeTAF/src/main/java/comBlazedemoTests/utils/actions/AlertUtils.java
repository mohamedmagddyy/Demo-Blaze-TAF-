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

    public static void acceptAlertIfPresent(WebDriver driver) {
        try {
            Alert alert = driver.switchTo().alert();
            LogsManager.info("Alert found: " + alert.getText());
            alert.accept();
            LogsManager.pass("Alert accepted");
        } catch (NoAlertPresentException e) {
            LogsManager.warn("No alert present to accept");
        }
    }

    public static void dismissAlertIfPresent(WebDriver driver) {
        try {
            Alert alert = driver.switchTo().alert();
            LogsManager.info("Alert found: " + alert.getText());
            alert.dismiss();
            LogsManager.pass("Alert dismissed");
        } catch (NoAlertPresentException e) {
            LogsManager.warn("No alert present to dismiss");
        }
    }

    public static String getAlertText(WebDriver driver) {
        try {
            String text = driver.switchTo().alert().getText();
            LogsManager.info("Alert text: " + text);
            return text;
        } catch (NoAlertPresentException e) {
            LogsManager.warn("No alert present — getText returned null");
            return null;
        }
    }

    public static void sendKeysToAlert(WebDriver driver, String text) {
        try {
            Alert alert = driver.switchTo().alert();
            LogsManager.info("Sending keys to alert: " + text);
            alert.sendKeys(text);
            alert.accept();
            LogsManager.pass("Alert input sent and accepted");
        } catch (NoAlertPresentException e) {
            LogsManager.warn("No alert present to send keys");
        }
    }
}