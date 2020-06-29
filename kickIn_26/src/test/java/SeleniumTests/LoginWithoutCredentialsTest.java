package SeleniumTests;// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import nl.utwente.di.team26.Constants;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class LoginWithoutCredentialsTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    System.setProperty("webdriver.chrome.driver", Constants.CHROME_DRIVER);
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void enteringNothing() throws InterruptedException {
    driver.get(Constants.ISSUER);
    driver.manage().window().setSize(new Dimension(838, 770));
    {
      WebElement element = driver.findElement(By.cssSelector(".btn"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.get("http://localhost:8080/kickInTeam26/");

    // Log in without credentials
    driver.findElement(By.cssSelector(".btn")).click();
    Thread.sleep(7000);

    // Log in with only email.
    driver.findElement(By.id("inputEmail")).sendKeys("hk@gmail.com");
    driver.findElement(By.cssSelector(".btn")).click();
    Thread.sleep(7000);

    // Log in with only password.
    driver.findElement(By.id("inputPassword")).sendKeys("password");
    driver.findElement(By.cssSelector(".btn")).click();
    Thread.sleep(7000);
  }
}
