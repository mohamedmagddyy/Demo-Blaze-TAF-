package comBlazedemoTests.drivers;

import org.openqa.selenium.WebDriver;

/**
 * AbstractDriver Interface - Factory Contract
 * Defines the contract for creating WebDriver instances
 */
public interface AbstractDriver {

    /**
     * Creates and returns a configured WebDriver instance
     * @return WebDriver instance
     */
    WebDriver createDriver();
}
