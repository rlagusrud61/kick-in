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
public class AdminNavigationTest {
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
  public void adminNavigation() throws InterruptedException {
    driver.get("http://localhost:8080/kickInTeam26/login.html");
    driver.manage().window().setSize(new Dimension(838, 770));
    driver.findElement(By.id("inputEmail")).sendKeys("admin@gmail.com");
    driver.findElement(By.id("inputPassword")).sendKeys("adminpass");
    driver.findElement(By.id("inputPassword")).sendKeys(Keys.ENTER);
    Thread.sleep(7000);

    driver.findElement(By.id("addEventBtn")).click();
    driver.findElement(By.id("eventName")).click();
    driver.findElement(By.id("eventName")).sendKeys("Adding Event");
    driver.findElement(By.id("eventDescription")).sendKeys("As an Admin");
    driver.findElement(By.id("eventDate")).click();
    driver.findElement(By.id("eventDate")).sendKeys("2020-06-24");
    driver.findElement(By.cssSelector(".bottomright > .align-self-end")).click();
    driver.findElement(By.cssSelector("tr:nth-child(5) .glyphicon-pencil")).click();
    driver.findElement(By.id("eventName")).click();
    driver.findElement(By.id("eventName")).sendKeys("Editing Event");
    driver.findElement(By.id("eventDescription")).sendKeys("As an Admin");
    driver.findElement(By.id("eventDate")).click();
    driver.findElement(By.id("eventDate")).sendKeys("2020-06-26");
    driver.findElement(By.id("eventLocation")).click();
    {
      WebElement dropdown = driver.findElement(By.id("eventLocation"));
      dropdown.findElement(By.xpath("//option[. = 'Outside Campus (City)']")).click();
    }
    driver.findElement(By.id("eventLocation")).click();
    driver.findElement(By.id("myBtn")).click();
    driver.findElement(By.id("deleteEvent")).click();
    driver.findElement(By.id("deleteEvent")).click();
    driver.findElement(By.id("home")).click();
    driver.findElement(By.cssSelector("tr:nth-child(5) .glyphicon-trash")).click();
    driver.findElement(By.id("yesDeleteButton")).click();
    driver.findElement(By.cssSelector(".glyphicon-user")).click();
    driver.findElement(By.id("myBtn")).click();
    driver.findElement(By.id("userName")).click();
    driver.findElement(By.id("userName")).sendKeys("NewUser");
    driver.findElement(By.id("email")).sendKeys("newUser@email.com");
    driver.findElement(By.id("password")).sendKeys("newuser");
    driver.findElement(By.id("clearanceLevel")).click();
    {
      WebElement dropdown = driver.findElement(By.id("clearanceLevel"));
      dropdown.findElement(By.xpath("//option[. = 'Editor']")).click();
    }
    driver.findElement(By.id("clearanceLevel")).click();
    driver.findElement(By.cssSelector(".bottomright > .align-self-end")).click();
    driver.findElement(By.cssSelector(".glyphicon-home")).click();
    driver.findElement(By.cssSelector(".glyphicon-globe")).click();
    driver.findElement(By.id("addMapBtn")).click();
    driver.findElement(By.id("mapName")).click();
    driver.findElement(By.id("mapName")).sendKeys("New Map");
    driver.findElement(By.id("mapDescription")).sendKeys("Adding New Map As Admin");
    driver.findElement(By.cssSelector(".bottomright > .align-self-end")).click();
    driver.findElement(By.cssSelector("tr:nth-child(24) .glyphicon-trash")).click();
    driver.findElement(By.id("yesDeleteButton")).click();
    driver.findElement(By.cssSelector(".glyphicon-cog")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".glyphicon-cog"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.cssSelector(".glyphicon-home")).click();
    driver.findElement(By.id("logout")).click();
  }
}
