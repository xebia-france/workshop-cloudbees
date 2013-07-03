package fr.xebia.yawyl.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static junit.framework.Assert.assertEquals;

public class LoginSelenium {

    private WebDriver driver;

    @Before
    public void setUp() throws Exception {

        String sauce_base_url = System.getenv("SELENIUM_STARTING_URL");

        String[] split = sauce_base_url.split("-");
        String groupId = split[1];

        DesiredCapabilities capabillities = DesiredCapabilities.firefox();
        capabillities.setCapability("version", "5");
        capabillities.setCapability("platform", Platform.MAC);
        capabillities.setCapability("name", "[" +  groupId + "]: login on application");

        String sauce_user_name = System.getenv("SAUCE_USER_NAME");
        String sauce_api_key = System.getenv("SAUCE_API_KEY");

        this.driver = new RemoteWebDriver(
                new URL(String.format("http://%s:%s@ondemand.saucelabs.com:80/wd/hub", sauce_user_name, sauce_api_key)),
                capabillities);
    }

    @Test
    public void should_connect() throws Exception {
        String sauce_base_url = System.getenv("SELENIUM_STARTING_URL");

        driver.get(sauce_base_url);
        assertEquals("You are what you listen!", driver.getTitle());

        WebElement loginModalBtn = driver.findElement(By.id("loginModalBtn"));
        loginModalBtn.click();


        WebElement email = driver.findElement(By.id("email"));
        email.sendKeys("test@gmail.com");

        WebElement city = driver.findElement(By.id("city"));
        city.sendKeys("Courbevoie");

        WebElement loginBtn = driver.findElement(By.id("loginBtn"));
        loginBtn.click();

        loginModalBtn = driver.findElement(By.id("loginModalBtn"));

        System.out.println(loginModalBtn.getText());

        assertEquals(loginModalBtn.getText(), "test@gmail.com");
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

}
