package comBlazedemoTests.utils.dataReader;

import comBlazedemoTests.utils.logs.LogsManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    private static final String BASE_PATH = "src/test/resources/test-data/";

    // ─── Load .properties file ────────────────────────────────
    private static Properties load(String fileName) {
        String filePath = BASE_PATH + fileName;
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            props.load(fis);
            LogsManager.info("Loaded properties file: " + filePath);
        } catch (IOException e) {
            LogsManager.error("Failed to load properties file: " + filePath, e);
            throw new RuntimeException("Cannot read properties file: " + filePath, e);
        }
        return props;
    }

    // ─── Get String value ─────────────────────────────────────
    public static String get(String fileName, String key) {
        String value = load(fileName).getProperty(key);
        if (value == null) {
            LogsManager.warn("Key not found in [" + fileName + "]: " + key);
        }
        return value;
    }

    // ─── Get with default fallback ────────────────────────────
    public static String get(String fileName, String key, String defaultValue) {
        String value = load(fileName).getProperty(key, defaultValue);
        LogsManager.info("Property [" + key + "] = " + value);
        return value;
    }

    // ─── Get int value ────────────────────────────────────────
    public static int getInt(String fileName, String key) {
        String value = get(fileName, key);
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            LogsManager.error("Cannot parse int for key [" + key + "] value: " + value, e);
            throw new RuntimeException("Invalid int value for key: " + key, e);
        }
    }

    // ─── Get boolean value ────────────────────────────────────
    public static boolean getBoolean(String fileName, String key) {
        String value = get(fileName, key);
        return Boolean.parseBoolean(value.trim());
    }
}