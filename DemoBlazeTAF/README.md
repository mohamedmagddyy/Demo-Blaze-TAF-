# DemoBlaze Test Automation Framework

## Overview
A production-ready Selenium Java framework using the Abstract Factory design pattern with ThreadLocal WebDriver management for parallel test execution.

## Architecture

### Design Pattern: Abstract Factory
The framework implements the **Abstract Factory Pattern** to create WebDriver instances:
- **AbstractDriver (Interface)**: Defines contract for driver factories
- **Browser (Enum)**: Maps browser types to factory implementations
- **Concrete Factories**: ChromeDriverFactory, EdgeDriverFactory, FirefoxDriverFactory

### Thread Safety
- Uses **ThreadLocal<WebDriver>** for thread-safe driver management
- **DriverManager**: Central point for driver lifecycle management
- Supports parallel test execution (TestNG/JUnit)

## Project Structure

```
DemoBlazeTAF/
├── src/
│   ├── main/
│   │   ├── java/comBlazedemoTests/
│   │   │   ├── drivers/
│   │   │   │   ├── AbstractDriver.java (Interface)
│   │   │   │   ├── Browser.java (Enum)
│   │   │   │   ├── ChromeDriverFactory.java
│   │   │   │   ├── EdgeDriverFactory.java
│   │   │   │   ├── FirefoxDriverFactory.java
│   │   │   │   ├── DriverManager.java (ThreadLocal Management)
│   │   │   │   ├── WebDriverFactory.java (Alternative Factory)
│   │   │   │   └── GUIDriver.java (Wrapper)
│   │   │   ├── utils/
│   │   │   │   └── PropertyReader.java (Configuration)
│   │   │   ├── pages/ (Page Object Models)
│   │   │   ├── apis/ (API testing)
│   │   │   └── customlistners/ (TestNG Listeners)
│   │   └── resources/
│   │       └── config.properties (Configuration file)
│   └── test/
│       ├── java/comBlazedemoTests/
│       │   ├── BaseTest.java (Base test class)
│       │   └── tests/
│       │       └── SampleTest.java (Example tests)
│       └── resources/
│           └── test-data/ (Test data files)
├── pom.xml (Maven configuration)
└── testng.xml (TestNG configuration)
```

## Dependencies

### Core
- **Selenium WebDriver 4.15.0**: Browser automation
- **WebDriverManager 5.6.3**: Automatic driver management
- **TestNG 7.8.1**: Test framework

### Logging
- **SLF4J API 2.0.9**: Logging facade
- **SLF4J Simple 2.0.9**: Simple logging implementation

## Key Classes

### 1. AbstractDriver (Interface)
```java
public interface AbstractDriver {
    WebDriver createDriver();
}
```

### 2. Browser (Enum)
```java
public enum Browser {
    CHROME(new ChromeDriverFactory()),
    EDGE(new EdgeDriverFactory()),
    FIREFOX(new FirefoxDriverFactory());
    
    public AbstractDriver getDriverFactory() { ... }
}
```

### 3. Driver Factories

#### ChromeDriverFactory
- Manages chrome-specific WebDriver setup
- Configures ChromeOptions:
  - Window maximization
  - Notification disabling
  - Performance optimizations
  - Optional headless mode

#### EdgeDriverFactory
- Manages Edge-specific WebDriver setup
- Similar configuration to Chrome
- Edge-specific arguments for automation

#### FirefoxDriverFactory
- Manages Firefox-specific WebDriver setup
- Clean profile setup
- Firefox-specific preferences

### 4. DriverManager (Thread-Safe)
```java
public static WebDriver getDriver()        // Get driver for current thread
public static void setDriver(WebDriver)    // Set driver for current thread
public static void quitDriver()            // Quit and cleanup driver
public static boolean isDriverInitialized() // Check driver status
public static void removeDriver()          // Remove without quitting
```

### 5. PropertyReader (Configuration)
- Reads from `config.properties`
- Provides property access with defaults
- Fallback to default values if config not found

### 6. GUIDriver (Wrapper)
- High-level wrapper for driver management
- Uses GUIDriver for initialization
- Uses DriverManager for thread-safe access
- Handles driver lifecycle

## Configuration

### config.properties
```properties
browserType=CHROME                 # CHROME, EDGE, FIREFOX
baseURL=https://www.demoblaze.com
implicitWait=10
pageLoadTimeout=15
explicitWait=15
headless=false
maximizeWindow=true
takeScreenshotOnFailure=true
screenshotDirectory=./screenshots/
reportsDirectory=./reports/
logLevel=INFO
```

## Usage Examples

### Basic Test Class
```java
public class MyTest extends BaseTest {
    @Test
    public void testExample() {
        driver.navigate().to("https://www.demoblaze.com");
        // Test code here
    }
}
```

### Using GUIDriver Directly
```java
GUIDriver guiDriver = new GUIDriver();
WebDriver driver = guiDriver.get();
driver.navigate().to("https://example.com");
guiDriver.quitDriver();
```

### Using WebDriverFactory
```java
// Create driver with default browser
WebDriver driver = WebDriverFactory.createWebDriver();

// Create driver for specific browser
WebDriver driver = WebDriverFactory.createWebDriver("FIREFOX");

// Create managed driver (registers with DriverManager)
WebDriver driver = WebDriverFactory.createManagedDriver("EDGE");
```

## Parallel Test Execution

### TestNG Configuration (testng.xml)
```xml
<suite parallel="methods" thread-count="3">
    <test name="Functional Tests">
        <classes>
            <class name="comBlazedemoTests.tests.SampleTest"/>
        </classes>
    </test>
</suite>
```

### Running Tests
```bash
# Run all tests
mvn clean test

# Run with specific thread count
mvn clean test -Dthread-count=5

# Run specific test class
mvn clean test -Dtest=SampleTest

# Run specific test method
mvn clean test -Dtest=SampleTest#testBrowserNavigation
```

## Browser Options

### Chrome (Headless Example)
In `ChromeDriverFactory.getChromeOptions()`:
```java
options.addArguments("--headless");  // Enable headless mode
options.addArguments("--no-sandbox");
options.addArguments("--disable-gpu");
```

### Firefox (Custom Profile)
Already configured with clean profile in `FirefoxDriverFactory`

### Edge (Custom Arguments)
Configured with edge-specific automation arguments

## Best Practices Implemented

✅ **SOLID Principles**
- Single Responsibility: Each factory handles one browser
- Open/Closed: Easy to add new browsers
- Liskov Substitution: All factories implement AbstractDriver
- Interface Segregation: Clean interface design
- Dependency Inversion: Depends on abstractions

✅ **Thread Safety**
- ThreadLocal for thread-isolated storage
- Null checks in DriverManager
- Safe cleanup with finally blocks

✅ **Resource Management**
- Proper driver cleanup in tearDown
- No memory leaks in ThreadLocal
- Exception handling in quit operations

✅ **Scalability**
- Easy to add new browsers
- Simple configuration management
- Support for parallel execution

✅ **Production Ready**
- Comprehensive logging support
- Error messages and exceptions
- Resource cleanup
- Configuration externalization

## Adding New Browser Support

1. Create a new factory class implementing `AbstractDriver`:
```java
public class SafariDriverFactory implements AbstractDriver {
    @Override
    public WebDriver createDriver() {
        // Safari-specific setup
    }
}
```

2. Add to Browser enum:
```java
public enum Browser {
    // ...existing...
    SAFARI(new SafariDriverFactory());
}
```

3. Update configuration file if needed

## Troubleshooting

### WebDriver not initialized
```
IllegalStateException: WebDriver is not initialized
```
- Ensure `GUIDriver()` or `WebDriverFactory.createManagedDriver()` is called
- Check `BaseTest.setUp()` is running

### Property not found
```
IllegalArgumentException: Property 'key' not found
```
- Check `config.properties` exists in resources folder
- Use `getProperty(key, defaultValue)` for optional properties

### Driver quit error
```
org.openqa.selenium.NoSuchSessionException
```
- Safe error handling already in place
- Check driver is not quit twice

## CI/CD Integration

### GitHub Actions Example
```yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '11'
      - run: mvn clean test
```

## Future Enhancements

- [ ] Add test listeners for reporting
- [ ] Implement screenshot on failure
- [ ] Add API testing support
- [ ] Implement database utilities
- [ ] Add performance monitoring
- [ ] Add video recording for failures

## Contributing

1. Follow existing code style
2. Add new browsers via factory pattern
3. Update documentation
4. Test parallel execution
5. Ensure thread safety

## License

This project is part of DemoBlaze TAF (Test Automation Framework).

## Support

For issues or questions:
1. Check the troubleshooting section
2. Review the code examples
3. Verify configuration file
4. Check thread safety implementation

