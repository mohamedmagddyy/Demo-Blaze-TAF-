package comBlazedemoTests.drivers;

/**
 * Browser Enum - Factory Pattern Implementation
 * Maps browser types to their corresponding driver factory implementations
 */
public enum Browser {

    CHROME(new ChromeDriverFactory()),
    EDGE(new EdgeFactory()),
    FIREFOX(new FireFoxFactory());

    private final AbstractDriver driverFactory;

    Browser(AbstractDriver driverFactory) {
        this.driverFactory = driverFactory;
    }

    /**
     * Returns the driver factory for the given browser
     * @return AbstractDriver implementation
     */
    public AbstractDriver getDriverFactory() {
        return driverFactory;
    }
}
