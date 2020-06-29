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
public class TestEvent {
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
  public void tesst() {
    driver.get("http://localhost:8080/kickInTeam26/list.html");
    driver.manage().window().setSize(new Dimension(1920, 1039));
    driver.findElement(By.id("addEventBtn")).click();
    driver.findElement(By.id("eventName")).click();
    driver.findElement(By.id("eventName")).sendKeys("Test event");
    driver.findElement(By.id("eventDescription")).sendKeys("event test description");
    driver.findElement(By.id("eventDate")).sendKeys("2020-06-26");
    driver.findElement(By.id("eventLocation")).click();
    {
      WebElement dropdown = driver.findElement(By.id("eventLocation"));
      dropdown.findElement(By.xpath("//option[. = 'Outside Campus (City)']")).click();
    }
    driver.findElement(By.id("eventLocation")).click();
    driver.findElement(By.cssSelector(".bottomright > .align-self-end")).click();
    driver.findElement(By.cssSelector("tr:nth-child(10) .glyphicon-pencil")).click();
    driver.findElement(By.id("eventName")).click();
    driver.findElement(By.id("eventName")).sendKeys("test event edit");
    driver.findElement(By.id("eventDescription")).sendKeys("edited");
    driver.findElement(By.id("eventDate")).click();
    driver.findElement(By.id("eventDate")).sendKeys("2020-06-26");
    driver.findElement(By.id("eventDate")).click();
    driver.findElement(By.id("eventDate")).sendKeys("2020-06-30");
    driver.findElement(By.id("eventLocation")).click();
    {
      WebElement dropdown = driver.findElement(By.id("eventLocation"));
      dropdown.findElement(By.xpath("//option[. = 'Both']")).click();
    }
    driver.findElement(By.id("eventLocation")).click();
    driver.findElement(By.cssSelector(".form-group:nth-child(2)")).click();
    driver.findElement(By.id("myBtn")).click();
    driver.findElement(By.cssSelector(".col-md-6")).click();
    driver.findElement(By.id("addMapsToEvent")).click();
    driver.findElement(By.cssSelector(".custom-control:nth-child(10) > .custom-control-label")).click();
    driver.findElement(By.cssSelector(".bottomright:nth-child(4) > .btn")).click();
    driver.findElement(By.cssSelector(".glyphicon-eye-open")).click();
    driver.findElement(By.cssSelector(".glyphicon-home")).click();
    driver.findElement(By.cssSelector(".dropdown-toggle")).click();
    driver.findElement(By.linkText("A - Z")).click();
    driver.findElement(By.cssSelector(".dropdown-toggle")).click();
    driver.findElement(By.linkText("Z - A")).click();
    driver.findElement(By.cssSelector(".dropdown-toggle")).click();
    driver.findElement(By.linkText("Event Date (New - Old)")).click();
    driver.findElement(By.cssSelector(".dropdown-toggle")).click();
    driver.findElement(By.linkText("Event Date (Old - New)")).click();
    driver.findElement(By.id("searchTable")).click();
  }
}
