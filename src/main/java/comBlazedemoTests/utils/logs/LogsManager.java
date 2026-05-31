package comBlazedemoTests.utils.logs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogsManager {

    // ─── Singleton Logger per class ──────────────────────────
    private static final Logger logger = LogManager.getLogger(LogsManager.class);

    // Private constructor — utility class, no instantiation
    private LogsManager() {}

    // ─── INFO ─────────────────────────────────────────────────
    public static void info(String message) {
        logger.info(message);
    }

    // ─── PASS (mapped to INFO with prefix) ───────────────────
    public static void pass(String message) {
        logger.info("[PASS] " + message);
    }

    // ─── WARN ─────────────────────────────────────────────────
    public static void warn(String message) {
        logger.warn(message);
    }

    // ─── ERROR ────────────────────────────────────────────────
    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    // ─── DEBUG ────────────────────────────────────────────────
    public static void debug(String message) {
        logger.debug(message);
    }

    // ─── FATAL ────────────────────────────────────────────────
    public static void fatal(String message, Throwable throwable) {
        logger.fatal(message, throwable);
    }

    // ─── Step Logger (for test steps readability) ─────────────
    public static void step(String stepDescription) {
        logger.info("──► STEP: " + stepDescription);
    }

    public static void testStart(String testName) {
        logger.info("╔══════════════════════════════════════════╗");
        logger.info("║  TEST START: " + testName);
        logger.info("╚══════════════════════════════════════════╝");
    }

    public static void testEnd(String testName, String status) {
        logger.info("╔══════════════════════════════════════════╗");
        logger.info("║  TEST END: " + testName + " → " + status);
        logger.info("╚══════════════════════════════════════════╝");
        logger.info("");
    }

    public static void suiteStart() {
        logger.info("╔══════════════════════════════════════════╗");
        logger.info("║         NEW TEST RUN STARTED              ║");
        logger.info("║  " + new java.util.Date()                  );
        logger.info("╚══════════════════════════════════════════╝");
        logger.info("");
    }
}