import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class JcommuneAT {
    private String[] registrationData = {"USERNAME10", "testtreesol+10@yandex.ru", "123"};
    private String[] mailLoginData = {"testtreesol@yandex.ru", "123456TS"};
    private String[] loginData = {"klamach89@gmail.com", "145278Goo"};
    private String[] topicdata = {"NewTopic Subject", "This topic was created by auto-test"};
    private WebDriver driver;

    public static File makeSrceenshot(WebDriver driver, String pathName){
        Screenshot shot = new AShot().takeScreenshot(driver);
        String path = pathName + ".png";
        File outputFile = new File(path);
        try {
            ImageIO.write(shot.getImage(), "png", outputFile);
        }
        catch (IOException e){e.printStackTrace();}
        return new File(path);
    }


    @BeforeTest
    public void startWebDriver(){
        driver = new FirefoxDriver();
    }


    @Test
    public void Registration() throws Exception{
        driver.get("http://qa.jtalks.org/jcommune/");
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        makeSrceenshot(driver, "mainpage");

        driver.findElement(By.id("top-signup-link")).click();
        makeSrceenshot(driver, "registration");

        WebElement username = driver.findElement(By.id("username"));
        username.clear();
        username.sendKeys(registrationData[0]);

        WebElement email = driver.findElement(By.id("email"));
        email.clear();
        email.sendKeys(registrationData[1]);

        WebElement password = driver.findElement(By.id("password"));
        password.clear();
        password.sendKeys(registrationData[2]);

        WebElement passwordConfirm = driver.findElement(By.id("passwordConfirm"));
        passwordConfirm.clear();
        passwordConfirm.sendKeys(registrationData[2]);
        passwordConfirm.submit();

        driver.get("https://mail.yandex.ru/");
        WebElement ya_email = driver.findElement(By.name("login"));
        ya_email.clear();
        ya_email.sendKeys(mailLoginData[0]);

        WebElement ya_password = driver.findElement(By.name("passwd"));
        ya_password.clear();
        ya_password.sendKeys(mailLoginData[1]);
        ya_password.submit();
        Thread.sleep(500);

        driver.findElement(By.xpath("//a[contains(@href,'#message')]")).click();
        driver.findElement(By.xpath("//a[contains(@href,'jtalks')]")).click();
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }
        Assert.assertTrue(driver.getCurrentUrl().equals("http://qa.jtalks.org/jcommune/"));
        makeSrceenshot(driver, "RegSucsess");

    }

    @Test(priority = 1, alwaysRun = true)
    public void NewTopic() throws Exception {
        driver.get("http://qa.jtalks.org/jcommune/login");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement username = driver.findElement(By.id("dialog-userName"));
        username.clear();
        username.sendKeys(loginData[0]);

        WebElement password = driver.findElement(By.id("dialog-password"));
        password.clear();
        password.sendKeys(loginData[1]);
        password.submit();

        driver.get("http://qa.jtalks.org/jcommune/branches/3");
        makeSrceenshot(driver, "branch-page");

        driver.findElement(By.xpath("//a[contains(@href,'/jcommune/topics/new')]")).click();
        makeSrceenshot(driver, "newtopicpage");

        WebElement subject = driver.findElement(By.id("subject"));
        subject.clear();
        subject.sendKeys(topicdata[0]);

        if(!driver.findElement(By.id("sticked")).isSelected())
            driver.findElement(By.id("sticked")).click();
        if(!driver.findElement(By.id("announcement")).isSelected())
            driver.findElement(By.id("announcement")).click();

        WebElement messege = driver.findElement(By.id("postBody"));
        messege.clear();
        messege.sendKeys(topicdata[1]);
        messege.submit();

        Thread.sleep(1000);
        makeSrceenshot(driver, "newtopic");
    }

    @AfterTest
    public void stopWebDriver(){
        driver.quit();
    }
}
