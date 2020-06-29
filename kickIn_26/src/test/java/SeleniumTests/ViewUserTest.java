package SeleniumTests;

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
public class ViewUserTest {
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
  public void viewUser() throws InterruptedException {
    driver.get(Constants.ISSUER);
    driver.findElement(By.id("inputEmail")).sendKeys("joep@gmail.com");
    driver.findElement(By.id("inputPassword")).sendKeys("joep");
    driver.findElement(By.id("inputPassword")).sendKeys(Keys.ENTER);
    Thread.sleep(7000);

    // Go to the users page.
    driver.findElement(By.cssSelector(".glyphicon-user")).click();
    Thread.sleep(7000);

    // View the information on a user.
    driver.findElement(By.cssSelector("tr:nth-child(1) .glyphicon")).click();
    Thread.sleep(7000);
  }
}
