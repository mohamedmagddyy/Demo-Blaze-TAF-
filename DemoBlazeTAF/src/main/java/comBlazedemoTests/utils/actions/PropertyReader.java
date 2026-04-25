package comBlazedemoTests.utils.actions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * PropertyReader - Configuration Properties Management
 * Reads configuration from properties files
 * Provides centralized access to framework properties
 */
public class PropertyReader {

    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadProperties();
    }

    private PropertyReader() {
        // Private constructor to prevent instantiation
    }

    /**
     * Loads properties from config file
     */
    private static void loadProperties() {
        try (InputStream input = PropertyReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("WARNING: " + CONFIG_FILE + " file not found. Using default values.");
                setDefaultProperties();
            }
        } catch (IOException ex) {
            System.err.println("Error loading properties: " + ex.getMessage());
            setDefaultProperties();
        }
    }

    /**
     * Sets default properties if config file is not found
     */
    private static void setDefaultProperties() {
        properties.setProperty("browserType", "CHROME");
        properties.setProperty("baseURL", "https://www.demoblaze.com");
        properties.setProperty("implicitWait", "10");
        properties.setProperty("pageLoadTimeout", "15");
        properties.setProperty("headless", "false");
    }

    /**
     * Gets a property value by key
     * @param key Property key
     * @return Property value or throws exception if not found
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Property '" + key + "' not found in configuration.");
        }
        return value;
    }

    /**
     * Gets a property value with a default fallback
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Gets all properties
     * @return Properties object
     */
    public static Properties getAllProperties() {
        return properties;
    }
}

