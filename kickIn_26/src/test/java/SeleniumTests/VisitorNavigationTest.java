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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
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
  public void visitorNavigation() {
    driver.get("http://localhost:8080/kickInTeam26/login.html");
    driver.manage().window().setSize(new Dimension(840, 772));
    driver.findElement(By.id("inputEmail")).sendKeys("vicsitor@email.com");
    driver.findElement(By.id("inputPassword")).sendKeys("vicsitorpassword");
    driver.findElement(By.id("inputPassword")).sendKeys(Keys.ENTER);
    driver.findElement(By.cssSelector("tr:nth-child(5) .glyphicon-trash")).click();
    driver.findElement(By.id("yesDeleteButton")).click();
    driver.findElement(By.id("noBtn")).click();
    driver.findElement(By.id("addEventBtn")).click();
    driver.findElement(By.id("eventName")).click();
    driver.findElement(By.id("eventName")).sendKeys("New event");
    driver.findElement(By.id("eventDescription")).sendKeys("as Visitor");
    driver.findElement(By.id("eventDate")).click();
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
    {
      WebElement element = driver.findElement(By.cssSelector(".bottomright > .align-self-end"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".bottomright > .align-self-end"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector(".bottomright > .align-self-end")).click();
    driver.findElement(By.cssSelector("#addEvent .close")).click();
    driver.findElement(By.cssSelector("tr:nth-child(2) .glyphicon-eye-open")).click();
    driver.findElement(By.cssSelector(".glyphicon-eye-open")).click();
    driver.findElement(By.id("deleteEvent")).click();
    driver.findElement(By.id("yes")).click();
    driver.findElement(By.cssSelector("#popupMapDelete .close")).click();
    driver.findElement(By.id("mapEditBtn")).click();
    driver.findElement(By.id("mapEditBtn")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".leaflet-drag-target"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".leaflet-drag-target"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".leaflet-drag-target"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector(".leaflet-image-layer:nth-child(11)")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".leaflet-drag-target"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".leaflet-drag-target"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".leaflet-drag-target"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector(".leaflet-image-layer:nth-child(11)")).click();
    driver.findElement(By.cssSelector(".w3-bar-item:nth-child(3)")).click();
    driver.findElement(By.cssSelector(".leaflet-image-layer:nth-child(1)")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".leaflet-drag-target"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".leaflet-drag-target"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector(".leaflet-drag-target"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector(".leaflet-marker-icon:nth-child(4)")).click();
    driver.findElement(By.cssSelector(".w3-bar-item:nth-child(3)")).click();
    {
      WebElement element = driver.findElement(By.cssSelector("html"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector("html"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.cssSelector("html"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector("html")).click();
    driver.findElement(By.cssSelector(".glyphicon-cog")).click();
    driver.findElement(By.id("addResourceBtn")).click();
    driver.findElement(By.id("resourceName")).click();
    driver.findElement(By.id("resourceName")).sendKeys("New Resource");
    driver.findElement(By.id("resourceDescription")).click();
    driver.findElement(By.id("resourceDescription")).sendKeys("Add");
    driver.findElement(By.cssSelector(".custom-control:nth-child(1) > .custom-control-label")).click();
    driver.findElement(By.id("resourceImage")).click();
    driver.findElement(By.cssSelector(".bottomright > .align-self-end")).click();
    driver.findElement(By.cssSelector("#addResource > .modal-content")).click();
    driver.findElement(By.cssSelector("#addResource .close")).click();
    driver.findElement(By.id("resources")).click();
    driver.findElement(By.id("addResourceBtn")).click();
    driver.findElement(By.cssSelector("#addResource .close")).click();
    driver.findElement(By.cssSelector(".col-8")).click();
    driver.findElement(By.cssSelector(".glyphicon-globe")).click();
    driver.findElement(By.id("addMapBtn")).click();
    driver.findElement(By.id("mapName")).click();
    driver.findElement(By.id("mapName")).sendKeys("New Map");
    driver.findElement(By.id("mapDescription")).sendKeys("As visitor");
    driver.findElement(By.cssSelector(".bottomright > .align-self-end")).click();
    driver.findElement(By.cssSelector("#addMap .close")).click();
    driver.findElement(By.cssSelector(".glyphicon-user")).click();
    driver.findElement(By.id("logout")).click();
  }
}