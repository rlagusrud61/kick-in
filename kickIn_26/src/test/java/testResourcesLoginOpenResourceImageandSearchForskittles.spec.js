// Generated by Selenium IDE
const { Builder, By, Key, until } = require('selenium-webdriver')
const assert = require('assert')

describe('TestResources: Login, Open Resource Image and Search For "skittles".', function() {
  this.timeout(30000)
  let driver
  let vars
  beforeEach(async function() {
    driver = await new Builder().forBrowser('chrome').build()
    vars = {}
  })
  afterEach(async function() {
    await driver.quit();
  })
  it('TestResources: Login, Open Resource Image and Search For "skittles".', async function() {
    await driver.get("http://localhost:8080/kickInTeam26/")
    await driver.findElement(By.id("inputEmail")).sendKeys("priya@email.com")
    await driver.findElement(By.id("inputPassword")).click()
    await driver.findElement(By.id("inputPassword")).sendKeys("priya")
    await driver.findElement(By.id("inputPassword")).sendKeys(Key.ENTER)
    await driver.findElement(By.css(".glyphicon-cog")).click()
    await driver.findElement(By.css("tr:nth-child(2) .glyphicon-eye-open")).click()
    await driver.findElement(By.css("#imageModal .close")).click()
    await driver.findElement(By.css("tr:nth-child(3) .glyphicon-eye-open")).click()
    await driver.findElement(By.css("#imageModal .close")).click()
    await driver.findElement(By.id("searchTable")).click()
    await driver.findElement(By.id("searchTable")).sendKeys("skittles")
    await driver.findElement(By.id("searchTable")).click()
    await driver.findElement(By.id("searchTable")).sendKeys("does not exist")
  })
})
