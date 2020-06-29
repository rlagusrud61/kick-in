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
public class ExportMapTest {
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
  public void exportMap() throws InterruptedException {
    driver.get("http://localhost:8080/kickInTeam26/");

    // Log in.
    driver.findElement(By.id("inputEmail")).sendKeys("joep@gmail.com");
    driver.findElement(By.id("inputPassword")).sendKeys("joep");
    driver.findElement(By.id("inputPassword")).sendKeys(Keys.ENTER);
    Thread.sleep(7000);

    // Go to the maps page.
    driver.findElement(By.cssSelector(".glyphicon-globe")).click();
    Thread.sleep(7000);

    // View a map.
    driver.findElement(By.cssSelector("tr:nth-child(2) .glyphicon-eye-open")).click();
    Thread.sleep(7000);

    // Download the report for the map.
    {
      WebElement element = driver.findElement(By.id("downloadReport"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }

    // Look at map.
    js.executeScript("window.scrollTo(0,398.3999938964844)");
    Thread.sleep(7000);
    driver.findElement(By.id("leafletEasyPrint")).click();

    // Log out.
    driver.findElement(By.id("logout")).click();
    Thread.sleep(7000);
  }
}
