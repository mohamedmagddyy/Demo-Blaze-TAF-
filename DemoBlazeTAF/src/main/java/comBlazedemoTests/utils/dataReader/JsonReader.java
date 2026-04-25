package comBlazedemoTests.utils.dataReader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import comBlazedemoTests.utils.logs.LogsManager;

import java.io.File;
import java.io.IOException;

public class JsonReader {

    private static final String BASE_PATH = "src/test/resources/test-data/";
    private static final ObjectMapper mapper = new ObjectMapper();

    // ─── Load entire JSON file as JsonNode ───────────────────
    public static JsonNode loadJson(String fileName) {
        String filePath = BASE_PATH + fileName;
        try {
            LogsManager.info("Loading JSON file: " + filePath);
            return mapper.readTree(new File(filePath));
        } catch (IOException e) {
            LogsManager.error("Failed to load JSON file: " + filePath, e);
            throw new RuntimeException("Cannot read JSON file: " + filePath, e);
        }
    }

    // ─── Get String value by key ──────────────────────────────
    public static String getString(String fileName, String key) {
        JsonNode node = loadJson(fileName);
        if (!node.has(key)) {
            LogsManager.warn("Key not found in JSON [" + fileName + "]: " + key);
            return null;
        }
        return node.get(key).asText();
    }

    // ─── Get nested String value by parent → key ─────────────
    public static String getString(String fileName, String parent, String key) {
        JsonNode node = loadJson(fileName);
        if (!node.has(parent) || !node.get(parent).has(key)) {
            LogsManager.warn("Nested key not found [" + fileName + "]: " + parent + "." + key);
            return null;
        }
        return node.get(parent).get(key).asText();
    }

    // ─── Get int value by key ─────────────────────────────────
    public static int getInt(String fileName, String key) {
        JsonNode node = loadJson(fileName);
        if (!node.has(key)) {
            LogsManager.warn("Key not found in JSON [" + fileName + "]: " + key);
            return -1;
        }
        return node.get(key).asInt();
    }

    // ─── Get boolean value by key ─────────────────────────────
    public static boolean getBoolean(String fileName, String key) {
        JsonNode node = loadJson(fileName);
        if (!node.has(key)) {
            LogsManager.warn("Key not found in JSON [" + fileName + "]: " + key);
            return false;
        }
        return node.get(key).asBoolean();
    }

    // ─── Map JSON to POJO class ───────────────────────────────
    public static <T> T loadAs(String fileName, Class<T> clazz) {
        String filePath = BASE_PATH + fileName;
        try {
            LogsManager.info("Mapping JSON to class: " + clazz.getSimpleName());
            return mapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            LogsManager.error("Failed to map JSON [" + filePath + "] to " + clazz.getSimpleName(), e);
            throw new RuntimeException("Cannot map JSON to class: " + clazz.getSimpleName(), e);
        }
    }
}