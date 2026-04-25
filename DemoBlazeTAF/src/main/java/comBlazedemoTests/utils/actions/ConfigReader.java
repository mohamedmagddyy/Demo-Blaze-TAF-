package comBlazedemoTests.utils.actions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader class - Utility for reading configuration from properties file
 * Loads properties from config.properties file and provides getters for configuration values
 */
public class ConfigReader {

    private static Properties properties = new Properties();
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Failed to load config.properties from " + CONFIG_FILE_PATH);
            e.printStackTrace();
            throw new RuntimeException("Cannot continue without config.properties");
        }
    }

    /**
     * Get property value from config file by key
     *
     * @param key the property key
     * @return the property value as string, null if not found
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value with a default fallback value
     *
     * @param key the property key
     * @param defaultValue the default value if key not found
     * @return the property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}