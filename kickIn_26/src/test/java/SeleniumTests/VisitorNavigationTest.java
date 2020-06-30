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

import javax.ws.rs.Consumes;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class VisitorNavigationTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    System.setProperty("webdriver.chrome.driver", "C:\\Users\\Priya\\Desktop\\Module 4\\Project\\chromedriver_win32\\chromedriver.exe");
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void visitorNavigation() throws InterruptedException {
    driver.get(Constants.ISSUER);

    // Log in.
    driver.findElement(By.id("inputEmail")).sendKeys("vicsitor@email.com");
    driver.findElement(By.id("inputPassword")).sendKeys("vicsitorpassword");
    driver.findElement(By.id("inputPassword")).sendKeys(Keys.ENTER);
    Thread.sleep(20000);

    // Delete an event.
    driver.findElement(By.cssSelector("tr:nth-child(2) .glyphicon-trash")).click();
    driver.findElement(By.id("yesDeleteButton")).click();
    Thread.sleep(7000);

    // Cannot delete event so no button pressed.
    driver.findElement(By.id("noBtn")).click();
    Thread.sleep(7000);

    // Add an event which cannot be done.
    driver.findElement(By.id("addEventBtn")).click();
    driver.findElement(By.id("eventName")).click();
    driver.findElement(By.id("eventName")).sendKeys("New event");
    driver.findElement(By.id("eventDescription")).sendKeys("as Visitor");
    driver.findElement(By.id("eventDescription")).sendKeys(Keys.TAB);
    driver.findElement(By.id("eventDate")).clear();
    driver.findElement(By.id("eventDate")).sendKeys("2020-07-02");
    driver.findElement(By.id("eventLocation")).click();
    {
      WebElement dropdown = driver.findElement(By.id("eventLocation"));
      dropdown.findElement(By.xpath("//option[. = 'Outside Campus (City)']")).click();
    }
    driver.findElement(By.id("eventLocation")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".bottomright > .align-self-end"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).clickAndHold().perform();
    }
    driver.findElement(By.cssSelector(".bottomright > .align-self-end")).click();
    driver.findElement(By.cssSelector("#addEvent .close")).click();
    Thread.sleep(7000);

    // View an event.
    driver.findElement(By.cssSelector("tr:nth-child(2) .glyphicon-eye-open")).click();
    Thread.sleep(7000);

    // Delete an event which is not possible.
    driver.findElement(By.id("deleteEvent")).click();
    driver.findElement(By.id("yesEventDelete")).click();
    Thread.sleep(7000);
    driver.findElement(By.id("no")).click();
    Thread.sleep(7000);

    // Go to the maps page.
    driver.findElement(By.cssSelector(".glyphicon-globe")).click();
    Thread.sleep(7000);

    // Add a map which is not possible.
    driver.findElement(By.id("addMapBtn")).click();
    driver.findElement(By.id("mapName")).click();
    driver.findElement(By.id("mapName")).sendKeys("New Map");
    driver.findElement(By.id("mapDescription")).sendKeys("As visitor");
    driver.findElement(By.cssSelector(".bottomright > .align-self-end")).click();
    driver.findElement(By.cssSelector("#addMap .close")).click();
    Thread.sleep(7000);

    // Go to the users page.
    driver.findElement(By.cssSelector(".glyphicon-user")).click();
    Thread.sleep(7000);

    // Log out.
    driver.findElement(By.id("logout")).click();
    Thread.sleep(7000);
  }
}
