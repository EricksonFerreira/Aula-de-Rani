import org.junit.AfterClass;
import org.junit.BeforeClass;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxOptions;


public class WebTest {

    protected static WebDriver driver;

    @BeforeClass
    public static void setupTest() {
        String home = System.getProperty("user.home");
        String os = System.getProperty("os.name");

        System.setProperty("webdriver.gecko.driver", home + "/.ravac/libs/geckodriver.linux64");
        if (os.toLowerCase().contains("mac os")) {
            System.setProperty("webdriver.gecko.driver", home + "/.ravac/libs/geckodriver.macos");
        }

        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);

        driver = new FirefoxDriver(firefoxOptions);
        // driver = new FirefoxDriver();
        System.out.println("--");
    }

    @AfterClass
    public static void tearDownTest(){
        System.out.println("--");
        driver.quit();
    }

    protected static void get(String relative) {
        driver.get("http://localhost:9990/" + relative);
    }

}