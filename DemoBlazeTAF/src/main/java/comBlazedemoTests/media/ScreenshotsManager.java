package comBlazedemoTests.media;

import comBlazedemoTests.utils.logs.LogsManager;
import comBlazedemoTests.utils.logs.TimeManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScreenshotsManager {

    private static final String SCREENSHOT_DIR = "test-output/screenshots/";

    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            // ✅ Create directory if not exists
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String filePath = SCREENSHOT_DIR + testName + "_" + TimeManager.getTimestamp() + ".png";
            Files.copy(srcFile.toPath(), Paths.get(filePath));

            LogsManager.info("Screenshot saved: " + filePath);
            return filePath;

        } catch (IOException e) {
            LogsManager.error("Failed to take screenshot: " + testName, e);
            return null;
        }
    }

    public static String takeScreenshotAsBase64(WebDriver driver, String testName) {
        try {
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String base64Screenshot = java.util.Base64.getEncoder().encodeToString(screenshotBytes);
            LogsManager.info("Screenshot captured as base64 for: " + testName);
            return base64Screenshot;
        } catch (Exception e) {
            LogsManager.error("Failed to take screenshot as base64: " + testName, e);
            return null;
        }
    }
}